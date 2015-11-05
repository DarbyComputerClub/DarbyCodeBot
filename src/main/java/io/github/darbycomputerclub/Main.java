package io.github.darbycomputerclub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.UserstreamEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import com.twitter.hbc.twitter4j.Twitter4jUserstreamClient;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;

import io.github.darbycomputerclub.message.MessageEvent;
import io.github.darbycomputerclub.message.response.AttachTest;
import io.github.darbycomputerclub.message.response.Help;
import io.github.darbycomputerclub.message.response.Ping;
import io.github.darbycomputerclub.twitter.TwitterNotificationHandler;
import twitter4j.UserStreamListener;

/**
 * Starting point when run.
 */
public class Main {

	/**
	 * Twitter msgQueue size.
	 */
	private static final int MSG_QUEUE_SIZE = 100000;

	/**
	 * Twitter eventQueue size.
	 */
	private static final int EVENT_QUEUE_SIZE = 1000;

	/**
	 * List of registered commands.
	 */
	private static ArrayList<MessageEvent> commands = 
			new ArrayList<MessageEvent>();

	/**
	 * Logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(Main.class);

	/**
	 * Constant used to reduce the amount of calls to slack.
	 */
	private static final int SLEEP_CONSTANT = 1000;
	
	/**
	 * Slack session.
	 */
	private static SlackSession session;

	/**
	 * See if connected to slack.
	 */
	private static boolean isConnectedToSlack = false;

	/**
	 * @return slack session
	 */
	public static SlackSession getSession() {
		return session;
	}

	/**
	 * This class created as an object.
	 */
	protected Main() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Starting point when run.
	 * 
	 * @param args
	 *            None.
	 */
	public static void main(final String[] args) {
		try {
			logger.info("Current Version" + Version.CURRENT);
			
			logger.info("Working Directory = "
					+ System.getProperty("user.dir"));
			
			// Remember: Never commit the authentication token!
			session = SlackSessionFactory
					.createWebSocketSlackSession(System.getenv("SLACK_API"));
			
			commands.add(new Help());
			commands.add(new Ping());
			commands.add(new AttachTest());
			//commands.add(new QR());
			
			session.addMessagePostedListener(new SlackMessagePostedListener() {
				@Override
				public void onEvent(final SlackMessagePosted event, 
						final SlackSession slackSession) {
					for (MessageEvent runEvent : commands) {
						logger.info("Checking " + runEvent.helpMessage());
						
						runEvent.processEvent(event, slackSession);
					}
				}
			});
			

			
	        Authentication darbyCodeBotAuth = new OAuth1(
	                System.getenv("TWITTER_CONSUMER_KEY"), 
	                System.getenv("TWITTER_CONSUMER_SECRET"), 
	                System.getenv("TWITTER_TOKEN"), 
	                System.getenv("TWITTER_SECRET"));
	        
			List<UserStreamListener> darbyCodeBotListeners = 
					Lists.newArrayList();
			darbyCodeBotListeners.add(new TwitterNotificationHandler());
	        
			Twitter4jUserstreamClient darbyCodeBotTwitter = 
					createTwitterBot(darbyCodeBotAuth, darbyCodeBotListeners);
			
			Authentication darbyCompClubAuth = new OAuth1(
	                System.getenv("COMP_CLUB_TWITTER_CONSUMER_KEY"), 
	                System.getenv("COMP_CLUB_TWITTER_CONSUMER_SECRET"), 
	                System.getenv("COMP_CLUB_TWITTER_TOKEN"), 
	                System.getenv("COMP_CLUB_TWITTER_SECRET"));
	        
			List<UserStreamListener> darbyCompClubListeners = 
					Lists.newArrayList();
			darbyCodeBotListeners.add(new TwitterNotificationHandler());
			
			Twitter4jUserstreamClient darbyCompClubTwitter = 
					createTwitterBot(darbyCompClubAuth, darbyCompClubListeners);
			
			darbyCodeBotTwitter.connect();
			darbyCodeBotTwitter.process();
			
			darbyCompClubTwitter.connect();
			darbyCompClubTwitter.process();

			try {
				session.connect();
				isConnectedToSlack  = true;
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
			
			Runtime.getRuntime().addShutdownHook(new Thread() {
			    @Override
			        public void run() {
			    		if (isConnectedToSlack) {
							session.sendMessage(
									session.findChannelByName("bottesting"),
											"I'm shutting down. " 
												+ "Hopefully I'm getting "
												+ "an upgrade... [Version "
												+ Version.CURRENT + "]", null);
						}
			        }   
			    });
			
			session.sendMessage(
					session.findChannelByName("bottesting"),
					"I'm Back!!! Now running Version " 
							+ Version.CURRENT, null);
			
			while (true) {
				try {
					Thread.sleep(SLEEP_CONSTANT);
				} catch (InterruptedException e) {
					logger.error(e.getMessage());
				}
			}
		} catch (Exception e) {
			if (isConnectedToSlack) {
				session.sendMessage(
						session.findChannelByName("bottesting"),
						"Oops.  I seem to have crashed... [Version " 
								+ Version.CURRENT + "]", null);
			}
		}
	}
	
	/**
	 * @return List of registered commands.
	 */
	public static ArrayList<MessageEvent> getCommands() {
		return commands;
	}
	
	/**
	 * @param command Command to add.
	 */
	public static void setCommands(final MessageEvent command) {
		commands.add(command);
	}
	
	/**
	 * Creates bot given authentication and listeners.
	 * 
	 * @param botAuth Authentication.
	 * @param listeners List of listeners.
	 * @return Twitter bot.
	 */
	public static Twitter4jUserstreamClient createTwitterBot(
			final Authentication botAuth, 
			final List<UserStreamListener> listeners) {
		/** 
		 * Set up your blocking queues: Be sure to size 
		 * these properly based on expected TPS of your stream 
		 */
		BlockingQueue<String> msgQueue = 
				new LinkedBlockingQueue<String>(MSG_QUEUE_SIZE);
		BlockingQueue<Event> eventQueue = 
				new LinkedBlockingQueue<Event>(EVENT_QUEUE_SIZE);

		ClientBuilder builder = new ClientBuilder()
			.name("Hosebird-Client-01")
			.hosts(new HttpHosts(Constants.USERSTREAM_HOST))
			.authentication(botAuth)
			.endpoint(new UserstreamEndpoint())
			.processor(new StringDelimitedProcessor(msgQueue))
			.eventMessageQueue(eventQueue);

		Client hosebirdClient = builder.build();
		// Attempts to establish a connection.
		
		ThreadFactory threadFactory = new ThreadFactoryBuilder()
			.setDaemon(true)
			.setNameFormat("hosebird-client-io-thread-%d")
			.build();
		ExecutorService executorService = 
				Executors.newSingleThreadExecutor(threadFactory);
		
		Twitter4jUserstreamClient t4jClient = 
				new Twitter4jUserstreamClient(hosebirdClient, msgQueue,
						listeners, executorService);
		
		return t4jClient;
	}
}

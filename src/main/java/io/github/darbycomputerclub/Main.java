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
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.UserstreamEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import com.twitter.hbc.twitter4j.Twitter4jUserstreamClient;
import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;

import io.github.darbycomputerclub.message.MessageEvent;
import io.github.darbycomputerclub.message.response.AttachTest;
import io.github.darbycomputerclub.message.response.Help;
import io.github.darbycomputerclub.message.response.Ping;
import io.github.darbycomputerclub.twitter.MainTwitterHandler;
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
			
			Runtime.getRuntime().addShutdownHook(new Thread() {
		        @Override
		            public void run() {
		        		session.sendMessage(
		        				session.findChannelByName("bottesting"), 
		        				"I'm getting an upgrade...? [Version " 
									+ Version.CURRENT + "]", null);
		            }   
		        });
			
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
			
			session.sendMessage(session.findChannelByName("bottesting"), 
					"Up and Running! - Running Version " 
							+ Version.CURRENT, null);
			
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
			
			/** 
			 * Set up your blocking queues: Be sure to size 
			 * these properly based on expected TPS of your stream 
			 */
			BlockingQueue<String> msgQueue = 
					new LinkedBlockingQueue<String>(MSG_QUEUE_SIZE);
			BlockingQueue<Event> eventQueue = 
					new LinkedBlockingQueue<Event>(EVENT_QUEUE_SIZE);

			/** 
			 * Declare the host you want to connect to, 
			 * the endpoint, and authentication (basic auth or oauth) 
			 */
			Hosts hosebirdHosts = new HttpHosts(Constants.USERSTREAM_HOST);
			UserstreamEndpoint hosebirdEndpoint = new UserstreamEndpoint();

			Authentication hosebirdAuth = new OAuth1(
					System.getenv("TWITTER_CONSUMER_KEY"), 
					System.getenv("TWITTER_CONSUMER_SECRET"), 
					System.getenv("TWITTER_TOKEN"), 
					System.getenv("TWITTER_SECRET"));
			ClientBuilder builder = new ClientBuilder()
				.name("Hosebird-Client-01")
				.hosts(hosebirdHosts)
				.authentication(hosebirdAuth)
				.endpoint(hosebirdEndpoint)
				.processor(new StringDelimitedProcessor(msgQueue))
				.eventMessageQueue(eventQueue);

			Client hosebirdClient = builder.build();
			// Attempts to establish a connection.
			
			UserStreamListener listener = new MainTwitterHandler();
				  
			List<UserStreamListener> listeners = Lists.newArrayList();
			listeners.add(listener);
			
			ThreadFactory threadFactory = new ThreadFactoryBuilder()
			        .setDaemon(true)
			        .setNameFormat("hosebird-client-io-thread-%d")
			        .build();
			ExecutorService executorService = 
					Executors.newSingleThreadExecutor(threadFactory);
			
			Twitter4jUserstreamClient t4jClient = 
					new Twitter4jUserstreamClient(hosebirdClient, msgQueue,
							listeners, executorService);
			t4jClient.connect();

			// Call this once for every thread you want 
			// to spin off for processing the raw messages.
			// This should be called at least once.
			t4jClient.process();

			try {
				session.connect();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}

			while (true) {
				try {
					Thread.sleep(SLEEP_CONSTANT);
				} catch (InterruptedException e) {
					logger.error(e.getMessage());
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			session.sendMessage(session.findChannelByName("bottesting"), 
					"Welp. I seem to have had an error... [Version " 
							+ Version.CURRENT + "]", 
							new SlackAttachment("Exception: ", "Exception: ",
									e.getMessage(), e.getClass().getName()));
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
}

package io.github.darbycomputerclub;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;

import io.github.darbycomputerclub.message.MessageEvent;

/**
 * Starting point when run.
 */
public class Main {

	/**
	 * HTTP Robocode Update time.
	 */
	private static final int CHECK_UPDATE_TIME = 1;

	/**
	 * List of registered commands.
	 */
	private static ArrayList<MessageEvent> commands = new ArrayList<MessageEvent>();

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
	 * Last Robocode update time.
	 */
	private static String lastupdate = "";

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

			// commands.add(new Help());
			// commands.add(new Ping());
			// commands.add(new QR());

			session.addMessagePostedListener(new SlackMessagePostedListener() {
				@Override
				public void onEvent(final SlackMessagePosted event, 
						final SlackSession slackSession) {
					if (commands != null) {
						for (MessageEvent runEvent : commands) {
							logger.info("Checking " + runEvent.helpMessage());

							runEvent.processEvent(event, slackSession);
						}
					}
				}
			});

			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					if (isConnectedToSlack) {
						session.sendMessage(
								session.findChannelByName("bottesting"), "I'm shutting down. "
										+ "Hopefully I'm getting " + "an upgrade... [Version " + Version.CURRENT + "]",
								null);
					}
				}
			});

			try {
				session.connect();
				isConnectedToSlack  = true;
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
			
			logger.debug("Started.");

			//session.sendMessage(session.findChannelByName("bottesting"),
			//		"I'm Back!!! Now running Version " + Version.CURRENT, null);

			final ScheduledExecutorService executorService 
				= Executors.newSingleThreadScheduledExecutor();
			executorService.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					logger.debug("Checking Robocode.");
					robocodeChecker();
				}

				/**
				 * 
				 */
				private void robocodeChecker() {
					
					String get = null;
					URL url = null;
					try {
						url = new URL("https://darbycomputerclub.github.io/"
								+ "darbyrobocode/api/lastupdate.txt");
					} catch (MalformedURLException e1) {
						e1.printStackTrace();
					}

					try {
						BufferedReader reader 
							= new BufferedReader(
									new InputStreamReader(
											url.openStream(), "UTF-8"));
						get = reader.readLine();

						if (get != null && !get.equals(lastupdate)) {
							logger.debug("Changed");
							URL resultsUrl = null;
							try {
								resultsUrl = new URL(
									"https://darbycomputerclub.github.io/"
									+ "darbyrobocode/results-columns.txt");
							} catch (MalformedURLException e1) {
								e1.printStackTrace();
							}

							String getResults = "";
							try {
								BufferedReader resultsReader 
									= new BufferedReader(
										new InputStreamReader(
												resultsUrl.openStream(), 
												"UTF-8"));
								for (String line; 
										(line = resultsReader.readLine()) 
										!= null;) {
									logger.debug(line);
							        getResults += line + "\n";
							    }

							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							}
							
							logger.debug("Printing results");
							logger.debug(getResults);
							
							logger.debug("Sent");
							session.sendMessage(
									session.findChannelByName("bottesting"), 
									"```\n" + getResults + "\n```", null);
							lastupdate = get;
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}, 0, CHECK_UPDATE_TIME, TimeUnit.MINUTES);

			while (true) {
				try {
					Thread.sleep(SLEEP_CONSTANT);
				} catch (InterruptedException e) {
					logger.error(e.getMessage());
				}
			}
		} catch (Exception e) {
			if (isConnectedToSlack) {
				session.sendMessage(session.findChannelByName("bottesting"),
						"Oops.  I seem to have crashed... [Version " + Version.CURRENT + "]", null);
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
	 * @param command
	 *            Command to add.
	 */
	public static void setCommands(final MessageEvent command) {
		commands.add(command);
	}
}

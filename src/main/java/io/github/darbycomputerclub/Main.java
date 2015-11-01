package io.github.darbycomputerclub;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;

import io.github.darbycomputerclub.error.Error;
import io.github.darbycomputerclub.message.MessageEvent;
import io.github.darbycomputerclub.message.response.*;

/**
 * Starting point when run.
 */
public class Main {

	/**
	 * List of registered commands.
	 */
	private static ArrayList<MessageEvent> commands = 
			new ArrayList<MessageEvent>();

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
	 * Logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(Class.class);

	/**
	 * Constant used to reduce the amount of calls to slack.
	 */
	private static final int SLEEP_CONSTANT = 1000;

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
		for (int i = 0; i < args.length; i++) {
			logger.debug("Argument #" + i + ": " + args[i]);
		}
		
		logger.info("Working Directory = "
				+ System.getProperty("user.dir"));
		
		// Remember: Never commit the authentication token!
		SlackSession session = SlackSessionFactory
				.createWebSocketSlackSession(System.getenv("SLACK_API"));
		
		commands.add(new Help());
		commands.add(new Ping());
		//commands.add(new QR());
		
		session.addMessagePostedListener(new SlackMessagePostedListener() {
			@Override
			public void onEvent(final SlackMessagePosted event, 
					final SlackSession session) {
				for (MessageEvent runEvent : commands) {
					logger.info("Checking " + runEvent.helpMessage());
					
					runEvent.processEvent(event, session);
				}
			}
		});

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

	}
}

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
	 * Address to bind socket to.
	 */
	private static final byte[] BIND_ADDRESS = new byte[] { 127, 0, 0, 1 };

	/**
	 * Socket: Currently used only to create only one bot at a time.
	 */
	private static ServerSocket socket;

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
		
		checkIfRunning();

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

	/**
	 * Checks to see if there is another bot running and stops it. Uses
	 * specified port from configuration.
	 * 
	 * @see http://stackoverflow.com/a/920403
	 */
	private static void checkIfRunning() {
		try {
			// Bind to localhost adapter with a zero connection queue
			setSocket(new ServerSocket(
					Integer.parseInt(Configuration.getConfig("port")), 0, 
					InetAddress.getByAddress(BIND_ADDRESS)));
		} catch (BindException e) {
			logger.error(e.getMessage());
			logger.error("Likely cause: " 
					+ Error.ALREADY_RUNNING.getDescription());
			System.exit(Error.ALREADY_RUNNING.getCode());
		} catch (IOException e) {
			logger.error(e.getMessage());
			logger.error("Likely cause: " 
					+ Error.SOCKET_ERROR.getDescription());
			System.exit(Error.SOCKET_ERROR.getCode());
		}
	}

	/**
	 * @return socket
	 */
	public static ServerSocket getSocket() {
		return socket;
	}

	/**
	 * @param newSocket 
	 */
	public static void setSocket(final ServerSocket newSocket) {
		socket = newSocket;
	}
}

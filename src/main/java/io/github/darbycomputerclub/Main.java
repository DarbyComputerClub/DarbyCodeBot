package io.github.darbycomputerclub;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.nio.channels.FileLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;

import io.github.darbycomputerclub.error.Error;
import io.github.darbycomputerclub.message.ProcessMessage;

/**
 * Starting point when run.
 */
public class Main {

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
		checkIfRunning();

		// Remember: Never commit the authentication token!
		SlackSession session = null;
		try {
			session = SlackSessionFactory.createWebSocketSlackSession(
					Configuration.getConfig("authenticationtoken"));
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());

			logger.error("Likely cause: " + Error.NO_CONFIG.getDescription());
			System.exit(Error.NO_CONFIG.getCode());
		} catch (IOException e) {
			logger.error(e.getMessage());

			logger.error("Likely cause: " + Error.CONFIG_READ.getDescription());
			System.exit(Error.CONFIG_READ.getCode());
		}

		session.addMessagePostedListener(new SlackMessagePostedListener() {
			@Override
			public void onEvent(final SlackMessagePosted event, final SlackSession session) {
				logger.info("[" + event.getTimeStamp() + " - " + event.getSender().getUserName() + "] "
						+ event.getMessageContent());
				ProcessMessage.processMessage(event, session);
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
			socket = new ServerSocket(
					Integer.parseInt(Configuration.getConfig("port")), 0, 
					InetAddress.getByAddress(BIND_ADDRESS));
		} catch (BindException e) {
			System.err.println("Already running.");
			
			
			
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Unexpected error.");
			e.printStackTrace();
			System.exit(2);
		}
	}
}

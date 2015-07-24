package io.github.darbycomputerclub.message.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

/**
 * Performs the "!help" action.
 */
public class Help {

	/**
	 * Logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(Help.class);
	
	/**
	 * This class should not be created as an object.
	 */
	protected Help() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Performs the "!help" action.
	 * @param session 
	 * @param event 
	 */
	public static void doHelp(final SlackMessagePosted event, 
			final SlackSession session) {
		session.sendMessage(event.getChannel(), "List of commands: \n" 
			+ "!help: Displays this message.", null);
		logger.info("Responded with: \n" + "List of commands: \n" 
			+ "!help: Displays this message.");
	}
}

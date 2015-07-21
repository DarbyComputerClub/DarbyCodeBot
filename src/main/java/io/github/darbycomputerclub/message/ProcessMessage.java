/**
 * 
 */
package io.github.darbycomputerclub.message;

import com.sun.istack.internal.logging.Logger;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

import io.github.darbycomputerclub.message.response.Help;

/**
 * Method to start the message processing. //TODO: Reword.
 */
public final class ProcessMessage {
	/**
	 * Logger.
	 */
	private static Logger logger = Logger.getLogger(ProcessMessage.class);
	
	/**
	 * This class should not be created as an object.
	 */
	protected ProcessMessage() {
		throw new UnsupportedOperationException(); 
	}
	
	/**
	 * @param event Passed from main.
	 * @param session Passed from main.
	 */
	public static void processMessage(final SlackMessagePosted event, 
					final SlackSession session) {
		if (event.getMessageContent().toString().equalsIgnoreCase("!help")) {
			logger.info("Processing Help Command");
			Help.doHelp(event, session);
		}
	}
}

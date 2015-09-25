package io.github.darbycomputerclub.message.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

/**
 * Performs the "!help" action.
 */
public class Help extends MessageEvent {

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
	public void processEvent(final SlackMessagePosted event, 
			final SlackSession session) {
		if (event.getMessageContent().toString()
				.equalsIgnoreCase("!help")) {
			String response = "List of commands: \n" + "!help: Displays this message.\n"
					+ "!ping: Responds with pong to test server uptime.";
			session.sendMessage(event.getChannel(), response, null);
			logger.info("Responded with: \n" + response);
		}
	}
}

package io.github.darbycomputerclub.message.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

/**
 * Performs the "!ping" action.
 */
public class Ping extends Response {

	/**
	 * Logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(Help.class);
	
	/**
	 * This class should not be created as an object.
	 */
	protected Ping() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Performs the "!help" action.
	 * @param session 
	 * @param event 
	 */
	public static void processEvent(final SlackMessagePosted event, 
			final SlackSession session) {
		String response = "Pong.";
		
		session.sendMessage(event.getChannel(), response, null);
		logger.info("Responded with: \n" + response);
	}

}

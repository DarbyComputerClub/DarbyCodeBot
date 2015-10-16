package io.github.darbycomputerclub.message.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

import io.github.darbycomputerclub.message.MessageEvent;

/**
 * Performs the "!ping" action.
 */
public class Ping extends MessageEvent {

	/**
	 * Logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(Help.class);
	
	/**
	 * Performs the "ping" action.
	 * @param session 
	 * @param event 
	 */
	public final void processEvent(final SlackMessagePosted event, 
			final SlackSession session) {
		if (event.getMessageContent().toString()
				.equalsIgnoreCase("!ping")) {
			String response = "Pong.";
			session.sendMessage(event.getChannel(), response, null);
			logger.info("Responded with: \n" + response);
		}
	}

	@Override
	public final String helpMessage() {
		return "`ping`: Responds with pong.";
	}
}

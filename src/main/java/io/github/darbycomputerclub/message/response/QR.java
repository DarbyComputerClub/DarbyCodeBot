package io.github.darbycomputerclub.message.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

/**
 * Performs the "!help" action.
 */
public class QR extends Response {

	/**
	 * Logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(QR.class);
	
	/**
	 * This class should not be created as an object.
	 */
	protected QR() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Performs the "!help" action.
	 * @param session 
	 * @param event 
	 */
	public static void processEvent(final SlackMessagePosted event, 
			final SlackSession session) {
		String response = "http://api.qrserver.com/v1/create-qr-code/?size=256x256&data="
				+ event.getMessageContent().split(" ")[1];
		
		session.sendMessage(event.getChannel(), response, null);
		logger.info("Responded with: \n" + response);
	}
}

package io.github.darbycomputerclub.message.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

import io.github.darbycomputerclub.message.MessageEvent;

/**
 * Performs the "!help" action.
 */
public class QR extends MessageEvent {

	/**
	 * Logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(QR.class);
	
	/**
	 * Performs the "qr" action.
	 * @param session 
	 * @param event 
	 */
	public final void processEvent(final SlackMessagePosted event, 
			final SlackSession session) {
		if (event.getMessageContent().toString()
				.startsWith("qr")) {
			String response = "http://api.qrserver.com/v1/create-qr-code/?data="
					+ event.getMessageContent().split(" ")[1];
			session.sendMessage(event.getChannel(), response, null);
			logger.info("Responded with: \n" + response);
		}
	}

	@Override
	public final String helpMessage() {
		return "`qr [text/url]`: Creates a QR code from text/url";
	}
}

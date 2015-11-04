package io.github.darbycomputerclub.message.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ullink.slack.simpleslackapi.SlackAttachment;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

import io.github.darbycomputerclub.message.MessageEvent;

/**
 * Performs the "!ping" action.
 */
public class AttachTest extends MessageEvent {

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
				.equalsIgnoreCase("!attachtest")) {
			String response = "Attachment Test";
			session.sendMessage(
					session.findChannelByName("bottesting"), 
					"Testing main body", 
					new SlackAttachment("Test Title: ", "Test Title Fallback: ",
							"Test Text", "Test Pretext"));
			logger.info("Responded with: \n" + response);
		}
	}

	@Override
	public final String helpMessage() {
		return "`attachtest`: Responds with an attachment test.";
	}
}

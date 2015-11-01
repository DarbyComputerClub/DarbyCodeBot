package io.github.darbycomputerclub.message.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

import io.github.darbycomputerclub.Main;
import io.github.darbycomputerclub.Version;
import io.github.darbycomputerclub.message.MessageEvent;

/**
 * Performs the "!help" action.
 */
public class Help extends MessageEvent {

	/**
	 * Logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(Help.class);
	
	/**
	 * Performs the "help" action.
	 * @param session 
	 * @param event 
	 */
	public final void processEvent(final SlackMessagePosted event, 
			final SlackSession session) {
		if (event.getMessageContent().toString()
				.equalsIgnoreCase("!help")) {
			String response = "Current Version: " + Version.CURRENT 
					+ "\n\nList of commands: " 
					+ "(Format: `command [required] (optional)`)\n";
			
			for (MessageEvent runEvent : Main.getCommands()) {
				logger.info("Checking " + runEvent.getClass().getSimpleName());
				if (!runEvent.helpMessage().equals(null)) {
					response += runEvent.helpMessage() + "\n";
				}
			}
			
			session.sendMessage(event.getChannel(), response, null);
			logger.info("Responded with: \n" + response);
		}
	}

	@Override
	public final String helpMessage() {
		return "`help`: Returns this message";
	}
}

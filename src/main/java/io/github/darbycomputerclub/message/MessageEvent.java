package io.github.darbycomputerclub.message;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

/**
 * Basis for responses.
 */
public abstract class MessageEvent {
	/**
	 * Performs action when event received.  This method will be called on all 
	 * MessageEvents registered in Main, so check if the command received 
	 * should be acted on.
	 * @param session 
	 * @param event 
	 */
	public abstract void processEvent(final SlackMessagePosted event, 
			final SlackSession session);
	
	/**
	 * Text for help message.  Should be in the form of:
	 * <pre>`command [required] (optional)`: Message</pre>
	 * 
	 * @return help message
	 */
	public abstract String helpMessage();
}

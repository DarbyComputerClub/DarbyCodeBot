package io.github.darbycomputerclub;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;

import io.github.darbycomputerclub.error.Error;

/**
 * Starting point when run.
 */
public class Main {

	/**
	 * Main logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(Class.class);
	
	/**
	 * Constant used to reduce the amount of calls to slack.
	 */
	private static final int SLEEP_CONSTANT = 1000;

	/**
	 * This class should not be used.
	 */
	protected Main() {
		throw new UnsupportedOperationException(); 
	}
	
	/**
	 * Starting point when run.
	 * 
	 * @param args None.
	 */
	public static void main(final String[] args) {
		
		final AuthToken authToken = new AuthToken();
		
		//Remember: Never commit the authentication token!
		SlackSession session = null;
		try {
			session = SlackSessionFactory
					.createWebSocketSlackSession(authToken.getAuthToken());
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
			
			logger.error("Likely cause: " 
					+ Error.NO_CONFIG.getDescription());
			System.exit(Error.NO_CONFIG.getCode());
		} catch (IOException e) {
			logger.error(e.getMessage());
			
			logger.error("Likely cause: " 
					+ Error.CONFIG_READ.getDescription());
			System.exit(Error.CONFIG_READ.getCode());
		}
	    
		session.addMessagePostedListener(new SlackMessagePostedListener() {
			@Override
			public void onEvent(final SlackMessagePosted event, 
					final SlackSession session) {
				logger.info("[" + event.getTimeStamp() + " - " 
						+ event.getSender().getUserName()  + "] " 
						+ event.getMessageContent());
				
			}
	    });
	    
		try {
			session.connect();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

	    while (true) {
	    	try {
	    		Thread.sleep(SLEEP_CONSTANT);
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}
	    }

	}

}

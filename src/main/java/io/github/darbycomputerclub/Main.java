package io.github.darbycomputerclub;

import java.io.IOException;

import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;

public class Main {

	public static void main(String[] args) {
		final SlackSession session = SlackSessionFactory.createWebSocketSlackSession("authenticationtoken");
	    
		session.addMessagePostedListener(new SlackMessagePostedListener() {
			@Override
			public void onEvent(SlackMessagePosted event, SlackSession session) {
				System.out.println(event.getMessageContent());
			}
	    });
	    
		try {
			session.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    while (true)
	    {
	    	try {
	    		Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }

	}

}

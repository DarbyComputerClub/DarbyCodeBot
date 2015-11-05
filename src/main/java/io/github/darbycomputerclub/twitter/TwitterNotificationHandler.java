package io.github.darbycomputerclub.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ullink.slack.simpleslackapi.SlackAttachment;

import io.github.darbycomputerclub.Main;
import twitter4j.DirectMessage;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;

/**
 * @author Alex Shafer
 *
 */
public class TwitterNotificationHandler implements UserStreamListener {

	/**
	 * Logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(
			TwitterNotificationHandler.class);
	
	/**
	 * Channel to post to.
	 */
	private static final String CHANNEL = "twitter";
	
	@Override
	public final void onStatus(final Status status) {
		String message = "@" + status.getUser().getScreenName() 
				+ " Tweeted: " + status.getText() + "(https://twitter.com/" 
				+ status.getUser().getScreenName() 
				+ "/status/" + status.getId() + ")";
		logger.info(message);
		Main.getSession().sendMessage(
				Main.getSession().findChannelByName(CHANNEL), 
				message, null);
	}

	@Override
	public final void onDeletionNotice(
			final StatusDeletionNotice statusDeletionNotice) {
		String message = "Tweet Removed: " + statusDeletionNotice.getStatusId() 
				+ " - Please remove message as soon as possible...";
		logger.info(message);
		Main.getSession().sendMessage(
				Main.getSession().findChannelByName(CHANNEL), 
				message, null);
		
	}

	@Override
	public void onTrackLimitationNotice(final int numberOfLimitedStatuses) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScrubGeo(final long userId, final long upToStatusId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStallWarning(final StallWarning warning) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public final void onException(final Exception ex) {
		logger.error(ex.getMessage());
		Main.getSession().sendMessage(
				Main.getSession().findChannelByName(CHANNEL), 
				"We seem to have an exception with Twitter...", 
				new SlackAttachment("Exception: ", "Exception: ",
						ex.getMessage(), ex.getClass().getName()));
	}

	@Override
	public void onDeletionNotice(final long directMessageId, 
			final long userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFriendList(final long[] friendIds) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public final void onFavorite(final User source, 
			final User target, final Status favoritedStatus) {
		String message = ":grinning: " + "https://twitter.com/" 
			+ target.getScreenName() + "/status/" + favoritedStatus.getId()
			+ " Favorited :heart: by @" + source.getScreenName();
		logger.info(message);
		Main.getSession().sendMessage(
				Main.getSession().findChannelByName(CHANNEL), 
				message, null);
		
	}

	@Override
	public final void onUnfavorite(final User source, final User target, 
			final Status unfavoritedStatus) {
		String message = ":disappointed: " + "https://twitter.com/" 
				+ target.getScreenName() + "/status/" 
				+ unfavoritedStatus.getId()
				+ "Unfavorited :broken_heart: by @" + source.getScreenName();
		logger.info(message);
		Main.getSession().sendMessage(
				Main.getSession().findChannelByName(CHANNEL), 
				message, null);
		
	}

	@Override
	public final void onFollow(final User source, final User followedUser) {
		String message = ":bangbang: New follow @" + source.getScreenName()
			+ "https://twitter.com/" + source.getScreenName()
			+ " followed @" + followedUser.getScreenName()
			+ "https://twitter.com/" + followedUser.getScreenName();
		logger.info(message);
		Main.getSession().sendMessage(
				Main.getSession().findChannelByName(CHANNEL), 
				message, null);
		
	}

	@Override
	public final void onUnfollow(final User source, final User unfollowedUser) {
		String message = ":bangbang: New follow @" + source.getScreenName() 
			+ "https://twitter.com/" + source.getScreenName()
			+ " unfollowed @" + unfollowedUser.getScreenName()
			+ "https://twitter.com/" + unfollowedUser.getScreenName();
		logger.info(message);
		Main.getSession().sendMessage(
				Main.getSession().findChannelByName(CHANNEL), 
				message, null);
		
	}

	@Override
	public final void onDirectMessage(final DirectMessage directMessage) {
		String message = ":bangbang: New DM: @" 
				+ directMessage.getSenderScreenName() 
				+ " sent " + directMessage.getText();
		logger.info(message);
		Main.getSession().sendMessage(
				Main.getSession().findChannelByName(CHANNEL), 
				message, null);
		
	}

	@Override
	public void onUserListMemberAddition(final User addedMember, 
			final User listOwner, final UserList list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUserListMemberDeletion(final User deletedMember, 
			final User listOwner, final UserList list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUserListSubscription(final User subscriber, 
			final User listOwner, final UserList list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUserListUnsubscription(final User subscriber, 
			final User listOwner, final UserList list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUserListCreation(final User listOwner, final UserList list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUserListUpdate(final User listOwner, final UserList list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUserListDeletion(final User listOwner, final UserList list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUserProfileUpdate(final User updatedUser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBlock(final User source, final User blockedUser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnblock(final User source, final User unblockedUser) {
		// TODO Auto-generated method stub
		
	}
}

package io.github.darbycomputerclub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class MainTwitterHandler implements UserStreamListener {

	/**
	 * Logger.
	 */
	private static Logger logger = LoggerFactory.getLogger(
			MainTwitterHandler.class);
	
	@Override
	public final void onStatus(final Status status) {
		logger.info(status.getUser().getName() + status.getText());
		
	}

	@Override
	public void onDeletionNotice(
			final StatusDeletionNotice statusDeletionNotice) {
		// TODO Auto-generated method stub
		
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
		logger.info(source.getName() + favoritedStatus.getText());
		Main.getSession().sendMessage(
				Main.getSession().findChannelByName("bottesting"), 
				"Favorite Test", null);
		
	}

	@Override
	public final void onUnfavorite(final User source, final User target, 
			final Status unfavoritedStatus) {
		logger.info(source.getName() + unfavoritedStatus.getText());
		Main.getSession().sendMessage(
				Main.getSession().findChannelByName("bottesting"), 
				"UnFavorite Test", null);
		
	}

	@Override
	public void onFollow(final User source, final User followedUser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnfollow(final User source, final User unfollowedUser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDirectMessage(final DirectMessage directMessage) {
		// TODO Auto-generated method stub
		
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

package com.fanclub.observers;

import com.fanclub.twitter.TwitterApp;
import com.fanclub.utils.FanClubConstants;

public interface TweetAuthObserver extends FanClubConstants{
	
	public void onAuthenticationComplete(TwitterApp a_twitterAppInstance);

}

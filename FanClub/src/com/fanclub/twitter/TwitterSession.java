package com.fanclub.twitter;

import com.fanclub.R;

import twitter4j.auth.AccessToken;
import android.content.Context;
import android.content.SharedPreferences;

public class TwitterSession {
	
	private Context m_cont = null;
	private static final String TWEET_AUTH_KEY = "fan_club_auth_key";
	private static final String TWEET_AUTH_SECRET_KEY = "fan_club_auth_secret_key";
	private static final String TWEET_USER_NAME = "fan_club_twitter_uname";
	private static final String SHARED = "com.fanclub";
	
	private String m_tweetAuthKey;
	private String m_tweetAuthSecretKey;
	private String m_tweetUserName;
	private String m_shared;

	public TwitterSession(Context context) {
		m_cont = context;
		
		String l_appName = context.getString(R.string.app_name);
		m_tweetAuthKey = TWEET_AUTH_KEY+"_"+l_appName;
		m_tweetAuthSecretKey = TWEET_AUTH_SECRET_KEY+"_"+l_appName;
		m_tweetUserName = TWEET_USER_NAME+"_"+l_appName;
		m_shared = SHARED+"_"+l_appName;
	}

	public void storeAccessToken(AccessToken accessToken, String username) {
		
		SharedPreferences l_sharedPref = m_cont.getSharedPreferences(m_shared, m_cont.MODE_PRIVATE);
		SharedPreferences.Editor l_editor = l_sharedPref.edit();
		l_editor.putString(m_tweetAuthKey, accessToken.getToken());
		l_editor.putString(m_tweetAuthSecretKey, accessToken.getTokenSecret());
		l_editor.putString(m_tweetUserName, username);

		l_editor.commit();
	}

	public void resetAccessToken() {
		SharedPreferences l_sharedPref = m_cont.getSharedPreferences(m_shared, m_cont.MODE_PRIVATE);
		SharedPreferences.Editor l_editor = l_sharedPref.edit();
		l_editor.putString(m_tweetAuthKey, null);
		l_editor.putString(m_tweetAuthSecretKey, null);
		l_editor.putString(m_tweetUserName, null);

		l_editor.commit();
	}

	public String getUsername() {
		SharedPreferences l_sharedPref = m_cont.getSharedPreferences(m_shared, m_cont.MODE_PRIVATE);
		return l_sharedPref.getString(m_tweetUserName, "");
	}

	public AccessToken getAccessToken() {
		SharedPreferences l_sharedPref = m_cont.getSharedPreferences(m_shared, m_cont.MODE_PRIVATE);
		String token = l_sharedPref.getString(m_tweetAuthKey, null);
		String tokenSecret = l_sharedPref.getString(m_tweetAuthSecretKey, null);

		if (token != null && tokenSecret != null)
			return new AccessToken(token, tokenSecret);
		else
			return null;
	}
}

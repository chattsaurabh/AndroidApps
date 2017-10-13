package com.fanclub.twitter;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.fanclub.R;
import com.fanclub.observers.OnDownloadComplete;
import com.harrison.lee.twitpic4j.TwitPic;

public class TwitterApp {
	private Twitter m_twitter;
	private TwitterSession m_session;
	private AccessToken m_accessToken;
	private CommonsHttpOAuthConsumer m_httpOauthConsumer;
	private OAuthProvider m_httpOauthprovider;
	private String m_consumerKey;
	private String m_secretKey;
	private ProgressDialog m_progressDlg;
	private TwDialogListener m_twtDialogListener;
	private Activity m_context;
	
	private SearchTweetsTask m_searchTask = null;
	private ExtendedSearchTask m_extendedSearchTask = null;
	private PostTweetTask m_postTweetTask = null;
	private OnDownloadComplete m_downloadCompleteListener = null;	
	private ArrayList<Status> m_searchResultList = null;
	private final int MAX_RESULT_PAGES = 5;
	private int m_currentSearchPage = 0;

	private static final String CONSUMER_KEY = "YlKpcJykFkprwvGcvcM9A";
	private static final String CONSUMER_SECRET = "FXWCa6TNF4KnsnJQz5JpPKyLykUt7cEca50t99lDN0";
	
	public static final String OAUTH_CALLBACK_SCHEME = "x-oauthflow-twitter";
	public static final String OAUTH_CALLBACK_HOST = "callback";
	public static final String CALLBACK_URL = "twitterapp://connect";
	
	// public static final String CALLBACK_URL =
	// "http://abhinavasblog.blogspot.com/";
	
	static String base_link_url = "http://www.google.co.in/";
	private static final String TWITTER_ACCESS_TOKEN_URL = "http://api.twitter.com/oauth/access_token";
	private static final String TWITTER_AUTHORZE_URL = "https://api.twitter.com/oauth/authorize";
	private static final String TWITTER_REQUEST_URL = "https://api.twitter.com/oauth/request_token";
	public static final String MESSAGE = "Hello Everyone...."
			+ "<a href= " + base_link_url + "</a>";

	public TwitterApp(Activity context) {
		this.m_context = context;
		
		m_searchResultList = new ArrayList<Status>();

		m_twitter = new TwitterFactory().getInstance();
		m_session = new TwitterSession(context);
		m_progressDlg = new ProgressDialog(context);

		m_progressDlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

		m_consumerKey = CONSUMER_KEY;
		m_secretKey = CONSUMER_SECRET;

		m_httpOauthConsumer = new CommonsHttpOAuthConsumer(m_consumerKey,
				m_secretKey);

		String request_url = TWITTER_REQUEST_URL;
		String access_token_url = TWITTER_ACCESS_TOKEN_URL;
		String authorize_url = TWITTER_AUTHORZE_URL;

		m_httpOauthprovider = new DefaultOAuthProvider(request_url,
				access_token_url, authorize_url);
		m_accessToken = m_session.getAccessToken();

		configureToken();
	}

	public void setListener(TwDialogListener listener) {
		m_twtDialogListener = listener;
	}

	private void configureToken() {
		if (m_accessToken != null) {
			m_twitter.setOAuthConsumer(m_consumerKey, m_secretKey);
			m_twitter.setOAuthAccessToken(m_accessToken);
		}
	}

	public boolean hasAccessToken() {
		return (m_accessToken == null) ? false : true;
	}

	public void resetAccessToken() {
		if (m_accessToken != null) {
			m_session.resetAccessToken();

			m_accessToken = null;
		}
	}

	public String getUsername() {
		return m_session.getUsername();
	}
	
	public void searchTweets(String a_query, OnDownloadComplete a_listener) {
		if(m_searchResultList.size() > 0)
		{
			m_searchResultList.clear();
		}
		m_downloadCompleteListener = a_listener;
		m_searchTask = new SearchTweetsTask();
		m_searchTask.execute(constructQuery(a_query));
	}

	public void updateStatus(String a_tweet, String a_tag)
	{
		if(m_postTweetTask != null)
		{
			m_postTweetTask.cancel(true);
			m_postTweetTask = null;
		}
		
		m_postTweetTask = new PostTweetTask();
		m_postTweetTask.execute(a_tweet+" "+a_tag);
	}
	

	public void uploadPic(File file, String message)
			throws Exception {
		TwitPic l_pic = new TwitPic();
		l_pic.upload(file);
	}

	public void authorize() {
		m_progressDlg.setMessage("Initializing ...");
		m_progressDlg.show();

		new Thread() {
			@Override
			public void run() {
				String authUrl = "";
				int what = 1;

				try {
					authUrl = m_httpOauthprovider.retrieveRequestToken(
							m_httpOauthConsumer, CALLBACK_URL);
					what = 0;
				} catch (Exception e) {
					e.printStackTrace();
				}
				mHandler.sendMessage(mHandler
						.obtainMessage(what, 1, 0, authUrl));
			}
		}.start();
	}

	public void processToken(String callbackUrl) {
		m_progressDlg.setMessage("Finalizing ...");
		m_progressDlg.show();

		final String verifier = getVerifier(callbackUrl);

		new Thread() {
			@Override
			public void run() {
				int what = 1;

				try {
					m_httpOauthprovider.retrieveAccessToken(m_httpOauthConsumer,
							verifier);

					m_accessToken = new AccessToken(
							m_httpOauthConsumer.getToken(),
							m_httpOauthConsumer.getTokenSecret());

					configureToken();

					User user = m_twitter.verifyCredentials();

					m_session.storeAccessToken(m_accessToken, user.getName());

					what = 0;
				} catch (Exception e) {
					e.printStackTrace();
				}

				mHandler.sendMessage(mHandler.obtainMessage(what, 2, 0));
			}
		}.start();
	}

	private String getVerifier(String callbackUrl) {
		String verifier = "";

		try {
			callbackUrl = callbackUrl.replace("twitterapp", "http");

			URL url = new URL(callbackUrl);
			String query = url.getQuery();

			String array[] = query.split("&");

			for (String parameter : array) {
				String v[] = parameter.split("=");

				if (URLDecoder.decode(v[0]).equals(
						oauth.signpost.OAuth.OAUTH_VERIFIER)) {
					verifier = URLDecoder.decode(v[1]);
					break;
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return verifier;
	}

	private void showLoginDialog(String url) {
		final TwDialogListener listener = new TwDialogListener() {

			public void onComplete(String value) {
				processToken(value);
			}

			public void onError(String value) {
				m_twtDialogListener.onError("Failed opening authorization page");
			}
		};

		new TwitterDialog(m_context, url, listener).show();
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			m_progressDlg.dismiss();

			if (msg.what == 1) {
				if (msg.arg1 == 1)
					m_twtDialogListener.onError("Error getting request token");
				else
					m_twtDialogListener.onError("Error getting access token");
			} else {
				if (msg.arg1 == 1)
					showLoginDialog((String) msg.obj);
				else
					m_twtDialogListener.onComplete("");
			}
		}
	};

	public interface TwDialogListener {
		public void onComplete(String value);

		public void onError(String value);
	}
	
	private void runSearchTask(QueryResult a_result) {
		if(m_searchTask != null)
		{
			m_searchTask.cancel(true);
			m_searchTask = null;
		}
		m_searchTask = new SearchTweetsTask();
		m_searchTask.execute(a_result.nextQuery());
	}

	private Query constructQuery(String a_query) {		
	    Query query = new Query(a_query);	
//	    query.setUntil("2013-06-15");
		query.setSince("2012-01-14");	 
//		query.setMaxId(100);
//		query.setCount(20);	 
		return query;		
	}
	
	private QueryResult triggerSearch(Query a_query) {
		QueryResult result = null;	        
        try {
			result = m_twitter.search(a_query);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
//			e.printStackTrace();
		}
		
		return result;
	}
	
	private class SearchTweetsTask extends AsyncTask<Query, Integer, QueryResult>
	{
		@Override
		protected QueryResult doInBackground(Query... params) {
			return triggerSearch(params[0]);
		}
		
		@Override
		protected void onPostExecute(QueryResult result) 
		{
//			m_progressBar.setVisibility(View.INVISIBLE);
			if((m_currentSearchPage == MAX_RESULT_PAGES) && (m_downloadCompleteListener != null) && (m_searchResultList.size() > 0))
			{
				m_downloadCompleteListener.onDownloadConplete(m_searchResultList);
			}
			else if(result != null)
			{
				List<twitter4j.Status> tweets = result.getTweets();
				for (twitter4j.Status tweet : tweets) {
//		            Log.d("FC", "@" + tweet.getUser().getScreenName() + " - " + tweet.getText());
		            m_searchResultList.add(tweet);
		        }
				
				if(m_extendedSearchTask != null)
				{
					m_extendedSearchTask.cancel(true);
					m_extendedSearchTask = null;
				}
				m_extendedSearchTask = new ExtendedSearchTask();
				m_extendedSearchTask.execute(result);
				
			}
			else
			{
				m_downloadCompleteListener.onDownloadConplete(m_searchResultList);
			}
			super.onPostExecute(result);
		}		
	}
	
	private class ExtendedSearchTask extends AsyncTask<QueryResult, Integer, Integer>
	{
		@Override
		protected Integer doInBackground(QueryResult... params) {
			m_currentSearchPage++;
			runSearchTask(params[0]);
			return 0;
		}
		
		@Override
		protected void onPostExecute(Integer result) {			
			super.onPostExecute(result);
		}		
	}
	
	private class PostTweetTask extends AsyncTask<String, Integer, Status>
	{
		@Override
		protected twitter4j.Status doInBackground(String... params) {
			// Posting Status
			twitter4j.Status status = null;
		    try {	    	
		        status = m_twitter.updateStatus(params[0]);		        
		    } catch (TwitterException e) {
		        e.printStackTrace();
		    }		
			return status;
		}
		
		@Override
		protected void onPostExecute(twitter4j.Status result) {		
			if(result != null)
			{
				Toast.makeText(m_context, m_context.getString(R.string.twitter_post_success), Toast.LENGTH_SHORT).show();
				if(m_downloadCompleteListener != null)
					m_downloadCompleteListener.onDownloadConplete(true);
//				searchTweets(m_currentQuerry,m_downloadCompleteListener);
			}
			else
			{
				Toast.makeText(m_context, m_context.getString(R.string.twitter_post_failed), Toast.LENGTH_SHORT).show();
				if(m_downloadCompleteListener != null)
					m_downloadCompleteListener.onDownloadConplete(false);
			}
			super.onPostExecute(result);
		}		
	}
	
	public void clean() 
	{
		if(m_searchTask != null)
		{
			m_searchTask.cancel(true);
			m_searchTask = null;
		}
		
		if(m_extendedSearchTask != null)
		{
			m_extendedSearchTask.cancel(true);
			m_extendedSearchTask = null;
		}
		
		m_searchResultList.clear();
		
		m_twitter = null;
		m_session = null;
		m_progressDlg = null;
		m_httpOauthprovider = null;
	}
}

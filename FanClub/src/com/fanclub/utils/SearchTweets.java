package com.fanclub.utils;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.fanclub.R;
import com.fanclub.observers.OnDownloadComplete;

public class SearchTweets {

	private static final String CONSUMER_KEY = "YlKpcJykFkprwvGcvcM9A";
	private static final String CONSUMER_SECRET = "FXWCa6TNF4KnsnJQz5JpPKyLykUt7cEca50t99lDN0";
	private static final String ACCESS_TOKEN = "135062013-mxbaG2AQjUZCD6ILiJLkSH5mTAhvOkKczeAUQXpg";
	private static final String ACCESS_TOKEN_SECRET = "dxTf61Are22zeW9POUv8gtkwyNsSXNcC4izihCAfo";
	
	private SearchTweetsTask m_searchTask = null;
	private ExtendedSearchTask m_extendedSearchTask = null;
	private PostTweetTask m_postTweetTask = null;
	
	private Twitter twitter;
	private OnDownloadComplete m_listener = null;	
	private ArrayList<Status> m_searchResultList = null;
	private final int MAX_RESULT_PAGES = 5;
	private int m_currentSearchPage = 0;
	private Context m_cont = null;
	
	public SearchTweets(OnDownloadComplete a_listener, Context a_cont) {
		twitter = new TwitterFactory().getInstance();
		m_cont = a_cont;
	    AccessToken accessToken = new AccessToken(ACCESS_TOKEN, ACCESS_TOKEN_SECRET);
	    twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
	    twitter.setOAuthAccessToken(accessToken);
	    
	    m_listener = a_listener;
	    m_searchResultList = new ArrayList<Status>();

	}
	
	public void postTweet(String a_tweet, String a_tag) 
	{
		if(m_postTweetTask != null)
		{
			m_postTweetTask.cancel(true);
			m_postTweetTask = null;
		}
		
		m_postTweetTask = new PostTweetTask();
		m_postTweetTask.execute(a_tweet+" "+a_tag);
		
	}
	
	public void startTweetSearch(String a_query) 
	{
		m_searchTask = new SearchTweetsTask();
		m_searchTask.execute(searchTweet(a_query));
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

	private Query searchTweet(String a_query) {		
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
			result = twitter.search(a_query);
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
			if((m_currentSearchPage == MAX_RESULT_PAGES) && (m_listener != null) && (m_searchResultList.size() > 0))
			{
				m_listener.onDownloadConplete(m_searchResultList);
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
				m_listener.onDownloadConplete(m_searchResultList);
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
		        status = twitter.updateStatus(params[0]);
		    } catch (TwitterException e) {
		        e.printStackTrace();
		    }		
			return status;
		}
		
		@Override
		protected void onPostExecute(twitter4j.Status result) {		
			if(result != null)
			{
				Toast.makeText(m_cont, m_cont.getString(R.string.twitter_post_success), Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(m_cont, m_cont.getString(R.string.twitter_post_failed), Toast.LENGTH_SHORT).show();
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
	}
}

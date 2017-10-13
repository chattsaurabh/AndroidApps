package com.fanclub;

import java.util.ArrayList;

import twitter4j.Status;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fanclub.adapter.FanwallAdapter;
import com.fanclub.observers.OnDownloadComplete;
import com.fanclub.observers.TweetAuthObserver;
import com.fanclub.twitter.TwitterApp;
import com.fanclub.utils.ConfigManager;
import com.fanclub.utils.FanClubConstants;


public class FanWallActivity extends Activity implements FanClubConstants, OnDownloadComplete, TweetAuthObserver{

	private ProgressBar m_progressBar = null;
	
	private ArrayList<Status> m_resultList = null;
	
	private ExpandableListView m_expandableList = null;
	private FanwallAdapter m_adapter = null;
	
	private EditText m_postEditText = null;
	private Button m_postBtn = null;
	
	private final String m_hashQuery = "#Sachin Tendulkar";
	
	private ConfigManager m_confMan = null;
	
//	twitter app
	
	private TwitterApp m_twitter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fan_wall);
		
		m_confMan = ConfigManager.getInstance();
		m_progressBar = (ProgressBar) findViewById(R.id.fanwallProgressBar);
		m_progressBar.setVisibility(View.VISIBLE);
		
		m_postEditText = (EditText) findViewById(R.id.fanwallPostTweetEditText);
		m_postBtn = (Button) findViewById(R.id.fanwallPostTweetBtn);
		
		m_expandableList = (ExpandableListView) findViewById(R.id.fanwallExpandableListView);
		
		ViewTreeObserver vto = m_expandableList.getViewTreeObserver();

	    vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	        @Override
	        public void onGlobalLayout() {
	        	m_expandableList.setIndicatorBounds(m_expandableList.getRight()- 60, m_expandableList.getWidth());
	        }
	    });
		
		m_postBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						m_progressBar.setVisibility(View.VISIBLE);
						m_twitter.updateStatus(m_postEditText.getText().toString(), m_hashQuery);
						m_postEditText.setText("");
						InputMethodManager imm = (InputMethodManager)getSystemService(
							      Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(m_postEditText.getWindowToken(), 0);
					}
				});
		
//		twitter app
		m_confMan.fetchTwitterInstance(this,this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fan_wall, menu);
		return true;
	}
	
	@Override
	public void onDownloadConplete(Object a_result) 
	{
		m_progressBar.setVisibility(View.INVISIBLE);
		if(a_result instanceof Boolean)
		{
			if(((Boolean)a_result))
			{
				Thread thread=  new Thread(){
			        @Override
			        public void run(){
			            try {
			                synchronized(this){
			                    wait(30*1000);
			                    m_twitter.searchTweets(m_hashQuery,FanWallActivity.this);
			                }
			            }
			            catch(InterruptedException ex){  
			            	ex.printStackTrace();
			            }

			            // TODO              
			        }
			    };

			    thread.start(); 
			}
		}
		else  
		{
			m_resultList = (ArrayList<Status>) a_result;
			if(m_resultList != null && m_resultList.size()>0)
			{
				for (Status tweet : m_resultList) 
				{
					Log.d("FC", "@" + tweet.getUser().getScreenName() + " - " + tweet.getText());            
				}

				if(m_adapter == null)
				{
					m_adapter = new FanwallAdapter(m_resultList, this);
					m_expandableList.setAdapter(m_adapter);
				}
				else
				{				
					m_adapter.setResultList(m_resultList);
					m_adapter.notifyDataSetChanged();
				}

			}
		}
	}
	
	@Override
	protected void onDestroy() {
		m_twitter.clean();
		m_adapter = null;
		super.onDestroy();
	}

	@Override
	public void onAuthenticationComplete(TwitterApp a_twitterAppInstance) {
		if(a_twitterAppInstance != null)
		{
			m_twitter = a_twitterAppInstance;
			m_twitter.searchTweets(m_hashQuery,this);
		}
		else 
		{
			Toast.makeText(this, getString(R.string.twitter_auth_failed), Toast.LENGTH_SHORT).show();
		}
		
		
	}

}

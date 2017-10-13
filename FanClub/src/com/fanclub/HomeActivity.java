package com.fanclub;

import com.fanclub.utils.ConfigManager;
import com.fanclub.utils.FanClubConstants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class HomeActivity extends Activity implements FanClubConstants{

	View homeVideosComp = null;
	View homeGossipComp = null;
	View homePhotosComp = null;
	View homeNewsComp = null;
	View homeFanClubComp = null;
	View homeProfileComp = null;
	
	private ConfigManager m_configMan = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		ConfigManager.m_context = this;
		m_configMan = ConfigManager.getInstance();
		
		homeVideosComp = (View) findViewById(R.id.homeVideoBtnComp);
		ImageButton l_videoBtn = (ImageButton) homeVideosComp.findViewById(R.id.homeComponentBtn);
		l_videoBtn.setImageResource(R.drawable.videos);
		l_videoBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(video_activity));
				
			}
		});
		TextView l_videoTitle = (TextView) homeVideosComp.findViewById(R.id.homeComponentTitle);
		l_videoTitle.setText(getResources().getString(R.string.home_video_button));
		
		homeGossipComp = (View) findViewById(R.id.homeGossipBtnComp);
		ImageButton l_gossipBtn = (ImageButton) homeGossipComp.findViewById(R.id.homeComponentBtn);
		l_gossipBtn.setImageResource(R.drawable.gossip);
		l_gossipBtn.setOnClickListener(new OnClickListener() {
			
				@Override
				public void onClick(View v) {
					Intent l_gossipIntent = new Intent(news_activity);
					l_gossipIntent.putExtra(NEWS_INTENT_TYPE, NEWS_DATA_ENUM.TYPE_GOSSIP.ordinal());
					startActivity(l_gossipIntent);
					
				}
			});
		TextView l_gossipTitle = (TextView) homeGossipComp.findViewById(R.id.homeComponentTitle);
		l_gossipTitle.setText(getResources().getString(R.string.home_gossip_button));
		
		homePhotosComp = (View) findViewById(R.id.homePhotosBtnComp);
		ImageButton l_photosBtn = (ImageButton) homePhotosComp.findViewById(R.id.homeComponentBtn);
		l_photosBtn.setImageResource(R.drawable.photos);
		l_photosBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent l_photoIntent = new Intent(photos_activity);
						l_photoIntent.putExtra(PHOTOS_INTENT_TYPE, PHOTOS_DATA_ENUM.TYPE_ALBUM.ordinal());
						startActivity(l_photoIntent);
						
					}
				});
		TextView l_photosTitle = (TextView) homePhotosComp.findViewById(R.id.homeComponentTitle);
		l_photosTitle.setText(getResources().getString(R.string.home_photos_button));
		
		homeNewsComp = (View) findViewById(R.id.homeNewsBtnComp);
		ImageButton l_newsBtn = (ImageButton) homeNewsComp.findViewById(R.id.homeComponentBtn);
		l_newsBtn.setImageResource(R.drawable.news);
		l_newsBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent l_newsIntent = new Intent(news_activity);
				l_newsIntent.putExtra(NEWS_INTENT_TYPE, NEWS_DATA_ENUM.TYPE_NEWS.ordinal());
				startActivity(l_newsIntent);
				
			}
		});
		TextView l_newsTitle = (TextView) homeNewsComp.findViewById(R.id.homeComponentTitle);
		l_newsTitle.setText(getResources().getString(R.string.home_news_button));
		
		homeFanClubComp = (View) findViewById(R.id.homeFanClubBtnComp);
		ImageButton l_fanClubBtn = (ImageButton) homeFanClubComp.findViewById(R.id.homeComponentBtn);
		l_fanClubBtn.setImageResource(R.drawable.comments);
		l_fanClubBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(fan_wall_activity));
			}
		});
		TextView l_fanClubTitle = (TextView) homeFanClubComp.findViewById(R.id.homeComponentTitle);
		l_fanClubTitle.setText(getResources().getString(R.string.home_fan_club_button));
		
		homeProfileComp = (View) findViewById(R.id.homeProfileBtnComp);
		ImageButton l_profileBtn = (ImageButton) homeProfileComp.findViewById(R.id.homeComponentBtn);
		l_profileBtn.setImageResource(R.drawable.generic_user);
		TextView l_profileTitle = (TextView) homeProfileComp.findViewById(R.id.homeComponentTitle);
		l_profileTitle.setText(getResources().getString(R.string.home_profile_button));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

}

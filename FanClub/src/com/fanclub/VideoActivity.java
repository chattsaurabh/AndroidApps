package com.fanclub;

import android.os.Bundle;
import android.view.GestureDetector.OnGestureListener;

import com.fanclub.abs.AbsPannerActivity;
import com.fanclub.custom_views.CustomViewFlipper;
import com.fanclub.custom_views.VideoView;
import com.fanclub.model.VideoModel;
import com.fanclub.utils.ConfigManager;
import com.fanclub.utils.FanClubConstants;

public class VideoActivity extends AbsPannerActivity implements  OnGestureListener, FanClubConstants{

	private ConfigManager m_configMan = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_video);
		m_configMan = ConfigManager.getInstance();		
		m_dataModel = m_configMan.getVideoModel();
		
		flipper = (CustomViewFlipper)findViewById(R.id.videosViewFlipper);
		flipper.setMaxScrollRange(5);
		
		super.onCreate(savedInstanceState);        	
    	
		addPage();
	}
	
	protected void addPage() {
		super.addPage();
		switch (m_currentPageIndex) {
		case PAGE_ONE:	
			m_currentPage = new VideoView(this, m_firstPageLinLayout);
			break;
		case PAGE_TWO:
			m_currentPage = new VideoView(this, m_secondPageLinLayout);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		m_configMan.clearVideoData();
	}
}

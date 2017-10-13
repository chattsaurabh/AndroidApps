package com.fanclub;

import android.os.Bundle;
import android.view.Menu;

import com.fanclub.abs.AbsPannerActivity;
import com.fanclub.custom_views.CustomViewFlipper;
import com.fanclub.custom_views.NewsView;
import com.fanclub.model.NewsModel;
import com.fanclub.utils.ConfigManager;
import com.fanclub.utils.FanClubConstants;

public class NewsActivity extends AbsPannerActivity implements FanClubConstants  {

	private ConfigManager m_configMan = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_news);
		
		int l_mode = getIntent().getIntExtra(NEWS_INTENT_TYPE, -1);
		m_configMan = ConfigManager.getInstance();		
		m_dataModel = m_configMan.getNewsModel();
		NEWS_DATA_ENUM l_enumMode = (l_mode == NEWS_DATA_ENUM.TYPE_NEWS.ordinal())?NEWS_DATA_ENUM.TYPE_NEWS : NEWS_DATA_ENUM.TYPE_GOSSIP;
		((NewsModel) m_dataModel).setDataMode(l_enumMode);
		
		flipper = (CustomViewFlipper)findViewById(R.id.newsViewFlipper);
		flipper.setMaxScrollRange(5);
		
		super.onCreate(savedInstanceState);        	
    	
		addPage();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news, menu);
		return true;
	}

	protected void addPage() {
		super.addPage();
		switch (m_currentPageIndex) {
		case PAGE_ONE:	
			m_currentPage = new NewsView(this, m_firstPageLinLayout);
			break;
		case PAGE_TWO:
			m_currentPage = new NewsView(this, m_secondPageLinLayout);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		m_configMan.clearNewsData();
	}
}

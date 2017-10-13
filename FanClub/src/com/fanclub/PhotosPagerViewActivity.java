package com.fanclub;

import com.fanclub.abs.AbsPannerActivity;
import com.fanclub.custom_views.CustomViewFlipper;
import com.fanclub.custom_views.PhotosPagerView;
import com.fanclub.custom_views.PhotosView;
import com.fanclub.model.PhotosElementModel;
import com.fanclub.model.PhotosPagerModel;
import com.fanclub.utils.ConfigManager;
import com.fanclub.utils.FanClubConstants;
import com.fanclub.utils.ImageDownloader;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ImageView;

public class PhotosPagerViewActivity extends AbsPannerActivity implements FanClubConstants{

	private ConfigManager m_configMan = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		setContentView(R.layout.activity_photos_pager_view);
		
		m_configMan = ConfigManager.getInstance();
		m_dataModel = m_configMan.getPhotosPagerModel();
		
		String l_currentFileName = getIntent().getStringExtra(PHOTOS_INTENT_ELEMENT_FILE_NAME);
		int l_currentIdx = getIntent().getIntExtra(PHOTOS_INTENT_ELEMENT_PAGER_IDX, -1);
		((PhotosPagerModel) m_dataModel).setCurrentIndex(l_currentIdx);
		((PhotosPagerModel) m_dataModel).setModelFile(l_currentFileName);
//		((PhotosPagerModel) m_dataModel).setMetaDataFile(m_configMan.getPhotosElementModel().getMetaDataFile());
		flipper = (CustomViewFlipper)findViewById(R.id.photosPagerViewFlipper);
		flipper.setMaxScrollRange(5);
		
		super.onCreate(savedInstanceState);
		
		addPage();
//		m_imageDownloader.download(l_url, m_imgView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photos_pager_view, menu);
		return true;
	}
	
	protected void addPage() {
		super.addPage();
		switch (m_currentPageIndex) {
		case PAGE_ONE:	
			m_currentPage = new PhotosPagerView(this, m_firstPageLinLayout);
			break;
		case PAGE_TWO:
			m_currentPage = new PhotosPagerView(this, m_secondPageLinLayout);
			break;
		default:
			break;
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		m_configMan.clearPhotosPagerData();
	}

}

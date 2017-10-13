package com.fanclub;

import android.os.Bundle;
import android.view.Menu;

import com.fanclub.abs.AbsPannerActivity;
import com.fanclub.custom_views.CustomViewFlipper;
import com.fanclub.custom_views.PhotosView;
import com.fanclub.custom_views.VideoView;
import com.fanclub.model.PhotosElementModel;
import com.fanclub.utils.ConfigManager;
import com.fanclub.utils.FanClubConstants;
import com.fanclub.utils.FanClubConstants.NEWS_DATA_ENUM;

public class PhotosActivity extends AbsPannerActivity implements FanClubConstants {

	private ConfigManager m_configMan = null;
	private PHOTOS_DATA_ENUM m_enumMode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_photos);
		
		int l_mode = getIntent().getIntExtra(PHOTOS_INTENT_TYPE, -1);
		m_configMan = ConfigManager.getInstance();		
		
		m_enumMode = (l_mode == PHOTOS_DATA_ENUM.TYPE_ALBUM.ordinal())?PHOTOS_DATA_ENUM.TYPE_ALBUM : PHOTOS_DATA_ENUM.TYPE_ELEMENT;
		
		switch (m_enumMode) {
		case TYPE_ALBUM:
			m_dataModel = m_configMan.getPhotosModel();
			PhotosView.m_mode = PHOTOS_DATA_ENUM.TYPE_ALBUM;
			break;
		case TYPE_ELEMENT:
			m_dataModel = m_configMan.getPhotosElementModel();
			PhotosView.m_mode = PHOTOS_DATA_ENUM.TYPE_ELEMENT;
			String l_filename = getIntent().getStringExtra(PHOTOS_INTENT_ELEMENT_FILE_NAME);
			((PhotosElementModel) m_dataModel).setModelFile(BASE_JSON_FILE+l_filename);
			break;
		default:
			break;
		}
		
		flipper = (CustomViewFlipper)findViewById(R.id.photosViewFlipper);
		flipper.setMaxScrollRange(5);
		
		super.onCreate(savedInstanceState);        	
    	
		addPage();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photos, menu);
		return true;
	}
	
	protected void addPage() {
		super.addPage();
		switch (m_currentPageIndex) {
		case PAGE_ONE:	
			m_currentPage = new PhotosView(this, m_firstPageLinLayout);
			break;
		case PAGE_TWO:
			m_currentPage = new PhotosView(this, m_secondPageLinLayout);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		switch (m_enumMode) {
		case TYPE_ALBUM:
			m_configMan.clearPhotosData();
			break;
		case TYPE_ELEMENT:
			m_configMan.clearPhotosElementsData();
			break;
		default:
			break;
		}
	}

}

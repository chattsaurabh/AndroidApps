package com.fanclub.custom_views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.fanclub.R;
import com.fanclub.abs.AbsPannerItem;
import com.fanclub.data.PhotoEntityVO;
import com.fanclub.utils.FanClubConstants;
import com.fanclub.utils.FanClubConstants.PHOTOS_DATA_ENUM;

public class PhotoItem extends AbsPannerItem implements FanClubConstants{

	
//	private DownloadBmp m_downloadBmpTask = null;
	
	public PhotoItem(Context context, ViewGroup a_root, PhotoEntityVO a_entity) {
		super(context, a_root, a_entity);
	}

	public PhotoItem(Context context, AttributeSet attrs, ViewGroup a_root, PhotoEntityVO a_entity) {
		super(context, attrs, a_root, a_entity);
	}
	
	public PhotoItem(Context context, AttributeSet attrs, int defStyle, ViewGroup a_root, PhotoEntityVO a_entity) {
		super(context, attrs, defStyle, a_root, a_entity);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub

	}
	
	protected void initView(final Context a_cont, ViewGroup a_root) {
		
		final PhotoEntityVO l_currentEntityVO = (PhotoEntityVO) m_entity;
		
		LayoutInflater infl = (LayoutInflater)a_cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		m_view = (ViewGroup) infl.inflate(R.layout.photo_item, a_root, true);
		
		if(l_currentEntityVO.getMode() == PHOTOS_DATA_ENUM.TYPE_ALBUM)
		{
			TextView l_albumDescTextView = (TextView) m_view.findViewById(R.id.photoItemDesc);
			l_albumDescTextView.setVisibility(View.VISIBLE);
			l_albumDescTextView.setSelected(true);
			l_albumDescTextView.setText(l_currentEntityVO.getTitle());
		}
		
		m_thumbnailImageView = (ImageView) m_view.findViewById(R.id.photoItemThumbnailImgView);
		
		m_thumbnailImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (l_currentEntityVO.getMode()) {
				case TYPE_ALBUM:
					Intent l_photoIntent = new Intent(photos_activity);
					l_photoIntent.putExtra(PHOTOS_INTENT_TYPE, PHOTOS_DATA_ENUM.TYPE_ELEMENT.ordinal());
					l_photoIntent.putExtra(PHOTOS_INTENT_ELEMENT_FILE_NAME, l_currentEntityVO.getPhotosFile_album());
					a_cont.startActivity(l_photoIntent);
					break;
				case TYPE_ELEMENT:
					Intent l_pagerIntent = new Intent(photos_pager_activity);										
					l_pagerIntent.putExtra(PHOTOS_INTENT_ELEMENT_FILE_NAME, l_currentEntityVO.getPhotosFile_album());
					l_pagerIntent.putExtra(PHOTOS_INTENT_ELEMENT_PAGER_IDX, l_currentEntityVO.getCurrentPhotoIdx());
					a_cont.startActivity(l_pagerIntent);
					break;
				default:
					break;
				}
				
			}
		});		
	}

}

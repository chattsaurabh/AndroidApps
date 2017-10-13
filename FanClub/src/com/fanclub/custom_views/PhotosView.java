package com.fanclub.custom_views;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fanclub.R;
import com.fanclub.abs.AbsPannerView;
import com.fanclub.data.PhotoEntityVO;
import com.fanclub.model.PhotosModel;
import com.fanclub.utils.ConfigManager;
import com.fanclub.utils.FanClubConstants;

public class PhotosView extends AbsPannerView implements FanClubConstants{

	public static PHOTOS_DATA_ENUM m_mode;
	
	public PhotosView(Context context, ViewGroup a_root) {
		super(context, a_root);
	}
	
	public PhotosView(Context context, AttributeSet attrs, ViewGroup a_root) {
		super(context, attrs, a_root);
	}
	
	public PhotosView(Context context, AttributeSet attrs, int defStyle, ViewGroup a_root) {
		super(context, attrs, defStyle, a_root);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		
	}
	
	protected void initView(Context a_cont, ViewGroup a_root) {
		m_confMan = ConfigManager.getInstance();
		LayoutInflater infl = (LayoutInflater)a_cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		m_view = (ViewGroup) infl.inflate(R.layout.photos_view, a_root, true);
		
		switch (m_mode) {
		case TYPE_ALBUM:			
			setPhotosItems(a_cont, m_confMan.getPhotosModel().getPhotosDataList());
			break;
		case TYPE_ELEMENT:
			setPhotosItems(a_cont, m_confMan.getPhotosElementModel().getPhotosElementsDataList());
			break;
		default:
			break;
		}
	}
	
	private void setPhotosItems(Context a_cont, ArrayList<PhotoEntityVO> a_photosDataList) 
	{
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);		
		LinearLayout l_topLL = (LinearLayout) m_view.findViewById(R.id.photosViewTopLL);
		
		int l_dataEndTopLL = 4;		
		if(a_photosDataList.size() < l_dataEndTopLL)
		{
			l_dataEndTopLL = a_photosDataList.size();
		}
		
		for(int i = 0; i<l_dataEndTopLL; i++)
		{
			LinearLayout l_coverView = new LinearLayout(a_cont);
			l_coverView.setLayoutParams(lp);
			l_topLL.addView(l_coverView);
			if(m_mode == PHOTOS_DATA_ENUM.TYPE_ELEMENT)
			{
				a_photosDataList.get(i).setCurrentPhotoIdx(m_confMan.getPhotosElementModel().getCurrentIndex() + i);
			}
			PhotoItem l_photoItem = new PhotoItem(a_cont, l_coverView, a_photosDataList.get(i));
			downloadImage(a_photosDataList.get(i).getThumbnailUrl(), l_photoItem.getImageView());
		}
		
		int l_dataEndMidLL = 8;
		if(a_photosDataList.size() < l_dataEndMidLL)
		{
			l_dataEndMidLL = a_photosDataList.size();
		}
		LinearLayout l_midLL = (LinearLayout) m_view.findViewById(R.id.photosViewMiddleLL);
		for(int i = 4; i<l_dataEndMidLL; i++)
		{
			LinearLayout l_coverView = new LinearLayout(a_cont);
			l_coverView.setLayoutParams(lp);
			l_midLL.addView(l_coverView);
			if(m_mode == PHOTOS_DATA_ENUM.TYPE_ELEMENT)
			{
				a_photosDataList.get(i).setCurrentPhotoIdx(m_confMan.getPhotosElementModel().getCurrentIndex() + i);
			}
			PhotoItem l_photoItem = new PhotoItem(a_cont, l_coverView, a_photosDataList.get(i));
			downloadImage(a_photosDataList.get(i).getThumbnailUrl(), l_photoItem.getImageView());
		}
		
		int l_dataEndBottomLL = 12;
		if(a_photosDataList.size() < l_dataEndBottomLL)
		{
			l_dataEndBottomLL = a_photosDataList.size();
		}
		LinearLayout l_bottomLL = (LinearLayout) m_view.findViewById(R.id.photosViewBottomLL);
		for(int i = 8; i<l_dataEndBottomLL; i++)
		{
			LinearLayout l_coverView = new LinearLayout(a_cont);
			l_coverView.setLayoutParams(lp);
			l_bottomLL.addView(l_coverView);
			if(m_mode == PHOTOS_DATA_ENUM.TYPE_ELEMENT)
			{
				a_photosDataList.get(i).setCurrentPhotoIdx(m_confMan.getPhotosElementModel().getCurrentIndex() + i);
			}
			PhotoItem l_photoItem = new PhotoItem(a_cont, l_coverView, a_photosDataList.get(i));
			downloadImage(a_photosDataList.get(i).getThumbnailUrl(), l_photoItem.getImageView());
		}
	}
	
	public void clean() {
		m_view.removeAllViews();
		m_view = null;
	}
	

}

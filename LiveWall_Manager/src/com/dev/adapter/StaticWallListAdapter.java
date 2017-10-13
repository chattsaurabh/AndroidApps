package com.dev.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dev.backend.ImageFetcher;
import com.dev.data.ImageEntity;

public class StaticWallListAdapter extends BaseAdapter{

//	private Context m_context = null;
	private ArrayList<ImageEntity> m_imageEntityList = null;
	
	public StaticWallListAdapter(Context a_context) {
//		m_context = a_context;
		m_imageEntityList = ImageFetcher.getInstance().getImageList();
	}
	@Override
	public int getCount() {		
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int position) {		
		return checkPos(position);
	}

	@Override
	public long getItemId(int position) {
		return checkPos(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		m_imageEntityList = ImageFetcher.getInstance().getImageList();
//		int pos = checkPos(position);
//
//		LayoutInflater li = (LayoutInflater) m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		convertView = (View) li.inflate(R.layout.video_filmstrip_item, null);
		
//		ImageView iv1 = (ImageView) convertView.findViewById(R.id.VideoPageWidgetFilmStripGalleryImageView1);
//		iv1.setImageURI(m_imageEntityList.get(pos).getM_imgUri());
		return convertView;
	}
	
	private int checkPos(int position) {		
		if (position >= m_imageEntityList.size()) { 
            position = position % m_imageEntityList.size(); 
        }
		
		int pos = position % m_imageEntityList.size();
		
		if (pos < 0)
			pos = pos + m_imageEntityList.size();
		
		return pos;
	}

}

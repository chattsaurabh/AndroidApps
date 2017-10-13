package com.adapter;



import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.backend.VideoListCreator;
import com.data.MediaEntity;
import com.dev.R;

public class VideoPageGallerAdapter extends BaseAdapter {

	int GalItemBg = 0;
	private Context cont;
	private  ArrayList<MediaEntity> currentVideoList;
	
	// Adding images.
	/*private Integer[] Imgid = { R.drawable.linkinpark_000,
								R.drawable.guitar_fender,
								R.drawable.dj_splash,
								R.drawable.xpress_splash};	*/
	
	public VideoPageGallerAdapter(Context c) {
		cont = c;
		currentVideoList = VideoListCreator.getInstance().getVideoList();
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

		currentVideoList = VideoListCreator.getInstance().getVideoList();
		int pos = checkPos(position);

		LayoutInflater li = (LayoutInflater) cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = (View) li.inflate(R.layout.video_filmstrip_item, null);
		
		ImageView iv1 = (ImageView) convertView.findViewById(R.id.VideoPageWidgetFilmStripGalleryImageView1);
		iv1.setImageResource(currentVideoList.get(pos).getTempThumbnailImage());
		return convertView;
	}
	
	private int checkPos(int position) {		
		if (position >= currentVideoList.size()) { 
            position = position % currentVideoList.size(); 
        }
		
		int pos = position % currentVideoList.size();
		
		if (pos < 0)
			pos = pos + currentVideoList.size();
		
		return pos;
	}
}

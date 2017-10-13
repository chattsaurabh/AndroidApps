package com.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.backend.AudioListCreator;
import com.backend.ImageCumulator;
import com.constants.XStream_Constants;
import com.data.MediaEntity;
import com.dev.R;

public class SearchListAdapter extends ArrayAdapter<MediaEntity> implements XStream_Constants {
	private Context context;
	private ImageView mediaIcon;
	private TextView mediaName;
	private ViewSwitcher mediaIconViewSwithcer;
	private List<MediaEntity> mediaList = new ArrayList<MediaEntity>();
	private SearchListAdapter instance = null;
	private ImageCumulator imgCum = null;
	private String mediaType = null;

	public SearchListAdapter(Context context, int textViewResourceId,
			List<MediaEntity> objects, String type) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.mediaList = objects;
		mediaType = type;
		imgCum = new ImageCumulator();
	}
	
	public int getCount() {
		return this.mediaList.size();
	}

	public MediaEntity getItem(int index) {
		return this.mediaList.get(index);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		instance = this;
		View row = convertView;
		if (row == null) {
			// ROW INFLATION
			LayoutInflater inflater = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.search_list_item, parent, false);			
		}

		// Get item
		MediaEntity media = getItem(position);
		
		mediaIconViewSwithcer = (ViewSwitcher) row.findViewById(R.id.SearchElementViewSwitcher);
		mediaIconViewSwithcer.setDisplayedChild(0);
		// Get reference to ImageView
		mediaIcon = (ImageView) row.findViewById(R.id.SearchElementIconImageView);

		// Get reference to TextView - media_name
		mediaName = (TextView) row.findViewById(R.id.SearchElementTextView);

		//Set media name
		mediaName.setText(media.getMedia_title());

		imgCum.addImageRequest(mediaIcon, media.getMedia_thumbnailUri(), context, mediaIconViewSwithcer);
		
		return row;
	}
	
	

}

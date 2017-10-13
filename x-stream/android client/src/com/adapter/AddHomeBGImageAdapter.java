package com.adapter;

import com.dev.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class AddHomeBGImageAdapter extends BaseAdapter {

	int GalItemBg = 0;
	private Context cont;
	
//	Adding Images
	private Integer[] Imgid = { R.drawable.dj_splash,
								R.drawable.guitar_fender,
								R.drawable.hp2_splash,
								R.drawable.hp_splash,
								R.drawable.linkinpark_000};
	
	public AddHomeBGImageAdapter(Context c) {
		cont = c;
//		TypedArray typArray = obtainStyledAttributes(R.styleable.GalleryTheme);
//		GalItemBg = typArray.getResourceId(R.styleable.GalleryTheme_android_galleryItemBackground, 0);
//		typArray.recycle();
		}
	
	@Override
	public int getCount() {
		return Imgid.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		 LayoutInflater vi = (LayoutInflater)cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);        
//         convertView = (View) vi.inflate(R.layout.custom_home_bg_image_view, null);
//         
//         ImageView imgView = (ImageView) convertView.findViewById(R.id.HomeCustomViewImageView);
// 		 imgView.setImageResource(Imgid[position]);
// 		 imgView.setBackgroundResource(GalItemBg);
 		 
 		
 		 return convertView;
	}

}

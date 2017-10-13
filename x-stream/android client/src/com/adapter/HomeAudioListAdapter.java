package com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dev.R;

public class HomeAudioListAdapter extends BaseAdapter {

	private Context cont;
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 5;
	}

	public HomeAudioListAdapter(Context c) {
			cont = c;
		}
	
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		  LayoutInflater vi = (LayoutInflater)cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);        
	      convertView = (View) vi.inflate(R.layout.home_list_item, null);
	      return convertView;
	}

}

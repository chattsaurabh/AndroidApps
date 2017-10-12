package com.chicago.library.model;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chicago.library.R;
import com.chicago.library.utils.ConfigurationManager;

public class LibraryListAdapter extends BaseAdapter{
	
	private Context mContext = null;
	private ArrayList<LibraryVO> mDataList = null;
	
	public LibraryListAdapter(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mDataList = ConfigurationManager.getInstance().getDataList();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
            convertView = LayoutInflater.from(mContext).
                inflate(R.layout.lib_content_item, parent, false);
        }
		LibraryVO currentItem = mDataList.get(position);
		TextView lNameView = (TextView)convertView.findViewById(R.id.item_name);
		lNameView.setText(currentItem.getName());
		return convertView;
	}

}

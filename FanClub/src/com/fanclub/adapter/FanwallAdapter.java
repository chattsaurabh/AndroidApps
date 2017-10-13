package com.fanclub.adapter;

import java.util.ArrayList;

import twitter4j.Status;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanclub.R;
import com.fanclub.utils.ImageDownloader;

public class FanwallAdapter extends BaseExpandableListAdapter {

	private ArrayList<Status> m_statusList;
	private LayoutInflater m_inflater;
	private Context m_cont;
	private ImageDownloader m_imageLoader;
	
	public FanwallAdapter(ArrayList<Status> a_resultList, Context a_cont) {
		m_statusList = new ArrayList<Status>();
		setResultList(a_resultList);
		m_cont = a_cont;
		m_imageLoader = new ImageDownloader(a_cont);
		m_inflater = (LayoutInflater)a_cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void setResultList(ArrayList<Status> a_resultList) {
		m_statusList.clear();
		for (Status status : a_resultList) {
			m_statusList.add(status);
		}		
	}
	
	@Override
	public Object getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			   boolean isLastChild, View convertView, ViewGroup parent) {
		
		Status l_currentTweet = m_statusList.get(groupPosition);
		
		if (convertView == null) 
		{
		   convertView = m_inflater.inflate(R.layout.fanwall_expandable_list_child, null);
	    }
		ImageView l_imgV = (ImageView) convertView.findViewById(R.id.expandableListChildItemImgView);
		m_imageLoader.download(l_currentTweet.getUser().getBiggerProfileImageURL(), l_imgV);
		
		TextView l_textV = (TextView) convertView.findViewById(R.id.expandableListChildTextView);
		l_textV.setText(l_currentTweet.getCreatedAt().toString()+"   "+l_currentTweet.getUser().getDescription());
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub
//		return m_statusList.size();
		return 1;
	}

	@Override
	public Object getGroup(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return m_statusList.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		Status l_currentTweet = m_statusList.get(groupPosition);
		if (convertView == null) 
		{
		   convertView = m_inflater.inflate(R.layout.fanwall_expandable_list_group, null);
	    }	
		
//		ImageView l_imgV = (ImageView) convertView.findViewById(R.id.expandableListItemImgView);
//		m_imageLoader.download(l_currentTweet.getUser().getMiniProfileImageURL(), l_imgV);
		
		TextView l_textV = (TextView) convertView.findViewById(R.id.expandableListTextView);
		l_textV.setText(l_currentTweet.getText());
		
		LinearLayout l_containerLL = (LinearLayout) convertView.findViewById(R.id.expandableListItemContainerLL);
		if (groupPosition % 2 == 1) {
			l_containerLL.setBackgroundColor(Color.LTGRAY);
			l_textV.setTextColor(Color.BLACK);
		} else {
			l_containerLL.setBackgroundColor(Color.DKGRAY);
			l_textV.setTextColor(Color.WHITE);
		}

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}

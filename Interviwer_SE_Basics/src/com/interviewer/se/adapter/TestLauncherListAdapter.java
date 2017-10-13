package com.interviewer.se.adapter;

import interviewer.se.basics.R;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TestLauncherListAdapter extends BaseAdapter {

	private ArrayList<String> m_topicList = null;
	private Context m_cont;
	private LayoutInflater m_inflater;
	
	public TestLauncherListAdapter(Context a_cont, ArrayList<String> a_topicList) 
	{
		m_cont = a_cont;
		m_topicList = a_topicList;
		m_inflater = (LayoutInflater)a_cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return m_topicList.size();
	}

	@Override
	public Object getItem(int arg0) {
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
		if (convertView == null) 
		{
		   convertView = m_inflater.inflate(R.layout.test_launcher_list_item, null);
	    }
		
		TextView l_tv = (TextView) convertView.findViewById(R.id.testLauncherListItemTV);
		l_tv.setText(m_topicList.get(position));
				
		return convertView;
	}

}

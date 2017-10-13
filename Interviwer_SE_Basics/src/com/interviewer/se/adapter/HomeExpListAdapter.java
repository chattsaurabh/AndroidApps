package com.interviewer.se.adapter;

import interviewer.se.basics.R;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.interviewer.se.data.HomeVO;
import com.interviewer.se.model.HomeModel;

public class HomeExpListAdapter extends BaseExpandableListAdapter {

	private HomeModel m_homeModel = null;
	private LayoutInflater m_inflater;
	private Context m_cont;
	
	public HomeExpListAdapter(HomeModel a_homeModel, Context a_cont) {
		m_homeModel = a_homeModel;
		m_cont = a_cont;
		m_inflater = (LayoutInflater)a_cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		HomeVO l_currentEntity = m_homeModel.getHomeDataList().get(groupPosition);
		if (convertView == null) 
		{
		   convertView = m_inflater.inflate(R.layout.home_expandable_list_child, null);
	    }
		
		TextView l_textV = (TextView) convertView.findViewById(R.id.expandableListChildTextView);
		Spanned s = Html.fromHtml(l_currentEntity.getDesc());
		l_textV.setText(s);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return m_homeModel.getHomeDataList().size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		HomeVO l_currentEntity = m_homeModel.getHomeDataList().get(groupPosition);
		if (convertView == null) 
		{
		   convertView = m_inflater.inflate(R.layout.home_expandable_list_group, null);
	    }
		
		TextView l_textV = (TextView) convertView.findViewById(R.id.expandableListGroupTextView);
		l_textV.setText(l_currentEntity.getHeading());
		
		/*LinearLayout l_containerLL = (LinearLayout) convertView.findViewById(R.id.expandableListItemContainerLL);
		if (groupPosition % 2 == 1) {
			l_containerLL.setBackgroundColor(Color.LTGRAY);
			l_textV.setTextColor(Color.BLACK);
		} else {
			l_containerLL.setBackgroundColor(Color.DKGRAY);
			l_textV.setTextColor(Color.WHITE);
		}*/
		
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	
}

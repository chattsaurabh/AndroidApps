package com.fanclub.custom_views;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.fanclub.R;
import com.fanclub.abs.AbsPannerView;
import com.fanclub.data.NewsEntityVO;
import com.fanclub.data.VideoEntityVO;
import com.fanclub.model.NewsModel;
import com.fanclub.model.VideoModel;
import com.fanclub.utils.ConfigManager;

public class NewsView  extends AbsPannerView{

	private NewsModel m_newsModel = null;
	
	public NewsView(Context context, ViewGroup a_root) {
		super(context, a_root);
	}
	
	public NewsView(Context context, AttributeSet attrs, ViewGroup a_root) {
		super(context, attrs, a_root);
	}
	
	public NewsView(Context context, AttributeSet attrs, int defStyle, ViewGroup a_root) {
		super(context, attrs, defStyle, a_root);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		
	}
	
	protected void initView(Context a_cont, ViewGroup a_root) {
		m_confMan = ConfigManager.getInstance();
		m_newsModel = m_confMan.getNewsModel();
		ArrayList<NewsEntityVO> l_newsDataList = m_newsModel.getNewsDataList();
		
		LayoutInflater infl = (LayoutInflater)a_cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		m_view = (ViewGroup) infl.inflate(R.layout.news_view, a_root, true);
		
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);		
		LinearLayout l_contentLL = (LinearLayout) m_view.findViewById(R.id.newsViewContentLL);
		
		int l_dataEndContentLL = 3;
		if(l_newsDataList.size() < l_dataEndContentLL)
		{
			l_dataEndContentLL = l_newsDataList.size();
		}
		
		for(int i = 0; i<l_dataEndContentLL; i++)
		{
			LinearLayout l_coverView = new LinearLayout(a_cont);
			l_coverView.setLayoutParams(lp);
			l_contentLL.addView(l_coverView);
			NewsItem l_newsItem = new NewsItem(a_cont, l_coverView, l_newsDataList.get(i));
			downloadImage(l_newsDataList.get(i).getThumb(), l_newsItem.getImageView());
		}	
		
	}
	
	public void clean() {
		m_view.removeAllViews();
		m_view = null;
	}
	
}

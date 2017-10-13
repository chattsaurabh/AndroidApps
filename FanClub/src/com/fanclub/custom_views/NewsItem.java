package com.fanclub.custom_views;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.fanclub.R;
import com.fanclub.abs.AbsPannerItem;
import com.fanclub.data.NewsEntityVO;
import com.fanclub.data.VideoEntityVO;
import com.fanclub.utils.FanClubConstants;

public class NewsItem  extends AbsPannerItem implements FanClubConstants{

	private TextView m_contentDesc = null;
	
//	private DownloadBmp m_downloadBmpTask = null;
	
	public NewsItem(Context context, ViewGroup a_root, NewsEntityVO a_entity) {
		super(context, a_root, a_entity);
	}

	public NewsItem(Context context, AttributeSet attrs, ViewGroup a_root, NewsEntityVO a_entity) {
		super(context, attrs, a_root, a_entity);
	}
	
	public NewsItem(Context context, AttributeSet attrs, int defStyle, ViewGroup a_root, NewsEntityVO a_entity) {
		super(context, attrs, defStyle, a_root, a_entity);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub

	}
	
	protected void initView(Context a_cont, ViewGroup a_root) {
		final NewsEntityVO l_currentEntityVO = (NewsEntityVO) m_entity;
		m_cont = a_cont;
		LayoutInflater infl = (LayoutInflater)a_cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		m_view = (ViewGroup) infl.inflate(R.layout.news_item, a_root, true);
		
		
		m_contentDesc = (TextView) m_view.findViewById(R.id.newsDescTextView);
		m_contentDesc.setText(l_currentEntityVO.getNewsDesc());
		
		m_thumbnailImageView = (ImageView) m_view.findViewById(R.id.newsItemThumbnailImgView);
		
		m_thumbnailImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				launchWebView(l_currentEntityVO);
				
			}
		});
		
		TextView l_newsDesc = (TextView) m_view.findViewById(R.id.newsDescTextView);
		l_newsDesc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				launchWebView(l_currentEntityVO);				
			}
		});
		
		/*if(m_downloadBmpTask != null)
		{
			m_downloadBmpTask.cancel(true);
			m_downloadBmpTask = null;
		}
		m_thumbnailViewSwitcher.setDisplayedChild(0);
		m_downloadBmpTask = new DownloadBmp();
//		m_downloadBmpTask.execute("http://image10.bizrate-images.com/resize?sq=60&uid=2216744464");
//		m_downloadBmpTask.execute("http://i472.photobucket.com/albums/rr84/harleypetebc/100x100.jpg");
		m_downloadBmpTask.execute(m_entity.getThumbnailUrl());*/
		
	}
	

	private void launchWebView(NewsEntityVO a_newsEntity) {
		Intent l_webIntent = new Intent(custom_web_view_activity);
		l_webIntent.putExtra(WEB_VIEW_URL, a_newsEntity.getNewsUrl());
		l_webIntent.putExtra(WEB_VIEW_TITLE, a_newsEntity.getTitle());
		m_cont.startActivity(l_webIntent);
	}

}
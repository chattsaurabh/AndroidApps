package com.fanclub.abs;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.fanclub.R;
import com.fanclub.data.VideoEntityVO;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

public abstract class AbsPannerItem  extends ViewGroup{
	
	protected ViewGroup m_view = null;
	protected Context m_cont = null;
	protected ImageView m_thumbnailImageView = null;
//	protected ViewSwitcher m_thumbnailViewSwitcher = null;
	protected AbsEntityVO m_entity = null;

	public AbsPannerItem(Context context, ViewGroup a_root, AbsEntityVO a_entity) 
	{
		super(context);
		m_cont = context;
		m_entity = a_entity;
		initView(context, a_root);
	}

	public AbsPannerItem(Context context, AttributeSet attrs, ViewGroup a_root, AbsEntityVO a_entity) {
		super(context, attrs);
		m_cont = context;
		m_entity = a_entity;
		initView(context, a_root);
	}
	
	public AbsPannerItem(Context context, AttributeSet attrs, int defStyle, ViewGroup a_root, AbsEntityVO a_entity) {
		super(context, attrs, defStyle);
		m_cont = context;
		m_entity = a_entity;
		initView(context, a_root);
	}
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub

	}
	
	public ImageView getImageView() {
		return m_thumbnailImageView;
	}
	
/*	public ViewSwitcher getViewSwitcher() {
		return m_thumbnailViewSwitcher;
	}*/
	
	protected abstract void initView(Context a_cont, ViewGroup a_root);
	

}

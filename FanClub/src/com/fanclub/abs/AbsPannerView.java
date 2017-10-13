package com.fanclub.abs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fanclub.utils.ConfigManager;
import com.fanclub.utils.ImageDownloader;

public abstract class AbsPannerView  extends ViewGroup{

	protected ViewGroup m_view = null;	
	protected Context m_cont = null;
	protected ConfigManager m_confMan = null;
	
	private ImageDownloader m_imageDownloader = null;
	
	public AbsPannerView(Context context, ViewGroup a_root) {
		super(context);		
		m_cont = context;
		m_imageDownloader = new ImageDownloader(m_cont);
		initView(context, a_root);
	}
	
	public AbsPannerView(Context context, AttributeSet attrs, ViewGroup a_root) {
		super(context, attrs);
		m_cont = context;
		m_imageDownloader = new ImageDownloader(m_cont);
		initView(context, a_root);
	}
	
	public AbsPannerView(Context context, AttributeSet attrs, int defStyle, ViewGroup a_root) {
		super(context, attrs, defStyle);
		m_cont = context;
		m_imageDownloader = new ImageDownloader(m_cont);
		initView(context, a_root);
	}
	
	protected abstract void initView(Context a_cont, ViewGroup a_root);
	protected abstract void clean();
	
	protected void downloadImage(String a_url, ImageView a_imageView) {
		ImageDownloader.Mode mode = ImageDownloader.Mode.CORRECT;
		m_imageDownloader.download(a_url, a_imageView);
	}
}

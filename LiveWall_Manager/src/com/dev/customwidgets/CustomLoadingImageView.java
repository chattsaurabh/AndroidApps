package com.dev.customwidgets;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ViewSwitcher;

import com.dev.data.ImageEntity;
import com.dev.observers.ImageLoadListener;

public class CustomLoadingImageView extends ViewSwitcher implements ImageLoadListener{

	private ProgressBar m_progress = null;
	private CustomImageView m_imgView = null;
	
	public CustomLoadingImageView(Context context, ImageEntity a_imageEntity, int a_width, int a_height) {
		super(context);
		LinearLayout.LayoutParams l_viewSwParams = new LinearLayout.LayoutParams(a_width, a_height);
		l_viewSwParams.gravity = Gravity.CENTER;
		setLayoutParams(l_viewSwParams);
		
		m_imgView = new CustomImageView(context, a_imageEntity, a_width, a_height);
		m_imgView.setImageLoadedListener(this);
		
		m_progress = new ProgressBar(context);
		m_progress.setIndeterminate(true);
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(40,40);
		lp.gravity = Gravity.CENTER;
//		lp.width = LayoutParams.MATCH_PARENT;
//		lp.width = LayoutParams.MATCH_PARENT;
		m_progress.setLayoutParams(lp);
		m_progress.setX(20);
		m_progress.setY(20);
		m_progress.setBackgroundColor(Color.TRANSPARENT);
		
		addView(m_progress, 0);
		addView(m_imgView, 1);
		
		setDisplayedChild(0);
	}

	public CustomLoadingImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	
	public Boolean isDataFilled() {
		return m_imgView.isDataFilled();
	}
	
	public void clear() {
//		setDisplayedChild(0);
		m_imgView.clear();
	}
	public void fillImage() {
		m_imgView.fillImage();		
//		setDisplayedChild(1);
	}

	@Override
	public void onImageLoaded(int a_listenerId) {
		setDisplayedChild(a_listenerId);		
	}

}

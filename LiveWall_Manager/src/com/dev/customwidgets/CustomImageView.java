package com.dev.customwidgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.dev.activity.R;
import com.dev.backend.ConfigManager;
import com.dev.constants.LiveWallManagerConstants;
import com.dev.data.ImageEntity;
import com.dev.observers.ImageLoadListener;

public class CustomImageView extends ImageView implements LiveWallManagerConstants{

	private ImageEntity m_imageEntity = null;
	private Boolean m_dataFilled = false;
	private int m_width = 0;
	private int m_height = 0;
	private static final int IMG_START = -80;
	private static final int IMG_END = 400;
	private ConfigManager m_configMan = null;
	private ImageLoadListener m_imgLoadedListener = null;
	
	
	public CustomImageView(Context context, ImageEntity a_imageEntity, int a_width, int a_height) {
		super(context);
		m_imageEntity = a_imageEntity;
		m_width = a_width;
		m_height = a_height;
		m_configMan = ConfigManager.getInstance();
		
		setLayoutParams(new LayoutParams(a_width, a_height));
		setImageResource(R.drawable.default_gallery_bg);
		setScaleType(ScaleType.CENTER_CROP);
	}
	
	public void setImageLoadedListener(ImageLoadListener a_imgLoadedListener) {
		m_imgLoadedListener = a_imgLoadedListener;
	}

	public CustomImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}
	
	public Boolean isDataFilled() {
		return m_dataFilled;
	}
	
	public void clear() {
		/*setImageResource(R.drawable.add);
		m_dataFilled = false;*/
		int location[]={0,0};
		getLocationInWindow(location);
		if((location[0] < IMG_START) || (location[0] > IMG_END))
			new ClearTask().execute();
//		System.gc();
	}
	public void fillImage() {
		if(!m_dataFilled)
		{
			/*Bitmap l_bmp = extractBmp(m_imageEntity.getImageURI(), m_width, m_height);
			setImageBitmap(l_bmp);
			m_dataFilled = true;*/
			new FillImageTask().execute(m_imageEntity.getImageURI());
			m_dataFilled = true;
		}		
	}
	
	private class ClearTask extends AsyncTask<Void, Void, Boolean>
	{

		@Override
		protected Boolean doInBackground(Void... params) {
			m_dataFilled = false;
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			setImageResource(R.drawable.blank_transparent);
			if(m_imgLoadedListener != null)
				m_imgLoadedListener.onImageLoaded(IMAGE_CLEAR);
			super.onPostExecute(result);
		}
		
	}
	
	private class FillImageTask extends AsyncTask<Uri, Boolean, Bitmap>
	{

		@Override
		protected Bitmap doInBackground(Uri... params) {
			Bitmap l_biBitmap = null;
			try {
				l_biBitmap = m_configMan.getImgCompressor().extractBmp(params[0], m_width, m_height);
			} catch (Exception e) {
				e.printStackTrace();				
			}
			return l_biBitmap;
		}
		
		@Override
		protected void onPostExecute(Bitmap resultBmp) {
			if(resultBmp != null)
			{	
				setImageBitmap(resultBmp);
				m_dataFilled = true;
				if(m_imgLoadedListener != null)
					m_imgLoadedListener.onImageLoaded(IMAGE_FILL);
			}
			super.onPostExecute(resultBmp);
		}
		
	}
	
	
}

package com.dev.customwidgets;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

import com.dev.constants.LiveWallManagerConstants;
import com.dev.observers.ScrollFinishedListener;

public class CustomHorizontalScrollView extends HorizontalScrollView implements LiveWallManagerConstants{

	private ScrollFinishedListener m_scrollFinishedListener = null;	
	private ScrollFinishedTask m_task = null;
	private static int DELAY = 200;
	
	private Handler handler = new Handler();
	private Runnable runnable = new Runnable() {        
	    
		@Override
		public void run() {
			if(m_scrollFinishedListener != null)
			{	
				m_scrollFinishedListener.onScrollFinished();
			}
		}
	};
	
	public CustomHorizontalScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CustomHorizontalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	public void setScrollFinishedListener(ScrollFinishedListener a_scrollFinishedListener) {
		m_scrollFinishedListener = a_scrollFinishedListener;	
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		int diff = l - oldl;
		if(diff<0)
			diff = diff*(-1);
		
		if(m_scrollFinishedListener != null)
		{
			if(diff <= 5)
			{

				if(m_task != null)
				{
					m_task.cancel(true);
					m_task = null;
				}
				m_task = new ScrollFinishedTask();
				m_task.execute();
			}
		}
		super.onScrollChanged(l, t, oldl, oldt);
	}
	
	private class ScrollFinishedTask extends AsyncTask<Void, Void, Boolean>
	{

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {	
				handler.postDelayed(runnable, DELAY);				
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}

		
	}
	
}

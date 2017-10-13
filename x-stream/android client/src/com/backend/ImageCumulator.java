package com.backend;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.dev.R;

public class ImageCumulator {

	private ArrayList<ImageEntity> imgList = null;
	private int currentIndex = -1;
	private ShowMediaIcon showIconAsyncTask = null;
	
	private Thread fetchThread = new Thread()
	{
		public void run() 
		{
			if(++currentIndex < imgList.size())
			{						
				if(null != showIconAsyncTask)
				{
					showIconAsyncTask.cancel(true);
					showIconAsyncTask = null;
				}
				showIconAsyncTask = new ShowMediaIcon();
				showIconAsyncTask.execute(imgList.get(currentIndex));					
			}
		};
	};
	
	public ImageCumulator() {
		if(null == imgList)
		{
			imgList = new ArrayList<ImageEntity>();
		}		
	}
	
	public void addImageRequest(ImageView view, String url,Context cont, ViewSwitcher vsw) {
		if(null != imgList)
		{
			imgList.add(new ImageEntity(view, url, cont, vsw));
			if(-1 == currentIndex)
				fetchThread.run();
		}
	}
	
	private class ImageEntity
	{
		public ImageView imgView = null;
		public String url = null;
		public Context currentContext = null;
		public ViewSwitcher currentViewSwitcher = null;
		
		public ImageEntity(ImageView im, String u, Context cont,ViewSwitcher vsw) {
			imgView = im;
			url = u;
			currentContext = cont;
			currentViewSwitcher = vsw;
		}
	}
	
	private class ShowMediaIcon extends AsyncTask<ImageEntity, Boolean, Bitmap>
	{
		private ImageView currentImageView = null;
		private ViewSwitcher currentViewSw = null;
		@Override
		protected Bitmap doInBackground(ImageEntity... params) {			
			try {
				currentImageView = params[0].imgView;
				currentViewSw = params[0].currentViewSwitcher;
				Bitmap bm = Cache.getImage(params[0].url, params[0].currentContext, true);
				if(null != bm)
					return bm;
				
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			if(null != result)
				currentImageView.setImageBitmap(result);
			else
				currentImageView.setImageResource(R.drawable.showphotos);
			currentViewSw.showNext();
			fetchThread.run();
			super.onPostExecute(result);			
		}
		
	}
}

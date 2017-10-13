package com.backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;


public class Cache {

	Thread getImageThread = new Thread(){
		public void run() {
			
		};
	};

	public static String getImagePath(String url, Context context) {
		HttpClient httpClient = HttpClient.getInstance(context);
		try {
			String filename = String.valueOf(url.hashCode());
			String path = context.getCacheDir().getAbsolutePath();
			String fullPath = path + "/" + filename;
			File bitmapFp = new File(fullPath);
			if(!bitmapFp.exists()) {
				Bitmap img = httpClient.getImageBitmap(url);
				FileOutputStream fOut= new FileOutputStream(bitmapFp);
				if(!img.compress(Bitmap.CompressFormat.PNG, 0, fOut)) {
					return null;
				}
			}  else {
				if(BitmapFactory.decodeFile(fullPath) == null) {
					clearCachedImage(url, context);
					return null;
				}
			}
			return fullPath;
		} catch (FileNotFoundException e) {
			return null;
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	public static void clearCachedImage(String url, Context context) {
		if(url != null) {
			String filename = String.valueOf(url.hashCode());
			String path = context.getCacheDir().getAbsolutePath();
			String fullPath = path + "/" + filename;
			File bitmapFp = new File(fullPath);
			if(bitmapFp.exists()) {
				bitmapFp.delete();
			}
		}
	}
	
	public static Bitmap getImage(String url, Context context, boolean isFreshCopy) {
			
//		Log.d("audio", url);
		if(isFreshCopy) {
			clearCachedImage(url, context);
		}
		HttpClient httpClient = HttpClient.getInstance(context);
		byte [] data;
		try {
			String filename = String.valueOf(url.hashCode());
			String path = context.getCacheDir().getAbsolutePath();
			String fullPath = path + "/" + filename;
			File bitmapFp = new File(fullPath);
			Bitmap result = BitmapFactory.decodeFile(fullPath);
			if(result == null) {
				try {
					clearCachedImage(url, context);
					data = httpClient.getBytes(url);
					result = BitmapFactory.decodeByteArray(data, 0, data.length);
					FileOutputStream fOut= new FileOutputStream(bitmapFp);
					result.compress(Bitmap.CompressFormat.PNG, 0, fOut);
				} catch (Exception e) {
					return null;
				}
			}
			return result;
		} catch (Exception e) {
			return null;
		}
	}
	
}

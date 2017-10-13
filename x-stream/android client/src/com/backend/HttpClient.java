package com.backend;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class HttpClient {
	
	public static final int CONNECTION_TIMEOUT = 1000;	
	private String USER_AGENT = "Android-Teleca";
	private static HttpClient instance = null;
	private String packageName;
	private String versionName;
    protected int versionCode;
    
	private HttpClient (Context context)
	{	
		PackageManager manager = context.getPackageManager();
        PackageInfo info;
		try {
			info = manager.getPackageInfo(context.getPackageName(), 0);
			this.packageName = info.packageName;
	        this.versionCode = info.versionCode;
	        this.versionName = info.versionName;
	        USER_AGENT = USER_AGENT + " " + packageName + " " + versionName;
		} catch (NameNotFoundException e) {
		}
	}
	
	public static HttpClient getInstance(Context context) {
    	if(instance == null) {
    		instance = new HttpClient(context);
    	}
    	return instance;
    }

	public synchronized Bitmap getImageBitmap(URL url) {
    	Bitmap bm;
        try {
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, 8000);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inPurgeable = true; 
            bm = BitmapFactory.decodeStream(bis, new Rect(-1, -1, -1, -1), opts);
            bis.close();
            is.close();
            return bm;
       } catch (IOException e) {
    	   return null;
       }
    }
    
    public synchronized Bitmap getImageBitmap(String url) {
    	Bitmap b = null;
		try {
			URL u = new URL(url);
			b = getImageBitmap(u);
		} catch (MalformedURLException e) {
		}
		return b;
    }
    
    public byte [] getBytes(String url) {
        try {
      			
        	URL source = new URL(url);
            URLConnection conn = source.openConnection();
            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.connect();
            InputStream is = conn.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int c;
            while((c=is.read()) != -1) {
            	baos.write(c);
            }
            return baos.toByteArray();
       } catch (IOException e) {
    	   return null;
       }
    }
        
}

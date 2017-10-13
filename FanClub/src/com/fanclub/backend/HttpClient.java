package com.fanclub.backend;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.fanclub.utils.FanClubConstants;

public class HttpClient implements FanClubConstants{
	
	private String packageName;
	private String versionName;
    protected int versionCode;
    private Context m_context = null;
    
	public HttpClient (Context context)
	{	
		m_context = context;
		PackageManager manager = context.getPackageManager();
        PackageInfo info;
		try {
			info = manager.getPackageInfo(context.getPackageName(), 0);
			this.packageName = info.packageName;
	        this.versionCode = info.versionCode;
	        this.versionName = info.versionName;
		} catch (NameNotFoundException e) {
		}
	}

	public synchronized Bitmap getImageBitmap(URL url) {
    	Bitmap bm;
        try {
            URLConnection conn = url.openConnection();
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
    
    public File getFileContents(String a_url, String a_fileName) {
		File l_result = null;
		try {
            URL url = new URL(a_url);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.connect();
            // this will be useful so that you can show a typical 0-100% progress bar
            int fileLength = connection.getContentLength();

            // download the file
            File l_tmpFile = new File(m_context.getFilesDir(),a_fileName);
            if(l_tmpFile.exists())
            {
            	l_tmpFile.delete();
            }
            
            File l_currentFile = new File(m_context.getFilesDir(),a_fileName);
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(l_currentFile,true);

            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
            
            l_result = new File(m_context.getFilesDir(),a_fileName);
        } catch (Exception e) {
        	e.printStackTrace();
        }
		return l_result;
	}
        
}

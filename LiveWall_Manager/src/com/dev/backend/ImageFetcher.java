package com.dev.backend;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import com.dev.constants.LiveWallManagerConstants;
import com.dev.data.ImageEntity;

public class ImageFetcher implements LiveWallManagerConstants {
	
	private ArrayList<ImageEntity> m_imageList;
	private static ImageFetcher m_instance = null;
	private ConfigManager m_configManager = null;
	
	
	
	public static ImageFetcher getInstance()
	{
		if(null == m_instance)
		{			
			m_instance = new ImageFetcher();
		}
		return m_instance;
	}
	
	
	private ImageFetcher() {
		 m_configManager = ConfigManager.getInstance();
		 ContentResolver l_contentResolver = m_configManager.getContenResolver();
		 
		 m_imageList = new ArrayList<ImageEntity>();
		 System.gc();
		 String[] projection = new String[]{
		            /*MediaStore.Images.Media._ID,
		            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
		            MediaStore.Images.Media.DATE_TAKEN,*/
//		            MediaStore.Images.Thumbnails.DATA,
				 MediaStore.Images.Media.DISPLAY_NAME,
				 MediaStore.Images.Media.DATA
		    };
//		 Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;		 
		 Cursor cur = l_contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
		            projection, // Which columns to return
		            "",         // Which rows to return (all rows)
		            null,       // Selection arguments (none)
		            ""          // Ordering
		            );
		 
		 if(cur.moveToFirst())
		 {
			 
			 do {
				 int index = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				 String path = cur.getString(index);
				 ImageEntity l_imgEntity = new ImageEntity();
				 l_imgEntity.setImageURI(path);
				 
				 int idx = cur.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
				 String imgName = cur.getString(idx);
				 l_imgEntity.setImageName(imgName);
//				 int l_thumbIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA);
//				 String l_thumbPath = cur.getString(l_thumbIndex);
//				 l_imgEntity.setThumbnailUri(l_thumbPath);
				 
				 m_imageList.add(l_imgEntity);
		        } while (cur.moveToNext());
		 }
	}
	
	public ArrayList<ImageEntity> getImageList() {
		return m_imageList;
	}
	
	public void clean() {
		for (ImageEntity entity : m_imageList) {
			entity.clean();
		}
		m_imageList.clear();
		m_instance = null;
	}

}

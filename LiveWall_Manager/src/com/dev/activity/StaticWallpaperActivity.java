package com.dev.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.dev.backend.ConfigManager;
import com.dev.backend.ImageFetcher;
import com.dev.constants.LiveWallManagerConstants;
import com.dev.customwidgets.CustomHorizontalScrollView;
import com.dev.customwidgets.CustomLoadingImageView;
import com.dev.data.ImageEntity;
import com.dev.observers.ScrollFinishedListener;

public class StaticWallpaperActivity extends Activity implements LiveWallManagerConstants, ScrollFinishedListener{
	
	private static final int IMG_SIZE = 80;
//	private static final int IMG_PADDING = 5;
	private static final int CACHE_LIMIT = 10;
//	private static final int FILL_LIMIT = 40;
	private static final int VISIBLE_COUNT = 5;
	private static final int IMAGE_START_VISIBILITY = -80;
	private static final int CROP_FROM_CAMERA = 2;
	private static final int PICK_FROM_FILE = 3;
	
	private CustomHorizontalScrollView m_contentImageGallery = null;
	private LinearLayout m_linearLayout = null;
	private ImageView m_contentImgView = null;
	private View m_content = null;
	private Button m_setWallButton = null;
	private ArrayList<CustomLoadingImageView> m_imageList = null;
	private int m_currentStartId = -1;	
	private PopulateImagesTask m_fillImgTask = null;
	private Boolean m_restartFill;
	private Boolean m_croppedImage = false;
	private Bitmap m_croppedBmp = null;
	private ListView m_slideListView = null;
	
	private int m_minWidth = -1;
	private int m_minHt = -1;
	
	private ConfigManager m_configMan = null;
	private Uri m_imageCaptureUri = null;
	private Uri m_imagecroppedUri = null;
	private String[] m_tools;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.static_wallpaper_page);
		
		WallpaperManager l_wallMan = WallpaperManager.getInstance(getApplicationContext());
		m_minWidth = l_wallMan.getDesiredMinimumWidth();
		m_minHt = l_wallMan.getDesiredMinimumHeight();
		
		m_imageList = new ArrayList<CustomLoadingImageView>();
		m_configMan = ConfigManager.getInstance();
		m_content = findViewById(R.id.StaticWallContent);
		m_contentImgView = (ImageView) m_content.findViewById(R.id.StatWallCompImgView);
		
		
		m_contentImageGallery = (CustomHorizontalScrollView) findViewById(R.id.Gallery);
		m_contentImageGallery.setScrollFinishedListener(this);
		m_linearLayout = (LinearLayout) m_contentImageGallery.findViewById(R.id.CustomGalleryLinearLayout);
		System.gc();
		ArrayList<ImageEntity> l_ImageEntityList = ImageFetcher.getInstance().getImageList();

		m_contentImgView.setImageURI(l_ImageEntityList.get(0).getImageURI());
		m_imageCaptureUri = l_ImageEntityList.get(0).getImageURI();
		
		m_setWallButton = (Button)m_content.findViewById(R.id.StatWallCompButton);
		m_setWallButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setSelectionAsWallpaper();
			}

		});
		
		int c = 0;
		for (ImageEntity imageEntity : l_ImageEntityList) {	
			CustomLoadingImageView l_custImgV = (CustomLoadingImageView) insertImage(imageEntity);
			m_linearLayout.addView(l_custImgV);
			m_imageList.add(l_custImgV);
			c++;
		}
		
		
		m_slideListView = (ListView) findViewById(R.id.StatWallContentDrawerListView);
//		m_slideListView.setAlpha((float) 0.6);
		m_tools = getResources().getStringArray(R.array.tools_array);
		
		m_slideListView.setAdapter(new ArrayAdapter(this, R.layout.list_item, m_tools));
		
		m_slideListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				if (m_tools[position].equals(CROP_IMAGE_FROM_GALLERY)) {
					Intent intent = new Intent();
					
	                intent.setType("image/*");
	                intent.setAction(Intent.ACTION_GET_CONTENT);
	                
	                startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
				}
				
				if(m_tools[position].equals(CROP_IMAGE))
				{
					Intent l_cropIntent = new Intent(crop_image_activity);
					l_cropIntent.putExtra(CROP_IMAGE_URI, m_imageCaptureUri.toString());
					
					startActivity(l_cropIntent);
				}
			}
		});
		fillVisibleImages(0);
		System.gc();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode != RESULT_OK) return;
	   
	    switch (requestCode) {
		    case PICK_FROM_FILE: 
		    	m_imagecroppedUri = data.getData();
		    	m_croppedImage = true;
		    	doCrop();
	    
		    	break;	    	
	    
		    case CROP_FROM_CAMERA:	    	
		        Bundle extras = data.getExtras();
	
		        if (extras != null) {	        	
		            m_croppedBmp = extras.getParcelable("data");
		            
		            m_contentImgView.setImageBitmap(m_croppedBmp);
		        }
	
		        File f = new File(m_imagecroppedUri.getPath());            
		        if (f.exists()) f.delete();
	
		        break;

	    }
	}
	
	private void doCrop() {
		Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        
        List<ResolveInfo> list = getPackageManager().queryIntentActivities( intent, 0 );
        
        int size = list.size();
        
        if (size == 0) {	        
        	Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();
        	
            return;
        } else {
        	intent.setData(m_imagecroppedUri);
            
            intent.putExtra("outputX", m_minWidth);
            intent.putExtra("outputY", m_minHt);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);
            
        	if (size == 1) {
        		Intent i 		= new Intent(intent);
	        	ResolveInfo res	= list.get(0);
	        	
	        	i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
	        	
				startActivityForResult(i, CROP_FROM_CAMERA);
			}
        }
	}

	private void setSelectionAsWallpaper() {
		new AlertDialog.Builder(this)
		.setTitle(R.string.confirm_wallpaper_title)
		.setMessage(R.string.confirm_wallpaper_desc)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

		    public void onClick(DialogInterface dialog, int whichButton) {
		    	Boolean l_ret = setWall(m_imageCaptureUri);
		    	String l_msg = getString(R.string.op_succesful);
		    	if(!l_ret)
		    		l_msg = getString(R.string.op_failed);
		        
		    	Toast.makeText(StaticWallpaperActivity.this, l_msg, Toast.LENGTH_SHORT).show();
		    }})
		 .setNegativeButton(android.R.string.no, null).show();
		
	}
	
	private Boolean setWall(Uri a_currentSelection) {
		WallpaperManager l_wallMan = WallpaperManager.getInstance(getApplicationContext());
		/*int l_minHt = l_wallMan.getDesiredMinimumHeight();
		int l_minWdth = l_wallMan.getDesiredMinimumWidth();*/
		Bitmap l_bmp = null;
		
		if(m_croppedImage)
			l_bmp = m_croppedBmp;
		else
			l_bmp = m_configMan.getImgCompressor().extractBmp(a_currentSelection, m_minWidth, m_minHt);
		
		try {
			l_wallMan.setBitmap(l_bmp);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private View insertImage(final ImageEntity a_imgEntity) {
		CustomLoadingImageView l_imgView = null;
		l_imgView = new CustomLoadingImageView(this, a_imgEntity, IMG_SIZE, IMG_SIZE);
	
		l_imgView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				if(m_croppedImage)
				{
					new AlertDialog.Builder(StaticWallpaperActivity.this)
					.setTitle(R.string.confirm_discard_cropped_title)
					.setMessage(R.string.confirm_discard_cropped_desc)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							updateImage(a_imgEntity);
						}
					})
					.setNegativeButton(android.R.string.no, null).show();
				}
				else
				{
					updateImage(a_imgEntity);
				}
				
			}
		});
		
		return l_imgView;
	}
	
	private void updateImage(ImageEntity a_imgEntity) {
		m_croppedImage = false;
		m_contentImgView.setImageResource(0);
		try {
			Bitmap l_bmp = m_configMan.getImgCompressor().extractBmp(a_imgEntity.getImageURI(), m_contentImgView.getRight(), m_contentImgView.getBottom());		
			m_contentImgView.setImageBitmap(l_bmp);		
			m_imageCaptureUri = a_imgEntity.getImageURI();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(StaticWallpaperActivity.this, getString(R.string.faulty_selection), Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void onScrollFinished() {
		m_restartFill = true;
		int c = 0;
		for (CustomLoadingImageView imgV : m_imageList) {
			int location[]={0,0};
			imgV.getLocationInWindow(location);
			
			if((location[0] >= IMAGE_START_VISIBILITY) && m_restartFill)
			{
				m_restartFill = false;
				if(null != m_fillImgTask)
				{
					m_fillImgTask.cancel(true);
					m_fillImgTask = null;
				}
				fillVisibleImages(c);
				m_fillImgTask = new PopulateImagesTask();
				m_fillImgTask.execute(c);
			}
			c++;
		}
	}
	
	private void fillVisibleImages(int a_idx) {
		int top = a_idx + VISIBLE_COUNT;
		if(top >= m_imageList.size())
			top = m_imageList.size();
		
		for(int i = a_idx;i<top;i++)
		{	
			fillImage(i);
		}
	}


	private void clearExtra(int a_currentIdx) {
		
		int l_topEdge = m_imageList.size();
		int l_bottomLimit = 0;
		if((a_currentIdx + CACHE_LIMIT) < l_topEdge)
		{
			l_topEdge = a_currentIdx + CACHE_LIMIT;
		}
		
		if((a_currentIdx - CACHE_LIMIT) > 0)
		{
			l_bottomLimit = a_currentIdx - CACHE_LIMIT;
		}
		for (int i = l_topEdge; i < m_imageList.size(); i++) {
			m_imageList.get(i).clear();			
		}
		
		for (int j = 0; j < l_bottomLimit; j++) {
			m_imageList.get(j).clear();
		}
	}


	private void fillSurroundingImage(int a_currentIdx) {
		int l_topLimit = m_imageList.size() - 1;
		int l_bottomLimit = 0;
		if((a_currentIdx + CACHE_LIMIT) < l_topLimit)
		{
			l_topLimit = a_currentIdx + CACHE_LIMIT;
		}
		
		if((m_currentStartId - CACHE_LIMIT) > 0)
		{
			l_bottomLimit = m_currentStartId - CACHE_LIMIT;
		}
		
		for(int i = a_currentIdx; i <= l_topLimit;i++)
		{
//			m_imageList.get(i).fillImage();
			
			fillImage(i);
			
		}
		for(int j = l_bottomLimit; j < a_currentIdx;j++)
		{
//			m_imageList.get(j).fillImage();
			
			fillImage(j);
			
		}
	}
	
	private void fillImage(int a_idx) {
		m_imageList.get(a_idx).fillImage();
	}
	
	private class PopulateImagesTask extends AsyncTask<Integer, Void, Boolean>
	{

		@Override
		protected Boolean doInBackground(Integer... params) {
			try {
				int c = params[0];
				int diff = -1;
				diff = m_currentStartId - c;
				if(diff < 0)
					diff = diff * (-1);
				
//				fillVisibleImages(c);
				if((!m_imageList.get(c).isDataFilled() && (diff > 5)) || (m_currentStartId == -1))
				{
					m_currentStartId = c;
					fillSurroundingImage(c);
					clearExtra(c);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return true;
		}
		
	}
	
	@Override
	protected void onDestroy() {
		m_croppedImage = false;
		for (int i = 0; i < m_imageList.size(); i++) {
			m_imageList.get(i).clear();			
		}
		System.gc();
		super.onDestroy();
	}
	
}

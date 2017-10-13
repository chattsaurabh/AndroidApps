package com.dev.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.dev.backend.ConfigManager;
import com.dev.backend.ImageFetcher;
import com.dev.constants.LiveWallManagerConstants;
import com.dev.data.ImageEntity;

public class HomeActivity extends Activity implements LiveWallManagerConstants {

	private static int MAX_HEADER_IMAGE_COUNT = 3;
	
	private Button m_liveWallButton = null;
	private Button m_galleryButton = null;
	private ConfigManager m_configManager = null;
	private Context context = null;
	
	private ArrayList<Uri> m_headerContentImgList;
	ArrayList<ImageEntity> m_imageEntityList;
	private Boolean m_headerImgSet = false;
	
	
//	view flipper items
	private View m_header = null;
	private ViewFlipper m_headerViewFlipper = null;
	
	private ImageView m_headerVFImgView_1 = null;
	private ImageView m_headerVFImgView_2 = null;
	private ImageView m_headerVFImgView_3 = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        m_liveWallButton = (Button) findViewById(R.id.LiveLauncherButton);
        m_galleryButton = (Button) findViewById(R.id.GalleryLauncherButton);
        m_configManager = ConfigManager.getInstance();
//        m_configManager.setContenResolver(getContentResolver());
        m_headerContentImgList = new ArrayList<Uri>();
        
        ImageFetcher l_imgF = ImageFetcher.getInstance();
        m_imageEntityList = l_imgF.getImageList();
        
        context = this;
        m_liveWallButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(live_wall_launcher);
				List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		        final boolean isInstalled = list.size() > 0;
		        if(isInstalled){
		        	startActivityForResult(intent, 0);
		        }else{
		        	Toast.makeText(context, "device does not support live wallpaper", Toast.LENGTH_LONG).show();
		        }
			}
		});
        
        
        m_galleryButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*Intent intentBrowseFiles = new Intent(Intent.ACTION_GET_CONTENT);
			    intentBrowseFiles.setType("image/*");
			    intentBrowseFiles.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			    startActivity(intentBrowseFiles);*/
				if((m_imageEntityList != null) && (m_imageEntityList.size() > 0))
		        {
					Intent intentStaticWall = new Intent(static_wall_activity);
					startActivity(intentStaticWall);
		        }
				else
				{
					Toast.makeText(HomeActivity.this, getString(R.string.no_images), Toast.LENGTH_SHORT).show();
				}
				
			}
		});
        
       /* AdView adview = (AdView)findViewById(R.id.adView);
        AdRequest re = new AdRequest();        
        re.setTesting(true);
        adview.loadAd(re);*/

        
        m_header = findViewById(R.id.HomeHeader);
        m_headerViewFlipper = (ViewFlipper) m_header.findViewById(R.id.HeaderViewFlipper);
        m_headerVFImgView_1 = (ImageView) m_headerViewFlipper.findViewById(R.id.HeaderViewFlipperItemImageView1);
        m_headerVFImgView_2 = (ImageView) m_headerViewFlipper.findViewById(R.id.HeaderViewFlipperItemImageView2);
        m_headerVFImgView_3 = (ImageView) m_headerViewFlipper.findViewById(R.id.HeaderViewFlipperItemImageView3);
        
        
        if((m_imageEntityList != null) && (m_imageEntityList.size() > 0))
        {
	        for (int i = 0; i < MAX_HEADER_IMAGE_COUNT; i++) {
				m_headerContentImgList.add(m_imageEntityList.get(getRan()).getImageURI());
			}
	        
	        m_header.addOnLayoutChangeListener(new OnLayoutChangeListener() {
				
				@Override
				public void onLayoutChange(View v, int left, int top, int right,
						int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
					
					if(!m_headerImgSet)
					{
						if(bottom > 0)
						{
							m_headerImgSet = true;
							setHeaderImages(right, bottom);
						}
					}
				}
			});
	        
	        settingAnimation(true);
	        
	        m_headerViewFlipper.setAutoStart(true);
	        m_headerViewFlipper.setFlipInterval(5000);
        }
        else
        {
        	m_headerVFImgView_1.setImageResource(R.drawable.ic_launcher);
        	Toast.makeText(HomeActivity.this, getString(R.string.no_images), Toast.LENGTH_LONG).show();
        }
        
        
    }
    
    private void setHeaderImages(int a_width, int a_height) {
    	m_headerVFImgView_1.setImageBitmap(getRandBmp(a_width, a_height));
    	m_headerVFImgView_2.setImageBitmap(getRandBmp(a_width, a_height));
    	m_headerVFImgView_3.setImageBitmap(getRandBmp(a_width, a_height));
	}
    
    private Bitmap getRandBmp(int a_width, int a_height) {
    	Bitmap l_bmp = null;
    	while(l_bmp == null)
    		l_bmp = m_configManager.getImgCompressor().extractBmp(m_imageEntityList.get(getRan()).getImageURI(), a_width, a_height);
    	
    	return l_bmp;
	}
    
    private int getRan() {
		int l_result = 0;
		
		l_result = (int)(Math.random() * (m_imageEntityList.size() - 2));
		return l_result;
	}
    
    private void settingAnimation(boolean left) {
		if (left) {
			m_headerViewFlipper.setInAnimation(this, R.anim.slide_animation);
			m_headerViewFlipper.setOutAnimation(this, R.anim.slide_out);
		} else {
			m_headerViewFlipper.setInAnimation(this, R.anim.slide_in_rev);
			m_headerViewFlipper.setOutAnimation(this, R.anim.slide_out_rev);
		}
	}

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		this.finish();
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    
    @Override
    protected void onDestroy() {
    	ImageFetcher.getInstance().clean();
    	m_configManager.setContenResolver(null);
    	m_headerVFImgView_1.setImageResource(0);
    	m_headerVFImgView_2.setImageResource(0);
    	m_headerVFImgView_3.setImageResource(0);
    	m_headerImgSet = false;
    	System.gc();
    	super.onDestroy();
    }
}

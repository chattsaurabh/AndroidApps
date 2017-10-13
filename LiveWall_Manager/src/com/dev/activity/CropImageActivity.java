package com.dev.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.dev.backend.ConfigManager;
import com.dev.constants.LiveWallManagerConstants;

public class CropImageActivity extends Activity implements LiveWallManagerConstants{
	
	private Uri m_imageCaptureUri = null;
	
	private View m_cropImageContent = null;
	private View m_cropView = null;
	private ImageView m_contentImgView = null;
	private ConfigManager m_confMan = null;
	private Button m_cropButton = null;
	
	private Bitmap m_imageBmp = null;
	private Rect m_sizeRect = null;
	
	public FrameLayout board;
    public View part1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.crop_image);
		m_confMan = ConfigManager.getInstance();
		
		m_cropImageContent = findViewById(R.id.CropImageContent);
		m_contentImgView = (ImageView) m_cropImageContent.findViewById(R.id.StatWallCompImgView);
		
		m_cropView = findViewById(R.id.CropImageRect);
		
		m_cropButton = (Button) m_cropImageContent.findViewById(R.id.StatWallCompButton);
		m_cropButton.setText(R.string.crop_wall_image);
		
		String l_imgUri = getIntent().getExtras().getString(CROP_IMAGE_URI);
		m_imageCaptureUri = Uri.parse(l_imgUri);
		m_contentImgView.setImageURI(m_imageCaptureUri);
		
		
		Display mDisplay= getWindowManager().getDefaultDisplay();
		
		
		
		m_imageBmp = m_confMan.getImgCompressor().extractBmp(m_imageCaptureUri, 320, 480);
		
		m_cropButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int location[]={0,0};
				m_cropView.getLocationInWindow(location);
				Bitmap l_bmp = Bitmap.createBitmap(m_imageBmp, location[0], location[1], 120, 120);
				m_contentImgView.setImageBitmap(l_bmp);
			}
		});
			
	}
	
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
	    if (resultCode != RESULT_OK) return;
	   
	    switch (requestCode) {
		    case 1:	    	
		        Bundle extras = data.getExtras();
	
		        if (extras != null) {	        	
		            Bitmap photo = extras.getParcelable("data");
		            
		            m_contentImgView.setImageBitmap(photo);
		        }
		        break;
		     default:
		    	 break;
	    }
	}
	
}

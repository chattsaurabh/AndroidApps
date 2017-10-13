package com.fanclub.custom_views;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.fanclub.R;
import com.fanclub.abs.AbsPannerView;
import com.fanclub.data.PhotoEntityVO;
import com.fanclub.observers.TweetAuthObserver;
import com.fanclub.twitter.TwitterApp;
import com.fanclub.utils.ConfigManager;
import com.fanclub.utils.FanClubConstants;

public class PhotosPagerView extends AbsPannerView implements FanClubConstants, TweetAuthObserver{
	
	private ImageView m_image;
	private ProgressBar m_progress;
	private Context m_cont;
	private PhotoEntityVO m_currentEntity;
	
	public PhotosPagerView(Context context, ViewGroup a_root) {
		super(context, a_root);
	}
	
	public PhotosPagerView(Context context, AttributeSet attrs, ViewGroup a_root) {
		super(context, attrs, a_root);
	}
	
	public PhotosPagerView(Context context, AttributeSet attrs, int defStyle, ViewGroup a_root) {
		super(context, attrs, defStyle, a_root);
	}

	@Override
	protected void initView(Context a_cont, ViewGroup a_root) {
		m_cont = a_cont;
		m_confMan = ConfigManager.getInstance();
		LayoutInflater infl = (LayoutInflater)a_cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		m_view = (ViewGroup) infl.inflate(R.layout.photos_pager_view, a_root, true);
		
		m_progress = (ProgressBar) m_view.findViewById(R.id.pagerViewProgress);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		FrameLayout l_containerFL = (FrameLayout) m_view.findViewById(R.id.pagerViewFL);
		
		m_image = (ImageView) m_view.findViewById(R.id.pagerImgView);		
		
		View l_shareBar = m_view.findViewById(R.id.pagerViewShareBar);
		
		Button l_saveBtn = (Button) l_shareBar.findViewById(R.id.shareBarSaveBtn);
		l_saveBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				saveImage();				
			}
		});
		
		Button l_tweetBtn = (Button) l_shareBar.findViewById(R.id.shareBarTweetBtn);
		l_tweetBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tweetImage();
			}
		});
		
		m_progress.setVisibility(View.INVISIBLE);
		m_currentEntity = m_confMan.getPhotosPagerModel().getCurrentElement();
		downloadImage(m_currentEntity.getPhotosElementUrl(), m_image);

	}

	@Override
	protected void clean() {
		m_view.removeAllViews();
		m_view = null;
	}

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		// TODO Auto-generated method stub

	}


	private void saveImage() {
		m_progress.bringToFront();
		m_progress.setVisibility(View.VISIBLE);
		Bitmap l_bmp = loadBitmapFromView(m_image);		 
		String l_name = UUID.randomUUID().toString();
		try {
			saveBitmap(l_bmp, l_name);
			m_confMan.showToast(m_cont.getString(R.string.image_saved));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			m_confMan.showToast(m_cont.getString(R.string.image_save_failed));
		}
		finally
		{
			m_progress.setVisibility(View.INVISIBLE);
		}

	}
	
	public Bitmap loadBitmapFromView(View v) {
	    Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
	            Bitmap.Config.ARGB_8888);
	    Canvas c = new Canvas(b);
	    v.draw(c);
	    v.invalidate();
	    return b;
	}
	
	protected String saveBitmap(Bitmap bm, String name) throws Exception {
		String tempFilePath = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES) + "/"
				+ m_cont.getPackageName() + "/" + name + ".jpg";
		File tempFile = new File(tempFilePath);
		if (!tempFile.exists()) {
			if (!tempFile.getParentFile().exists()) {
				tempFile.getParentFile().mkdirs();
			}
		}

		tempFile.delete();
		tempFile.createNewFile();

		int quality = 100;
		FileOutputStream fileOutputStream = null;
		BufferedOutputStream bos = null;

		try {
			fileOutputStream = new FileOutputStream(tempFile);
			bos = new BufferedOutputStream(fileOutputStream);
			bm.compress(CompressFormat.JPEG, quality, bos);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		finally
		{

			bos.flush();
			bos.close();

			fileOutputStream.close();
		}
		//	    bm.recycle();

		MediaStore.Images.Media.insertImage(m_cont.getContentResolver(), bm, tempFilePath, name);


		return tempFilePath;
	}


	private void tweetImage() {
		m_progress.bringToFront();
		m_progress.setVisibility(View.VISIBLE);
		m_confMan.fetchTwitterInstance(this,(Activity) m_cont);
	}

	@Override
	public void onAuthenticationComplete(final TwitterApp a_twitterAppInstance) {
		m_progress.setVisibility(View.INVISIBLE);
		if(a_twitterAppInstance != null)
		{
			final AlertDialog.Builder builder = new AlertDialog.Builder(m_cont);
		    // Get the layout inflater
			LayoutInflater infl = (LayoutInflater)m_cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		    // Inflate and set the layout for the dialog
		    // Pass null as the parent view because its going in the dialog layout
			final ViewGroup l_dialogView = (ViewGroup) infl.inflate(R.layout.post_tweet_dialog, null); 
		    builder.setView(l_dialogView);
		    builder.setTitle(m_cont.getString(R.string.post_tweet_title));
		    // Add action buttons
		           builder.setPositiveButton(R.string.post_tweet_button, new DialogInterface.OnClickListener() {
		               @Override
		               public void onClick(DialogInterface dialog, int id) {
		                   EditText l_postEditText = (EditText) l_dialogView.findViewById(R.id.postTweetCommentEditText);		                   
		                   a_twitterAppInstance.updateStatus(l_postEditText.getText().toString()+" "+m_currentEntity.getPhotosElementUrl(), "");		
		                   
		               }
		           });
		           builder.setNegativeButton(R.string.cancel_tweet_button, new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int id) {
		               }
		           });      
		    
		    builder.show();	
		}
		else 
		{
			m_confMan.showToast(m_cont.getString(R.string.twitter_auth_failed));
		}		
	}

}

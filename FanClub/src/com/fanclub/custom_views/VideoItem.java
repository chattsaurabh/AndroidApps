package com.fanclub.custom_views;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.fanclub.R;
import com.fanclub.abs.AbsPannerItem;
import com.fanclub.data.VideoEntityVO;

public class VideoItem extends AbsPannerItem {

	
	private TextView m_numOfViews = null;
	private ImageButton m_playBtn = null;
	
//	private DownloadBmp m_downloadBmpTask = null;
	
	public VideoItem(Context context, ViewGroup a_root, VideoEntityVO a_entity) {
		super(context, a_root, a_entity);
	}

	public VideoItem(Context context, AttributeSet attrs, ViewGroup a_root, VideoEntityVO a_entity) {
		super(context, attrs, a_root, a_entity);
		
	}
	
	public VideoItem(Context context, AttributeSet attrs, int defStyle, ViewGroup a_root, VideoEntityVO a_entity) {
		super(context, attrs, defStyle, a_root, a_entity);		
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub

	}
	
	protected void initView(Context a_cont, ViewGroup a_root) {
		
		final VideoEntityVO l_currentEntityVO = (VideoEntityVO) m_entity;
		final String l_vidUrl = l_currentEntityVO.getVideoUrl_2();
		
		LayoutInflater infl = (LayoutInflater)a_cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		m_view = (ViewGroup) infl.inflate(R.layout.video_item, a_root, true);
		
		/*FrameLayout l_containerFL = (FrameLayout) m_view.findViewById(R.id.videoItemFrameLayout);
		l_containerFL.setSelected(true);*/
		
		TextView l_titleView = (TextView) m_view.findViewById(R.id.videoItemTitle);		
		l_titleView.setText(l_currentEntityVO.getTitle());
		l_titleView.setSelected(true);
		
		
		m_numOfViews = (TextView) m_view.findViewById(R.id.videoItemNumberOfViews);
		m_numOfViews.setText(l_currentEntityVO.getNumViews());
		
		m_thumbnailImageView = (ImageView) m_view.findViewById(R.id.viewItemThumbnailImgView);
		
		m_playBtn = (ImageButton) m_view.findViewById(R.id.videoPlayBtn);
		m_playBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent videoClient = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:"+l_currentEntityVO.getId()));
				videoClient.putExtra("VIDEO_ID", l_currentEntityVO.getId()); 
//	            videoClient.setData(Uri.parse(l_vidUrl));
//	            videoClient.setClassName("com.google.android.youtube", "com.google.android.youtube.PlayerActivity");
	            try{
	                m_cont.startActivity(videoClient);
	            }catch(ActivityNotFoundException excp){
	                try{
	                    videoClient.setClassName("com.google.android.youtube", "com.google.android.youtube.WatchActivity");
	                    m_cont.startActivity(videoClient);
	                }catch(ActivityNotFoundException exc){
	                    exc.printStackTrace();
	                }
	            }
				
			}
		});	
	}
}

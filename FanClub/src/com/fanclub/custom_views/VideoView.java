package com.fanclub.custom_views;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fanclub.R;
import com.fanclub.abs.AbsPannerView;
import com.fanclub.data.VideoEntityVO;
import com.fanclub.model.VideoModel;
import com.fanclub.utils.ConfigManager;

public class VideoView extends AbsPannerView{

	private VideoModel m_videoModel = null;
	
	public VideoView(Context context, ViewGroup a_root) {
		super(context, a_root);
	}
	
	public VideoView(Context context, AttributeSet attrs, ViewGroup a_root) {
		super(context, attrs, a_root);
	}
	
	public VideoView(Context context, AttributeSet attrs, int defStyle, ViewGroup a_root) {
		super(context, attrs, defStyle, a_root);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		Log.d("FC", "video layout");
	}
	
	protected void initView(Context a_cont, ViewGroup a_root) {
		m_confMan = ConfigManager.getInstance();
		m_videoModel = m_confMan.getVideoModel();
		ArrayList<VideoEntityVO> l_videoDataList = m_videoModel.getVideoDataList();
		
		LayoutInflater infl = (LayoutInflater)a_cont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		m_view = (ViewGroup) infl.inflate(R.layout.video_view, a_root, true);
		
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);		
		LinearLayout l_topLL = (LinearLayout) m_view.findViewById(R.id.videoViewTopLL);
		
		int l_dataEndTopLL = 3;
		if(l_videoDataList.size() < l_dataEndTopLL)
		{
			l_dataEndTopLL = l_videoDataList.size();
		}
		
		for(int i = 0; i<l_dataEndTopLL; i++)
		{
			LinearLayout l_coverView = new LinearLayout(a_cont);
			l_coverView.setLayoutParams(lp);
			l_topLL.addView(l_coverView);
			VideoItem l_vidItem = new VideoItem(a_cont, l_coverView, l_videoDataList.get(i));
			downloadImage(l_videoDataList.get(i).getHQThumb(), l_vidItem.getImageView());
		}
		
		int l_dataEndBottomLL = 6;
		if(l_videoDataList.size() < l_dataEndBottomLL)
		{
			l_dataEndBottomLL = l_videoDataList.size();
		}
		LinearLayout l_bottomLL = (LinearLayout) m_view.findViewById(R.id.videoViewBottomLL);
		for(int i = 3; i<l_dataEndBottomLL; i++)
		{
			LinearLayout l_coverView = new LinearLayout(a_cont);
			l_coverView.setLayoutParams(lp);
			l_bottomLL.addView(l_coverView);
			VideoItem l_vidItem = new VideoItem(a_cont, l_coverView, l_videoDataList.get(i));
			downloadImage(l_videoDataList.get(i).getHQThumb(), l_vidItem.getImageView());
		}
	}
	
	public void clean() {
		m_view.removeAllViews();
		m_view = null;
	}
	

}

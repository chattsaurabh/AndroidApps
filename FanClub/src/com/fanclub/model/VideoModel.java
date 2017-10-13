package com.fanclub.model;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;

import com.fanclub.abs.AbsDataModel;
import com.fanclub.data.VideoEntityVO;
import com.fanclub.observers.OnDownloadComplete;
import com.fanclub.utils.ConfigManager;
import com.fanclub.utils.FanClubConstants;

public class VideoModel extends AbsDataModel implements FanClubConstants, OnDownloadComplete{

	private ArrayList<VideoEntityVO> m_videoDataList = null;
	private JSONArray m_jsonArray = null;
	private ConfigManager m_confMan = null;
	
	public VideoModel(Context a_cont) {
		m_confMan = ConfigManager.getInstance();
		m_cont = a_cont;
		PAGE_DATA_RANGE = 6;
		
//		TODO remove
		m_fileName = FanClubConstants.VIDEO_JSON_FILE;
		calculateCurrentDataSet();
		
//		TODO add
//		m_confMan.getVideoMetadataFile(this);
	}

	protected void calculateCurrentDataSet() {
		if(m_videoDataList == null)
		{
			m_videoDataList = new ArrayList<VideoEntityVO>();
		}
		m_videoDataList.clear();
		getContents();
	}
	
	public void setCurrentIndex(int a_currentIndex) {
		m_currentIndex = a_currentIndex;
	}
	
	public ArrayList<VideoEntityVO> getVideoDataList() {
		return m_videoDataList;
	}
	
	
	@Override
	protected void processData(String a_data) {
		try {
			m_jsonArray = new JSONArray(a_data);
			if(m_dataSize == -1)
			{
				m_dataSize = m_jsonArray.length();
			}
			
			int k = 0;
			int l_modelEnd = m_currentIndex + PAGE_DATA_RANGE;
			if(l_modelEnd > m_jsonArray.length())
			{
				l_modelEnd = m_jsonArray.length();
			}
			for (int i = m_currentIndex; i < l_modelEnd; i++) {
				VideoEntityVO l_entity = new VideoEntityVO();
				
				l_entity.setId(m_jsonArray.getJSONObject(i).getString(VIDEO_ID));
				l_entity.setTitle(m_jsonArray.getJSONObject(i).getString(VIDEO_TITLE));				
				l_entity.setDesc(m_jsonArray.getJSONObject(i).getString(VIDEO_DESC));				
				l_entity.setNumViews(m_jsonArray.getJSONObject(i).getString(VIDEO_VIEW_COUNT));
				l_entity.setDuration(m_jsonArray.getJSONObject(i).getString(VIDEO_DURATION));				
				l_entity.setHQThumb(m_jsonArray.getJSONObject(i).getString(VIDEO_HQ_THUMB));
				l_entity.setMQThumb(m_jsonArray.getJSONObject(i).getString(VIDEO_MQ_THUMB));
				l_entity.setLQThumb(m_jsonArray.getJSONObject(i).getString(VIDEO_LQ_THUMB));
				l_entity.setVideoUrl_1(m_jsonArray.getJSONObject(i).getString(VIDEO_MEDIA_URL_1));
				l_entity.setVideoUrl_2(m_jsonArray.getJSONObject(i).getString(VIDEO_MEDIA_URL_2));
				l_entity.setEmbedded(m_jsonArray.getJSONObject(i).getString(VIDEO_EMBEDDED));
				
				m_videoDataList.add(l_entity);  
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	@Override
	public void clean() {
		m_dataSize = -1;
		m_videoDataList.clear();
		m_videoDataList = null;		
	}

	@Override
	public void onDownloadConplete(Object a_result) {
		File l_result = (File) a_result;
		if(l_result != null)
		{
			m_metadataFile = l_result;			
			calculateCurrentDataSet();
		}
	}
}

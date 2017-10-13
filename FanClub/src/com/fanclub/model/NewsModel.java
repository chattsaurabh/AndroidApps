package com.fanclub.model;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;

import com.fanclub.abs.AbsDataModel;
import com.fanclub.data.NewsEntityVO;
import com.fanclub.observers.OnDownloadComplete;
import com.fanclub.utils.ConfigManager;
import com.fanclub.utils.FanClubConstants;

public class NewsModel extends AbsDataModel implements FanClubConstants, OnDownloadComplete{

	private ArrayList<NewsEntityVO> m_newsDataList = null;
	private JSONArray m_jsonArray = null;
	private ConfigManager m_confMan = null;
	
	public NewsModel(Context a_cont) {
		m_confMan = ConfigManager.getInstance();
		m_cont = a_cont;
		PAGE_DATA_RANGE = 3;
		
//		calculateCurrentDataSet();
	}

	public void setDataMode(NEWS_DATA_ENUM a_mode) 
	{
		switch (a_mode) {
		case TYPE_NEWS:
			m_fileName = FanClubConstants.NEWS_JSON_FILE;
//			TODO add
//			m_confMan.getNewsMetadataFile(this);
			break;
		case TYPE_GOSSIP:
			m_fileName = FanClubConstants.GOSSIP_JSON_FILE;
//			TODO add
//			m_confMan.getNewsMetadataFile(this);
			break;
		default:
			break;
		}
		calculateCurrentDataSet();
	}
	protected void calculateCurrentDataSet() {
		if(m_newsDataList == null)
		{
			m_newsDataList = new ArrayList<NewsEntityVO>();
		}
		m_newsDataList.clear();
		getContents();
	}
	
	public void setCurrentIndex(int a_currentIndex) {
		m_currentIndex = a_currentIndex;
	}
	
	public ArrayList<NewsEntityVO> getNewsDataList() {
		return m_newsDataList;
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
				NewsEntityVO l_entity = new NewsEntityVO();
				
				l_entity.setTitle(m_jsonArray.getJSONObject(i).getString(VIDEO_TITLE));
				l_entity.setNewsUrl(m_jsonArray.getJSONObject(i).getString(NEWS_URL));
				l_entity.setNewsSource(m_jsonArray.getJSONObject(i).getString(NEWS_SOURCE));
				l_entity.setNewsDesc(m_jsonArray.getJSONObject(i).getString(NEWS_DESC));
				l_entity.setDate(m_jsonArray.getJSONObject(i).getString(NEWS_DATE));
				l_entity.setThumb(m_jsonArray.getJSONObject(i).getString(NEWS_THUMB));
				
				m_newsDataList.add(l_entity);  
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Override
	public void clean() {
		m_dataSize = -1;
		m_newsDataList.clear();
		m_newsDataList = null;
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

package com.fanclub.model;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;

import com.fanclub.abs.AbsDataModel;
import com.fanclub.data.PhotoEntityVO;
import com.fanclub.observers.OnDownloadComplete;
import com.fanclub.utils.ConfigManager;
import com.fanclub.utils.FanClubConstants;

public class PhotosElementModel extends AbsDataModel implements FanClubConstants, OnDownloadComplete{

	protected ArrayList<PhotoEntityVO> m_photosDataList = null;
	private JSONArray m_jsonArray = null;
	private ConfigManager m_confMan = null;
	
	public PhotosElementModel(Context a_cont, int a_pageDataRange) {
		m_confMan = ConfigManager.getInstance();
		m_cont = a_cont;
		PAGE_DATA_RANGE = a_pageDataRange;
	}
	
	public void setModelFile(String a_filename) {
		m_fileName = a_filename;
		
//		TODO add
//		m_confMan.getPhotosElementMetadataFile(this, a_filename);
		
		calculateCurrentDataSet();
	}
	@Override
	protected void calculateCurrentDataSet() 
	{
		if(m_photosDataList == null)
		{
			m_photosDataList = new ArrayList<PhotoEntityVO>();
		}
		m_photosDataList.clear();
		getContents();

	}
	
	public void setCurrentIndex(int a_currentIndex) {
		m_currentIndex = a_currentIndex;
	}
	
	public ArrayList<PhotoEntityVO> getPhotosElementsDataList() {
		return m_photosDataList;
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
				PhotoEntityVO l_entity = new PhotoEntityVO();
				l_entity.setMode(PHOTOS_DATA_ENUM.TYPE_ELEMENT);
				l_entity.setThumbnailUrl(m_jsonArray.getJSONObject(i).getString(PHOTOS_THUMB));				
				l_entity.setPhotosElementUrl(m_jsonArray.getJSONObject(i).getString(PHOTOS_ELMENT_URL));
				l_entity.setPhotosFile_album(m_fileName);
				m_photosDataList.add(l_entity);  
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void clean() {
		m_dataSize = -1;
		m_photosDataList.clear();
		m_photosDataList = null;
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
	
	public File getMetaDataFile() {
		return m_metadataFile;
	}
	
	

}

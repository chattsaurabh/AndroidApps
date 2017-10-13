package com.interviewer.se.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.interviewer.se.abs.AbsModel;
import com.interviewer.se.abs.AbsPannerModel;
import com.interviewer.se.data.TestDataVO;
import com.interviewer.se.util.ConfigurationManager;

public class TestDataModel extends AbsPannerModel {
	
	private ArrayList<TestDataVO> m_testDataList;
	private int m_resultCount = 0;
	private ConfigurationManager m_confMan = null;
	
	public TestDataModel(Context a_cont) {
		m_cont = a_cont;
		PAGE_DATA_RANGE = 1;
		m_confMan = ConfigurationManager.getInstance();
		m_testDataList = new ArrayList<TestDataVO>();
	}

	public void setData(String a_filename) {
		populate(BASE_JSON_FILE+a_filename);
	}
	
	@Override
	protected void processData(String a_data) {
		try {
			m_jsonArray = new JSONArray(a_data);
			
			for (int i = 0; i < m_jsonArray.length(); i++) {
				TestDataVO l_entity = new TestDataVO();
				
				l_entity.setQuestion(m_jsonArray.getJSONObject(i).getString(TEST_PAGE_QUESTION));
				l_entity.setTitle(m_jsonArray.getJSONObject(i).getString(TEST_PAGE_TITLE));
				
				JSONArray l_allAnswers = m_jsonArray.getJSONObject(i).getJSONArray(TEST_PAGE_ALL_ANSWERS);
				for(int j = 0;j < l_allAnswers.length(); j++)
				{
					JSONObject l_currentAnswerObj = l_allAnswers.getJSONObject(j);
					l_entity.setAnswer(l_currentAnswerObj.getString(TEST_PAGE_ANSWER), l_currentAnswerObj.getBoolean(TEST_PAGE_CORRECT));
				}
				
				m_testDataList.add(l_entity);  
			}
			m_dataSize = m_testDataList.size();
			Log.d("LING", "");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public ArrayList<TestDataVO> getTestDataList() {
		return m_testDataList;
	}
	
	public void resetCurrentIndex() {
		m_currentIndex = 0;
	}
	
	public TestDataVO getCurrentData() {
		return m_testDataList.get(m_currentIndex);
	}
	
	public int getResultCount() {
		return m_resultCount;
	}
	
	public void incrementResultCount() {
		if(m_resultCount < m_testDataList.size())
			m_resultCount++;
	}

	@Override
	public void clear() {
		m_currentIndex = 0;
		m_testDataList.clear();
		m_testDataList = null;

	}


}

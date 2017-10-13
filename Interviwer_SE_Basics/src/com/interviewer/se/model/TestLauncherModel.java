package com.interviewer.se.model;

import interviewer.se.basics.R;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.util.Log;

import com.interviewer.se.abs.AbsModel;
import com.interviewer.se.data.TestlauncherVO;

public class TestLauncherModel extends AbsModel {

	ArrayList<TestlauncherVO> m_testLauncherDataList = null;
	public TestLauncherModel(Context a_cont) {
		m_cont = a_cont;
		m_testLauncherDataList = new ArrayList<TestlauncherVO>();
		
		TestlauncherVO l_allEntity = new TestlauncherVO();
		l_allEntity.setTopic(m_cont.getResources().getString(R.string.test_launcher_all_tests));
		m_testLauncherDataList.add(l_allEntity);
		
		populate(TEST_LAUNCHER_JSON_FILE);
	}
	
	@Override
	protected void processData(String a_data) {
		try {
			m_jsonArray = new JSONArray(a_data);
			
			for (int i = 0; i < m_jsonArray.length(); i++) {
				TestlauncherVO l_entity = new TestlauncherVO();
				
				l_entity.setTopic(m_jsonArray.getJSONObject(i).getString(TEST_LAUNCHER_TOPIC));
				l_entity.setJsonFileName(m_jsonArray.getJSONObject(i).getString(TEST_LAUNCHER_DATA_FILE_NAME));
				
				m_testLauncherDataList.add(l_entity);  
			}
			TestlauncherVO l_allEntity = m_testLauncherDataList.get(0);
			for (TestlauncherVO entity : m_testLauncherDataList) 
			{
				ArrayList<String> l_dataFiles = entity.getJsonFileName();
				if(l_dataFiles.size() > 0)
				{
					for (String fileName : l_dataFiles) 
					{
						l_allEntity.setJsonFileName(fileName);
					}
				}
			}
			Log.d("LING", "");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ArrayList<TestlauncherVO> getLauncherList() {
		return m_testLauncherDataList;
	}
	
	@Override
	public void clear() {
		m_testLauncherDataList.clear();
		m_testLauncherDataList = null;
	}

}

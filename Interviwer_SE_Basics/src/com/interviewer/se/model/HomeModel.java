package com.interviewer.se.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.util.Log;

import com.interviewer.se.abs.AbsModel;
import com.interviewer.se.data.HomeVO;
import com.interviewer.se.util.Interviewer_SE_Basics_Constants;

public class HomeModel extends AbsModel{
	
	private ArrayList<HomeVO> m_homeDataList;
	
	public HomeModel(Context a_cont) {
		m_cont = a_cont;
		m_homeDataList = new ArrayList<HomeVO>();
		populate(HOME_JSON_FILE);
	}
	
	
	public ArrayList<HomeVO> getHomeDataList() {
		return m_homeDataList;
	}

	@Override
	protected void processData(String a_data) {
		try {
			m_jsonArray = new JSONArray(a_data);
			
			for (int i = 0; i < m_jsonArray.length(); i++) {
				HomeVO l_entity = new HomeVO();
				
				l_entity.setHeading(m_jsonArray.getJSONObject(i).getString(HOME_HEADING));
				l_entity.setDesc(m_jsonArray.getJSONObject(i).getString(HOME_DESC));
				l_entity.setImgPath(m_jsonArray.getJSONObject(i).getString(HOME_DESC_IMAGE));
				
				m_homeDataList.add(l_entity);  
			}
			Log.d("LING", "");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void clear() {
		m_homeDataList.clear();
		m_homeDataList = null;
	}
}

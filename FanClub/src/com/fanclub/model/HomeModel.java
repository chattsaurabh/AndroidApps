package com.fanclub.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.fanclub.data.HomeEntityVO;
import com.fanclub.utils.FanClubConstants;

public class HomeModel extends Object implements FanClubConstants{
	
	private JSONObject m_jsonObject = null;
	private Context m_cont = null;
	private JSONArray m_homeJSONArray;
	private ArrayList<HomeEntityVO> m_homeItemList = null;

	
	public HomeModel(File a_metaDataFile, Context a_cont) {
		m_cont = a_cont;
		getContents(a_metaDataFile);
		
	}
	
	private void getContents(File a_metaDataFile) {
		int ch;
		String data = new String();
		StringBuffer fileContent = new StringBuffer("");
		try {
			
//			InputStream fis = new FileInputStream(a_metaDataFile);
			InputStream fis = m_cont.getResources().getAssets().open(
			"themeMetaData.json");
			
			while ((ch = fis.read()) != -1)
				fileContent.append((char) ch);
			data = new String(fileContent);
		} catch (FileNotFoundException e) {
			Log.d("file", "file is not found");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			m_jsonObject = new JSONObject(data);			
			m_homeJSONArray = m_jsonObject.getJSONArray(HOME);
			m_homeItemList = new ArrayList<HomeEntityVO>();
			for (int i = 0; i < m_homeJSONArray.length(); i++) {
					HomeEntityVO l_entity = new HomeEntityVO();
					l_entity.setComponenId(m_homeJSONArray.getJSONObject(i).getString(COMPONENT_ID));
					l_entity.setTitle(m_homeJSONArray.getJSONObject(i).getString(TITLE));
					
					m_homeItemList.add(l_entity);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public int getSize() {
		return m_homeItemList.size();
	}

}

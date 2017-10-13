package com.fanclub.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.fanclub.utils.FanClubConstants;

public class MetaDataVO implements FanClubConstants{
	
	private JSONObject m_jsonObject = null;
	private Context m_cont = null;
	
	private String m_bgColor = null;
	
	public MetaDataVO(File a_metaDataFile, Context a_cont) {
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
			
			m_bgColor = m_jsonObject.getString(BGCOLOR);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	getters 
	
	public String getBGColor() {
		return m_bgColor;
	}
}

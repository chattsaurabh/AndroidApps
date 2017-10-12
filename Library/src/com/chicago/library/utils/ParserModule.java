package com.chicago.library.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.chicago.library.model.LibraryVO;

public class ParserModule implements LibraryConstants{
	
	private ConfigurationManager mConfMan = null;
	private ArrayList<LibraryVO> mDataList = null;
	private IParseComplete mParseListener = null;

	public void parseData(final String data, IParseComplete listener) {
		mParseListener = listener;
		mConfMan = ConfigurationManager.getInstance();
		mDataList = mConfMan.getDataList();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				parseJSON(data);
			}
		}).start();
	}
	
	private void parseJSON(String jsonString) {
		try {
			JSONArray libList = new JSONArray(jsonString);
			for(int i = 0; i < libList.length();i++) {
				JSONObject obj = libList.getJSONObject(i);
				LibraryVO libElement = new LibraryVO();
				
				libElement.setTeacherInLibrary(obj.getString(TEACHERS_LIB));
				libElement.setZip(obj.getString(ZIP));
				libElement.setHoursOfOperation(obj.getString(HOURS_OPERATION));
				libElement.setWebsite((obj.getJSONObject(WEBSITE)).getString(WEBSITE_URL));
				libElement.setAddress(obj.getString(ADDRESS));
				libElement.setCity(obj.getString(CITY));
				libElement.setPhone(obj.getString(PHONE));
				JSONObject locationObj = (obj.getJSONObject(LOCATION));
				libElement.setLocation(locationObj.getString(LATITUTE), 
						locationObj.getBoolean(NEEDS_RECORDING), 
						locationObj.getString(LONGITUDE));
				libElement.setState(obj.getString(STATE));
				libElement.setCyberNavigator(obj.getString(CYBERNAVIGATION));
				libElement.setName(obj.getString(NAME));
				
				mDataList.add(libElement);				
			}
			
			mParseListener.onParseCompleted(true);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mParseListener.onParseCompleted(false);
		}
	}
}

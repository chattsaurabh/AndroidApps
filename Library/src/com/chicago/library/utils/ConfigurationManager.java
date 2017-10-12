package com.chicago.library.utils;

import java.util.ArrayList;

import com.chicago.library.model.LibraryVO;

import android.util.Log;

public class ConfigurationManager implements IDownloadComplete,
			IParseComplete{

	private final String DATA_URL = "https://data.cityofchicago.org/resource/x8fc-8rcq.json";
	private static ConfigurationManager mInstance = null;
	
	private HttpModule mHttpModule = null;
	private ParserModule mParser = null;
	private ArrayList<LibraryVO> mDataList = null;
	private IParseComplete mParseListener = null;
	
	private ConfigurationManager(){};
	
	public static ConfigurationManager getInstance() {
		if(null == mInstance) {
			mInstance = new ConfigurationManager();
		}
		return mInstance;
	}
	
	public void init(IParseComplete listener) {
		mParseListener = listener;
		mHttpModule = new HttpModule();
		mHttpModule.downloadFromUrl(DATA_URL, this);
		
		mDataList = new ArrayList<LibraryVO>();
		mParser = new ParserModule();
	}

	@Override
	public void onDownloadComplete(boolean successVal, String result) {
		// TODO Auto-generated method stub
//		Log.d("LIB", result);
		if(successVal) {
			mParser.parseData(result, this);
		}
	}

	public ArrayList<LibraryVO> getDataList() {
		return mDataList;
	}

	public void setDataList(ArrayList<LibraryVO> mDataList) {
		this.mDataList = mDataList;
	}

	@Override
	public void onParseCompleted(boolean successVal) {
		// TODO Auto-generated method stub
		mParseListener.onParseCompleted(successVal);
	}
	
	
}

package com.fanclub.data;

import com.fanclub.observers.OnDownloadComplete;
import com.fanclub.utils.FanClubConstants;

public class HttpLauncherVO implements FanClubConstants{

	private String m_url = null;
	private HttpEnums m_type = null;
	private String m_fileName = null;
	private OnDownloadComplete m_listener = null;
	
	public HttpLauncherVO(String a_url, HttpEnums a_type, OnDownloadComplete a_listener, String a_fileName) {
		m_url = a_url;
		m_type = a_type;
		m_listener = a_listener;
		m_fileName = a_fileName;
	}
	
	public String getUrl() {
		return m_url;		
	}
	
	public HttpEnums getType() {
		return m_type;
	}
	
	public OnDownloadComplete getListener() {
		return m_listener;
	}
	
	public String getFileName() {
		return m_fileName;
	}
}

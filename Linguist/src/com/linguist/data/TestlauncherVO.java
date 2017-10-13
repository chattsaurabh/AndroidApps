package com.linguist.data;

import java.util.ArrayList;

public class TestlauncherVO {
	
	private String m_topic;
	private ArrayList<String> m_jsonFileName;
	
	public TestlauncherVO() {
		m_jsonFileName = new ArrayList<String>();
	}
	public void setTopic(String a_topic) {
		this.m_topic = a_topic;
	}
	public String getTopic() {
		return m_topic;
	}
	public void setJsonFileName(String a_jsonFileName) {
		this.m_jsonFileName.add(a_jsonFileName);
	}
	public ArrayList<String> getJsonFileName() {
		return m_jsonFileName;
	}
	
	
}

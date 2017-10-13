package com.linguist.data;

public class HomeVO {

	private String m_heading;
	private String m_desc;
	private String m_imgPath;

	public void setHeading(String a_heading) {
		m_heading = a_heading;
	}
	
	public void setDesc(String a_desc) {
		m_desc = a_desc;
	}
	
	public String getDesc() {
		return m_desc;
	}
	
	public String getHeading() {
		return m_heading;
	}

	public void setImgPath(String m_imgPath) {
		this.m_imgPath = m_imgPath;
	}

	public String getImgPath() {
		return m_imgPath;
	}
	
	
}

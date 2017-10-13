package com.fanclub.data;

import com.fanclub.abs.AbsEntityVO;

public class NewsEntityVO extends AbsEntityVO {

	private String m_title;
	private String m_newsUrl;
	private String m_newsSource;
	private String m_newsDesc;
	private String m_date;
	private String m_thumb;
	
	
	public void setTitle(String m_title) {
		this.m_title = m_title;
	}
	public String getTitle() {
		return m_title;
	}
	public void setNewsUrl(String a_newsUrl) {
		this.m_newsUrl = a_newsUrl;
	}
	public String getNewsUrl() {
		return m_newsUrl;
	}
	public void setNewsSource(String a_newsSource) {
		this.m_newsSource = a_newsSource;
	}
	public String getNewsSource() {
		return m_newsSource;
	}
	public void setNewsDesc(String a_newsDesc) {
		this.m_newsDesc = a_newsDesc;
	}
	public String getNewsDesc() {
		return m_newsDesc;
	}
	public void setDate(String a_date) {
		this.m_date = a_date;
	}
	public String getDate() {
		return m_date;
	}
	public void setThumb(String a_thumb) {
		this.m_thumb = a_thumb;
	}
	public String getThumb() {
		return m_thumb;
	}
}

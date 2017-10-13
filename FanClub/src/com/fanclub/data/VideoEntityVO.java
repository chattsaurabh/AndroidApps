package com.fanclub.data;

import com.fanclub.abs.AbsEntityVO;

public class VideoEntityVO extends AbsEntityVO{	
	
	private String m_id;
	private String m_title;
	private String m_desc;
	private String m_numViews;
	private String m_duration;
	private String m_hqThumb;
	private String m_mqThumb;
	private String m_lqThumb;
	private String m_videoUrl_1;
	private String m_videoUrl_2;
	private String m_embedded;
	
	
	public void setTitle(String a_title) {
		this.m_title = a_title;
	}
	public String getTitle() {
		return m_title;
	}
	public void setNumViews(String a_numViews) {
		this.m_numViews = a_numViews;
	}
	public String getNumViews() {
		return m_numViews;
	}
	public void setVideoUrl_1(String a_videoUrl) {
		this.m_videoUrl_1 = a_videoUrl;
	}
	public String getVideoUrl_1() {
		return m_videoUrl_1;
	}
	
	public void setVideoUrl_2(String a_videoUrl) {
		this.m_videoUrl_2 = a_videoUrl;
	}
	public String getVideoUrl_2() {
		return m_videoUrl_2;
	}
	
	public void setId(String a_id) {
		this.m_id = a_id;
	}
	public String getId() {
		return m_id;
	}
	public void setDesc(String a_desc) {
		this.m_desc = a_desc;
	}
	public String getDesc() {
		return m_desc;
	}
	public void setDuration(String a_duration) {
		this.m_duration = a_duration;
	}
	public String getDuration() {
		return m_duration;
	}
	public void setHQThumb(String a_hqThumb) {
		this.m_hqThumb = a_hqThumb;
	}
	public String getHQThumb() {
		return m_hqThumb;
	}
	public void setMQThumb(String a_mqThumb) {
		this.m_mqThumb = a_mqThumb;
	}
	public String getMQThumb() {
		return m_mqThumb;
	}
	public void setLQThumb(String a_lqThumb) {
		this.m_lqThumb = a_lqThumb;
	}
	public String getLQThumb() {
		return m_lqThumb;
	}
	public void setEmbedded(String a_embedded) {
		this.m_embedded = a_embedded;
	}
	public String getEmbedded() {
		return m_embedded;
	}

}

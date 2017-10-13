package com.fanclub.data;

import com.fanclub.abs.AbsEntityVO;
import com.fanclub.utils.FanClubConstants;

public class PhotoEntityVO extends AbsEntityVO implements FanClubConstants
{
	private String m_title;
	private String m_photosCount_album;
	private String m_photosTitle;
	private String m_photosFile_album;
	private String m_photosElementUrl;
	
	private PHOTOS_DATA_ENUM m_mode; 
	
	private int m_currentPhotoIdx = -1;

	public void setTitle(String m_title) {
		this.m_title = m_title;
	}
	public String getTitle() {
		return m_title;
	}
	public void setCount_Album(String a_count) {
		this.m_photosCount_album = a_count;
	}
	public String getCount_Album() {
		return m_photosCount_album;
	}
	
	public void setMode(PHOTOS_DATA_ENUM a_mode) {
		this.m_mode = a_mode;
	}
	public PHOTOS_DATA_ENUM getMode() {
		return m_mode;
	}
	public void setPhotosTitle(String a_photosTitle) {
		this.m_photosTitle = a_photosTitle;
	}
	public String getPhotosTitle() {
		return m_photosTitle;
	}
	public void setPhotosFile_album(String a_photosFile_album) {
		this.m_photosFile_album = a_photosFile_album;
	}
	public String getPhotosFile_album() {
		return m_photosFile_album;
	}
	public void setPhotosElementUrl(String a_photosElementUrl) {
		this.m_photosElementUrl = a_photosElementUrl;
	}
	public String getPhotosElementUrl() {
		return m_photosElementUrl;
	}
	public void setCurrentPhotoIdx(int a_currentPhotoIdx) {
		this.m_currentPhotoIdx = a_currentPhotoIdx;
	}
	public int getCurrentPhotoIdx() {
		return m_currentPhotoIdx;
	}

}

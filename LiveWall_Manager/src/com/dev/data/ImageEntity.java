package com.dev.data;

import android.net.Uri;

public class ImageEntity {
	
	private String m_imgUri = null;
	private String m_thumbUri = null;
	private String m_imgName = null;
	

	public void setImageURI(String m_imgUri) {
		this.m_imgUri = m_imgUri;
	}

	public Uri getImageURI() {
		Uri l_uri = Uri.parse(m_imgUri);
		return l_uri;
	}

	public void setThumbnailUri(String m_thumbUri) {
		this.m_thumbUri = m_thumbUri;
	}

	public Uri getThumbnailUri() {
		Uri l_uri = Uri.parse(m_thumbUri);
		return l_uri;
	}
	
	public void clean() {
		m_imgUri = null;
		m_thumbUri = null;
	}

	public void setImageName(String m_imgName) {
		this.m_imgName = m_imgName;
	}

	public String getImageName() {
		return m_imgName;
	}

}

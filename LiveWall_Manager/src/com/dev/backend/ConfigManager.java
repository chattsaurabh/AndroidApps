package com.dev.backend;

import android.content.ContentResolver;

public class ConfigManager {

	private static ConfigManager m_instance = null;

	private ContentResolver m_contenResolver = null;
	private ImageCompresser m_imgCompressor = null;
	
	
	private ConfigManager() {
		m_imgCompressor = new ImageCompresser();
	}
	
	public static ConfigManager getInstance()
	{
		if(null == m_instance)
		{
			m_instance = new ConfigManager();
		}
		return m_instance;
	}

	public void setContenResolver(ContentResolver m_contenResolver) {
		this.m_contenResolver = m_contenResolver;
	}

	public ContentResolver getContenResolver() {
		return m_contenResolver;
	}
	
	public ImageCompresser getImgCompressor() {
		return m_imgCompressor;
	}
}

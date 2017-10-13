package com.interviewer.se.abs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.util.Log;

public abstract class AbsPannerModel extends AbsModel{
	
	protected int PAGE_DATA_RANGE = 0;
	protected int m_currentIndex = 0; 
	protected int m_dataSize = -1;
	protected String m_fileName = null;
	protected File m_metadataFile = null;
	
	
	protected void prevDataSet() 
	{
		if(m_currentIndex - PAGE_DATA_RANGE < 0)
		{
			if(m_currentIndex == 0)
			{
				m_currentIndex = ((m_dataSize - PAGE_DATA_RANGE) >= 0)? (m_dataSize - PAGE_DATA_RANGE) : 0;
			}
			else
			{
				m_currentIndex = 0;
			}
		}
		else
		{
			m_currentIndex = m_currentIndex - PAGE_DATA_RANGE;
		}
	}	
	
	protected void nextDataSet() 
	{
		if(m_currentIndex + PAGE_DATA_RANGE >= m_dataSize)
		{
			m_currentIndex = 0;
		}
		else
		{
			m_currentIndex = m_currentIndex + PAGE_DATA_RANGE;
		}		
	}
	
	public int getCurrentIndex() {
		return m_currentIndex;
	}
	
	protected void getContents() {
		if(m_fileName != null)
		{
			int ch;
			String data = new String();
			StringBuffer fileContent = new StringBuffer("");
			try {
				
//				InputStream fis = new FileInputStream(m_metadataFile);
				InputStream fis = m_cont.getResources().getAssets().open(m_fileName);
				
				while ((ch = fis.read()) != -1)
					fileContent.append((char) ch);
				data = new String(fileContent);
			} catch (FileNotFoundException e) {
				Log.d("file", "file is not found");
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			processData(data);
		}
	}
	
	/*protected abstract void processData(String a_data);
	protected abstract void clean();*/
	
	public String getCurrentJsonFileName() {
		return m_fileName;
	}
	
	public int getDataSize() {
		return m_dataSize;
	}
}

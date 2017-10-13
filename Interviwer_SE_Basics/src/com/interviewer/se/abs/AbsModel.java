package com.interviewer.se.abs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;

import com.interviewer.se.util.Interviewer_SE_Basics_Constants;

import android.content.Context;
import android.util.Log;

public abstract class AbsModel implements Interviewer_SE_Basics_Constants{
	
	protected Context m_cont;
	protected JSONArray m_jsonArray;
	
	protected void populate(String a_jsonFile)
	{
		if(a_jsonFile != null)
		{
			int ch;
			String data = new String();
			StringBuffer fileContent = new StringBuffer("");
			try {
				
//				InputStream fis = new FileInputStream(m_metadataFile);
				InputStream fis = m_cont.getResources().getAssets().open(a_jsonFile);
				
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
	
	protected abstract void processData(String a_data);
	public abstract void clear();
}

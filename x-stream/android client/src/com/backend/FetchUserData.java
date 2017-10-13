package com.backend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import com.constants.JSON_Tag_Constants;
import com.constants.XStream_Constants;
import com.data.MediaEntity;

public class FetchUserData implements XStream_Constants, JSON_Tag_Constants{
	
	private File channelFile = null;
	private FileOutputStream fOut = null;
	private OutputStreamWriter osw = null;
	private MediaEntity currentMediaEntity = null;
	private Context currentContext = null;
	
	public FetchUserData(MediaEntity entity, Context cont) {
		currentMediaEntity = entity;
		currentContext = cont;
	}

	public String[] fetchChannelNames() {
		String[] channelTitle = null;
		
		String currentFileType = "";
		if(currentMediaEntity.getMedia_type().equalsIgnoreCase(AUDIO))
		{
			currentFileType = USERCHANNELSAUDIO;
		}
		else if (currentMediaEntity.getMedia_type().equalsIgnoreCase(VIDEO)) 
		{
			currentFileType = USERCHANNELSVIDEO;
		}
		File findChannelFile = new File(currentContext.getFilesDir(),currentFileType);	
		if(findChannelFile.exists())
		{
			try {
				InputStream tmpfis = new FileInputStream(findChannelFile);
				int tmpch;
				String tmpdata = new String();
				StringBuffer tmpfileContent = new StringBuffer("");
				while ((tmpch = tmpfis.read()) != -1)
					tmpfileContent.append((char) tmpch);
				tmpdata = new String(tmpfileContent);
				JSONArray fileJSONArray = new JSONArray(tmpdata);
				ArrayList<String> tmpChNameList = new ArrayList<String>();				
				for (int i = 0; i < fileJSONArray.length(); i++)
				{	
					Boolean addChName = true;
					if(tmpChNameList.size() > 0)
					{					
						for (String chName : tmpChNameList) {
							if(fileJSONArray.getJSONObject(i).getString(CHANNEL).equalsIgnoreCase(chName))
							{
								addChName = false;
							}
						}
					}
					if(addChName)
					{
						tmpChNameList.add(fileJSONArray.getJSONObject(i).getString(CHANNEL));						
					}					
				}	
				int c = 0;
				channelTitle = new String[tmpChNameList.size()];
				for (String name : tmpChNameList) {
					channelTitle[c] = name;
					c++;
				}
				tmpfis.close();
			} catch (FileNotFoundException e) {
				Log.d("file", "file is not found");
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return channelTitle;
	}
	
	public void setNewChannelName(String chName) {
		currentMediaEntity.setMedia_channel(chName);
	}
	
	
	public Boolean saveSongToFile() {
		
		Boolean added = true;
		
		String currentFileType = "";
		if(currentMediaEntity.getMedia_type().equalsIgnoreCase(AUDIO))
		{
			currentFileType = USERCHANNELSAUDIO;
		}
		else if (currentMediaEntity.getMedia_type().equalsIgnoreCase(VIDEO)) 
		{
			currentFileType = USERCHANNELSVIDEO;
		}
		
		channelFile = new File(currentContext.getFilesDir(),currentFileType);		
		if(!channelFile.exists())
		{
			try {
				Boolean fileCreated = channelFile.createNewFile();
				if(fileCreated)
				{
					Map<String,String> entityMap = assortJSonData(currentMediaEntity);
					fOut = new FileOutputStream(channelFile,true);
					osw = new OutputStreamWriter(fOut);
					JSONObject jo = new JSONObject(entityMap);
					osw.write("[");
					osw.write(jo.toString());
					osw.write("]");
					osw.close();
					fOut.close();
				}
			} catch (IOException e) {				
				e.printStackTrace();
				added = false;
			} 
		}
		else
		{	
			Boolean addChannel = true;
			try {
				InputStream tmpfis = new FileInputStream(channelFile);
				int tmpch;
				String tmpdata = new String();
				StringBuffer tmpfileContent = new StringBuffer("");
				while ((tmpch = tmpfis.read()) != -1)
					tmpfileContent.append((char) tmpch);
				tmpdata = new String(tmpfileContent);
				JSONArray fileJSONArray = new JSONArray(tmpdata);
				for (int i = 0; i < fileJSONArray.length(); i++)
				{
					if(currentMediaEntity.getMedia_artist().equalsIgnoreCase(fileJSONArray.getJSONObject(i).getString(ARTIST)) &&
						currentMediaEntity.getMedia_channel().equalsIgnoreCase(fileJSONArray.getJSONObject(i).getString(CHANNEL)) &&
						currentMediaEntity.getMedia_title().equalsIgnoreCase(fileJSONArray.getJSONObject(i).getString(TITLE)) &&
						currentMediaEntity.getMedia_type().equalsIgnoreCase(fileJSONArray.getJSONObject(i).getString(TYPE)))
					{
						addChannel = false;
					}
				}	
				tmpfis.close();
			} catch (FileNotFoundException e) {
				Log.d("file", "file is not found");
				e.printStackTrace();
				added = false;
				addChannel = false;
			} catch (IOException e) {
				e.printStackTrace();
				added = false;
				addChannel = false;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				added = false;
				addChannel = false;
			}
			
			if(addChannel)
			{
				Map<String,String> entityMap = assortJSonData(currentMediaEntity);
				int ch;								
				try {
					File tmp = new File(currentContext.getFilesDir(),"temp");
					
					fOut = new FileOutputStream(tmp,true);
					osw = new OutputStreamWriter(fOut);
					
					InputStream fis = new FileInputStream(channelFile);
					while ((ch = fis.read()) != -1)
					{
						if(93 != ch)
						{
							osw.write(ch);
						}
					}
					JSONObject jo = new JSONObject(entityMap);
					osw.write(",");
					osw.write(jo.toString());
					osw.write("]");
					
					fis.close();
					osw.close();
					fOut.close();
					channelFile.delete();
					File toName = new File(currentContext.getFilesDir(),currentFileType);
					tmp.renameTo(toName);
					
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					added = false;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					added = false;
				} 
			}
			else
			{
				showPopUpAlert(CHANNELALREADYPRESENT, "Add Song");	
				added = false;
			}
		}
		return added;
	}
	
	private Map<String,String> assortJSonData(MediaEntity media_entity) {
		
		Map<String, String> currentEntityMap = new HashMap<String, String>();
		currentEntityMap.put(ARTIST, media_entity.getMedia_artist());
		currentEntityMap.put(CHANNEL, media_entity.getMedia_channel());
		currentEntityMap.put(DESCRIPTION, media_entity.getMedia_desc());
		currentEntityMap.put(MEDIA_STREAM_URL, media_entity.getMedia_mediaUri());
		currentEntityMap.put(THUMBNAIL, media_entity.getMedia_thumbnailUri());
		currentEntityMap.put(TITLE, media_entity.getMedia_title());
		currentEntityMap.put(TYPE, media_entity.getMedia_type());
		
		return currentEntityMap;
	}
	
	public void showPopUpAlert(String text, String title) {
		@SuppressWarnings("unused")
		AlertDialog alertDialog;
		alertDialog = new AlertDialog.Builder(currentContext).
		setTitle(title).
		setMessage(text).
		setPositiveButton("OK", null).show();
	}
}

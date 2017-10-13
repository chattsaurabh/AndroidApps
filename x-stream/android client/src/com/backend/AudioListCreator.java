package com.backend;

import java.util.ArrayList;
import java.util.Map;

import com.data.MediaEntity;
import com.dev.Home;

public class AudioListCreator {
	
	private static AudioListCreator currentClassInstance = null;
	
	public static ArrayList<MediaEntity> currentAudioList;
	private String currentChannel;
	private MediaEntity homeLaunchEntity = null;

	private AudioListCreator() {
		if(null == currentAudioList)
			currentAudioList = new ArrayList<MediaEntity>();
	}
	
	public static AudioListCreator getInstance() {
		if(null == currentClassInstance)
		{
			currentClassInstance = new AudioListCreator();
		}
		return currentClassInstance;
	}
	
	public void setHomeLaunchEntity(MediaEntity entity) {
		homeLaunchEntity = entity;
	}
	
	public void setChannelName(String channel) {
		currentChannel = channel;
	}
	
	public ArrayList<MediaEntity> getAudioList() {
		return currentAudioList;
	}
	
	public void setAudioListData() {
		
		currentAudioList.clear();
		
		if(null != homeLaunchEntity)
		{
			currentAudioList.add(homeLaunchEntity);
		}
		ArrayList<Map<String, ArrayList<MediaEntity>>> currentArtists = Home.audioChannelMap.get(currentChannel);
		ArrayList<String> artistNames = new ArrayList<String>();
		for (Map<String, ArrayList<MediaEntity>> artist : currentArtists) {
			artistNames.add(artist.keySet().toString());
			for ( MediaEntity entity : artist.get(artist.keySet().toString().replace("[", "").replace("]", ""))) {
				currentAudioList.add(entity);
			}
		}
	}
}

package com.backend;

import java.util.ArrayList;
import java.util.Map;

import com.data.MediaEntity;
import com.dev.Home;

public class VideoListCreator {

	private static VideoListCreator currentClassInstance = null;
	
	public static ArrayList<MediaEntity> currentVideoList;
	private String currentChannel;
	private MediaEntity homeLaunchEntity = null;
	
	
	private VideoListCreator() {
		if(null == currentVideoList)
			currentVideoList = new ArrayList<MediaEntity>();
	}
	
	public static VideoListCreator getInstance() {
		if(null == currentClassInstance)
		{
			currentClassInstance = new VideoListCreator();
		}
		return currentClassInstance;
	}
	
	public void setVideoListData() {
		
		currentVideoList.clear();
		
		if(null != homeLaunchEntity)
		{
			currentVideoList.add(homeLaunchEntity);
		}
		ArrayList<Map<String, ArrayList<MediaEntity>>> currentArtists = Home.videoChannelMap.get(currentChannel);
		ArrayList<String> artistNames = new ArrayList<String>();
		for (Map<String, ArrayList<MediaEntity>> artist : currentArtists) {
			artistNames.add(artist.keySet().toString());
			for ( MediaEntity entity : artist.get(artist.keySet().toString().replace("[", "").replace("]", ""))) {
				currentVideoList.add(entity);
			}
		}
	}
	
	public void setHomeLaunchEntity(MediaEntity entity) {
		homeLaunchEntity = entity;
	}
	
	public void setChannelName(String channel) {
		currentChannel = channel;
	}
	
	public ArrayList<MediaEntity> getVideoList() {
		return currentVideoList;
	}
}

package com.dev;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.AdapterView.OnItemClickListener;

import com.adapter.SearchListAdapter;
import com.backend.AudioListCreator;
import com.backend.VideoListCreator;
import com.constants.XStream_Constants;
import com.data.MediaEntity;

public class SearchActivity extends Activity implements XStream_Constants{
	
	private ArrayList<MediaEntity> currentAudioList = null;
	private ArrayList<MediaEntity> currentVideoList = null;
	private ArrayList<MediaEntity> searchAudioList = null;
	private ArrayList<MediaEntity> searchVideoList = null;
	private ListView audioTabListView = null;
	private ListView videoTabListView = null;
	private TabHost searchTabHost = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_list);
		audioTabListView = (ListView) findViewById(R.id.SearchAudioListView);
		videoTabListView = (ListView) findViewById(R.id.SearchVideoListView);
		
		searchTabHost = (TabHost) findViewById(R.id.SearchTabhost);
		searchTabHost.setup();
		
		TabHost.TabSpec spec1 = searchTabHost.newTabSpec("tag1");

        spec1.setContent(R.id.SearchAudioListView);
        spec1.setIndicator("Audio");

        searchTabHost.addTab(spec1);

        TabHost.TabSpec spec2 = searchTabHost.newTabSpec("tag2");
        spec2.setContent(R.id.SearchVideoListView);
        spec2.setIndicator("Video");

        searchTabHost.addTab(spec2);
        
        
		
		final Intent queryIntent = getIntent();
		final String queryAction = queryIntent.getAction();
		if (Intent.ACTION_SEARCH.equals(queryAction)) {
			String searchKeywords = queryIntent.getStringExtra(SearchManager.QUERY);
			Log.d("search", "input --> "+searchKeywords);
			
			
			Set<String> audioChannelNames = Home.audioChannelMap.keySet();
			ArrayList<String> audioChNamesList = new ArrayList<String>(audioChannelNames);
			for(int i = 0;i<audioChNamesList.size();i++)
			{
				ArrayList<Map<String, ArrayList<MediaEntity>>> currentArtists = Home.audioChannelMap.get(audioChNamesList.get(i));
				ArrayList<String> artistNames = new ArrayList<String>();
				for (Map<String, ArrayList<MediaEntity>> artist : currentArtists) {
					artistNames.add(artist.keySet().toString());
					for ( MediaEntity entity : artist.get(artist.keySet().toString().replace("[", "").replace("]", ""))) {
						if(null == currentAudioList)
						{
							currentAudioList = new ArrayList<MediaEntity>();
						}
						currentAudioList.add(entity);
					}
				}
			}
			for (MediaEntity media : currentAudioList) {
				if(media.getMedia_title().equalsIgnoreCase(searchKeywords))
				{
					if(null == searchAudioList)
					{
						searchAudioList = new ArrayList<MediaEntity>();
					}
					searchAudioList.add(media);
				}
				if(media.getMedia_artist().equalsIgnoreCase(searchKeywords))
				{
					if(null == searchAudioList)
					{
						searchAudioList = new ArrayList<MediaEntity>();
					}
					searchAudioList.add(media);
				}
				if(media.getMedia_channel().equalsIgnoreCase(searchKeywords))
				{
					if(null == searchAudioList)
					{
						searchAudioList = new ArrayList<MediaEntity>();
					}
					searchAudioList.add(media);
				}
				if(media.getMedia_desc().contains(searchKeywords))
				{
					if(null == searchAudioList)
					{
						searchAudioList = new ArrayList<MediaEntity>();
					}
					searchAudioList.add(media);
				}
			}
			
			if((null != searchAudioList) && (searchAudioList.size() > 0))
			{
				SearchListAdapter audioAdapter = new SearchListAdapter(this, R.id.search, searchAudioList,AUDIO);
				audioTabListView.setAdapter(audioAdapter);
				audioTabListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						AudioListCreator.getInstance().setChannelName(searchAudioList.get(position).getMedia_channel());
						AudioListCreator.getInstance().setHomeLaunchEntity(searchAudioList.get(position));
						AudioListCreator.getInstance().setAudioListData();
						
						Intent audioPage = new Intent(audio_page_activity);
						audioPage.putExtra(LAUNCHEDFROMHOME, true);
						
						startActivity(audioPage);
					}
				});
			}
			
			
			Set<String> videoChannelNames = Home.videoChannelMap.keySet();
			ArrayList<String> videoChNamesList = new ArrayList<String>(videoChannelNames);
			for(int i = 0;i<videoChNamesList.size();i++)
			{
				ArrayList<Map<String, ArrayList<MediaEntity>>> currentArtists = Home.videoChannelMap.get(videoChNamesList.get(i));
				ArrayList<String> artistNames = new ArrayList<String>();
				for (Map<String, ArrayList<MediaEntity>> artist : currentArtists) {
					artistNames.add(artist.keySet().toString());
					for ( MediaEntity entity : artist.get(artist.keySet().toString().replace("[", "").replace("]", ""))) {
						if(null == currentVideoList)
						{
							currentVideoList = new ArrayList<MediaEntity>();
						}
						currentVideoList.add(entity);
					}
				}
			}
			for (MediaEntity media : currentVideoList) {
				if(media.getMedia_title().equalsIgnoreCase(searchKeywords))
				{
					if(null == searchVideoList)
					{
						searchVideoList = new ArrayList<MediaEntity>();
					}
					searchVideoList.add(media);
				}
				if(media.getMedia_artist().equalsIgnoreCase(searchKeywords))
				{
					if(null == searchVideoList)
					{
						searchVideoList = new ArrayList<MediaEntity>();
					}
					searchVideoList.add(media);
				}
				if(media.getMedia_channel().equalsIgnoreCase(searchKeywords))
				{
					if(null == searchVideoList)
					{
						searchVideoList = new ArrayList<MediaEntity>();
					}
					searchVideoList.add(media);
				}
				if(media.getMedia_desc().contains(searchKeywords))
				{
					if(null == searchVideoList)
					{
						searchVideoList = new ArrayList<MediaEntity>();
					}
					searchVideoList.add(media);
				}

			}
			
			if((null != searchVideoList) && (searchVideoList.size() > 0))
			{
				SearchListAdapter videoAdapter = new SearchListAdapter(this, R.id.search, searchVideoList,VIDEO);
				videoTabListView.setAdapter(videoAdapter);
				videoTabListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						VideoListCreator.getInstance().setHomeLaunchEntity(searchVideoList.get(position));
						VideoListCreator.getInstance().setChannelName(searchVideoList.get(position).getMedia_channel());
						VideoListCreator.getInstance().setVideoListData();
									
						startActivity(new Intent(video_player_activity));
						
					}
				});
			}
			
		}
		
	}
}

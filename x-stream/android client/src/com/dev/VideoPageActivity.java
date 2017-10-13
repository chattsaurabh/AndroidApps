package com.dev;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

import com.adapter.VideoPageGallerAdapter;
import com.backend.VideoListCreator;
import com.constants.XStream_Constants;
import com.data.MediaEntity;

public class VideoPageActivity extends Activity implements XStream_Constants, OnDrawerOpenListener, OnDrawerCloseListener {
	
	private ImageButton addChannelsButton;
	private Gallery filmStripGallery;
	private ListView videoChannelsList;
	private SlidingDrawer videoSlider;
	public ViewFlipper videoViewFlipper;
	private static View bodyImage;
	private ImageButton stream;
	private  ArrayList<MediaEntity> currentVideoList;
	private VideoPageGallerAdapter gallerryAdapter;
	private VideoListCreator dataInstance;
	private int currentVideoIndex = 0;
	private ArrayList<MediaEntity> newVideoList = null;


	/*private Integer[] galleryImgId = {  R.drawable.linkinpark_000,
										R.drawable.guitar_fender,
										R.drawable.dj_splash,
										R.drawable.xpress_splash};
	*/
	
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.about:
	    	Intent about = new Intent(about_help_activity);	    	
	    	about.putExtra(ABOUTHELPTEXT, true);
	    	startActivity(about);
	        return true;
	    case R.id.help:
	    	Intent help = new Intent(about_help_activity);
	    	help.putExtra(ABOUTHELPTEXT, false);
	    	startActivity(help);
	        return true;
	    case R.id.settings:
	    	startActivity(new Intent(settings_activity));
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_page);
		
		dataInstance = VideoListCreator.getInstance();
		
		Home.current_media = VIDEO;
		setGalleryData(0);
		
		addChannelsButton = (ImageButton)findViewById(R.id.HomeViewFlipperAddChannelsImageButton);
		addChannelsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// check and add to user's channels here
				Bundle mediaBubdle = new Bundle();
				mediaBubdle.putParcelable(MEDIAPARCELABLE, currentVideoList.get(currentVideoIndex));
				Intent addChannelsIntent = new Intent(UserChannels_activity);
				addChannelsIntent.putExtra(MEDIABUNDLE, mediaBubdle);
				startActivityForResult(addChannelsIntent, 1);				
			}
		});
		
		bodyImage = findViewById(R.id.VideoPageWidgetFilmStripImage);
		stream = (ImageButton) bodyImage.findViewById(R.id.HomeViewFlipperItemImageButton);
		stream.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				launchVideo();				
			}
		});
		setStreamURI(0);
		gallerryAdapter = new VideoPageGallerAdapter(this);
		filmStripGallery = (Gallery) findViewById(R.id.VideoPageWidgetFilmStripGallery);
		filmStripGallery.setAdapter(gallerryAdapter);
		filmStripGallery.setSelection((int)( Integer.MAX_VALUE / 2 ) - (( Integer.MAX_VALUE / 2 ) % currentVideoList.size()));
		

		filmStripGallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (position >= currentVideoList.size()) { 
	                position = position % currentVideoList.size(); 
	            } 
						
				if (position < 0)
				    position = position + currentVideoList.size();
				
				currentVideoIndex = position;
				setBodyImage(currentVideoList.get(position).getTempThumbnailImage());
				setStreamURI(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
			
		videoSlider = (SlidingDrawer) findViewById(R.id.VideoPageWidgetDrawer);
		videoSlider.setOnDrawerCloseListener(this);
		videoSlider.setOnDrawerOpenListener(this);
		
		videoChannelsList = (ListView) findViewById(R.id.VideoPageWidgetListView);
		videoChannelsList.setAdapter(new ArrayAdapter(this, R.layout.list_item, setChannelNames()));
			
		videoChannelsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Log.d("file", "position --> "+position);				
				setGalleryData(position);
				resetGalleryImages();
			}
			
		});
		
		setBodyImage(currentVideoList.get(currentVideoIndex).getTempThumbnailImage());
		setStreamURI(currentVideoIndex);
		
	}
	
	private void setGalleryData(int index) {
		Set<String> channelNames = Home.videoChannelMap.keySet();
		ArrayList<String> chNamesList = new ArrayList<String>(channelNames);
		dataInstance.setChannelName(chNamesList.get(index));
		dataInstance.setVideoListData();
					
		currentVideoList = dataInstance.getVideoList();	
	}
	
	private void resetGalleryImages() {
		int position = filmStripGallery.getSelectedItemPosition();
		if (position >= currentVideoList.size()) { 
            position = position % currentVideoList.size(); 
        } 
				
		if (position < 0)
		    position = position + currentVideoList.size();
		
		currentVideoIndex = position;
		gallerryAdapter.notifyDataSetChanged();
		setBodyImage(currentVideoList.get(currentVideoIndex).getTempThumbnailImage());
		setStreamURI(currentVideoIndex);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == 1)
		{
			Bundle currentMediaBundle = data.getBundleExtra(MEDIABUNDLE);
			MediaEntity currentMediaEntity = currentMediaBundle.getParcelable(MEDIAPARCELABLE);
			resetDS(currentMediaEntity);
			Log.d("file", "channel --> "+currentMediaEntity.getMedia_channel());
		}
	}

	private void resetDS(MediaEntity new_entity) {
		
		if(null == newVideoList)
		{
			Set<String> videoChannelNames = Home.videoChannelMap.keySet();
			ArrayList<String> videoChNamesList = new ArrayList<String>(videoChannelNames);
			for(int i = 0;i<videoChNamesList.size();i++)
			{
				ArrayList<Map<String, ArrayList<MediaEntity>>> currentArtists = Home.videoChannelMap.get(videoChNamesList.get(i));
				ArrayList<String> artistNames = new ArrayList<String>();
				for (Map<String, ArrayList<MediaEntity>> artist : currentArtists) {
					artistNames.add(artist.keySet().toString());
					for ( MediaEntity entity : artist.get(artist.keySet().toString().replace("[", "").replace("]", ""))) {
						if(null == newVideoList)
						{
							newVideoList = new ArrayList<MediaEntity>();
						}
						newVideoList.add(entity);
					}
				}
			}
		}
		newVideoList.add(0, new_entity);
		
		if((null != newVideoList) && (newVideoList.size() > 0))
		{
			Home.videoChannelMap.clear();
			Home.videoChannelMap = Home.assortJSonData(newVideoList);
		}
		
		videoChannelsList.setAdapter(new ArrayAdapter(this, R.layout.list_item, setChannelNames()));	
		setGalleryData(0);
		resetGalleryImages();
	}

	private String[] setChannelNames() {
		String[] videoTitle = new String[Home.videoChannelMap.keySet().size()];
		int c = 0;
		
		for (String title : Home.videoChannelMap.keySet()) {
			videoTitle[c] = title;
			c++;
		}
		
		return videoTitle;
	}
	private void setStreamURI(int position) {
		VideoPlayer.setCurrentIndex(position);
		
	}
	public static void setBodyImage(int rscrId) {
		ImageView iv = (ImageView) bodyImage.findViewById(R.id.HomeViewFlipperItemImageView);
		iv.setImageResource(rscrId);
		
	}
	@Override
	public void onDrawerOpened() {
		
	}

	@Override
	public void onDrawerClosed() {
		
	}
	
	private void launchVideo() {
		startActivityForResult(new Intent(video_player_activity),1);
	}
}

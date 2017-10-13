package com.dev;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SlidingDrawer;
import android.widget.ViewSwitcher;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

import com.backend.AudioListCreator;
import com.backend.Cache;
import com.constants.AudioUrlConstants;
import com.constants.XStream_Constants;
import com.data.MediaEntity;

public class AudioPageActivity extends Activity implements XStream_Constants, OnDrawerOpenListener, OnDrawerCloseListener, AudioUrlConstants {

	private static final String TAG = "audio";
	private ImageButton playPauseButton;
	private ImageButton nextButton;
	private ImageButton addChannelsButton;
	private SeekBar audioSeek;
	private ImageView thumbnailImage;
	private Boolean play = false;
	private Boolean continuePlay = true;
	private MediaPlayer audioPlayer;
	public Runnable seekRunnable;
	private Timer timer1;
	private TimerTask mTask;
	private int currentAudioIndex = 0;
	private ListView audioChannelsList;
	SlidingDrawer slider;
	private AlertDialog.Builder builder;
	private ArrayList<MediaEntity> currentAudioList;
	private Context currentContext;
	private PlayAudioTask playTask = null;
	private ProgressDialog loadingDialog;
	private AudioListCreator dataInstance;
	private ShowThumbNail showThumbnail = null;
	private ViewSwitcher thumbnailViewSwithcer;
	private ProgressBar thumbnailProgress;
	private AudioPageActivity instance;
	private String[] audioTitle;
	private ArrayList<MediaEntity> newAudioList = null;
	
	private String url = "http://cdn.mblrd.com/i/80-120/s/aHR0cDovL2R2MS5tYmxyZC5jb20vaS80ODAtNDgwL3MvYUhSMGNEb3ZMMjF2WW1sc1pYSnZZV1JwWlM1amIyMHZabWxzWlhNdmRYQnNiMkZrY3k4NFpDODRaR1EyWVRZME0yRTRPV1JpTUdWbU0yRmtNemhsWkdNM1pqTTVPR05pTXcsLA,,";
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what){
			case 1:
				seekRunnable.run();
				break;				
			default:
				break;		    				
	
			}
		}
	
	};
	
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
	
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	@SuppressWarnings("unchecked")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.audio_page);
		audioPlayer = null;		
		dataInstance = AudioListCreator.getInstance();
		instance = this;
		
		builder = new AlertDialog.Builder(this);
		builder.setMessage("Sorry!! Please check your network connection and try again.")
	       .setCancelable(false)
	       .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	                dialog.cancel();
	           }
	       });

		thumbnailViewSwithcer = (ViewSwitcher) findViewById(R.id.ArtistDetailsThumbnailViewSwitcher);
		thumbnailImage = (ImageView) findViewById(R.id.ArtistDetailsThumbnailImageView);
		thumbnailProgress = (ProgressBar) findViewById(R.id.ArtistDetailsThumbnailProgress);
		
		currentContext = this;		
		Home.current_media = AUDIO;
		
		Boolean isLaunchedFromHome = getIntent().getBooleanExtra(LAUNCHEDFROMHOME, false);
		if(!isLaunchedFromHome)
		{
			Set<String> channelNames = Home.audioChannelMap.keySet();
			ArrayList<String> chNamesList = new ArrayList<String>(channelNames);		
			dataInstance.setChannelName(chNamesList.get(0));
			dataInstance.setAudioListData();
		}
		currentAudioList = dataInstance.getAudioList();
		
		slider = (SlidingDrawer) findViewById(R.id.AudioPageWidgetDrawer);
		slider.setOnDrawerCloseListener(this);
		slider.setOnDrawerOpenListener(this);
		
		audioChannelsList = (ListView) findViewById(R.id.AudioPageWidgetListView);

		audioTitle = new String[Home.audioChannelMap.keySet().size()];
		int c = 0;
		for (String title : Home.audioChannelMap.keySet()) {
			audioTitle[c] = title;
			Log.d("audio", "title -> "+audioTitle[c]);
			c++;
		}
		audioChannelsList.setAdapter(new ArrayAdapter(this, R.layout.list_item, audioTitle));
		
		audioChannelsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				Set<String> channelNames = Home.audioChannelMap.keySet();
				ArrayList<String> chNamesList = new ArrayList<String>(channelNames);
				dataInstance.setChannelName(chNamesList.get(position));
				dataInstance.setAudioListData();
				currentAudioList = dataInstance.getAudioList();
				
				if(null != audioPlayer)
				{
					if(audioPlayer.isPlaying())
					{
						audioPlayer.stop();						
					}
					audioPlayer.reset();
				}
				currentAudioIndex = 0;

				if(null != playTask)
				{
					playTask.cancel(true);
					playTask = null;
				}
				playTask = new PlayAudioTask();				
				playTask.execute(Uri.parse(currentAudioList.get(currentAudioIndex).getMedia_mediaUri()));
			}
		});
		
		timer1 = new Timer();
		mTask = new TimerTask() {
			@Override
			public void run() {
			/*Give the callback to caller*/
					mHandler.sendEmptyMessage(1);
				}
			};
		
		timer1.schedule(mTask, 10,10);
		
		thumbnailViewSwithcer.showNext();
		playTask = new PlayAudioTask();		
		playTask.execute(Uri.parse(currentAudioList.get(0).getMedia_mediaUri()));

		seekRunnable = new Runnable()
        {
            @Override
            public void run() 
            {
                if (audioPlayer != null) 
                {
                    if (audioPlayer.isPlaying()) 
                    {
                        try 
                        {
                           int currentPosition = audioPlayer.getCurrentPosition();                                
                           audioSeek.setProgress(currentPosition);                           

                        } catch (Exception e) {
                            e.printStackTrace();
                        }                      
                    }
                }
            }

        };
        
        playPauseButton = (ImageButton) findViewById(R.id.PlayPauseImageButton);
//		startPlay();
		play = true;
		playPauseButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				playOrPause();
			}
		});
		
		audioSeek = (SeekBar) findViewById(R.id.AudioPlayerSeekBar);
//		audioSeek.setMax(audioPlayer.getDuration());
		
		
		nextButton = (ImageButton) findViewById(R.id.NextImageButton);
		nextButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				playNext();
			}
		});
		
		addChannelsButton = (ImageButton)findViewById(R.id.AddImageButton);
		addChannelsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// check and add to user's channels here
				Bundle mediaBubdle = new Bundle();
				mediaBubdle.putParcelable(MEDIAPARCELABLE, currentAudioList.get(currentAudioIndex));
				Intent addChannelsIntent = new Intent(UserChannels_activity);
				addChannelsIntent.putExtra(MEDIABUNDLE, mediaBubdle);
				startActivityForResult(addChannelsIntent, 1);				
			}
		});
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
		
		if(null == newAudioList)
		{
			Set<String> audioChannelNames = Home.audioChannelMap.keySet();
			ArrayList<String> audioChNamesList = new ArrayList<String>(audioChannelNames);
			for(int i = 0;i<audioChNamesList.size();i++)
			{
				ArrayList<Map<String, ArrayList<MediaEntity>>> currentArtists = Home.audioChannelMap.get(audioChNamesList.get(i));
				ArrayList<String> artistNames = new ArrayList<String>();
				for (Map<String, ArrayList<MediaEntity>> artist : currentArtists) {
					artistNames.add(artist.keySet().toString());
					for ( MediaEntity entity : artist.get(artist.keySet().toString().replace("[", "").replace("]", ""))) {
						if(null == newAudioList)
						{
							newAudioList = new ArrayList<MediaEntity>();
						}
						newAudioList.add(entity);
					}
				}
			}
		}
		newAudioList.add(0, new_entity);
		
		
		// resetting channel list
		Boolean addNewChannelName = true;
		ArrayList<String> newChannels = new ArrayList<String>();
		for (String ch : audioTitle) {
			newChannels.add(ch);
			if(ch.equalsIgnoreCase(new_entity.getMedia_channel()))
			{
				addNewChannelName = false;
			}
		}
		if(addNewChannelName)
		{
			newChannels.add(0, new_entity.getMedia_channel());
		}
		audioTitle = null;
		audioTitle = new String[newChannels.size()];
		int c = 0;
		for (String title : newChannels) {
			audioTitle[c] = title;
			Log.d("audio", "title -> "+audioTitle[c]);
			c++;
		}		
		audioChannelsList.setAdapter(new ArrayAdapter(this, R.layout.list_item, audioTitle));
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if((null != newAudioList) && (newAudioList.size() > 0))
		{
			Home.audioChannelMap.clear();
			Home.audioChannelMap = Home.assortJSonData(newAudioList);
		}
	}
	
	private void playNext() {
		if(null != audioPlayer)
		{
			if(audioPlayer.isPlaying())
			{
				audioPlayer.stop();				
			}
			audioPlayer.reset();
		}
		currentAudioIndex++;
		if(currentAudioIndex > (currentAudioList.size()-1))
			currentAudioIndex = 0;
		
		if(null != playTask)
		{
			playTask.cancel(true);
			playTask = null;
		}
		playTask = new PlayAudioTask();
		playTask.execute(Uri.parse(currentAudioList.get(currentAudioIndex).getMedia_mediaUri()));
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(null != audioPlayer)
			audioPlayer.pause();
	}
	
	private void playOrPause() {
		if(play)
		{
			pausePlay();
		}
		else
		{
			startPlay();
		}
		play = !play;
	}
	
	private void startPlay() {
		playPauseButton.setBackgroundResource(R.drawable.pause);
		if(null != audioPlayer)
		{
			audioPlayer.start();
		}
	}
	
	private void pausePlay() {
		playPauseButton.setBackgroundResource(R.drawable.play);
		if(null != audioPlayer)
		{
			audioPlayer.pause();
		}
	}
	
	@Override
	public void onDrawerOpened() {

//		audioChannelsList.setVisibility(ListView.GONE);
	}
	@Override
	public void onDrawerClosed() {

//		audioChannelsList.setVisibility(ListView.VISIBLE);
	}
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	private class PlayAudioTask extends AsyncTask<Uri, Boolean, Boolean>
	{
		public PlayAudioTask()
		{
			continuePlay = true;
			thumbnailViewSwithcer.setDisplayedChild(0);
			showLoading();
		}
		@Override
		protected Boolean doInBackground(Uri... params) {
			try {				
				playerSetup(params[0]);				
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {			
			hideLoading();		
			Log.d("audio", "result --> "+result+"   continue playing --> "+continuePlay+"  audio player --> "+audioPlayer);
			if(result && (null != audioPlayer))
			{	
				Log.d(TAG, "playing...");
				if(continuePlay)
				{	
					startPlay();
					audioSeek.setMax(audioPlayer.getDuration());
				}
				else
				{
					cancelPlay();
				}
			}
			else if(!result)
			{
				if(null != audioPlayer)
				{
					audioPlayer.reset();
					audioPlayer = null;
				}
				errorHandle();
			}
			if(null != showThumbnail)
			{
				showThumbnail.cancel(true);
				showThumbnail = null;
			}
			showThumbnail = new ShowThumbNail();				
			showThumbnail.execute(currentAudioList.get(currentAudioIndex).getMedia_thumbnailUri());
			
			super.onPostExecute(result);
		}
		private void playerSetup(Uri resourceId) {	
			if(null != audioPlayer)
			{
				audioPlayer.reset();
				audioPlayer = null;
			}
			audioPlayer = MediaPlayer.create(currentContext, resourceId);		
			audioPlayer.setLooping(false);
			audioPlayer.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					playNext();					
				}
			});
		}		
	}
	
	private void showLoading() {
		if(loadingDialog != null)
		{
			loadingDialog.dismiss();
			loadingDialog = null;
		}		
		loadingDialog = new ProgressDialog(this);
		loadingDialog.setCancelable(true);
		loadingDialog.setMessage("Loading. Please wait...");
	/*	loadingDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				continuePlay = false;
			}
		});*/
		loadingDialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				Log.d(TAG, "inside cancel");
				continuePlay = false;								
			}
		});
		loadingDialog.show();
	}
	
	private void hideLoading() {
		if(loadingDialog != null)
		{
			loadingDialog.dismiss();
			loadingDialog = null;
		}		
	}
	
	private void errorHandle() {
		AlertDialog alertDialog;
		alertDialog = new AlertDialog.Builder(this).
		setTitle("Error !").
		setMessage("Sorry!! Please check your network connection and try again.").
		setPositiveButton("OK", null).show();
	}
	
	private void cancelPlay() {
		if(null != audioPlayer)
		{
			Log.d(TAG, "cancelling...");
			if(audioPlayer.isPlaying())
			{
				audioPlayer.stop();				
			}
			audioPlayer.reset();
			audioPlayer = null;					
		}
	}	
	
	private class ShowThumbNail extends AsyncTask<String, Boolean, Bitmap>
	{

		@Override
		protected Bitmap doInBackground(String... params) {			
			try {
				Bitmap bm = Cache.getImage(params[0], currentContext, true);
				if(null != bm)
					return bm;
				
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			if(null != result)
				thumbnailImage.setImageBitmap(result);
			else
				thumbnailImage.setImageResource(R.drawable.showphotos);
			thumbnailViewSwithcer.showNext();
			super.onPostExecute(result);
		}
		
	}
	
}

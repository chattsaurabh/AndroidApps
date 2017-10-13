package com.dev;

import java.io.IOException;
import java.util.ArrayList;
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
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.backend.VideoListCreator;
import com.constants.VidioUrlConstants;
import com.constants.XStream_Constants;
import com.data.MediaEntity;

public class VideoPlayer extends Activity implements
OnBufferingUpdateListener, OnCompletionListener,
OnPreparedListener, OnVideoSizeChangedListener, SurfaceHolder.Callback, VidioUrlConstants, XStream_Constants{

	private static final String TAG = "video";
    private int mVideoWidth;
    private int mVideoHeight;
    private MediaPlayer mMediaPlayer;
    private SurfaceView mPreview;
    private SurfaceHolder holder;
    private boolean mIsVideoSizeKnown = false;
    private boolean mIsVideoReadyToBePlayed = false;
    public Runnable seekRunnable;
	private Timer timer1;
	private TimerTask mTask;
	public SeekBar videoSeek;
	public ImageButton playPauseButton;
	public ImageButton nextButton;
	public ImageButton addChannelsButton;
	Boolean play = true;
	private static int currentVideoIndex = 0;
	private ProgressDialog loadingDialog;
	private Context currentContext;
	private OnBufferingUpdateListener onBufferingUpdateListener;
	private OnCompletionListener onCompletionListener;
	private OnPreparedListener onPreparedListener;
	private OnVideoSizeChangedListener onVideoSizeChangedListener;
	private PlayVideoTask playTask;
	private  ArrayList<MediaEntity> currentVideoList;
	private ArrayList<MediaEntity> newVideoList = null;
	
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
	
    
	public static void setCurrentIndex(int currentIndex) {
//		currentVideoIndex = currentIndex;
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_player);
		
		currentVideoList = VideoListCreator.getInstance().getVideoList();
		currentContext = this;
		onBufferingUpdateListener = this;
		onCompletionListener = this;
		onPreparedListener = this;
		onVideoSizeChangedListener = this;
//        extras = getIntent().getExtras();		
        
        timer1 = new Timer();
		mTask = new TimerTask() {
			@Override
			public void run() {
			/*Give the callback to caller*/
					mHandler.sendEmptyMessage(1);
				}
			};
		
		
		seekRunnable = new Runnable()
        {
            @Override
            public void run() 
            {
                if (mMediaPlayer != null) 
                {
                    if (mMediaPlayer.isPlaying()) 
                    {
                        try 
                        {
                           int currentPosition = mMediaPlayer.getCurrentPosition();

                                // the other change:
                           videoSeek.setProgress(currentPosition);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        
                    }
                }
            }

        };

        videoSeek = (SeekBar) findViewById(R.id.AudioPlayerSeekBar);
        
        playPauseButton = (ImageButton) findViewById(R.id.PlayPauseImageButton);
		playPauseButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				playOrPause();
			}
		});
		
		nextButton = (ImageButton) findViewById(R.id.NextImageButton);
		nextButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						playNext();
					}
				});
		
		addChannelsButton = (ImageButton)findViewById(R.id.AddImageButton);
		addChannelsButton.setVisibility(8);
		addChannelsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// check and add to user's channels here
				Bundle mediaBubdle = new Bundle();
				mediaBubdle.putParcelable(MEDIAPARCELABLE, currentVideoList.get(currentVideoIndex));
				Intent addChannelsIntent = new Intent(UserChannels_activity);
				addChannelsIntent.putExtra(MEDIABUNDLE, mediaBubdle);
//				startActivityForResult(addChannelsIntent, 1);
				startActivity(addChannelsIntent);
			}
		});
		
		mPreview = (SurfaceView) findViewById(R.id.surface);
        holder = mPreview.getHolder();
        holder.addCallback(this);
        timer1.schedule(mTask, 10,10);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        
        playTask = new PlayVideoTask();
		playTask.execute(true);
        
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
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
			Set<String> audioChannelNames = Home.audioChannelMap.keySet();
			ArrayList<String> audioChNamesList = new ArrayList<String>(audioChannelNames);
			for(int i = 0;i<audioChNamesList.size();i++)
			{
				ArrayList<Map<String, ArrayList<MediaEntity>>> currentArtists = Home.audioChannelMap.get(audioChNamesList.get(i));
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
		
		
		// resetting channel list
		/*Boolean addNewChannelName = true;
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
		audioChannelsList.setAdapter(new ArrayAdapter(this, R.layout.list_item, audioTitle));*/
	}


	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	private void playNext() {		
		currentVideoIndex++;
		if(currentVideoIndex >= currentVideoList.size())
			currentVideoIndex = 0;
		
		Log.d(TAG, "currentVideoIndex --> "+currentVideoIndex+"  VideoPageActivity.currentVideoList.size() --> "+currentVideoList.size());
		if((null != mMediaPlayer) && mMediaPlayer.isPlaying())
			mMediaPlayer.stop();
		
		releaseMediaPlayer();
		playTask.cancel(true);
		playTask = null;
		playTask = new PlayVideoTask();
		playTask.execute(true);
	}
	
	
    public void onBufferingUpdate(MediaPlayer arg0, int percent) {
        Log.d(TAG, "onBufferingUpdate percent:" + percent);
    }

    public void onCompletion(MediaPlayer arg0) {
        Log.d(TAG, "onCompletion called");
    }

    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        Log.v(TAG, "onVideoSizeChanged called");
        if (width == 0 || height == 0) {
            Log.e(TAG, "invalid video width(" + width + ") or height(" + height + ")");
            return;
        }
        mIsVideoSizeKnown = true;
        mVideoWidth = width;
        mVideoHeight = height;
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            startVideoPlayback();
        }
    }

    public void onPrepared(MediaPlayer mediaplayer) {
        Log.d(TAG, "onPrepared called");
        hideLoading();
        if(null != mMediaPlayer)
        {
        	mIsVideoReadyToBePlayed = true;
	        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
	            startVideoPlayback();	            
	        }
        }
    }

    public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
        Log.d(TAG, "surfaceChanged called");
    }

    public void surfaceDestroyed(SurfaceHolder surfaceholder) {
        Log.d(TAG, "surfaceDestroyed called");
    }


    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated called");
//        playVideo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaPlayer();
        doCleanUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
        doCleanUp();
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void doCleanUp() {
        mVideoWidth = 0;
        mVideoHeight = 0;
        mIsVideoReadyToBePlayed = false;
        mIsVideoSizeKnown = false;        
    }

    private void startVideoPlayback() {
        Log.v(TAG, "startVideoPlayback");
        if(null != mMediaPlayer)
        {
        	Log.d(TAG, "currentVideoIndex --> "+currentVideoIndex);
        	holder.setFixedSize(mVideoWidth, mVideoHeight);
        	try {
				mMediaPlayer.start();
			} catch (IllegalStateException e) {
				e.printStackTrace();
				AlertDialog alertDialog;
				alertDialog = new AlertDialog.Builder(currentContext).
				setTitle("Error !").
				setMessage("Sorry!! Please check your network connection and try again.").
				setPositiveButton("OK", null).show();
			}
        	videoSeek.setMax(mMediaPlayer.getDuration());	  
        }
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
		if(null != mMediaPlayer)
			mMediaPlayer.start();
	}
	
	private void pausePlay() {
		playPauseButton.setBackgroundResource(R.drawable.play);
		if(null != mMediaPlayer)
			mMediaPlayer.pause();
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
		
		loadingDialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				doCleanUp();
				releaseMediaPlayer();
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
	
	private class PlayVideoTask extends AsyncTask<Boolean, Boolean, Boolean>
	{
		public PlayVideoTask()
		{
			showLoading();
		}
		@Override
		protected Boolean doInBackground(Boolean... params) {
			try {				
				playVideo();				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if(!result)
			{
				doCleanUp();
				releaseMediaPlayer();
				errorHandle();
			}
			super.onPostExecute(result);
		}

		private void playVideo() {
	        doCleanUp();
	        try {
	            // Create a new media player and set the listeners
	            mMediaPlayer = new MediaPlayer();
	            playerSetup();
	        } catch (Exception e) {
	        	Log.d(TAG, "currentVideoIndex --> "+currentVideoIndex+"  VideoPageActivity.currentVideoList.size() --> "+currentVideoList.size());
	            Log.e(TAG, "error: " + e.getMessage(), e);
	        }
	    }

		private void playerSetup() {
			try {
				Log.d(TAG, "player setup called. media url = "+currentVideoList.get(currentVideoIndex).getMedia_mediaUri());
				mMediaPlayer.setDataSource(currentVideoList.get(currentVideoIndex).getMedia_mediaUri());
				mMediaPlayer.setDisplay(holder);
				mMediaPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
				mMediaPlayer.setOnCompletionListener(onCompletionListener);
				mMediaPlayer.setOnPreparedListener(onPreparedListener);
				mMediaPlayer.setOnVideoSizeChangedListener(onVideoSizeChangedListener);
				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mMediaPlayer.prepare();	
				mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer mp) {
						playNext();
					}
				});
				mMediaPlayer.setOnErrorListener(new OnErrorListener() {
					
					@Override
					public boolean onError(MediaPlayer mp, int what, int extra) {
						doCleanUp();
						releaseMediaPlayer();
						errorHandle();
						return false;
					}
				});
			} catch (IllegalArgumentException e) {
				hideLoading();
				errorHandle();
				Log.d(TAG, "illegal argument");
				e.printStackTrace();				
			} catch (IllegalStateException e) {
				hideLoading();
				errorHandle();
				Log.d(TAG, "illegal state");
				e.printStackTrace();				
			} catch (IOException e) {
				hideLoading();
				errorHandle();
				Log.d(TAG, "io exception");
				e.printStackTrace();				
			}
		}
	}
	
	private void errorHandle() {
		@SuppressWarnings("unused")
		AlertDialog alertDialog;
		alertDialog = new AlertDialog.Builder(this).
		setTitle("Error !").
		setMessage("Sorry!! Please check your network connection and try again.").
		setPositiveButton("OK", null).show();
	}
}

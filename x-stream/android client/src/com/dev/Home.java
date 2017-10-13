package com.dev;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemClickListener;

import com.backend.AudioListCreator;
import com.backend.VideoListCreator;
import com.constants.JSON_Tag_Constants;
import com.constants.XStream_Constants;
import com.data.MediaEntity;

public class Home extends Activity implements XStream_Constants,
JSON_Tag_Constants {

	private ArrayList<MediaEntity> audioDataList;
	private ArrayList<MediaEntity> videoDataList;

	public static Map<String, ArrayList<Map<String, ArrayList<MediaEntity>>>> videoChannelMap;
	public static Map<String, ArrayList<Map<String, ArrayList<MediaEntity>>>> audioChannelMap;

	public static String current_media;

	private Button audioButton;
	private Button videoButton;
	private ViewFlipper homeViewFlipper;
	private ImageView searchButton;	

	protected boolean _active = true;
	public Timer timer1 = new Timer();
	private TimerTask mTask;
	protected int page_count = 0;
	private Boolean forward = true;
	private Boolean flippingStopped = false;
	private Thread fwdFlip;
	private static ProgressDialog loadingDialog;

	private JSONObject jsonObject;
	private JSONArray homeJSONArray;
	private JSONArray audioJSONArray;
	private JSONArray videoJSONArray;
	private ArrayList<Map<String, Object>> homeFlipperItemArrayList;

	private ArrayList<MediaEntity> newMediaList;

	public static Boolean videoLaunchedFromHome = false;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			int res = msg.arg1;
			switch (msg.what) {
			case 1:
				if (!homeViewFlipper.isFlipping()) {
					flipForward();
				}
				break;
			default:
				break;
			}
		}

	};

	private Integer[] galleryImgId = { R.drawable.linkinpark_000,
			R.drawable.guitar_fender, R.drawable.dj_splash,
			R.drawable.xpress_splash };

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

		searchButton = (ImageView) findViewById(R.id.search);
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onSearchRequested();
				//				startActivity(new Intent(search_activity));
			}
		});
		audioDataList = new ArrayList<MediaEntity>();
		videoDataList = new ArrayList<MediaEntity>();

		//checking and adding user channels

		//audio
		File findAudioChannelFile = new File(getFilesDir(),USERCHANNELSAUDIO);	
		if(findAudioChannelFile.exists())
		{
			try {
				InputStream tmpfis = new FileInputStream(findAudioChannelFile);
				int tmpch;
				String tmpdata = new String();
				StringBuffer tmpfileContent = new StringBuffer("");
				while ((tmpch = tmpfis.read()) != -1)
					tmpfileContent.append((char) tmpch);
				tmpdata = new String(tmpfileContent);
				JSONArray fileJSONArray = new JSONArray(tmpdata);				
				for (int i = 0; i < fileJSONArray.length(); i++)
				{
					MediaEntity entity = new MediaEntity();
					entity.setMedia_title(fileJSONArray.getJSONObject(i)
							.getString(TITLE));
					entity.setMedia_thumbnailUri(fileJSONArray
							.getJSONObject(i).getString(THUMBNAIL));
					entity.setMedia_desc(fileJSONArray.getJSONObject(i).getString(
							DESCRIPTION));
					entity.setMedia_mediaUri(fileJSONArray
							.getJSONObject(i).getString(MEDIA_STREAM_URL));
					entity.setMedia_artist(fileJSONArray.getJSONObject(i)
							.getString(ARTIST));
					entity.setMedia_channel(fileJSONArray.getJSONObject(i)
							.getString(CHANNEL));
					entity.setMedia_type(fileJSONArray.getJSONObject(i)
							.getString(TYPE));

					audioDataList.add(entity);
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

		//video
		File findVideoChannelFile = new File(getFilesDir(),USERCHANNELSVIDEO);	
		if(findVideoChannelFile.exists())
		{
			try {
				InputStream tmpfis = new FileInputStream(findVideoChannelFile);
				int tmpch;
				String tmpdata = new String();
				StringBuffer tmpfileContent = new StringBuffer("");
				while ((tmpch = tmpfis.read()) != -1)
					tmpfileContent.append((char) tmpch);
				tmpdata = new String(tmpfileContent);
				JSONArray fileJSONArray = new JSONArray(tmpdata);	
				int t = 0;
				for (int i = 0; i < fileJSONArray.length(); i++)
				{
					MediaEntity entity = new MediaEntity();
					entity.setMedia_title(fileJSONArray.getJSONObject(i)
							.getString(TITLE));
					entity.setMedia_thumbnailUri(fileJSONArray
							.getJSONObject(i).getString(THUMBNAIL));
					entity.setMedia_desc(fileJSONArray.getJSONObject(i).getString(
							DESCRIPTION));
					entity.setMedia_mediaUri(fileJSONArray
							.getJSONObject(i).getString(MEDIA_STREAM_URL));
					entity.setMedia_artist(fileJSONArray.getJSONObject(i)
							.getString(ARTIST));
					entity.setMedia_channel(fileJSONArray.getJSONObject(i)
							.getString(CHANNEL));
					entity.setMedia_type(fileJSONArray.getJSONObject(i)
							.getString(TYPE));

					entity.setTempThumbnailImage(galleryImgId[t]);
					t++;
					if (t > 3)
						t = 0;
					
					videoDataList.add(entity);
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

		int ch;
		String data = new String();
		StringBuffer fileContent = new StringBuffer("");
		try {
			InputStream fis = this.getResources().getAssets().open(
			"json_object/json");
			while ((ch = fis.read()) != -1)
				fileContent.append((char) ch);
			data = new String(fileContent);
		} catch (FileNotFoundException e) {
			Log.d("file", "file is not found");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			jsonObject = new JSONObject(data);
			homeJSONArray = jsonObject.getJSONArray(HOME);
			homeFlipperItemArrayList = new ArrayList<Map<String, Object>>();
			int k = 0;
			for (int i = 0; i < homeJSONArray.length(); i++) {
				if (homeJSONArray.getJSONObject(i).getString(TYPE)
						.equalsIgnoreCase(AUDIO)) {
					MediaEntity entity = new MediaEntity();
					entity.setMedia_title(homeJSONArray.getJSONObject(i)
							.getString(TITLE));
					entity.setMedia_thumbnailUri(homeJSONArray
							.getJSONObject(i).getString(THUMBNAIL));
					entity.setMedia_desc(homeJSONArray.getJSONObject(i)
							.getString(DESCRIPTION));
					entity.setMedia_mediaUri(homeJSONArray
							.getJSONObject(i).getString(MEDIA_STREAM_URL));
					entity.setMedia_artist(homeJSONArray.getJSONObject(i)
							.getString(ARTIST));
					entity.setMedia_channel(homeJSONArray.getJSONObject(i)
							.getString(CHANNEL));
					entity.setMedia_type(homeJSONArray.getJSONObject(i)
							.getString(TYPE));

					entity.setTempThumbnailImage(galleryImgId[k]);
					k++;
					if (k > 3)
						k = 0;

					Map<String, Object> item = new HashMap<String, Object>();
					item.put(AUDIO, entity);
					homeFlipperItemArrayList.add(item);
				} else if (homeJSONArray.getJSONObject(i).getString(TYPE)
						.equalsIgnoreCase(VIDEO)) {
					MediaEntity entity = new MediaEntity();
					entity.setMedia_title(homeJSONArray.getJSONObject(i)
							.getString(TITLE));
					entity.setMedia_thumbnailUri(homeJSONArray
							.getJSONObject(i).getString(THUMBNAIL));
					entity.setMedia_desc(homeJSONArray.getJSONObject(i)
							.getString(DESCRIPTION));
					entity.setMedia_mediaUri(homeJSONArray.getJSONObject(i)
							.getString(MEDIA_STREAM_URL));
					entity.setMedia_artist(homeJSONArray.getJSONObject(i)
							.getString(ARTIST));
					entity.setMedia_channel(homeJSONArray.getJSONObject(i)
							.getString(CHANNEL));					
					entity.setMedia_type(homeJSONArray.getJSONObject(i)
							.getString(TYPE));

					entity.setTempThumbnailImage(galleryImgId[k]);
					k++;
					if (k > 3)
						k = 0;

					Map<String, Object> item = new HashMap<String, Object>();
					item.put(VIDEO, entity);
					homeFlipperItemArrayList.add(item);
				}

			}

			audioJSONArray = jsonObject.getJSONArray(AUDIO);
			for (int i = 0; i < audioJSONArray.length(); i++) {
				MediaEntity entity = new MediaEntity();
				entity.setMedia_title(audioJSONArray.getJSONObject(i)
						.getString(TITLE));
				entity.setMedia_thumbnailUri(audioJSONArray
						.getJSONObject(i).getString(THUMBNAIL));
				entity.setMedia_desc(audioJSONArray.getJSONObject(i).getString(
						DESCRIPTION));
				entity.setMedia_mediaUri(audioJSONArray
						.getJSONObject(i).getString(MEDIA_STREAM_URL));
				entity.setMedia_artist(audioJSONArray.getJSONObject(i)
						.getString(ARTIST));
				entity.setMedia_channel(audioJSONArray.getJSONObject(i)
						.getString(CHANNEL));
				entity.setMedia_type(audioJSONArray.getJSONObject(i)
						.getString(TYPE));

				audioDataList.add(entity);
			}
			audioChannelMap = assortJSonData(audioDataList);
			// Log.d("HOME", "audio map -> "+audioChannelMap);
			videoJSONArray = jsonObject.getJSONArray(VIDEO);
			int c = 0;
			for (int i = 0; i < videoJSONArray.length(); i++) {
				MediaEntity entity = new MediaEntity();
				entity.setMedia_title(videoJSONArray.getJSONObject(i)
						.getString(TITLE));
				entity.setMedia_thumbnailUri(videoJSONArray
						.getJSONObject(i).getString(THUMBNAIL));
				entity.setMedia_desc(videoJSONArray.getJSONObject(i).getString(
						DESCRIPTION));
				entity.setMedia_mediaUri(videoJSONArray.getJSONObject(i)
						.getString(MEDIA_STREAM_URL));
				entity.setMedia_artist(videoJSONArray.getJSONObject(i)
						.getString(ARTIST));
				entity.setMedia_channel(videoJSONArray.getJSONObject(i)
						.getString(CHANNEL));
				entity.setMedia_type(videoJSONArray.getJSONObject(i)
						.getString(TYPE));

				entity.setTempThumbnailImage(galleryImgId[c]);
				c++;
				if (c > 3)
					c = 0;

				videoDataList.add(entity);
			}
			videoChannelMap = assortJSonData(videoDataList);
			// Log.d("HOME", "video map -> "+videoChannelMap);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mTask = new TimerTask() {
			@Override
			public void run() {
				/* Give the callback to caller */
				mHandler.sendEmptyMessage(1);
			}
		};

		// setting the contents for auto-panning home image flipper
		homeViewFlipper = (ViewFlipper) findViewById(R.id.HomeViewFlipper);
		settingAnimation(true);

		/*
		 * int[] Images = { R.drawable.dj_splash, R.drawable.guitar_fender,
		 * R.drawable.hp2_splash, R.drawable.hp_splash,
		 * R.drawable.linkinpark_000};
		 */

		int img1 = 0;
		if (homeFlipperItemArrayList.get(0).keySet().toString()
				.replace("[", "").replace("]", "").equalsIgnoreCase(AUDIO)) {
			MediaEntity entity = (MediaEntity) homeFlipperItemArrayList.get(0)
			.get(AUDIO);
			img1 = entity.getTempThumbnailImage();
		} else if (homeFlipperItemArrayList.get(0).keySet().toString().replace(
				"[", "").replace("]", "").equalsIgnoreCase(VIDEO)) {
			MediaEntity entity = (MediaEntity) homeFlipperItemArrayList.get(0)
			.get(VIDEO);
			img1 = entity.getTempThumbnailImage();
		}
		View v1 = findViewById(R.id.ListItem1);
		ImageView iv1 = (ImageView) v1
		.findViewById(R.id.HomeViewFlipperItemImageView);
		iv1.setImageResource(img1);
		ImageButton add1 = (ImageButton)v1.findViewById(R.id.HomeViewFlipperAddChannelsImageButton);
		add1.setVisibility(8);
		ImageButton ib1 = (ImageButton) v1
		.findViewById(R.id.HomeViewFlipperItemImageButton);
		ib1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				homeViewImgBtnClicked(0);
			}
		});

		int img2 = 0;
		if (homeFlipperItemArrayList.get(1).keySet().toString()
				.replace("[", "").replace("]", "").equalsIgnoreCase(AUDIO)) {
			MediaEntity entity = (MediaEntity) homeFlipperItemArrayList.get(1)
			.get(AUDIO);
			img2 = entity.getTempThumbnailImage();
		} else if (homeFlipperItemArrayList.get(1).keySet().toString().replace(
				"[", "").replace("]", "").equalsIgnoreCase(VIDEO)) {
			MediaEntity entity = (MediaEntity) homeFlipperItemArrayList.get(1)
			.get(VIDEO);
			img2 = entity.getTempThumbnailImage();
		}
		View v2 = findViewById(R.id.ListItem2);
		ImageView iv2 = (ImageView) v2
		.findViewById(R.id.HomeViewFlipperItemImageView);
		ImageButton add2 = (ImageButton)v2.findViewById(R.id.HomeViewFlipperAddChannelsImageButton);
		add2.setVisibility(8);
		iv2.setImageResource(img2);
		ImageButton ib2 = (ImageButton) v2
		.findViewById(R.id.HomeViewFlipperItemImageButton);
		ib2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				homeViewImgBtnClicked(1);
			}
		});

		int img3 = 0;
		if (homeFlipperItemArrayList.get(2).keySet().toString()
				.replace("[", "").replace("]", "").equalsIgnoreCase(AUDIO)) {
			MediaEntity entity = (MediaEntity) homeFlipperItemArrayList.get(2)
			.get(AUDIO);
			img3 = entity.getTempThumbnailImage();
		} else if (homeFlipperItemArrayList.get(2).keySet().toString().replace(
				"[", "").replace("]", "").equalsIgnoreCase(VIDEO)) {
			MediaEntity entity = (MediaEntity) homeFlipperItemArrayList.get(2)
			.get(VIDEO);
			img3 = entity.getTempThumbnailImage();
		}
		View v3 = findViewById(R.id.ListItem3);
		ImageView iv3 = (ImageView) v3
		.findViewById(R.id.HomeViewFlipperItemImageView);
		ImageButton add3 = (ImageButton)v3.findViewById(R.id.HomeViewFlipperAddChannelsImageButton);
		add3.setVisibility(8);
		iv3.setImageResource(img3);
		ImageButton ib3 = (ImageButton) v3
		.findViewById(R.id.HomeViewFlipperItemImageButton);
		ib3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				homeViewImgBtnClicked(2);
			}
		});

		int img4 = 0;
		if (homeFlipperItemArrayList.get(3).keySet().toString()
				.replace("[", "").replace("]", "").equalsIgnoreCase(AUDIO)) {
			MediaEntity entity = (MediaEntity) homeFlipperItemArrayList.get(3)
			.get(AUDIO);
			img4 = entity.getTempThumbnailImage();
		} else if (homeFlipperItemArrayList.get(3).keySet().toString().replace(
				"[", "").replace("]", "").equalsIgnoreCase(VIDEO)) {
			MediaEntity entity = (MediaEntity) homeFlipperItemArrayList.get(3)
			.get(VIDEO);
			img4 = entity.getTempThumbnailImage();
		}
		View v4 = findViewById(R.id.ListItem4);
		ImageView iv4 = (ImageView) v4
		.findViewById(R.id.HomeViewFlipperItemImageView);
		ImageButton add4 = (ImageButton)v4.findViewById(R.id.HomeViewFlipperAddChannelsImageButton);
		add4.setVisibility(8);
		iv4.setImageResource(img4);
		ImageButton ib4 = (ImageButton) v4
		.findViewById(R.id.HomeViewFlipperItemImageButton);
		ib4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				homeViewImgBtnClicked(3);
			}
		});

		int img5 = 0;
		if (homeFlipperItemArrayList.get(4).keySet().toString()
				.replace("[", "").replace("]", "").equalsIgnoreCase(AUDIO)) {
			MediaEntity entity = (MediaEntity) homeFlipperItemArrayList.get(4)
			.get(AUDIO);
			img5 = entity.getTempThumbnailImage();
		} else if (homeFlipperItemArrayList.get(4).keySet().toString().replace(
				"[", "").replace("]", "").equalsIgnoreCase(VIDEO)) {
			MediaEntity entity = (MediaEntity) homeFlipperItemArrayList.get(4)
			.get(VIDEO);
			img5 = entity.getTempThumbnailImage();
		}
		View v5 = findViewById(R.id.ListItem5);
		ImageView iv5 = (ImageView) v5
		.findViewById(R.id.HomeViewFlipperItemImageView);
		ImageButton add5 = (ImageButton)v5.findViewById(R.id.HomeViewFlipperAddChannelsImageButton);
		add5.setVisibility(8);
		iv5.setImageResource(img5);
		ImageButton ib5 = (ImageButton) v5
		.findViewById(R.id.HomeViewFlipperItemImageButton);
		ib5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				homeViewImgBtnClicked(4);
			}
		});

		// setting attributes to start auto pann
		homeViewFlipper.setAutoStart(true);
		homeViewFlipper.setFlipInterval(5000);

		homeViewFlipper.setOnTouchListener(new OnTouchListener() {
			float downXValue;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// Get the action that was done on this touch event

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					// store the X value when the user's finger was pressed down
					flippingStopped = true;
					if (homeViewFlipper.isFlipping()) {
						homeViewFlipper.stopFlipping();
					}
					downXValue = event.getX();
					break;
				}

				case MotionEvent.ACTION_UP: {
					// Get the X value when the user released his/her finger
					float currentX = event.getX();
					flippingStopped = false;
					// going backwards: pushing stuff to the right
					if (downXValue < currentX) {
						settingAnimation(false);
						homeViewFlipper.showPrevious();
						homeViewFlipper.startFlipping();
					}

					// going forwards: pushing stuff to the left
					if (downXValue > currentX) {
						flipForward();
					}
					break;
				}
				}

				return true;
			}
		});

		// getting instance for audio and video buttons
		audioButton = (Button) findViewById(R.id.AudioStreamButton);
		videoButton = (Button) findViewById(R.id.VideoStreamButton);

		// handling click on audio and video buttons i.e. launching of
		// respective pages
		audioButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent audioPage = new Intent(audio_page_activity);
				// audioPage.putExtra(LAUNCHEDFROMHOME, false);
				startActivity(audioPage);
			}
		});

		videoButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(video_page_activity));
			}
		});
	}

	private void homeViewImgBtnClicked(int id) {
		if (homeFlipperItemArrayList.get(id).keySet().toString().replace("[",
		"").replace("]", "").equalsIgnoreCase(AUDIO)) {

			MediaEntity entity = (MediaEntity) homeFlipperItemArrayList.get(id)
			.get(AUDIO);

			AudioListCreator.getInstance().setChannelName(entity.getMedia_channel());
			AudioListCreator.getInstance().setHomeLaunchEntity(entity);
			AudioListCreator.getInstance().setAudioListData();

			Intent audioPage = new Intent(audio_page_activity);
			audioPage.putExtra(LAUNCHEDFROMHOME, true);

			startActivity(audioPage);
			Log.d("home", "audio entity clicked !!");
		} else if (homeFlipperItemArrayList.get(id).keySet().toString()
				.replace("[", "").replace("]", "").equalsIgnoreCase(VIDEO)) {
			MediaEntity entity = (MediaEntity) homeFlipperItemArrayList.get(id)
			.get(VIDEO);
			VideoListCreator.getInstance().setHomeLaunchEntity(entity);
			VideoListCreator.getInstance().setChannelName(entity.getMedia_channel());
			VideoListCreator.getInstance().setVideoListData();

			startActivity(new Intent(video_player_activity));

			Log.d("home", "video entity clicked !!");
		}
	}

	private void settingAnimation(boolean left) {
		if (left) {
			homeViewFlipper.setInAnimation(this, R.anim.slide_animation);
			homeViewFlipper.setOutAnimation(this, R.anim.slide_out);
		} else {
			homeViewFlipper.setInAnimation(this, R.anim.slide_in_rev);
			homeViewFlipper.setOutAnimation(this, R.anim.slide_out_rev);
		}
	}

	private void flipForward() {
		settingAnimation(true);
		homeViewFlipper.showNext();
		homeViewFlipper.startFlipping();
	}

	@SuppressWarnings("unchecked")
	public  static HashMap<String, ArrayList<Map<String, ArrayList<MediaEntity>>>> assortJSonData(ArrayList<MediaEntity> mediaDataList) {
		HashMap<String, ArrayList<Map<String, ArrayList<MediaEntity>>>> mapData = new HashMap<String, ArrayList<Map<String,ArrayList<MediaEntity>>>>();

		for (MediaEntity entity : mediaDataList) {
			Boolean setChannel = true;
			for (String ch : mapData.keySet()) {
				if (entity.getMedia_channel().equalsIgnoreCase(ch))
					setChannel = false;
			}
			if (setChannel) {
				mapData.put(entity.getMedia_channel(),
						new ArrayList<Map<String, ArrayList<MediaEntity>>>());
			}
		}
		// adding empty artist map lists.
		for (MediaEntity entity : mediaDataList) {
			Boolean setArtist = true;
			for (Map.Entry<String, ArrayList<Map<String, ArrayList<MediaEntity>>>> entry : mapData
					.entrySet()) {
				String currentChannel = entry.getKey();
				ArrayList<Map<String, ArrayList<MediaEntity>>> artistList = entry
				.getValue();

				for (Iterator iterator = artistList.iterator(); iterator
				.hasNext();) {
					Map<String, ArrayList<MediaEntity>> map = (Map<String, ArrayList<MediaEntity>>) iterator
					.next();
					for (String ar : map.keySet()) {
						if (ar.equalsIgnoreCase(entity.getMedia_artist())
								&& currentChannel.equalsIgnoreCase(entity
										.getMedia_channel())) {
							setArtist = false;
						}
					}

				}
			}
			if (setArtist) {
				Map<String, ArrayList<MediaEntity>> artistEntry = new HashMap<String, ArrayList<MediaEntity>>();
				artistEntry.put(entity.getMedia_artist(),
						new ArrayList<MediaEntity>());
				for (String ch : mapData.keySet()) {
					if (ch.equalsIgnoreCase(entity.getMedia_channel())) {
						mapData.get(ch).add(artistEntry);
					}
				}
			}
		}

		// adding songs list
		for (MediaEntity entity : mediaDataList) {

			for (Map.Entry<String, ArrayList<Map<String, ArrayList<MediaEntity>>>> entry : mapData
					.entrySet()) {
				String currentChannel = entry.getKey();
				ArrayList<Map<String, ArrayList<MediaEntity>>> artistList = entry
				.getValue();

				for (Iterator iterator = artistList.iterator(); iterator
				.hasNext();) {
					Map<String, ArrayList<MediaEntity>> map = (Map<String, ArrayList<MediaEntity>>) iterator
					.next();
					for (String ar : map.keySet()) {
						if (ar.equalsIgnoreCase(entity.getMedia_artist())
								&& currentChannel.equalsIgnoreCase(entity
										.getMedia_channel())) {
							map.get(ar).add(entity);
						}
					}

				}
			}
		}

		return mapData;
	}
}

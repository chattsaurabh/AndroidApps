package com.fanclub.utils;

import com.fanclub.R;

public interface FanClubConstants {
	
//	activities
	public static final String home_activity = "android.intent.action.HOME";
	public static final String video_activity = "android.intent.action.VIDEO_ACTIVITY";
	public static final String photos_activity = "android.intent.action.PHOTOS_ACTIVITY";
	public static final String photos_pager_activity = "android.intent.action.PHOTOS_PAGER_ACTIVITY";
	public static final String news_activity = "android.intent.action.NEWS_ACTIVITY";
	public static final String custom_web_view_activity = "android.intent.action.CUSTOM_WEB_VIEW_ACTIVITY";
	public static final String fan_wall_activity = "android.intent.action.FANWALL_ACTIVITY";
	
	
//	integer constants
	public static final int CONNECTION_TIMEOUT = 1000;
	public static final int TWEET_AUTH_SUCCESS = 0;
	public static final int TWEET_AUTH_FAIL = 1;
	
	public static enum HttpEnums
	{
		TYPE_BITMAP,
		TYPE_BYTE,
		TYPE_FILE;
	}
	
//	url constants

	public static final String baseUrl =  "http://192.168.0.103/Fanclub/";
	public static final String metaDataUrl = baseUrl + "MyTestApp/AppMetaData/themeMetaData.php";
	public static final String videoDataUrl = baseUrl + "MyTestApp/AppMetaData/themeMetaData.php";
	public static final String photosDataUrl = baseUrl + "MyTestApp/AppMetaData/themeMetaData.php";
	public static final String newsDataUrl = baseUrl + "MyTestApp/AppMetaData/themeMetaData.php";
	public static final String photosElementBaseUrl = baseUrl + "MyTestApp/AppMetaData/themeMetaData.php";
	
	
//	metadata filenames
	
	public static final String videoDataFileName = "video_file_name";
	public static final String photosDataFileName = "photos_file_name";
	public static final String newsDataFileName = "news_file_name";
	public static final String photosElementBaseFileName = "photos_element_";
	
	
//	Intent data constants
	public static final String NEWS_INTENT_TYPE = "news_intent_type";
	
	public static enum NEWS_DATA_ENUM
	{
		TYPE_NEWS,
		TYPE_GOSSIP;
	}
	
	public static final String PHOTOS_INTENT_TYPE = "photos_intent_type";
	public static final String PHOTOS_INTENT_ELEMENT_FILE_NAME = "photos_intent_element_file_name";
	public static final String PHOTOS_INTENT_ELEMENT_PAGER_URL = "photos_intent_element_pager_url";
	public static final String PHOTOS_INTENT_ELEMENT_PAGER_IDX = "photos_intent_element_pager_idx";
	public static final int PHOTOS_ELEMENT_PAGE_RANGE = 12;
	public static final int PHOTOS_PAGER_PAGE_RANGE = 1;
	public static enum PHOTOS_DATA_ENUM
	{
		TYPE_ALBUM,
		TYPE_ELEMENT;
	}
	
	public static final String WEB_VIEW_URL = "web_view_url";
	public static final String WEB_VIEW_TITLE = "web_view_title";
	
	//	JSON File names
	public static final String BASE_JSON_FILE = "json_object/";
	public static final String VIDEO_JSON_FILE = BASE_JSON_FILE + "videos.json";
	public static final String PHOTOS_ALBUM_JSON_FILE = BASE_JSON_FILE + "albums.json";
	public static final String NEWS_JSON_FILE = BASE_JSON_FILE + "news.json";
	public static final String GOSSIP_JSON_FILE = BASE_JSON_FILE + "news.json";

	
//	JSON tags
	
	public static final String BGCOLOR = "BGColor";
	
	// Home elements
	public static final String HOME = "Home";
	public static final String COMPONENT_ID = "Component_id";
	public static final String TITLE = "Title";
	
//	Video elements
	public static final String VIDEO_ID = "ID";
	public static final String VIDEO_TITLE = "Title";
	public static final String VIDEO_DESC = "Description";
	public static final String VIDEO_VIEW_COUNT = "ViewCount";
	public static final String VIDEO_DURATION = "Duration";
	public static final String VIDEO_HQ_THUMB = "HQThumb";
	public static final String VIDEO_MQ_THUMB = "MQThumb";
	public static final String VIDEO_LQ_THUMB = "LQThumb";
	public static final String VIDEO_MEDIA_URL_1 = "RTSP1";
	public static final String VIDEO_MEDIA_URL_2 = "RTSP6";
	public static final String VIDEO_EMBEDDED = "Embed";
	
	
//	News elements
	public static final String NEWS_TITLE = "Title";
	public static final String NEWS_URL = "Url";
	public static final String NEWS_SOURCE = "Source";
	public static final String NEWS_DESC = "Description";
	public static final String NEWS_DATE = "Date";
	public static final String NEWS_THUMB = "Thumb";
	
//	Photos elements
	public static final String PHOTOS_THUMB = "Thumb";
	public static final String PHOTOS_COUNT = "Count";
	public static final String PHOTOS_TITLE = "Title";
	public static final String PHOTOS_FILE = "File";
	public static final String PHOTOS_ELMENT_URL = "Url";
	
//	Image entity defaults
	public static final int VIEW_IMAGE = 1;
	public static final int VIEW_PROGRESS = 0;
	public static final int DEFAULT_IMAGE = R.drawable.photos;
}

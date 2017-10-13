package com.dev.constants;

public interface LiveWallManagerConstants {

//	logs
	public static final String TAG = "LiveWallManager";

//	activities
	public static final String home_page_activity = "android.intent.action.HOME_PAGE";
	public static final String static_wall_activity = "android.intent.action.STATIC_WALLPAPER_PAGE";
	public static final String live_wall_launcher = "android.service.wallpaper.LIVE_WALLPAPER_CHOOSER";
	public static final String crop_image_activity = "android.intent.action.CROP_IMAGE_PAGE";
	
//	utility constants
	public static final String CROP_IMAGE_URI = "crop_image_uri";
	
	public static final String CROP_IMAGE_FROM_GALLERY = "Crop image from gallery";
	public static final String COMING_SOON = "Coming soon...";
	public static final String CROP_IMAGE = "Crop image";
	
//	enums
	public static final int IMAGE_CLEAR = 0;
	public static final int IMAGE_FILL = 1;

}

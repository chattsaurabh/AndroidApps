package com.interviewer.se.util;

public interface Interviewer_SE_Basics_Constants {

//	explicit intent definitions
	public static final String TEST_LAUNCHER_ACTIVITY = "android.intent.action.TEST_LAUNCHER";
	public static final String TEST_PAGE_ACTIVITY = "android.intent.action.TEST_PAGE";
	public static final String RESULT_ACTIVITY = "android.intent.action.RESULT";
	public static final String REVIEW_ACTIVITY = "android.intent.action.REVIEW";
	
//	intent data markers
	public static final String DATA_FILE_LIST_MARKER = "data_file_list_marker";
	public static final String IMAGE_ASSETS_BASE_DIR = "file:///android_asset/img_assets/";
	
//	json file names
	public static final String BASE_JSON_FILE = "json_object/";
	public static final String HOME_JSON_FILE = BASE_JSON_FILE + "home_json";
	public static final String TEST_LAUNCHER_JSON_FILE = BASE_JSON_FILE + "test_launcher_topics";
	
	
//	json tags
	
//  home tags
	
	public static final String HOME_HEADING = "heading";
	public static final String HOME_DESC = "desc";
	public static final String HOME_DESC_IMAGE = "image";
	
//	test launcher tags
	public static final String TEST_LAUNCHER_TOPIC = "topic";
	public static final String TEST_LAUNCHER_DATA_FILE_NAME = "data_file";
	
//	test page tags
	public static final String TEST_PAGE_TITLE = "title";
	public static final String TEST_PAGE_QUESTION = "question";
	public static final String TEST_PAGE_ALL_ANSWERS = "answers";
	public static final String TEST_PAGE_ANSWER = "answer";
	public static final String TEST_PAGE_CORRECT = "correct";
	
	
//	shared preference
	
	public static final String SHARED_PREFERENCE_TAG = "Interviewer_se_basics_shared_pref_tag";
	public static final String SHARED_PREFERENCE_FIRST_RUN = "Interviewer_se_basics_shared_pref_first_run";
	public static final String SHARED_PREFERENCE_RATE_DNT_SHOW_AGAIN = "Interviewer_se_basics_shared_pref_dont_show_again";
	public static final String SHARED_PREFERENCE_RATE_LAUNCH_COUNT = "Interviewer_se_basics_shared_pref_launch_count";
	
}

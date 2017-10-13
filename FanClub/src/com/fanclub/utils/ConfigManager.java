package com.fanclub.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.fanclub.R;
import com.fanclub.backend.HttpClient;
import com.fanclub.data.HttpLauncherVO;
import com.fanclub.data.MetaDataVO;
import com.fanclub.model.HomeModel;
import com.fanclub.model.NewsModel;
import com.fanclub.model.PhotosElementModel;
import com.fanclub.model.PhotosModel;
import com.fanclub.model.PhotosPagerModel;
import com.fanclub.model.VideoModel;
import com.fanclub.observers.OnDownloadComplete;
import com.fanclub.observers.TweetAuthObserver;
import com.fanclub.twitter.TwitterApp;
import com.fanclub.twitter.TwitterApp.TwDialogListener;

public class ConfigManager implements FanClubConstants, OnDownloadComplete{
	
	private static ConfigManager m_instance = null;	
	
	public static Context m_context = null;
	private HttpClient m_httpClient = null;
	private MetaDataVO m_metaDataVO = null;
	
//	HTTP Authentication instances
	private LauchHttpReq m_launchHttpReqTask = null;
	private TwitterApp m_twitter = null;
	private TweetAuthObserver m_tweetAuthListener = null;
	
//	model instances
	private HomeModel m_homeModel = null;
	private VideoModel m_videoDataModel = null;
	private PhotosModel m_photosModel = null;
	private PhotosElementModel m_photosElementModel = null;
	private PhotosPagerModel m_photosPagerModel = null;
	private NewsModel m_newsModel = null;
	
	private ConfigManager() {		
		instanciateEntities();
	}
	
	public static ConfigManager getInstance() {
		if(m_instance == null)
		{
			m_instance = new ConfigManager();
		}
		
		return m_instance;
	}
	
	private void instanciateEntities() {
		m_httpClient = new HttpClient(m_context);
	}
	
//	getters for global members

	public HttpClient getHttpClient() {
		return m_httpClient;
	}
	
	public MetaDataVO getMetaDataVO() {
		return m_metaDataVO;
	}
	
	public HomeModel getHomeModel() {
		return m_homeModel;
	}
	
	public void getVideoMetadataFile(OnDownloadComplete a_listener) 
	{
		if(null != m_launchHttpReqTask)
		{
			m_launchHttpReqTask.cancel(true);
			m_launchHttpReqTask = null;
		}
		m_launchHttpReqTask = new LauchHttpReq();
		m_launchHttpReqTask.execute(new HttpLauncherVO(videoDataUrl,
													   HttpEnums.TYPE_FILE,
													   a_listener,
													   m_context.getString(R.string.app_name)+"_"+videoDataFileName));
	}
	
	public VideoModel getVideoModel() {
		if(m_videoDataModel == null)
		{
			m_videoDataModel = new VideoModel(m_context);
		}
		return m_videoDataModel;
	}
	
	public void clearVideoData() {		
		m_videoDataModel = null;
	}
	
	
	public void getPhotosMetadataFile(OnDownloadComplete a_listener) 
	{
		if(null != m_launchHttpReqTask)
		{
			m_launchHttpReqTask.cancel(true);
			m_launchHttpReqTask = null;
		}
		m_launchHttpReqTask = new LauchHttpReq();
		m_launchHttpReqTask.execute(new HttpLauncherVO(photosDataUrl,
													   HttpEnums.TYPE_FILE,
													   a_listener,
													   m_context.getString(R.string.app_name)+"_"+photosDataFileName));
	}
	
	public PhotosModel getPhotosModel() {
		if(m_photosModel == null)
		{
			m_photosModel = new PhotosModel(m_context);
		}
		return m_photosModel;
	}
	
	public void clearPhotosData() {		
		m_photosModel = null;
	}
	
	public void getPhotosElementMetadataFile(OnDownloadComplete a_listener, String a_fileName) 
	{
		if(null != m_launchHttpReqTask)
		{
			m_launchHttpReqTask.cancel(true);
			m_launchHttpReqTask = null;
		}
		m_launchHttpReqTask = new LauchHttpReq();
		m_launchHttpReqTask.execute(new HttpLauncherVO(photosElementBaseUrl+a_fileName,
													   HttpEnums.TYPE_FILE,
													   a_listener,
													   m_context.getString(R.string.app_name)+"_"+photosElementBaseFileName+a_fileName));
	}
	
	public PhotosElementModel getPhotosElementModel() {
		if(m_photosElementModel == null)
		{
			m_photosElementModel = new PhotosElementModel(m_context, PHOTOS_ELEMENT_PAGE_RANGE);
		}
		return m_photosElementModel;
	}
	
	public void clearPhotosElementsData() {		
		m_photosElementModel = null;
	}
	
	public PhotosPagerModel getPhotosPagerModel() {
		if(m_photosPagerModel == null)
		{
			m_photosPagerModel = new PhotosPagerModel(m_context, PHOTOS_PAGER_PAGE_RANGE);
		}
		return m_photosPagerModel;
	}
	
	public void clearPhotosPagerData() {
		m_photosPagerModel = null;
	}
	
	public void getNewsMetadataFile(OnDownloadComplete a_listener) 
	{
		if(null != m_launchHttpReqTask)
		{
			m_launchHttpReqTask.cancel(true);
			m_launchHttpReqTask = null;
		}
		m_launchHttpReqTask = new LauchHttpReq();
		m_launchHttpReqTask.execute(new HttpLauncherVO(newsDataUrl,
													   HttpEnums.TYPE_FILE,
													   a_listener,
													   m_context.getString(R.string.app_name)+"_"+newsDataFileName));
	}
	
	public NewsModel getNewsModel() {
		if(m_newsModel == null)
		{
			m_newsModel = new NewsModel(m_context);
		}
		return m_newsModel;
	}
	
	public void clearNewsData() {
		m_newsModel = null;
	}
	
//	App configuration methods
	
	public void triggerMetaDataCaching() {
		/*if(null != m_launchHttpReqTask)
		{
			m_launchHttpReqTask.cancel(true);
			m_launchHttpReqTask = null;
		}
		m_launchHttpReqTask = new LauchHttpReq();
		m_launchHttpReqTask.execute(new HttpLauncherVO(metaDataUrl,HttpEnums.TYPE_FILE,this));*/
		
		m_metaDataVO = new MetaDataVO(null,m_context);
		m_homeModel = new HomeModel(null, m_context);
		
	}

	@Override
	public void onDownloadConplete(Object a_result) {
		File l_result = (File) a_result;
		m_metaDataVO = new MetaDataVO(l_result,m_context);		
	}
	
	private class LauchHttpReq extends AsyncTask<HttpLauncherVO, Integer, Object>
	{
		private OnDownloadComplete m_downloadCompleteListerner = null;
		@Override
		protected Object doInBackground(HttpLauncherVO... a_httpLauncherVO) {
			m_downloadCompleteListerner = a_httpLauncherVO[0].getListener();
			Object l_result = null;
			switch (a_httpLauncherVO[0].getType()) {
			case TYPE_BITMAP:				
				break;
			case TYPE_BYTE:
				break;
			case TYPE_FILE:
				l_result = m_httpClient.getFileContents(a_httpLauncherVO[0].getUrl(), a_httpLauncherVO[0].getFileName());
				break;
			default:
				break;
			}			
			return l_result;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			if(result != null)
			{
				m_downloadCompleteListerner.onDownloadConplete(result);
			}
			super.onPostExecute(result);
		}
	}
	
	
//	Twitter Authentication APIs
	
	public void fetchTwitterInstance(TweetAuthObserver a_tweetAuthListener, Activity a_act) 
	{
		m_tweetAuthListener = a_tweetAuthListener;
		m_twitter = null;
		if(m_twitter == null)
		{
			m_twitter = new TwitterApp(a_act);
		}
		initiateTwitterAuth();		
	}
	
	private void initiateTwitterAuth() {
		m_twitter.setListener(mTwLoginDialogListener);
		if (m_twitter.hasAccessToken() == true) 
		{
			m_tweetAuthListener.onAuthenticationComplete(m_twitter);
		} 
		else 
		{
			m_twitter.authorize();
		}
	}
	
	private TwDialogListener mTwLoginDialogListener = new TwDialogListener() {

		public void onError(String value) {			
			Log.e("TWITTER", value);
			m_twitter.resetAccessToken();
			m_tweetAuthListener.onAuthenticationComplete(null);
		}

		public void onComplete(String value) {
			try {
				m_tweetAuthListener.onAuthenticationComplete(m_twitter);
			} catch (Exception e) {
				if (e.getMessage().toString().contains("duplicate")) {
				}
				e.printStackTrace();
				m_tweetAuthListener.onAuthenticationComplete(null);
			}
		}
	};
	
	public void cleanTwitterTokens() {
		if(m_twitter != null)
		{
			m_twitter.resetAccessToken();
		}
	}

	
	public void showToast(String a_msg) 
	{
		Toast.makeText(m_context, a_msg, Toast.LENGTH_SHORT).show();
	}
}

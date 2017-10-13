package com.interviewer.se.util;

import com.interviewer.se.ResultActivity;
import com.interviewer.se.ReviewActivity;
import com.interviewer.se.TestActivity;
import com.interviewer.se.TestLauncherActivity;
import com.interviewer.se.abs.AbsPannerActivity;
import com.interviewer.se.abs.AbsPannerModel;
import com.interviewer.se.model.HomeModel;
import com.interviewer.se.model.TestDataModel;
import com.interviewer.se.model.TestLauncherModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class ConfigurationManager {
	
	private static ConfigurationManager m_instance = null;
	
	private Context m_context = null;
	
	private AbsPannerActivity m_pannerActivity;
	private Boolean m_isCircularView = true;
	
	private HomeModel m_homeModel = null;
	private TestLauncherModel m_testLauncherModel = null;
	private TestDataModel m_testDataModel = null;
	
	private TestActivity m_testActivity = null;
	private ReviewActivity m_reviewActivity = null;
	private ResultActivity m_resultActivity = null;
	
	private ConfigurationManager(){		
	}
	
	public static ConfigurationManager getInstance() {
		if(m_instance == null)
		{
			m_instance = new ConfigurationManager();
		}
		
		return m_instance;
	}
	
	public void init(Context a_cont) {
		m_context = a_cont;
	}
	
	public void setCurrentPannerActivity(AbsPannerActivity a_currentPannerAct) {
		m_pannerActivity = a_currentPannerAct;
	}
	
	public void showNextPage() {
		m_pannerActivity.showNext();
	}
	
	public void showPreviousPage() {
		m_pannerActivity.showPrev();
	}

	
	public HomeModel getHomeModel() {
		if(m_homeModel == null)
		{
			m_homeModel = new HomeModel(m_context);
		}
		
		return m_homeModel;
	}
	
	public void cleanHomeModel() {
		m_homeModel.clear();
		m_homeModel = null;
	}
	
	public TestLauncherModel getTestLauncherModel() {
		if(m_testLauncherModel == null)
		{
			m_testLauncherModel = new TestLauncherModel(m_context);
		}
		return m_testLauncherModel;
	}
	
	public void cleanTestLauncherModel() {
		if(m_testLauncherModel != null)
		{
			m_testLauncherModel.clear();
		}
		m_testLauncherModel = null;
	}
	
	public TestDataModel getTestDataModel() {
		if(m_testDataModel == null)
		{
			m_testDataModel = new TestDataModel(m_context);
		}
		return m_testDataModel;
	}
	
	public void cleanTestDataModel() {
		m_testDataModel.clear();
		m_testDataModel = null;
	}
	
	public void setCircularModel(Boolean a_isCircular) {
		m_isCircularView = a_isCircular;
	}
	
	public Boolean isCircularView() {
		return m_isCircularView;
	}
	
	public void setTestActivity(TestActivity a_testActivity) {
		m_testActivity = a_testActivity;
	}
	
	public void setResultActivity(ResultActivity a_resultActivity) {
		m_resultActivity = a_resultActivity;
	}
	
	public void setReviewActivity(ReviewActivity a_reviewActivity) {
		m_reviewActivity = a_reviewActivity;
	}
	
	public void resetToHome() {		
		m_reviewActivity.finish();
		m_resultActivity.finish();
		m_testActivity.finish();
		
//		m_context.startActivity(new Intent(m_context,TestLauncherActivity.class));
	}
}

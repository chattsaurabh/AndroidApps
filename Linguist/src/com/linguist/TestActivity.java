package com.linguist;

import java.util.ArrayList;

import linguist.student.test.guide.R;
import android.os.Bundle;
import android.view.Menu;

import com.linguist.abs.AbsPannerActivity;
import com.linguist.customviews.CustomViewFlipper;
import com.linguist.model.TestDataModel;
import com.linguist.util.ConfigurationManager;
import com.linguist.views.TestPageView;

public class TestActivity extends AbsPannerActivity {

	private TestDataModel m_testDataModel = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_test);
		flipper = (CustomViewFlipper)findViewById(R.id.testPageViewFlipper);
		flipper.setMaxScrollRange(5);
		
		m_confMan = ConfigurationManager.getInstance();
		
//		m_testDataModel = m_confMan.getTestDataModel();
		m_dataModel = m_confMan.getTestDataModel();
		m_testDataModel = (TestDataModel) m_dataModel;
		
		ArrayList<String> l_dataFileList = getIntent().getStringArrayListExtra(DATA_FILE_LIST_MARKER);
		for (String fileName : l_dataFileList) {
			m_testDataModel.setData(fileName);
		}
		
		super.onCreate(savedInstanceState);
		m_confMan.setCurrentPannerActivity(this);
		m_confMan.setTestActivity(this);
		addPage();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
		return true;
	}

	protected void addPage() {
		super.addPage();
		switch (m_currentPageIndex) {
		case PAGE_ONE:	
			m_currentPage = new TestPageView(this, m_firstPageLinLayout);
			break;
		case PAGE_TWO:
			m_currentPage = new TestPageView(this, m_secondPageLinLayout);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		m_confMan.cleanTestDataModel();
		super.onDestroy();
	}
}
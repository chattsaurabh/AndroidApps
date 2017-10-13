package com.linguist;

import linguist.student.test.guide.R;
import android.os.Bundle;
import android.view.Menu;

import com.linguist.abs.AbsPannerActivity;
import com.linguist.customviews.CustomViewFlipper;
import com.linguist.util.ConfigurationManager;
import com.linguist.views.ReviewPageView;

public class ReviewActivity extends AbsPannerActivity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_review);
		m_confMan = ConfigurationManager.getInstance();
		flipper = (CustomViewFlipper)findViewById(R.id.testPageViewFlipper);
		flipper.setMaxScrollRange(5);
		
		m_dataModel = m_confMan.getTestDataModel();
		
		super.onCreate(savedInstanceState);
		m_confMan.setCurrentPannerActivity(this);
		m_confMan.setReviewActivity(this);
		addPage();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.review, menu);
		return true;
	}
	
	protected void addPage() {
		super.addPage();
		switch (m_currentPageIndex) {
		case PAGE_ONE:	
			m_currentPage = new ReviewPageView(this, m_firstPageLinLayout);
			break;
		case PAGE_TWO:
			m_currentPage = new ReviewPageView(this, m_secondPageLinLayout);
			break;
		default:
			break;
		}
	}

}

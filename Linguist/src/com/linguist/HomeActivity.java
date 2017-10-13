package com.linguist;

import java.util.ArrayList;

import linguist.student.test.guide.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linguist.adapter.HomeExpListAdapter;
import com.linguist.adapter.TestLauncherListAdapter;
import com.linguist.data.HomeVO;
import com.linguist.model.HomeModel;
import com.linguist.util.ConfigurationManager;
import com.linguist.util.LinguistConstants;

public class HomeActivity extends Activity implements LinguistConstants{

	private ExpandableListView m_expandableList = null;
	private ListView m_list = null;
	private HomeExpListAdapter m_adapter = null;
	private HomeModel m_homeModel = null;
	private ConfigurationManager m_confMan = null;
	private ArrayList<String> m_introHeadingList;
	private ArrayList<String> m_introDescList;

	private Button m_testLauncherBtn = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		m_confMan = ConfigurationManager.getInstance();
		m_confMan.init(this);
		m_homeModel = m_confMan.getHomeModel();

		TextView l_homeTitle = (TextView) findViewById(R.id.homeTitleTextView);
		l_homeTitle.setText(getResources().getString(R.string.home_title));

		m_testLauncherBtn = (Button) findViewById(R.id.startTestBtn);
		m_testLauncherBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//				startActivity(new Intent(TEST_LAUNCHER_ACTIVITY));				
				startActivity(new Intent(getApplicationContext(),TestLauncherActivity.class));
			}
		});

		//		m_expandableList = (ExpandableListView) findViewById(R.id.homeExpandableListView);
		m_list = (ListView) findViewById(R.id.homeListView);
		m_introHeadingList = new ArrayList<String>();
		m_introDescList = new ArrayList<String>();
		for (HomeVO l_homeEntityVO : m_homeModel.getHomeDataList()) {
			m_introHeadingList.add(l_homeEntityVO.getHeading());
			m_introDescList.add(l_homeEntityVO.getDesc());
		}
		TestLauncherListAdapter l_homeListAdapter = new TestLauncherListAdapter(this, m_introHeadingList);

		m_list.setAdapter(l_homeListAdapter);

		m_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {


				HomeVO l_homeVO = m_homeModel.getHomeDataList().get(position);
				// custom dialog
				final Dialog dialog = new Dialog(HomeActivity.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.home_desc_dialog);				

				TextView l_title = (TextView) dialog.findViewById(R.id.homeDescTitleTextView);
				l_title.setText(l_homeVO.getHeading());

				// set the custom dialog components - text, image and button
				TextView text = (TextView) dialog.findViewById(R.id.homeDescTV);
				text.setText(l_homeVO.getDesc());

				WebView wv = (WebView) dialog.findViewById(R.id.homeDescImageWebView);
				if(!l_homeVO.getImgPath().trim().equalsIgnoreCase(""))
				{					
					wv.getSettings().setBuiltInZoomControls(true);
					wv.setInitialScale(100);
					wv.loadUrl(IMAGE_ASSETS_BASE_DIR+l_homeVO.getImgPath());
				}
				else
				{
					RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(0, 0);
					wv.setLayoutParams(lp);
				}

				Button dialogButton = (Button) dialog.findViewById(R.id.homeDescOKBtn);
				// if button is clicked, close the custom dialog
				dialogButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});

				dialog.show();

			}
		});

		//		m_adapter = new HomeExpListAdapter(m_homeModel, this);
		//		m_expandableList.setAdapter(m_adapter);

		/*ViewTreeObserver vto = m_expandableList.getViewTreeObserver();

	    vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	        @Override
	        public void onGlobalLayout() {
	        	m_expandableList.setIndicatorBounds(m_expandableList.getRight()- 100, m_expandableList.getWidth());	        	
	        }
	    });*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		m_confMan.cleanHomeModel();
		m_confMan.cleanTestLauncherModel();
		m_confMan = null;
		super.onDestroy();
	}

}

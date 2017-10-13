package com.interviewer.se;

import interviewer.se.basics.R;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.interviewer.se.adapter.TestLauncherListAdapter;
import com.interviewer.se.data.TestlauncherVO;
import com.interviewer.se.model.TestLauncherModel;
import com.interviewer.se.util.ConfigurationManager;
import com.interviewer.se.util.Interviewer_SE_Basics_Constants;

public class TestLauncherActivity extends Activity implements Interviewer_SE_Basics_Constants{

	private ListView m_list;
	private ConfigurationManager m_confMan = null;
	private TestLauncherModel m_model = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_launcher);
		m_confMan = ConfigurationManager.getInstance();
		m_model = m_confMan.getTestLauncherModel();
		
		m_list = (ListView) findViewById(R.id.testLauncherListView);
	
		ArrayList<String> l_topicList = new ArrayList<String>();
		for (TestlauncherVO l_testLauncherVO : m_model.getLauncherList()) {
			l_topicList.add(l_testLauncherVO.getTopic());
		}
		TestLauncherListAdapter l_testLauncherListAdapter = new TestLauncherListAdapter(this, l_topicList);
		
		m_list.setAdapter(l_testLauncherListAdapter);
		
		m_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				ArrayList<String> l_dataFileList = m_model.getLauncherList().
													get(position).getJsonFileName();
				
//				Intent l_testPageIntent = new Intent(TEST_PAGE_ACTIVITY);
				Intent l_testPageIntent = new Intent(getApplicationContext(),TestActivity.class);
				l_testPageIntent.putStringArrayListExtra(DATA_FILE_LIST_MARKER, l_dataFileList);
				
				startActivity(l_testPageIntent);
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test_launcher, menu);
		return true;
	}

}

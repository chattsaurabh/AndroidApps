package com.chicago.library;

import java.util.ArrayList;

import com.chicago.library.model.LibraryVO;
import com.chicago.library.utils.ConfigurationManager;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class InfoActivity extends ActionBarActivity {
	
	private TextView mNameTV = null;
	private TextView mAddressTV = null;
	private TextView mHoursTV = null;
	
	private ConfigurationManager mConfMan = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		setTitle(getString(R.string.title_info));
		mConfMan = ConfigurationManager.getInstance();
		ArrayList<LibraryVO> dataList = mConfMan.getDataList();
		int position = getIntent().getIntExtra("position", 0);
		mNameTV = (TextView) findViewById(R.id.libName);
		mAddressTV = (TextView) findViewById(R.id.libAddress);
		mHoursTV = (TextView) findViewById(R.id.libHrs);
		
		mNameTV.setText(dataList.get(position).getName());
		mAddressTV.setText(dataList.get(position).getAddress());
		mHoursTV.setText(dataList.get(position).getHoursOfOperation());
		
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.info, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

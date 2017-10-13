package com.dev;

import com.constants.XStream_Constants;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SettingsActivity extends Activity implements XStream_Constants{
	
	private TextView content = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		content = (TextView) findViewById(R.id.SettingsTextView);
		content.setText(R.string.settings_text);
	}
}

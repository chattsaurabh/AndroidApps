package com.dev;

import com.constants.XStream_Constants;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutHelp extends Activity implements XStream_Constants{

	private TextView contentText = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_help);
		
		contentText = (TextView) findViewById(R.id.AboutHelpTextView);
		
		if(getIntent().getBooleanExtra(ABOUTHELPTEXT, true))
			contentText.setText(R.string.about_text);
		else
			contentText.setText(R.string.help_text);
	}
}

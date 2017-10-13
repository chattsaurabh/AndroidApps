package com.dev;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

import com.backend.FetchUserData;
import com.constants.JSON_Tag_Constants;
import com.constants.XStream_Constants;
import com.customwidgets.CustomEditText;
import com.data.MediaEntity;

public class UserChannelsActivity extends Activity implements XStream_Constants, JSON_Tag_Constants{

	private ListView addChannelsList = null;
	private Button addChannelButton = null;
	private String currentChannelName = null;
	private CustomEditText newChannelEditText = null;
	private MediaEntity currentMediaEntity = null;
	private FetchUserData userData = null;
	private Boolean addedSuccessfully = false;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_channels);
				
		Bundle currentMediaBundle = getIntent().getBundleExtra(MEDIABUNDLE);
		currentMediaEntity = currentMediaBundle.getParcelable(MEDIAPARCELABLE);
		
		userData = new FetchUserData(currentMediaEntity, this);
		
		newChannelEditText = (CustomEditText) findViewById(R.id.AddNewChannelEditText);
		newChannelEditText.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) { 
					InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(newChannelEditText.getWindowToken(), 0);
		        } 
				return false;
			}
		});
		
		addChannelsList = (ListView) findViewById(R.id.AddChannelsListView);
		addChannelButton = (Button) findViewById(R.id.AddChannelButton);
		addChannelButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currentChannelName = newChannelEditText.getText().toString();
				if(currentChannelName.trim().equalsIgnoreCase(""))
				{	
					userData.showPopUpAlert(ENTERCHANNELNAME, "Add Song");
				}
				else
				{
					userData.setNewChannelName(currentChannelName);
					addedSuccessfully = userData.saveSongToFile();
					if(addedSuccessfully)
					{
						sendResultAndClose();
					}
				}
			}
		});
		
		// to check and display already present channels here.
		
		final String[] channelTitle = userData.fetchChannelNames();	
		if(null != channelTitle) 
		{
			addChannelsList.setAdapter(new ArrayAdapter(this, R.layout.list_item, channelTitle));
			
			addChannelsList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position,
						long id) {
						Log.d("file", "ch name --> "+channelTitle[position]);
						currentChannelName = channelTitle[position];
						userData.setNewChannelName(currentChannelName);
						addedSuccessfully = userData.saveSongToFile();
						if(addedSuccessfully)
						{
							sendResultAndClose();
						}								
				}
			});
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();		
		setResult(0);
	}
	
	private void sendResultAndClose() {
		Intent resultIntent = new Intent();
		Bundle mediaBubdle = new Bundle();
		mediaBubdle.putParcelable(MEDIAPARCELABLE, currentMediaEntity);		
		resultIntent.putExtra(MEDIABUNDLE, mediaBubdle);
		setResult(1,resultIntent);
		finish();
	}
}

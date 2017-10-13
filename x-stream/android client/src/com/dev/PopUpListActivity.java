package com.dev;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.constants.XStream_Constants;

public class PopUpListActivity extends Activity implements XStream_Constants{

	ListView popUpListView;
	TextView titleTextView;
	private String[] artists;
//	ImageView titleImageView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		
		setContentView(R.layout.pop_up_list);
		popUpListView = (ListView) findViewById(R.id.PopUpListView);

		
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
		titleTextView = (TextView) findViewById(R.id.TitleTextView);
		titleTextView.setText("artists");
		
		ArrayList<String> artistList = getIntent().getStringArrayListExtra(pop_up_identifier);
		artists = new String[artistList.size()];
		int c = 0;
		for (String artist : artistList) {			
			artist = artist.replace("[", "");
			artist = artist.replace("]", "");
			artists[c] = artist;
			c++;
		}
		popUpListView.setAdapter(new ArrayAdapter(this, R.layout.list_item, artists));
		
		popUpListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				if(Home.current_media.equalsIgnoreCase(AUDIO))
				{
//					AudioPageActivity.setCurrentAudioList(artists[position]);
				}
			}
		});
	}
}

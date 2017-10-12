package com.chicago.library;

import com.chicago.library.model.LibraryListAdapter;
import com.chicago.library.utils.ConfigurationManager;
import com.chicago.library.utils.IParseComplete;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity implements IParseComplete {

	private ListView mLibContentListView = null;
	private ConfigurationManager mConfMan = null;
	private ProgressDialog mProgressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mLibContentListView = (ListView) findViewById(R.id.libContent);
		mLibContentListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.d("LIB", "Position :: "+position);
				Intent infoIntent = new Intent(getApplicationContext(),
						InfoActivity.class);
				infoIntent.putExtra("position", position);
				startActivity(infoIntent);
			}
		});
		mConfMan = ConfigurationManager.getInstance();
		showProgressDialog();
		mConfMan.init(this);
	}
	
	private void showProgressDialog() {
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setTitle("Loading...");
		mProgressDialog.setMessage("Please wait.");
		mProgressDialog.setCancelable(false);
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	@Override
	public void onParseCompleted(boolean successVal) {
		// TODO Auto-generated method stub
		mProgressDialog.dismiss();
		if(successVal){
			final LibraryListAdapter adapter = new LibraryListAdapter(this);
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					mLibContentListView.setAdapter(adapter);
				}
			});

		}
	}
}

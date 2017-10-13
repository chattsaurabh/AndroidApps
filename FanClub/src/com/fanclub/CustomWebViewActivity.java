package com.fanclub;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fanclub.utils.FanClubConstants;

public class CustomWebViewActivity extends Activity implements FanClubConstants{

	private String m_url = null;
	private String m_title = null;
	private WebView m_webView = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_PROGRESS);
	    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	    
	    m_title = getIntent().getStringExtra(WEB_VIEW_TITLE);
	    if(m_title != null)
	    {
	    	setTitle(m_title);
	    }
	    
		setContentView(R.layout.activity_custom_web_view);
		
		m_url = getIntent().getStringExtra(WEB_VIEW_URL);
		
		m_webView = (WebView) findViewById(R.id.FCWebView);
		m_webView.getSettings().setJavaScriptEnabled(true);
		
		
		setProgressBarIndeterminateVisibility(true);
	    setProgressBarVisibility(true);
		
		final Activity activity = this;
		m_webView.setWebChromeClient(new WebChromeClient(){
	         public void onProgressChanged(WebView view, int progress) 
	         {
                 activity.setProgress(progress * 100);	                    
             }
	    });
		
		m_webView.setWebViewClient(new WebViewClient() {
		    public boolean shouldOverrideUrlLoading(WebView view, String url){		        
		        view.loadUrl(url);
		        return false; 
		   }
		});
		
		
		
		m_webView.loadUrl(m_url);
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch(keyCode)
            {
            case KeyEvent.KEYCODE_BACK:
                if(m_webView.canGoBack() == true){
                	m_webView.goBack();
                }else{
                    finish();
                }
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.custom_web_view, menu);
		return true;
	}

}

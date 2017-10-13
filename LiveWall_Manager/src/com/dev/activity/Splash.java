package com.dev.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.dev.backend.ConfigManager;
import com.dev.backend.ImageFetcher;
import com.dev.constants.LiveWallManagerConstants;

public class Splash extends Activity implements LiveWallManagerConstants{
	
    protected int m_splashTime = 1000;
	private ConfigManager m_configManager = null;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       	super.onCreate(savedInstanceState);
       	
       	setContentView(R.layout.splash);
       	m_configManager = ConfigManager.getInstance();
        m_configManager.setContenResolver(getContentResolver());

        
        Handler HANDLER = new Handler();

        // thread for displaying the SplashScreen
        HANDLER.postDelayed(new Runnable() {
            @Override
            public void run() {
           	 ImageFetcher l_imf = ImageFetcher.getInstance();
             finish();
             startActivity (new Intent(home_page_activity));
            }
          }, m_splashTime);
    }
   
}

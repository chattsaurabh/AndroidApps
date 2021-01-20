package com.kobo.demo.challenge.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import com.kobo.demo.challenge.R


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        val handler = Handler()
        handler.postDelayed(Runnable {
            val intent = Intent(this@SplashActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}

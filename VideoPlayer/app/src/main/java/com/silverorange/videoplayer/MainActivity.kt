package com.silverorange.videoplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.silverorange.videoplayer.utils.Failure
import com.silverorange.videoplayer.utils.Loading
import com.silverorange.videoplayer.utils.Success
import com.silverorange.videoplayer.viewmodels.VideoPlayerViewmodel

class MainActivity : AppCompatActivity() {

    private val viewmodel: VideoPlayerViewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerObservers()
        viewmodel.fetchVideos()
    }

    private fun registerObservers() {
        viewmodel.videosLiveData.observe(this, Observer {
            var msg = ""
            when(it) {
                is Success -> {
                    msg = "Success:: Data = ${it.payload?.get(0)?.title}"
                }
                is Failure -> {
                    msg = "Failure:: reason = ${it.httpResponse}"
                }
                is Loading -> {
                    msg = "Loading"
                }
            }
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        })
    }
}
package com.silverorange.videoplayer

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
                    initVideoPlayer()
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

    private fun initVideoPlayer() {
        val videoView = findViewById<VideoView>(R.id.video)
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        val uri: Uri = Uri.parse(viewmodel.videoList[0].fullURL)
        videoView.setMediaController(mediaController)
        videoView.setVideoURI(uri)
        videoView.requestFocus()
        videoView.start()
    }
}
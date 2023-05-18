package com.silverorange.videoplayer

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Observer
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.ExoPlayer
import com.silverorange.videoplayer.databinding.ActivityMainBinding
import com.silverorange.videoplayer.utils.Failure
import com.silverorange.videoplayer.utils.Loading
import com.silverorange.videoplayer.utils.Success
import com.silverorange.videoplayer.viewmodels.VideoPlayerViewmodel


class MainActivity : AppCompatActivity() {

    private val viewmodel: VideoPlayerViewmodel by viewModels()
    private var player: ExoPlayer? = null
    private lateinit var binding: ActivityMainBinding
    private var playWhenReady = true
    private var mediaItemIndex = 0
    private var playbackPosition = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        if(viewmodel.videoList.isEmpty()) {
            return
        }

        player = ExoPlayer.Builder(this)
            .build()
            .also { exoPlayer ->
                binding.video.player = exoPlayer
                val mediaItem = viewmodel.videoList[mediaItemIndex].fullURL?.let { MediaItem.fromUri(it) }
                mediaItem?.let {
                    exoPlayer.setMediaItem(it)
                    exoPlayer.playWhenReady = playWhenReady
                    exoPlayer.prepare()
                }
            }
    }

    public override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initVideoPlayer()
        }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUi()
        if ((Util.SDK_INT <= 23 || player == null)) {
            initVideoPlayer()
        }
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.video).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }


    public override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }


    private fun releasePlayer() {
        player?.let { exoPlayer ->
            playbackPosition = exoPlayer.currentPosition
            mediaItemIndex = exoPlayer.currentMediaItemIndex
            playWhenReady = exoPlayer.playWhenReady
            exoPlayer.release()
        }
        player = null
    }
}
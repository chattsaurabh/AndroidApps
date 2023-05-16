package com.silverorange.videoplayer

import android.app.Application
import com.silverorange.videoplayer.utils.DependencyRegistry
import org.koin.core.component.KoinComponent

class VideoPlayerApplication
    : Application(), KoinComponent {
        override fun onCreate() {
            super.onCreate()
            DependencyRegistry().start(this)
        }
    }
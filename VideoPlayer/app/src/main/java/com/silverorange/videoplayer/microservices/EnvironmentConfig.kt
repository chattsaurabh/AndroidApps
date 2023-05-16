package com.silverorange.videoplayer.microservices

import android.content.Context
import com.silverorange.videoplayer.R
import org.koin.core.component.KoinComponent

class EnvironmentConfig(context: Context) : KoinComponent {
    val BASE_URL = context.getString(R.string.base_url)
}
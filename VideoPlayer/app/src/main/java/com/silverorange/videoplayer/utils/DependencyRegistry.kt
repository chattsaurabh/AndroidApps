package com.silverorange.videoplayer.utils

import com.silverorange.videoplayer.VideoPlayerApplication
import com.silverorange.videoplayer.microservices.ApiClient
import com.silverorange.videoplayer.microservices.EnvironmentConfig
import com.silverorange.videoplayer.viewmodels.VideoPlayerViewmodel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class DependencyRegistry {

    private val viewmodelModules = module {
        viewModel { VideoPlayerViewmodel(get()) }
    }

    private val singleModules = module {
        single {
            EnvironmentConfig(androidContext())
        }
        factory {
            ApiClient()
        }
    }

    private val appComponents = listOf(viewmodelModules, singleModules)

    fun start(app: VideoPlayerApplication) {
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(app)
            modules(appComponents)
        }
    }
}
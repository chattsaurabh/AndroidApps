package com.silverorange.videoplayer.microservices

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ApiClient : KoinComponent {
    private var retrofit: Retrofit? = null
    private val environmentConfig : EnvironmentConfig by inject()

    fun getClient(): Retrofit? {

        val client = buildHttpClient()
        retrofit = Retrofit.Builder()
            .baseUrl(environmentConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
        return retrofit
    }

    private fun buildHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build()

    }
}
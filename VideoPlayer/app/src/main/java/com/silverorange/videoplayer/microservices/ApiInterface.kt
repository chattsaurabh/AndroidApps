package com.silverorange.videoplayer.microservices

import com.silverorange.videoplayer.model.Videos
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("/videos")
    fun getVideos(): Call<List<Videos>>
}
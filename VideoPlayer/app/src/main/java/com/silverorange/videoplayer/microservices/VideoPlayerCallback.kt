package com.silverorange.videoplayer.microservices

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class VideoPlayerCallback<T> : Callback<T> {

    override fun onResponse(call: Call<T>?, response: Response<T>?) {
        if(response?.isSuccessful == true) {
            onSuccess(call, response)
        } else {
            onError(call, response)
        }
    }

    override fun onFailure(call: Call<T>?, t: Throwable?) {
        onError(call, null)
    }

    abstract fun onSuccess(call: Call<T>?, response: Response<T>?)
    abstract fun onError(call: Call<T>?, response: Response<T>?)
}
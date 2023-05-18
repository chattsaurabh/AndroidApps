package com.silverorange.videoplayer.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.silverorange.videoplayer.microservices.ApiClient
import com.silverorange.videoplayer.microservices.ApiInterface
import com.silverorange.videoplayer.microservices.VideoPlayerCallback
import com.silverorange.videoplayer.model.Videos
import com.silverorange.videoplayer.utils.Failure
import com.silverorange.videoplayer.utils.Resource
import com.silverorange.videoplayer.utils.Success
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Call
import retrofit2.Response

class VideoPlayerViewmodel(application: Application) : AndroidViewModel(application), KoinComponent {

    private val apiClient : ApiClient by inject()

    var videosLiveData = MutableLiveData<Resource<List<Videos>, String>>()
        private set

    var videoList = emptyList<Videos>()
    fun fetchVideos() {
        val apiInterface = apiClient.getClient()?.create(ApiInterface::class.java)
        val call : Call<List<Videos>>? = apiInterface?.getVideos()
        call?.enqueue(object : VideoPlayerCallback<List<Videos>>() {
            override fun onSuccess(call: Call<List<Videos>>?, response: Response<List<Videos>>?) {
                response?.body()?.let {
                    videoList = it
                }
                videosLiveData.postValue(Success(response?.body()))
            }

            override fun onError(call: Call<List<Videos>>?, response: Response<List<Videos>>?) {
                videosLiveData.postValue(Failure(null))
            }

        })
    }
}
package com.kobo.demo.challenge.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kobo.demo.challenge.models.HomeDataModel
import com.kobo.demo.challenge.network.Callback
import com.kobo.demo.challenge.network.KoboNetworkUtil

class HomeViewModel : ViewModel() {
    val endpoint = "https://gist.githubusercontent.com/dsandin/c8ed6c5a9486f311f4725f221b912220/raw/8c6d2d8e1f339d02e0ff3d2990560a4862c4beae/users_page_list"

    val progressLiveData = MutableLiveData<Any>()
    val homeUsersLiveData = MutableLiveData<HomeDataModel>()

    fun fetchUsers(context: Context) {
        val service = KoboNetworkUtil<HomeDataModel>()
        progressLiveData.postValue(null)
        service.getUsers(context, endpoint, object : Callback<HomeDataModel> {
            override val classType: Class<HomeDataModel>
                get() = HomeDataModel::class.java

            override fun onSuccess(response: HomeDataModel) {
                Log.d("HomeViewModel","response :: "+response.toString())
                homeUsersLiveData.postValue(response)
            }

            override fun onFailure() {
                Log.d("HomeViewModel","Failed !!")
            }

        })
    }
}
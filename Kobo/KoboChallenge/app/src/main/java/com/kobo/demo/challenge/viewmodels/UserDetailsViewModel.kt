package com.kobo.demo.challenge.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kobo.demo.challenge.models.HomeDataModel
import com.kobo.demo.challenge.models.UserDetailsModel
import com.kobo.demo.challenge.network.Callback
import com.kobo.demo.challenge.network.KoboNetworkUtil

class UserDetailsViewModel : ViewModel() {

    val progressLiveData = MutableLiveData<Any>()
    val homeUsersLiveData = MutableLiveData<Array<UserDetailsModel>>()

    fun fetchUserDetails(context: Context, endpoint: String) {
        val service = KoboNetworkUtil<UserDetailsModel>()
        progressLiveData.postValue(null)
        service.getUserDetails(context, endpoint, object : Callback<Array<UserDetailsModel>> {
            override val classType: Class<Array<UserDetailsModel>>
                get() = Array<UserDetailsModel>::class.java

            override fun onSuccess(response: Array<UserDetailsModel>) {
                Log.d("HomeViewModel","response :: "+response.toString())
                homeUsersLiveData.postValue(response)
            }

            override fun onFailure() {
                Log.d("HomeViewModel","Failed !!")
            }

        })
    }
}
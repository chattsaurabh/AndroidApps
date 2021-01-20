package com.kobo.demo.challenge.network

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Handler
import android.widget.ImageView

import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL
import java.util.HashMap
import android.content.ClipData.Item
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.kobo.demo.challenge.models.UserDetailsModel


class KoboNetworkUtil<T> {
    private val uiHandler = Handler()

    fun getUsers(context: Context, url: String, callback: Callback<T>) {
        makeRequest(context,Request.Method.GET, url, callback)
    }

    fun getUserDetails(context: Context, url: String, callback: Callback<Array<UserDetailsModel>>) {
        val queue = Volley.newRequestQueue(context)
        val request = object : StringRequest(Request.Method.GET, url, Response.Listener { response ->
            val gson = GsonBuilder().create()
            val items = gson.fromJson(response, Array<UserDetailsModel>::class.java)
            callback.onSuccess(items)
        }, Response.ErrorListener { callback.onFailure() }) {
        }

        queue.add(request)
    }

    private fun makeRequest(
        context: Context,
        method: Int,
        url: String,
        callback: Callback<T>
    ) {
        val queue = Volley.newRequestQueue(context)
        val request = object : StringRequest(method, url, Response.Listener { response ->
            val gson = GsonBuilder().create()
            val model = gson.fromJson(response, callback.classType)
            callback.onSuccess(model)
        }, Response.ErrorListener { callback.onFailure() }) {
        }

        queue.add(request)
    }

    fun loadImageFromUrl(imageView: ImageView, url: String) {
        object : Thread() {
            override fun run() {
                try {
                    val `is` = URL(url).content as InputStream
                    val drawable = Drawable.createFromStream(`is`, url)
                    uiHandler.post { imageView.setImageDrawable(drawable) }
                } catch (e: MalformedURLException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }.start()
    }

}

package com.silverorange.videoplayer.utils

import android.net.http.HttpResponseCache

sealed class Resource<out S, out F>

data class Success<S>(val payload: S?, val data: Any? = null) : Resource<S, Nothing>()
data class Failure<F>(val payload: F? = null, val data: Any? = null, val httpResponse: HttpResponseCache? = null) : Resource<Nothing, F>()
data class Loading<S>(val payload: S? = null, val data: Any? = null) : Resource<S, Nothing>()

package com.kobo.demo.challenge.network

/**
 * Callback interface to provide a contract fr service layer to provide a callback with data.
 */
interface Callback<T> {

    /**
     * Returns class type of model.
     * @return model class type.
     */
    val classType: Class<T>

    /**
     * success callback
     * @param response success response model.
     */
    fun onSuccess(response: T)

    /**
     * failure callback
     */
    fun onFailure()
}

package com.somethingsimple.garments.utils

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class GarmentsSharedPreferenceManager(
    private val activity: Activity
) {
    private val GARMENTS_SHARED_PREFERENCE_NAME = "GarmentsSharedPreference"
    private var sharedPreferences: SharedPreferences =
        activity.getSharedPreferences(GARMENTS_SHARED_PREFERENCE_NAME, MODE_PRIVATE)
    private var sharedPreferenceEditor = sharedPreferences.edit()

    fun persistsGarmentsList(key: String, garmentsJson: String) {
        sharedPreferenceEditor.putString(key, garmentsJson)
        sharedPreferenceEditor.apply()
    }

    fun fetchPersistedGarments(key: String): String? {
        return sharedPreferences.getString(key, "")
    }
}
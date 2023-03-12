package ca.guidomia.utils

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE

class GuidomiaSharePreferenceManager(private val context: Context) {

    private val GUIDOMIA_SHARED_PREFERENCE_NAME = "guidomia_shared_prefs"
    private val GUIDOMIA_DATA_KEY = "guidomia_data_key"
    private val prefs = context.getSharedPreferences(GUIDOMIA_SHARED_PREFERENCE_NAME, MODE_PRIVATE)
    private val editor = prefs.edit()

    fun persistGuidomiaDataList(data: String) {
        editor.putString(GUIDOMIA_DATA_KEY, data)
        editor.apply()
    }

    fun fetchPersistedGuidomiaData() : String? {
        return prefs.getString(GUIDOMIA_DATA_KEY, "")
    }
}
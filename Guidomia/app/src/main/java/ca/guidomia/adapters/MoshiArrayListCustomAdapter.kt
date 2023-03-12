package ca.guidomia.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class MoshiArrayListCustomAdapter {
    @ToJson
    fun arrayListToJson(list: ArrayList<GuidomiaData>) : List<GuidomiaData> = list

    @FromJson
    fun arrayListFromJson(list: List<GuidomiaData>) : ArrayList<GuidomiaData> = ArrayList(list)
}
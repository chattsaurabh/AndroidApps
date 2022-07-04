package com.somethingsimple.garments.models

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class MoshiArrayListCustomAdapter {
    @ToJson
    fun arrayListToJson(list: ArrayList<Garment>): List<Garment> = list

    @FromJson
    fun arrayListFromJson(list: List<Garment>): ArrayList<Garment> = ArrayList(list)
}
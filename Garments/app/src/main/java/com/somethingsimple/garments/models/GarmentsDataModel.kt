package com.somethingsimple.garments.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GarmentsDataModel(
    @Json(name ="garments") var garments:ArrayList<Garment>
)

@JsonClass(generateAdapter = true)
data class Garment(
    @Json(name = "name")var name: String,
    @Json(name = "timestamp")var timestamp: String
)
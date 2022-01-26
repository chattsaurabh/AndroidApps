package com.somethingsimple.healthall.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HomeModel(
    @Json(name ="sourceColors") var sourceColors:List<Color>,
    @Json(name ="sourceShapes") var sourceShapes:List<Shape>,
    @Json(name ="mappings") var mappings:List<Mapping>
)

@JsonClass(generateAdapter = true)
data class Color(
    @Json(name = "color")var color: String
)

@JsonClass(generateAdapter = true)
data class Shape(
    @Json(name = "shape")var shape: String
)

@JsonClass(generateAdapter = true)
data class Mapping(
    @Json(name = "sourceColor")var sourceColor: String,
    @Json(name = "sourceShape")var sourceShape: String,
    @Json(name = "destinationColor")var destinationColor: String,
    @Json(name = "destinationShape")var destinationShape: String
)


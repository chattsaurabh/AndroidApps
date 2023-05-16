package com.silverorange.videoplayer.model

import com.squareup.moshi.Json


data class Videos(
    @field:Json(name = "id") val id: String? = null,
    @field:Json(name = "title") val title: String? = null,
    @field:Json(name = "hlsURL") val hlsURL: String? = null,
    @field:Json(name = "fullURL") val fullURL: String? = null,
    @field:Json(name = "description") val description: String? = null,
    @field:Json(name = "publishedAt") val publishedAt: String? = null,
    @field:Json(name = "author") val author: Author? = null,
)

data class Author(
    @field:Json(name = "id") val id: String? = null,
    @field:Json(name = "name") val name: String? = null
)

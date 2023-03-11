package ca.guidomia.adapters

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GuidomiaData(
    @Json(name = "consList") var consList : List<String?>?,
    @Json(name = "customerPrice") var customerPrice : Int?,
    @Json(name = "make") var make : String?,
    @Json(name = "marketPrice") var marketPrice : Int?,
    @Json(name = "model") var model : String?,
    @Json(name = "prosList") var prosList : List<String?>?,
    @Json(name = "rating") var rating : Int?

)



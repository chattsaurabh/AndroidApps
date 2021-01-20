package com.kobo.demo.challenge.models

import com.google.gson.annotations.SerializedName

class UserDetailsModel {

    @SerializedName("id")
    var id: String = ""

    @SerializedName("first_name")
    var first_name: String? = null

    @SerializedName("last_name")
    var last_name: String? = null

    @SerializedName("text")
    var text: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("backgroundColor")
    var backgroundColor: String? = null

    @SerializedName("avatar")
    var avatar: String? = null

    @SerializedName("avatar_large")
    var avatar_large: String? = null

}
package com.somethingsimple.yelpreviews.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Location {

    @SerializedName("display_address")
    public List<String> display_address;
}

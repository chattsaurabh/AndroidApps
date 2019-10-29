package com.somethingsimple.yelpreviews.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class YelpReviewModel {

    @SerializedName("businesses")
    public List<Businesses> businesses;
}

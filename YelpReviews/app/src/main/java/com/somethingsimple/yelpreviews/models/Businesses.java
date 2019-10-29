package com.somethingsimple.yelpreviews.models;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Businesses implements Comparable{

    @SerializedName("id")
    public String id;

    @SerializedName("alias")
    public String alias;

    @SerializedName("name")
    public String name;

    @SerializedName("image_url")
    public String image_url;

    @SerializedName("phone")
    public String phone;

    @Nullable
    @SerializedName("photos")
    public List<String> photos;

    @SerializedName("location")
    public Location location;


    @Override
    public int compareTo(@NonNull Object object) {
        return this.name.compareTo(((Businesses) object).name);
    }
}

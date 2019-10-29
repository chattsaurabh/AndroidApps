package com.somethingsimple.yelpreviews.contracts;

import android.widget.ImageView;

public interface YelpDetailsServiceContract {

    void getDetailsforId(String id);

    void loadImage(final ImageView imageView, final String url);
}

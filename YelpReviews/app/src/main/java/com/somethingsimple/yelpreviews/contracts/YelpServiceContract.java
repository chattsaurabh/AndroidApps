package com.somethingsimple.yelpreviews.contracts;

import android.widget.ImageView;

/**
 * Service interface for interaction between presenter and services layer.
 */
public interface YelpServiceContract<T> {

    /**
     * search API.
     * @param keyword keyword for search.
     * @param callback response will be provided through call back instance.
     */
    void searchYelp(final String keyword, final Callback<T> callback);

    void loadImage(final ImageView imageView, final String url);
}

package com.somethingsimple.yelpreviews.contracts;

import com.somethingsimple.yelpreviews.models.Businesses;

/**
 * View interface for interaction between presenter and view.
 */
public interface YelpView {

    void reloadData();

    void launchDetailsPage(String businessesId);
}

package com.somethingsimple.yelpreviews.presenters;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.somethingsimple.yelpreviews.contracts.Callback;
import com.somethingsimple.yelpreviews.contracts.YelpServiceContract;
import com.somethingsimple.yelpreviews.contracts.YelpView;
import com.somethingsimple.yelpreviews.models.Businesses;
import com.somethingsimple.yelpreviews.models.YelpReviewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class YelpReviewPresenter {
    private final String TAG = YelpReviewPresenter.class.getSimpleName();

    private YelpView view;
    private YelpServiceContract service;
    private YelpReviewModel model;
    private String searchString;

    private enum Mode {
        STANDARD,
        SORT,
        SEARCH
    }

    Mode mode = Mode.STANDARD;

    public YelpReviewPresenter(final YelpView view, final YelpServiceContract service) {
        this.view = view;
        this.service = service;
    }
    public void init() {
        service.searchYelp("Ethiopian", new Callback<YelpReviewModel>() {
            @Override
            public void onSuccess(YelpReviewModel response) {
                Log.d(TAG, response.businesses.get(0).name);
                model = response;
                view.reloadData();
            }

            @Override
            public void onFailure() {

            }

            @Override
            public Class<YelpReviewModel> getClassType() {
                return YelpReviewModel.class;
            }
        });
    }

    public List<Businesses> getBusinessList() {
        switch (mode) {
            case SORT:
                return getSortedBusinesses();
            case SEARCH:
                return getSearchedBusinesses();
            case STANDARD:
            default:
        }
        return model != null ? model.businesses : null;
    }

    private List<Businesses> getSearchedBusinesses() {
        List<Businesses> searchedBusinesses = new ArrayList<>();
        for (Businesses businesses : model.businesses) {
            if(businesses.name.contains(searchString) || businesses.alias.contains(searchString)) {
                searchedBusinesses.add(businesses);
            }
        }
        return searchedBusinesses;
    }

    private List<Businesses> getSortedBusinesses() {
        List<Businesses> sortedBusinesses = model.businesses;
        Collections.sort(sortedBusinesses);
        return sortedBusinesses;
    }

    public void loadImage(final ImageView imageView, final String url) {
        service.loadImage(imageView, url);
    }

    public View.OnClickListener getItemClickListenter(final Businesses businesses) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View itemView) {
                view.launchDetailsPage(businesses.id);
            }
        };
    }

    public void sort() {
        mode = Mode.SORT;
        view.reloadData();
    }

    public void search(String searchQuery) {
        if (TextUtils.isEmpty(searchQuery)) {
            mode = Mode.STANDARD;
            searchString = "";
            view.reloadData();
        } else {
            mode = Mode.SEARCH;
            searchString = searchQuery;
            view.reloadData();
        }
    }
}

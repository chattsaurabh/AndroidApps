package com.somethingsimple.yelpreviews;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.somethingsimple.yelpreviews.adapters.DetailsViewPagerAdapter;
import com.somethingsimple.yelpreviews.contracts.Callback;
import com.somethingsimple.yelpreviews.contracts.YelpDetailsServiceContract;
import com.somethingsimple.yelpreviews.models.Businesses;
import com.somethingsimple.yelpreviews.services.YelpReviewServices;

public class YelpDetailsActivity extends AppCompatActivity implements YelpDetailsServiceContract {
    private final String TAG = YelpDetailsActivity.class.getSimpleName();
    public static final String ID_KEY = "id_key";

    private YelpReviewServices service;
    private Businesses detailsModel;
    private ViewPager viewPager;
    private TextView name;
    private TextView alias;
    private TextView address;
    private TextView phone;
    private ProgressBar progressBar;
    private DetailsViewPagerAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        String id = getIntent().getStringExtra(ID_KEY);
        setupUi();
        service = new YelpReviewServices();
        getDetailsforId(id);
        Log.d(TAG, id);
    }

    private void setupUi() {
        viewPager = findViewById(R.id.detailsViewPager);
        name = findViewById(R.id.detailsName);
        alias = findViewById(R.id.detailsAlias);
        address = findViewById(R.id.detailsAddress);
        phone = findViewById(R.id.detailsPhone);
        progressBar = findViewById(R.id.detailsProgress);
    }

    private void toggleProgress(boolean showProgress) {
        if (showProgress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void getDetailsforId(String id) {
        toggleProgress(true);
        service.getBusinessForId(this, id, new Callback<Businesses>() {
            @Override
            public void onSuccess(Businesses response) {
                toggleProgress(false);
                detailsModel = response;
                reloadUi();
            }

            @Override
            public void onFailure() {
                toggleProgress(false);
            }

            @Override
            public Class getClassType() {
                return Businesses.class;
            }
        });
    }

    private void reloadUi() {
        adapter = new DetailsViewPagerAdapter(this, detailsModel.photos, this);
        viewPager.setAdapter(adapter);

        name.setText(detailsModel.name);
        alias.setText(detailsModel.alias);
        phone.setText(detailsModel.phone);
        StringBuilder addressBuilder = new StringBuilder();
        for(String val : detailsModel.location.display_address) {
            addressBuilder
                    .append(val)
                    .append("\n");
        }
        address.setText(addressBuilder.toString());
    }

    @Override
    public void loadImage(ImageView imageView, String url) {
        service.LoadImageFromUrl(imageView, url);
    }
}

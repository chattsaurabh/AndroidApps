package com.somethingsimple.yelpreviews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.somethingsimple.yelpreviews.adapters.GridAdapter;
import com.somethingsimple.yelpreviews.contracts.Callback;
import com.somethingsimple.yelpreviews.contracts.YelpServiceContract;
import com.somethingsimple.yelpreviews.contracts.YelpView;
import com.somethingsimple.yelpreviews.models.Businesses;
import com.somethingsimple.yelpreviews.presenters.YelpReviewPresenter;
import com.somethingsimple.yelpreviews.services.YelpReviewServices;

public class YelpReviewActivity<T> extends AppCompatActivity implements YelpView, YelpServiceContract {

    private YelpReviewPresenter presenter;
    private YelpReviewServices service;
    private ProgressBar progressBar;
    private GridView gridView;
    private GridAdapter gridAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        service = new YelpReviewServices();
        presenter = new YelpReviewPresenter(this, this);
        setupUi();
        presenter.init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        final MenuItem searchMenuItem = menu.findItem( R.id.action_search);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print
                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                searchMenuItem.collapseActionView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                presenter.search(s);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_sort){
            presenter.sort();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupUi() {
        progressBar = findViewById(R.id.yelpProgress);
        gridView = findViewById(R.id.gridView);
        gridAdapter = new GridAdapter(presenter, this);
    }

    private void toggleProgress(boolean showProgress) {
        if (showProgress) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void searchYelp(String keyword, final Callback callback) {
        toggleProgress(true);
        service.getYelpBusiness(this, keyword, new Callback<T>(){
            @Override
            public void onSuccess(T response) {
                toggleProgress(false);
                callback.onSuccess(response);
            }

            @Override
            public void onFailure() {
                toggleProgress(false);
                callback.onFailure();
            }

            @Override
            public Class getClassType() {
                return callback.getClassType();
            }
        });
    }

    @Override
    public void loadImage(ImageView imageView, String url) {
        service.LoadImageFromUrl(imageView, url);
    }

    @Override
    public void reloadData() {
        gridView.setAdapter(gridAdapter);
    }

    @Override
    public void launchDetailsPage(String businessesId) {
        Intent detailsIntent = new Intent();
        detailsIntent.setClass(this, YelpDetailsActivity.class);
        detailsIntent.putExtra(YelpDetailsActivity.ID_KEY, businessesId);
        startActivity(detailsIntent);

    }
}

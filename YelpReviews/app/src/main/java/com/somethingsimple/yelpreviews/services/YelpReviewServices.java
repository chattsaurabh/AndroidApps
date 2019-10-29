package com.somethingsimple.yelpreviews.services;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.somethingsimple.yelpreviews.contracts.Callback;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class YelpReviewServices<T> {
    private Handler uiHandler = new Handler();

    private final String clientId = "m64KS7xeTFbFb4hVpBfpjQ";
    private final String API_KEY = "Q56spruApzKsZPm6WeeSobIHae-TdHYSo_mbvCuWFzIKWRZmx7-9fAmcM7PgxeWk4rb91UjtLplFTMEcMKnvfU6U28BN9BYL6esuYtRXg78m7hr7J5rISkEtRwq1XXYx";
    private final String BASE_URL = "https://api.yelp.com/v3/businesses/";
    private final String SEARCH_URL_KEY = "search";
    private final String HEADER_KEY = "Authorization";
    private final String HEADER_KEY_BEARER = "Bearer ";
    private final String HEADER_KEY_WORD = "keyword";
    private final String HEADER_KEY_LOCATION = "location";
    private final String DEFAULT_LOCATION = "toronto";


    public void getYelpBusiness(final Context context, final String keyword, final Callback callback) {
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put(HEADER_KEY, HEADER_KEY_BEARER + API_KEY );
        Uri builtUri = Uri.parse(BASE_URL+SEARCH_URL_KEY)
                .buildUpon()
                .appendQueryParameter(HEADER_KEY_WORD, keyword)
                .appendQueryParameter(HEADER_KEY_LOCATION, DEFAULT_LOCATION)
                .build();
        makeRequest(context, Request.Method.GET, builtUri.toString(), headerMap, callback);
    }

    public void getBusinessForId(final Context context, final String id, final Callback callback) {
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put(HEADER_KEY, HEADER_KEY_BEARER + API_KEY );
        Uri builtUri = Uri.parse(BASE_URL+id);
        makeRequest(context, Request.Method.GET, builtUri.toString(), headerMap, callback);
    }

    private void makeRequest(Context context, int method, String url, final Map<String, String> headers, final Callback<T> callback) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new GsonBuilder().create();
                T model = gson.fromJson(response, callback.getClassType());
                callback.onSuccess(model);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                for ( Map.Entry<String, String> mapEntry:
                        headers.entrySet()) {
                    params.put(mapEntry.getKey(), mapEntry.getValue());
                }
                return params;
            }
        };

        queue.add(request);
    }

    public void LoadImageFromUrl(final ImageView imageView, final String url) {
        new Thread(){
            @Override
            public void run() {
                try {
                    InputStream is = (InputStream) new URL(url).getContent();
                    final Drawable drawable = Drawable.createFromStream(is, url);
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageDrawable(drawable);
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}

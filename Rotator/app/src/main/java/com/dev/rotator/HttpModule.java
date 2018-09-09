package com.dev.rotator;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dev.rotator.model.TimeRs;
import com.dev.rotator.view.Callback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HttpModule<T> {

    public void getTime(final Context context, final Callback<TimeRs> callback) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url ="https://dateandtimeasjson.appspot.com/";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Gson gson = new GsonBuilder().create();
                        TimeRs model = gson.fromJson(response, TimeRs.class);
                        callback.onSuccess(model);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error);
            }
        });

        queue.add(stringRequest);
    }
}

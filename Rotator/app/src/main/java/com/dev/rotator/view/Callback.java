package com.dev.rotator.view;

import com.android.volley.VolleyError;

public interface Callback<T> {

    void onSuccess(T response);

    void onFailure(VolleyError failure);
}

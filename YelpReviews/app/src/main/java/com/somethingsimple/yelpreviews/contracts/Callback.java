package com.somethingsimple.yelpreviews.contracts;

/**
 * Callback interface to provide a contract fr service layer to provide a callback with data.
 */
public interface Callback<T> {

    /**
     * success callback
     * @param response success response model.
     */
    void onSuccess(T response);

    /**
     * failure callback
     */
    void onFailure();

    /**
     * Returns class type of model.
     * @return model class type.
     */
    Class<T> getClassType();
}

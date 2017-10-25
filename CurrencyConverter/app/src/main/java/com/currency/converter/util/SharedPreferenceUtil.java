package com.currency.converter.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtil implements ICurrencyConverterConstants {

    private Context mContext = null;
    private SharedPreferences mSharedPreference = null;

    public void init(Context context) {
        mContext = context;
        mSharedPreference = mContext.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
    }

    public void persistLatestRates(String currencyResponse) {
        SharedPreferences.Editor editor = mSharedPreference.edit();

        editor.putString(LATEST_RATES_PREFS, currencyResponse);
        editor.commit();
    }

    public String getPersistedLatesRates() {
        String ret = mSharedPreference.getString(LATEST_RATES_PREFS,null);
        return ret;
    }
}

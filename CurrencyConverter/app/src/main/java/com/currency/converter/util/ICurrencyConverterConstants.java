package com.currency.converter.util;

/**
 * Created by ADMI on 10/23/2017.
 */

public interface ICurrencyConverterConstants {

//  App constants

    public static final String TAG = "Currency_Converter";

//    HTTP module constants
    public static final String BASE_URL = "http://api.fixer.io/";
    public static final String LATEST_URL = "latest";
    public static final String BASE_PREFIX_URL = "?base=";

//    SharedPreference constants
    public static final String APP_SHARED_PREFS = "currency_converter_shared_prefs";
    public static final String LATEST_RATES_PREFS = "currency_converter_latest_rates_prefs";

//    JSON keys

    public static final String BASE = "base";
    public static final String DATA = "date";
    public static final String RATES = "rates";
}

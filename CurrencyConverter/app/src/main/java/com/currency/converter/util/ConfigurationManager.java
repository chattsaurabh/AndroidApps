package com.currency.converter.util;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.currency.converter.data.ExchangeRatesVO;

import java.util.ArrayList;
import java.util.HashMap;

public class ConfigurationManager implements ICurrencyConverterConstants,
        IDownloadComplete, IParseComplete {

    private static ConfigurationManager mInstance = null;

    private ConfigurationManager() {};

    public static ConfigurationManager getInstance() {
        if(null == mInstance) {
            mInstance = new ConfigurationManager();
        }
        return mInstance;
    }

    private Context mContext = null;
    private HttpModule mHttpModule = null;
    private IDownloadComplete mListener = null;
    private ArrayList<String> mCurrencyList = null;
    private ArrayList<ExchangeRatesVO> mExchangeRatesList = null;
    private Parser mParser = null;
    private SharedPreferenceUtil mSharedPrefUtil = null;

    public void init(IDownloadComplete listener, Context context) {
        mListener = listener;
        mContext = context;
        mCurrencyList = new ArrayList<String>();
        mExchangeRatesList = new ArrayList<ExchangeRatesVO>();
        mParser = new Parser();
        mParser.init(this);
        mSharedPrefUtil = new SharedPreferenceUtil();
        mSharedPrefUtil.init(context);

        mHttpModule = new HttpModule();
        fetchInitialCurrencyList();
    }

    public ArrayList<String> getCurrencyList() {
        return mCurrencyList;
    }

    public ArrayList<ExchangeRatesVO> getExchangeRatesList() {
        return mExchangeRatesList;
    }

    private void fetchInitialCurrencyList() {
        String latestCurrencies = mSharedPrefUtil.getPersistedLatesRates();
        Log.d(TAG, "latest currencies :: "+latestCurrencies);
        if(null == latestCurrencies) {
            downloadLatestCurrencies();
        } else {
            onDownloadComplete(true, latestCurrencies);
        }
    }

    private void downloadLatestCurrencies() {
        String url = BASE_URL + LATEST_URL;
        mHttpModule.downloadFromUrl(url, this);
    }

    public void downloadCurrenciesForBase(String base) {
        String url = BASE_URL + LATEST_URL + BASE_PREFIX_URL+base;
        mHttpModule.downloadFromUrl(url, this);
    }

    @Override
    public void onDownloadComplete(boolean successVal) {

    }

    @Override
    public void onDownloadComplete(boolean successVal, String result) {
        Log.d(TAG,"result :: "+result);
        if(successVal) {
            mSharedPrefUtil.persistLatestRates(result);
            downloadHandler.postDelayed(periodicDownloader, (1000 * 60 * 30));
            mCurrencyList.clear();
            mExchangeRatesList.clear();
            mParser.parseCurrencyList(result);

        }

    }

    @Override
    public void onParseComplete(boolean successVal) {
        Log.d(TAG,"currency list length :: "+mCurrencyList.size());
        mListener.onDownloadComplete(successVal);
    }

    final Handler downloadHandler = new Handler();
    final Runnable periodicDownloader = new Runnable() {
        public void run() {
            downloadLatestCurrencies();
        }
    };
}

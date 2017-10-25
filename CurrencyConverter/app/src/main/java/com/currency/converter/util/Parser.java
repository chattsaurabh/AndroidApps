package com.currency.converter.util;


import com.currency.converter.data.ExchangeRatesVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Parser implements ICurrencyConverterConstants {
    private ConfigurationManager mConfMan = null;
    private ArrayList<String> mCurrencyList = null;
    private ArrayList<ExchangeRatesVO> mExchangeRatesList = null;
    private IParseComplete mListener = null;

    public void init(IParseComplete listener) {
        mConfMan = ConfigurationManager.getInstance();
        mCurrencyList = mConfMan.getCurrencyList();
        mExchangeRatesList = mConfMan.getExchangeRatesList();
        mListener = listener;
    }

    public void parseCurrencyList(String jsonResponse) {
        try {
            synchronized (mCurrencyList) {
                JSONObject responseObj = new JSONObject(jsonResponse);
                mCurrencyList.add(responseObj.getString(BASE));

                JSONObject currencies = responseObj.getJSONObject(RATES);
                Iterator<String> keys = currencies.keys();
                while (keys.hasNext()) {
                    String keyVal = keys.next();
                    mCurrencyList.add(keyVal);

                    ExchangeRatesVO exchangeVO = new ExchangeRatesVO();
                    exchangeVO.setCurrency(keyVal);
                    exchangeVO.setRate(currencies.getString(keyVal));
                    mExchangeRatesList.add(exchangeVO);
                }
            }
            mListener.onParseComplete(true);
        } catch (JSONException e) {
            e.printStackTrace();
            mListener.onParseComplete(false);
        }
    }
}

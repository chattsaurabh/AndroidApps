package com.currency.converter.data;

/**
 * Created by ADMI on 10/24/2017.
 */

public class ExchangeRatesVO {

    private String mCurrency;
    private String mRate;

    public String getCurrency() {
        return mCurrency;
    }

    public void setCurrency(String mCurrency) {
        this.mCurrency = mCurrency;
    }

    public String getRate() {
        return mRate;
    }

    public void setRate(String mRate) {
        this.mRate = mRate;
    }
}

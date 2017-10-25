package com.currency.converter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.currency.converter.R;
import com.currency.converter.data.ExchangeRatesVO;
import com.currency.converter.util.ConfigurationManager;

import java.util.ArrayList;

/**
 * Created by ADMI on 10/24/2017.
 */

public class ExchangeListAdapter extends BaseAdapter {

    private Context mContext = null;
    private ArrayList<ExchangeRatesVO> mExchageRatesList = null;
    private LayoutInflater mInflator = null;

    public ExchangeListAdapter(Context context){
        mContext = context;
        mExchageRatesList = ConfigurationManager.getInstance().getExchangeRatesList();
        mInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mExchageRatesList.size();
    }

    @Override
    public Object getItem(int position) {
        return mExchageRatesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = mInflator.inflate(R.layout.exchange_list_item,null);

            ExchangeRatesVO exchange = mExchageRatesList.get(position);

            TextView currencyName = (TextView) convertView.findViewById(R.id.currencyName);
            TextView currencyRate = (TextView) convertView.findViewById(R.id.currencyRate);

            currencyName.setText(exchange.getCurrency());
            currencyRate.setText(exchange.getRate());
        }
        return convertView;
    }
}

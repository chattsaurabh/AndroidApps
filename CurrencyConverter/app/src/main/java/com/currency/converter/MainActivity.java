package com.currency.converter;

import android.app.AlertDialog;
import android.content.Context;
import android.nfc.Tag;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.currency.converter.adapters.ExchangeListAdapter;
import com.currency.converter.customviews.DialogUtil;
import com.currency.converter.util.ConfigurationManager;
import com.currency.converter.util.ICurrencyConverterConstants;
import com.currency.converter.util.IDownloadComplete;

public class MainActivity extends AppCompatActivity
        implements IDownloadComplete, ICurrencyConverterConstants{

    private ConfigurationManager mConfMan = null;
    private AlertDialog mProgressDialog = null;
    private Spinner mCurrencySpinner = null;
    private ListView mExchangeRatesListView = null;
    private Handler mUpdateSpinnerListHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCurrencySpinner = (Spinner) findViewById(R.id.currencySpinner);
        mCurrencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG,"item :: "+mConfMan.getCurrencyList().get(position));
                if(position != 0) {
                    mProgressDialog.show();
                    mConfMan.downloadCurrenciesForBase(mConfMan.getCurrencyList().get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mExchangeRatesListView = (ListView) findViewById(R.id.exchangeListView);

        init();
    }

    private void init() {
        mProgressDialog = DialogUtil.getProgressDialog(MainActivity.this,
                getString(R.string.prog_dialog_loading),getString(R.string.prog_dialog_wait));
        mProgressDialog.show();
        mConfMan = ConfigurationManager.getInstance();
        mConfMan.init(this, this);
    }

    @Override
    public void onDownloadComplete(boolean successVal, String result) {

    }

    @Override
    public void onDownloadComplete(boolean successVal) {

        Runnable updateUIRunnable = new Runnable() {
            @Override
            public void run() {
                mProgressDialog.dismiss();

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_spinner_item, mConfMan.getCurrencyList());
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mCurrencySpinner.setAdapter(dataAdapter);

                ExchangeListAdapter ratesAdapter = new ExchangeListAdapter(getApplicationContext());
                mExchangeRatesListView.setAdapter(ratesAdapter);
            }
        };
        mUpdateSpinnerListHandler.post(updateUIRunnable);
    }



}

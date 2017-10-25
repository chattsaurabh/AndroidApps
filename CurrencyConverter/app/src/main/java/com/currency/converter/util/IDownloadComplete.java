package com.currency.converter.util;

/**
 * Created by ADMI on 10/23/2017.
 */

public interface IDownloadComplete {
    public void onDownloadComplete(boolean successVal, String result);
    public void onDownloadComplete(boolean successVal);
}

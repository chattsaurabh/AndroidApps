package com.currency.converter.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ADMI on 10/23/2017.
 */

public class HttpModule {

    private IDownloadComplete mDownloadListener = null;

    public void downloadFromUrl(final String url, IDownloadComplete listener) {
        mDownloadListener = listener;
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                downloadJSON(url);
            }
        }).start();
    }

    private void downloadJSON(String url) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL lURL = new URL(url);
            connection = (HttpURLConnection) lURL.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line+"\n");
            }
            mDownloadListener.onDownloadComplete(true, buffer.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
            mDownloadListener.onDownloadComplete(false, null);
        } catch (IOException e) {
            e.printStackTrace();
            mDownloadListener.onDownloadComplete(false, null);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

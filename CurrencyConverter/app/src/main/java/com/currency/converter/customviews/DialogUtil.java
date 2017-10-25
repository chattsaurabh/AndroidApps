package com.currency.converter.customviews;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.currency.converter.R;

/**
 * Created by ADMI on 10/23/2017.
 */

public class DialogUtil {

    public static AlertDialog getProgressDialog(Context context, String title, String msg) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.custom_progress_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptsView);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        return alertDialog;
    }
}

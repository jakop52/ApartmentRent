package com.jakupIndustries.apartmentRentClient.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

import com.jakupIndustries.apartmentRentClient.R;

public class CommonMethod {
    public static final String DISPLAY_MESSAGE_ACTION =
            "com.codecube.broking.gcm";

    public static final String EXTRA_MESSAGE = "message";

    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isValidEmail(EditText editText) {
        if (!TextUtils.isEmpty(editText.getText()) && Patterns.EMAIL_ADDRESS.matcher(editText.getText()).matches()) {
            editText.setBackgroundResource(R.drawable.edittext_normal_line);
            return true;
        } else {
            editText.setBackgroundResource(R.drawable.edittext_error_line);
            return false;
        }
    }

    public static boolean isValidPassword(EditText editText) {
        if (!TextUtils.isEmpty(editText.getText())) {
            editText.setBackgroundResource(R.drawable.edittext_normal_line);
            return true;
        } else {
            editText.setBackgroundResource(R.drawable.edittext_error_line);
            return false;
        } //TODO PASSWORD VALIDATION
    }

    public static boolean isNotEmptyOnly(EditText editText){
        if (!TextUtils.isEmpty(editText.getText())) {
            editText.setBackgroundResource(R.drawable.edittext_normal_line);
            return true;
        } else {
            editText.setBackgroundResource(R.drawable.edittext_error_line);
            return false;
        }
    }

    public static void showAlert(String message, Activity context) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        try {
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

package com.webapp.a4_order_station_driver.utils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.webapp.a4_order_station_driver.R;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

import okhttp3.ResponseBody;

public class ToolUtil {
    public static boolean checkTheInternet() {
        ConnectivityManager cm = (ConnectivityManager) AppController.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void hideSoftKeyboard(Activity activity, EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void showLongToast(String s, Activity activity) {
        Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
    }


    public static String showError(Context context, ResponseBody s) {
        String message = context.getString(R.string.error);
        try {
            JSONObject jObjError = new JSONObject(s.string());
            message = jObjError.getString(AppContent.FIREBASE_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }

    public static String getTime(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("hh:mm", cal).toString();
        return date;
    }
}

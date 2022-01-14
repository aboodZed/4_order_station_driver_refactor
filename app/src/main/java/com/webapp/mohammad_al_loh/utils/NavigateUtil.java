package com.webapp.mohammad_al_loh.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.model.LatLng;
import com.webapp.mohammad_al_loh.R;
import com.webapp.mohammad_al_loh.feature.main.MainActivity;
import com.webapp.mohammad_al_loh.feature.main.hame.HomeFragment;
import com.webapp.mohammad_al_loh.feature.order.OrderActivity;
import com.webapp.mohammad_al_loh.models.Order;

public class NavigateUtil {

    public void replaceFragment(FragmentManager fragmentManager, Fragment fragment, int layout) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(layout, fragment);
        fragmentTransaction.commit();
    }

    public void activityIntent(Context from, Class to, boolean back) {
        Intent intent = new Intent(from, to);
        if (!back) intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        from.startActivity(intent);
    }

    public void activityIntentWithPage(Context from, Class to, boolean back, int pageNum) {
        Intent intent = new Intent(from, to);
        intent.putExtra(AppContent.PAGE, pageNum);
        if (!back) intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        from.startActivity(intent);
    }

    public void openNotification(Context from, String message) {
        Intent intent = new Intent(from, MainActivity.class);
        intent.putExtra(AppContent.PAGE, HomeFragment.page);
        intent.putExtra(AppContent.FIREBASE_MESSAGE, message);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        from.startActivity(intent);
    }

    public void openOrder(Context from, Order order, int page, boolean back) {
        Intent intent = new Intent(from, OrderActivity.class);
        intent.putExtra(AppContent.PAGE, page);
        intent.putExtra(AppContent.ORDER_OBJECT, order);
        if (!back) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        from.startActivity(intent);
    }

    public void setLocation(Activity activity, LatLng location) {
        String format = "geo:0,0?q=" + location.latitude
                + "," + location.longitude + "( GPSLocation title)";
        Uri uri = Uri.parse(format);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

    public void openLink(Activity activity, String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public void sendEmail(Activity activity, String email) {
        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
        activity.startActivity(Intent.createChooser(i, activity.getString(R.string.send_email)));
    }

    @SuppressLint("MissingPermission")
    public void makeCall(Activity activity, String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phone));
        activity.startActivity(intent);
    }
}
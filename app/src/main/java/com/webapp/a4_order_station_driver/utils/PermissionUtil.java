package com.webapp.a4_order_station_driver.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

public class PermissionUtil {

    public static void requestPermission(Activity activity, String permission, int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
    }

    public static void requestPermissionOnFragment(Fragment fragment, String permission, int requestCode) {
        fragment.requestPermissions(new String[]{permission}, requestCode);
    }

    public static boolean isPermissionGranted(String permission, Context context) {
        return ActivityCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED;
    }
}
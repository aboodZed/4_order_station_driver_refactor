package com.webapp.a4_order_station_driver.services.firebase;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

public class GenerateFCMService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("StartService", "StartService");
        generateFCMToken(getApplicationContext());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy", "onDestroy");
    }

    public void generateFCMToken(final Context context) {
        FirebaseApp.initializeApp(context);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("Main", "getInstanceId failed", task.getException());
                stopSelf();
                return;
            }
            // Get new Instance ID token
            String token = task.getResult();

            // Log and toast
            stopSelf();
            Log.e("fcm token", "" + token);
        });
    }
}

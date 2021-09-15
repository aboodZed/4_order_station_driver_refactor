package com.webapp.a4_order_station_driver.services.firebase;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
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
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.e("Main", "getInstanceId failed", task.getException());
                        stopSelf();
                        return;
                    }

                    // Get new Instance ID token
                    String token = task.getResult().getToken();

                    // Log and toast
                    stopSelf();
                    if (AppController.getInstance().getAppSettingsPreferences().getIsLogin())
                        new APIUtil<Message>(context).getData(AppController.getInstance().getApi()
                                .fcmToken(token), new RequestListener<Message>() {
                            @Override
                            public void onSuccess(Message message, String msg) {
                                Log.e(getClass().getName() + " : ChangeFCMResponse:", message.getMassage());
                            }

                            @Override
                            public void onError(String msg) {
                                Log.e(getClass().getName() + " : ChangeFCMError:", msg);

                            }

                            @Override
                            public void onFail(String msg) {
                                Log.e(getClass().getName() + " : ChangeFCMFail:", msg);

                            }
                        });
                    Log.e("fcm token", "" + token);
                });
    }
}

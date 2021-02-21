package com.webapp.a4_order_station_driver.utils;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationUtil {

    public static void sendMessageNotification(Activity activity, String invoice_no
            , String order_id, String user_id, String type) {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("order_id", order_id);
        map.put("invoice_no", invoice_no);
        map.put("type", type);
        Log.e(NotificationUtil.class.getName(), "map" + map.toString());

        new APIUtils<Message>(activity).getData(AppController.getInstance().getApi()
                .sendMessageNotification(map), new RequestListener<Message>() {
            @Override
            public void onSuccess(Message message, String msg) {
                Log.e(getClass().getName(), "response" + message);
            }

            @Override
            public void onError(String msg) {
                Log.e(getClass().getName(), "failure" + msg);
            }

            @Override
            public void onFail(String msg) {
                Log.e(getClass().getName(), "failure" + msg);
            }
        });
    }
}
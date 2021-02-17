package com.webapp.a4_order_station_driver.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.webapp.a4_order_station_driver.models.Message;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationUtil {

    public static void sendMessageNotification(String invoice_no, String order_id
            , String user_id, String type) {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", user_id);
        map.put("order_id", order_id);
        map.put("invoice_no", invoice_no);
        map.put("type", type);
        Log.e(NotificationUtil.class.getName(), "map" + map.toString());
        Call<Message> call = AppController.getInstance().getApi().sendMessageNotification(map);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
                Log.e(getClass().getName(), "response" + response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
                Log.e(getClass().getName(), "failure" + t.getMessage());
            }
        });
    }
}
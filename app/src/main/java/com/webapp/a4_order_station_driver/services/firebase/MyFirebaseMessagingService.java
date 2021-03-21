package com.webapp.a4_order_station_driver.services.firebase;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.webapp.a4_order_station_driver.feature.order.chat.ChatFragment;
import com.webapp.a4_order_station_driver.feature.order.publicOrderView.PublicOrderViewFragment;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.NavigateUtil;
import com.webapp.a4_order_station_driver.utils.NotificationUtil;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e("remoteMessage", "remote" + remoteMessage.getData().toString());
        try {
            Map<String, String> map = remoteMessage.getData();
            JSONObject mapJSON = new JSONObject(map);
            String message = mapJSON.getString(AppContent.FIREBASE_MESSAGE);
            JSONObject body = new JSONObject(message).getJSONObject(AppContent.FIREBASE_DATA);
            //data
            String msg = body.getString(AppContent.FIREBASE_MSG);
            String type = body.getString(AppContent.FIREBASE_TYPE);

            if (type.equals(AppContent.DRIVER_APPROVED)) {
                //send notification
                new NotificationUtil().sendNotification(this, msg, message);
            } else if (type.equals(AppContent.REJECT)) {
                //send notification
                new NotificationUtil().sendNotification(this, msg, message);

            } else if (!body.isNull(AppContent.FIREBASE_STATUS)) {
                String status = body.getString(AppContent.FIREBASE_STATUS);

                if (status.equals(AppContent.NEW_MESSAGE) &&
                        (PublicOrderViewFragment.isOpenPublicChat || ChatFragment.isOpenChat)) {
                } else if (status.equals(AppContent.IN_WAY_TO_STORE)) {
                    //send notification
                    new NotificationUtil().sendNotification(this, msg, message);
                    AppController.getInstance().getAppSettingsPreferences()
                            .setPayType(body.getString("payment_type"));
                } else {
                    new NotificationUtil().sendNotification(this, msg, message);
                    new NavigateUtil().openNotification(this, message);
                }
            } else {
                new NotificationUtil().sendNotification(this, msg, message);
                new NavigateUtil().openNotification(this, message);
            }
            
/*
            if (body.getString("msg").contains("new order")) {
                if (body.getString("type").equals("4station")) {
                    sendNotification(body.getString("msg"), body.getInt("order_id")
                            , body.getString("type"), body.getString("status"));

                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("order_id", body.getInt("order_id"));
                    intent.putExtra("type", body.getString("type"));
                    intent.putExtra("status", body.getString("status"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    sendNotification(body.getString("msg"), body.getInt("public_order_id")
                            , body.getString("type"), body.getString("status"));

                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("order_id", body.getInt("public_order_id"));
                    intent.putExtra("type", body.getString("type"));
                    intent.putExtra("status", body.getString("status"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            } else if (body.getString("type").equals("driver_approved")) {
                sendNotification(body.getString("msg"), -1, body.getString("type"), "");

            } else if (body.getString("type").equals("reject")) {
                sendNotification(body.getString("msg"), -1, body.getString("type"), "");

            } else if (body.getString("type").equals("wallet")) {
                sendNotification(body.getString("msg"), -1, body.getString("type"), "");

                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("order_id", -1);
                intent.putExtra("type", body.getString("type"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            } else if (body.getString("status") != null) {
                if (body.getString("status").equals("new_message")
                        && PublicOrderViewFragment.isOpenPublicChat) {

                } else if (body.getString("status").equals("new_message")
                        && ChatFragment.isOpenChat) {

                } else if (body.getString("status").equals("cancelled")) {
                    sendNotification(body.getString("msg"), -1, body.getString("type"), "");
                    /*if (GPSTracking.gpsTracking != null) {
                        GPSTracking.gpsTracking.removeUpdates();
                    }
                    OrderGPSTracking.newInstance(this).removeUpdates();
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("order_id", -1);
                    intent.putExtra("type", body.getString("type"));
                    intent.putExtra("status", body.getString("status"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else if (body.getString("status").equals("in_the_way_to_store")) {
                    sendNotification(body.getString("msg"), -1, body.getString("type"), "");
                    AppController.getInstance().getAppSettingsPreferences().setPayType(body.getString("payment_type"));

                } else {
                    sendNotification(body.getString("msg"), -1, body.getString("type"), body.getString("status"));

                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("order_id", -1);
                    intent.putExtra("type", body.getString("type"));
                    intent.putExtra("status", body.getString("status"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            } else {
                sendNotification(body.getString("msg"), -1, body.getString("type"), "");

                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("order_id", -1);
                intent.putExtra("type", body.getString("type"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }*/

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("error", "" + e.getMessage());
        }

        /*
        remote{moredata=dd, message={"data":{"msg":"new order #000000005","order_no":"000000005",
        "destination_address":"Ryaid","pickup_address_ar":"غزة","type_of_receive":"home","created_at":1616066071,
        "title":"new order #000000005","type":"4station","order_id":189,"pickup_address_en":"gaza","country_id":1,"status":"ready"}

        ,"sound":"mySound","icon":"myIcon","title":"4station","body":"new order #000000005","click_action"
        :"com.webapp.a4_order_station_driver.feture.home.MainActivity"}}
         */
    }

    @Override
    public void onNewToken(String refreshedToken) {
        super.onNewToken(refreshedToken);
        if (AppController.getInstance().getAppSettingsPreferences().getIsLogin())
            new APIUtil<Message>(this).getData(AppController.getInstance().getApi()
                    .fcmToken(refreshedToken), new RequestListener<Message>() {
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
    }
}
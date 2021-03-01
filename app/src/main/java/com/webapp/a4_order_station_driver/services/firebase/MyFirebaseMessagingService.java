package com.webapp.a4_order_station_driver.services.firebase;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.webapp.a4_order_station_driver.feature.order.chat.ChatFragment;
import com.webapp.a4_order_station_driver.feature.order.publicOrderView.PublicOrderViewFragment;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.NavigateUtil;
import com.webapp.a4_order_station_driver.utils.NotificationUtil;

import org.json.JSONObject;

import java.util.Map;

import static com.webapp.a4_order_station_driver.utils.ToolUtil.changeFcm;

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
            String status = body.getString(AppContent.FIREBASE_STATUS);

            if (type.equals(AppContent.DRIVER_APPROVED)) {
                //send notification
                new NotificationUtil().sendNotification(this, msg, message);
            } else if (type.equals(AppContent.REJECT)) {
                //send notification
                new NotificationUtil().sendNotification(this, msg, message);
            } else if (status != null) {

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

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", "" + e.getMessage());
        }
    }

    @Override
    public void onNewToken(String refreshedToken) {
        super.onNewToken(refreshedToken);
        if (AppController.getInstance().getAppSettingsPreferences().getIsLogin()) {
            changeFcm(refreshedToken);
        }
    }
}
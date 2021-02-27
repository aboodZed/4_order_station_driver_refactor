package com.webapp.a4_order_station_driver.services.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.feature.main.MainActivity;
import com.webapp.a4_order_station_driver.feature.order.publicOrderView.PublicOrderViewFragment;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.feature.order.chat.ChatFragment;
import com.webapp.a4_order_station_driver.utils.location.tracking.GPSTracking;
import com.webapp.a4_order_station_driver.utils.location.tracking.OrderGPSTracking;

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
            String jsonObject = mapJSON.getString("message");
            JSONObject body = new JSONObject(jsonObject).getJSONObject("data");

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
                    }*/
                    new OrderGPSTracking(this,null).removeUpdates();
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
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error", "" + e.getMessage());
        }
    }

    private void sendNotification(String title, int id, String type, String status) {
        Notification.Builder builder = new Notification.Builder(this);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setContentTitle(title)
                //.setStyle(new Notification.BigTextStyle().bigText(title))
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher);
        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                String channelID = "orderChannel"; // In case you don't add a channel android oreo will not create the notification and it will give you a log error
                NotificationChannel channel = new NotificationChannel(channelID, title,
                        NotificationManager.IMPORTANCE_DEFAULT);
                channel.setLightColor(Color.GREEN);
                notificationManager.createNotificationChannel(channel);
                builder.setChannelId(channelID);
            } catch (Exception e) {
            }
        }
        Notification notification = builder.build();
        notification.defaults = Notification.FLAG_ONLY_ALERT_ONCE;
        notification.defaults = Notification.FLAG_INSISTENT | Notification.FLAG_AUTO_CANCEL;
        //notification.defaults = Notification.FLAG_AUTO_CANCEL;
        notification.sound = alarmSound;
        Intent intent;
        intent = new Intent(this, MainActivity.class);
        intent.putExtra("order_id", id);
        intent.putExtra("type", type);
        intent.putExtra("status", status);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int iUniqueId = (int) (System.currentTimeMillis() & 0xfffffff);
        notification.contentIntent = PendingIntent.getActivity(getApplicationContext()
                , iUniqueId, intent, PendingIntent.FLAG_ONE_SHOT);
        notificationManager.notify(iUniqueId, notification);
    }

    @Override
    public void onNewToken(String refreshedToken) {
        super.onNewToken(refreshedToken);
        if (AppController.getInstance().getAppSettingsPreferences().getIsLogin()) {
            changeFcm(refreshedToken);
        }
    }
}
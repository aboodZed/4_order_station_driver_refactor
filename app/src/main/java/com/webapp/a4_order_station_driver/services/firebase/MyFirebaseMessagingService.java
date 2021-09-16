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
        remote(remoteMessage);
    }

    private void remote(RemoteMessage remoteMessage){
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

                if (status.equals(AppContent.NEW_MESSAGE) && PublicOrderViewFragment.isOpenPublicChat) { }
                else if (status.equals(AppContent.NEW_MESSAGE) && ChatFragment.isOpenChat){ }

                else if (status.equals(AppContent.IN_WAY_TO_STORE)) {
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
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(getClass().getName()+ " error", "" + e.getMessage());
        }
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
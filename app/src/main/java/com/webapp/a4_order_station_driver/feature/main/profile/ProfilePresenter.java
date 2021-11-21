package com.webapp.a4_order_station_driver.feature.main.profile;

import android.app.Activity;

import com.webapp.a4_order_station_driver.feature.login.LoginActivity;
import com.webapp.a4_order_station_driver.feature.main.MainActivity;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.NavigateUtil;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;
import com.webapp.a4_order_station_driver.utils.location.tracking.GPSTracking;

public class ProfilePresenter {

    private Activity baseActivity;
    private DialogView<Message> dialogView;

    public ProfilePresenter(Activity baseActivity, DialogView<Message> dialogView) {
        this.baseActivity = baseActivity;
        this.dialogView = dialogView;
    }

    public void logout() {
        dialogView.showDialog("");
        new APIUtil<Message>(baseActivity).getData(AppController.getInstance()
                .getApi().isOnline(MainActivity.offline), new RequestListener<Message>() {
            @Override
            public void onSuccess(Message message, String msg) {
                AppController.getInstance().getAppSettingsPreferences().setIsLogin(false);
                GPSTracking.getInstance(baseActivity).removeMyUpdates();
                new NavigateUtil().activityIntent(baseActivity, LoginActivity.class, false);
                dialogView.hideDialog();
            }

            @Override
            public void onError(String msg) {
                ToolUtil.showLongToast(msg, baseActivity);
                dialogView.hideDialog();
            }

            @Override
            public void onFail(String msg) {
                ToolUtil.showLongToast(msg, baseActivity);
                dialogView.hideDialog();
            }
        });
    }
}

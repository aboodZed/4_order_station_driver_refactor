package com.webapp.a4_order_station_driver.feature.main;

import com.webapp.a4_order_station_driver.feature.login.LoginActivity;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.models.User;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.NavigateUtil;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;
import com.webapp.a4_order_station_driver.utils.location.tracking.GPSTracking;

class MainPresenter {

    private BaseActivity baseActivity;
    private DialogView<Boolean> dialogView;

    public MainPresenter(BaseActivity baseActivity, DialogView<Boolean> dialogView) {
        this.baseActivity = baseActivity;
        this.dialogView = dialogView;
    }

    public void updateState(boolean status) {
        new APIUtil<Message>(baseActivity).getData(AppController.getInstance()
                .getApi().isOnline(status), new RequestListener<Message>() {
            @Override
            public void onSuccess(Message message, String msg) {
                //update saved user
                User user = AppController.getInstance().getAppSettingsPreferences().getUser();
                user.setOnline(status);
                AppController.getInstance().getAppSettingsPreferences().setUser(user);
                //set toggle
                ToolUtil.showLongToast(message.getMessage(), baseActivity);
                dialogView.setData(true);
            }

            @Override
            public void onError(String msg) {
                ToolUtil.showLongToast(msg, baseActivity);
                dialogView.setData(false);
            }

            @Override
            public void onFail(String msg) {
                ToolUtil.showLongToast(msg, baseActivity);
                dialogView.setData(false);
            }
        });
    }

    public void logout() {
        dialogView.showDialog("");
        new APIUtil<Message>(baseActivity).getData(AppController.getInstance()
                .getApi().isOnline(false), new RequestListener<Message>() {
            @Override
            public void onSuccess(Message message, String msg) {
                AppController.getInstance().getAppSettingsPreferences().setToken("");
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

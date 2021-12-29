package com.webapp.a4_order_station_driver.feature.main.profile;

import android.app.Activity;

import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;

public class ProfilePresenter {

    private Activity baseActivity;
    private DialogView<Message> dialogView;

    public ProfilePresenter(Activity baseActivity, DialogView<Message> dialogView) {
        this.baseActivity = baseActivity;
        this.dialogView = dialogView;
    }
}

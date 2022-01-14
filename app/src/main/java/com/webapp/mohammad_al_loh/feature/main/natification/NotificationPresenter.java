package com.webapp.mohammad_al_loh.feature.main.natification;

import android.app.Activity;

import com.webapp.mohammad_al_loh.models.NotificationList;
import com.webapp.mohammad_al_loh.utils.APIUtil;
import com.webapp.mohammad_al_loh.utils.AppController;
import com.webapp.mohammad_al_loh.utils.ToolUtil;
import com.webapp.mohammad_al_loh.utils.listeners.DialogView;
import com.webapp.mohammad_al_loh.utils.listeners.RequestListener;

class NotificationPresenter {

    private Activity activity;
    private DialogView<NotificationList> dialogView;

    public NotificationPresenter(Activity activity, DialogView<NotificationList> dialogView) {
        this.activity = activity;
        this.dialogView = dialogView;
        getData();
    }

    private void getData() {
        dialogView.showDialog("");
        new APIUtil<NotificationList>(activity).getData(AppController.getInstance()
                .getApi().getNotifications(), new RequestListener<NotificationList>() {
            @Override
            public void onSuccess(NotificationList notifications, String msg) {
                dialogView.setData(notifications);
                dialogView.hideDialog();
            }

            @Override
            public void onError(String msg) {
                ToolUtil.showLongToast(msg, activity);
                dialogView.hideDialog();
            }

            @Override
            public void onFail(String msg) {
                ToolUtil.showLongToast(msg, activity);
                dialogView.hideDialog();
            }
        });
    }
}

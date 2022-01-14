package com.webapp.mohammad_al_loh.feature.main.profile;

import com.webapp.mohammad_al_loh.feature.login.LoginActivity;
import com.webapp.mohammad_al_loh.feature.main.MainActivity;
import com.webapp.mohammad_al_loh.models.Message;
import com.webapp.mohammad_al_loh.utils.APIUtil;
import com.webapp.mohammad_al_loh.utils.AppController;
import com.webapp.mohammad_al_loh.utils.NavigateUtil;
import com.webapp.mohammad_al_loh.utils.ToolUtil;
import com.webapp.mohammad_al_loh.utils.language.BaseActivity;
import com.webapp.mohammad_al_loh.utils.listeners.DialogView;
import com.webapp.mohammad_al_loh.utils.listeners.RequestListener;
import com.webapp.mohammad_al_loh.utils.location.tracking.GPSTracking;

public class ProfilePresenter {

    private BaseActivity baseActivity;
    private DialogView<Message> dialogView;

    public ProfilePresenter(BaseActivity baseActivity, DialogView<Message> dialogView) {
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

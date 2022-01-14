package com.webapp.mohammad_al_loh.feature.main;

import com.webapp.mohammad_al_loh.databinding.ActivityMainBinding;
import com.webapp.mohammad_al_loh.models.Login;
import com.webapp.mohammad_al_loh.models.Message;
import com.webapp.mohammad_al_loh.utils.APIUtil;
import com.webapp.mohammad_al_loh.utils.AppController;
import com.webapp.mohammad_al_loh.utils.ToolUtil;
import com.webapp.mohammad_al_loh.utils.language.BaseActivity;
import com.webapp.mohammad_al_loh.utils.listeners.RequestListener;

import static com.webapp.mohammad_al_loh.feature.main.MainActivity.online;

class MainPresenter {

    private BaseActivity baseActivity;

    public MainPresenter(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    public void updateState(String status, ActivityMainBinding binding){
        new APIUtil<Message>(baseActivity).getData(AppController.getInstance()
                .getApi().isOnline(status), new RequestListener<Message>() {
            @Override
            public void onSuccess(Message message, String msg) {
                Login login = AppController.getInstance().getAppSettingsPreferences().getLogin();
                login.getUser().setIs_online(status);
                AppController.getInstance().getAppSettingsPreferences().setLogin(login);
                ToolUtil.showLongToast(message.getMassage(), baseActivity);
            }

            @Override
            public void onError(String msg) {
                ToolUtil.showLongToast(msg, baseActivity);
                if (status.equals(online)) {
                    binding.scAppear.setChecked(false);
                } else {
                    binding.scAppear.setChecked(true);
                }
            }

            @Override
            public void onFail(String msg) {
                ToolUtil.showLongToast(msg, baseActivity);
                if (status.equals(online)) {
                    binding.scAppear.setChecked(false);
                } else {
                    binding.scAppear.setChecked(true);
                }
            }
        });
    }
}

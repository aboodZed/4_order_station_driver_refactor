package com.webapp.a4_order_station_driver.feature.main;

import com.webapp.a4_order_station_driver.databinding.ActivityMainBinding;
import com.webapp.a4_order_station_driver.models.Login;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

import static com.webapp.a4_order_station_driver.feature.main.MainActivity.online;

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

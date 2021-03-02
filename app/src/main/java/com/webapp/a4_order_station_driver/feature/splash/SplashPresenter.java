package com.webapp.a4_order_station_driver.feature.splash;

import android.app.Activity;
import android.util.Log;

import com.webapp.a4_order_station_driver.feature.main.MainActivity;
import com.webapp.a4_order_station_driver.feature.login.LoginActivity;
import com.webapp.a4_order_station_driver.models.Login;
import com.webapp.a4_order_station_driver.models.User;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.language.AppLanguageUtil;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;
import com.webapp.a4_order_station_driver.utils.location.tracking.GPSTracking;
import com.webapp.a4_order_station_driver.utils.location.tracking.OrderGPSTracking;

class SplashPresenter {

    private BaseActivity baseActivity;

    public SplashPresenter(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
        //functions
        checkUserLogin();
        setLanguage();
        checkOrderProcess();
    }

    private void setLanguage() {
        if (AppController.getInstance().getAppSettingsPreferences().isFirstRun()) {
            AppLanguageUtil.getInstance().setAppFirstRunLng();
        } else {
            AppLanguageUtil.getInstance().setAppLanguage(baseActivity
                    , AppController.getInstance().getAppSettingsPreferences().getAppLanguage());
        }
    }

    private void checkOrderProcess() {
        if (AppController.getInstance().getAppSettingsPreferences().getTrackingOrder() != null) {
            OrderGPSTracking.newInstance(baseActivity).startGPSTracking();
            /*GPSTracking gpsTracking = GPSTracking.getInstance(activity, AppController.getInstance()
                    .getAppSettingsPreferences().getTrackingOrder());
            gpsTracking.startGPSTracking();*/
        }
      /*  if (AppController.getInstance().getAppSettingsPreferences().getTrackingOrder() != null) {
            new OrderGPSTracking(baseActivity, AppController.getInstance()
                    .getAppSettingsPreferences().getTrackingOrder()).startGPSTracking();

            GPSTracking gpsTracking = GPSTracking.getInstance(baseActivity, AppController.getInstance()
                    .getAppSettingsPreferences().getTrackingPublicOrder());
            gpsTracking.startGPSTracking();
        }*/
    }

    private void checkUserLogin() {
        if (AppController.getInstance().getAppSettingsPreferences().getIsLogin()) {

            new APIUtil<User>(baseActivity).getData(AppController.getInstance().getApi().getUserData()
                    , new RequestListener<User>() {
                        @Override
                        public void onSuccess(User user, String msg) {
                            //update user
                            Login login = AppController.getInstance().getAppSettingsPreferences().getLogin();
                            login.setUser(user);
                            AppController.getInstance().getAppSettingsPreferences().setLogin(login);
                            //print user token
                            Log.e("usertoken", AppController.getInstance()
                                    .getAppSettingsPreferences().getLogin().getAccess_token());
                            //navigate
                            baseActivity.navigate(MainActivity.page);
                        }

                        @Override
                        public void onError(String msg) {
                            ToolUtil.showLongToast(msg, baseActivity);
                            baseActivity.navigate(LoginActivity.page);
                        }

                        @Override
                        public void onFail(String msg) {
                            ToolUtil.showLongToast(msg, baseActivity);
                            baseActivity.navigate(LoginActivity.page);
                        }
                    });

        } else {
            baseActivity.navigate(LoginActivity.page);
        }
    }

}

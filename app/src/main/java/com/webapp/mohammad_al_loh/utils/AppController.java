package com.webapp.mohammad_al_loh.utils;

import android.app.Application;

import com.webapp.mohammad_al_loh.services.api.ApiInterface;
import com.webapp.mohammad_al_loh.services.api.ApiClient;

public class AppController extends Application {

    private static AppController mInstance;

    private AppSettingsPreferences appSettingsPreferences;
    private ApiInterface api;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        api = ApiClient.getRetrofit().create(ApiInterface.class);
        appSettingsPreferences = new AppSettingsPreferences(this);
    }

    public AppSettingsPreferences getAppSettingsPreferences() {
        return appSettingsPreferences;
    }

    public ApiInterface getApi() {
        return api;
    }
}

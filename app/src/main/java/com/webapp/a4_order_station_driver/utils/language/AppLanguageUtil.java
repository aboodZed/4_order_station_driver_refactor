package com.webapp.a4_order_station_driver.utils.language;

import android.content.Context;
import android.content.res.Configuration;

import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.AppSettingsPreferences;

import java.util.Locale;

public class AppLanguageUtil {

    private static AppLanguageUtil instance = null;
    private AppSettingsPreferences appSettingsPreferences;

    public static synchronized AppLanguageUtil getInstance() {
        if (instance == null) {
            instance = new AppLanguageUtil();
        }
        return instance;
    }

    public AppLanguageUtil() {
        appSettingsPreferences = new AppSettingsPreferences(getContext());
    }

    public void setAppFirstRunLng() {
        switch (Locale.getDefault().getLanguage()) {
            case "ar":
                setAppLanguage(getContext(), "ar");
                break;
            case "en":
                setAppLanguage(getContext(), "en");
                break;
            default:
                setAppLanguage(getContext(), "en");
                break;
        }
        appSettingsPreferences.setFirstRun();
    }

    private static void setLocale(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
    }

    public void setAppLanguage(Context context, String newLanguage) {
        setLocale(context, newLanguage);
        appSettingsPreferences.setAppLanguage(newLanguage);
    }

    private AppController getContext() {
        return AppController.getInstance();
    }
}

package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SettingsObject {

    @SerializedName("data")
    @Expose
    private SettingsData settings;

    public SettingsData getSettings() {
        return settings;
    }

    @Override
    public String toString() {
        return "SettingsObject{" +
                "settings=" + settings +
                '}';
    }
}

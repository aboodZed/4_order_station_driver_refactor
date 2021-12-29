package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppSettings extends Result<SettingsData> {

    @SerializedName("privacy_title")
    @Expose
    private String privacy_title;

    @SerializedName("privacy_content")
    @Expose
    private String privacy_content;

    private String fcm_token;

    public String getPrivacy_title() {
        return privacy_title;
    }

    public String getPrivacy_content() {
        return privacy_content;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    @Override
    public String toString() {
        return "AppSettings{" +
                "privacy_title='" + privacy_title + '\'' +
                ", privacy_content='" + privacy_content + '\'' +
                '}';
    }
}

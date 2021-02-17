package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Privacy {

    @SerializedName("privacy_title")
    @Expose
    private String privacy_title;

    @SerializedName("privacy_content")
    @Expose
    private String privacy_content;

    public String getPrivacy_title() {
        return privacy_title;
    }

    public String getPrivacy_content() {
        return privacy_content;
    }

    @Override
    public String toString() {
        return "Privacy{" +
                "privacy_title='" + privacy_title + '\'' +
                ", privacy_content='" + privacy_content + '\'' +
                '}';
    }
}

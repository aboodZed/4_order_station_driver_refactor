package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Customer implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("lat")
    @Expose
    private double lat;

    @SerializedName("lng")
    @Expose
    private double lng;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("full_mobile")
    @Expose
    private String full_mobile;

    @SerializedName("mobile")
    @Expose
    private String mobile;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("avatar_url")
    @Expose
    private String avatar_url;

    public int getId() {
        return id;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getName() {
        return name;
    }

    public String getFull_mobile() {
        return full_mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAddress() {
        return address;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    @Override
    public String toString() {
        return "TestCustomer{" +
                "id=" + id +
                ", lat=" + lat +
                ", lng=" + lng +
                ", name='" + name + '\'' +
                ", full_mobile='" + full_mobile + '\'' +
                ", mobile='" + mobile + '\'' +
                ", address='" + address + '\'' +
                ", avatar_url='" + avatar_url + '\'' +
                '}';
    }
}

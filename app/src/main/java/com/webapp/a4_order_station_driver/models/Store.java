package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Store implements Serializable {

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

    @SerializedName("mobile")
    @Expose
    private String mobile;

    @SerializedName("logo_url")
    @Expose
    private String logo_url;

    @SerializedName("cover_image_url")
    @Expose
    private String cover_image_url;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("rate")
    @Expose
    private float rate;

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

    public String getMobile() {
        return mobile;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public String getCover_image_url() {
        return cover_image_url;
    }

    public String getAddress() {
        return address;
    }

    public float getRate() {
        return rate;
    }

    @Override
    public String toString() {
        return "TestStore{" +
                "id=" + id +
                ", lat=" + lat +
                ", lng=" + lng +
                ", name=" + name +
                ", mobile='" + mobile + '\'' +
                ", logo_url='" + logo_url + '\'' +
                ", cover_image_url='" + cover_image_url + '\'' +
                ", address='" + address + '\'' +
                ", rate=" + rate +
                '}';
    }
}

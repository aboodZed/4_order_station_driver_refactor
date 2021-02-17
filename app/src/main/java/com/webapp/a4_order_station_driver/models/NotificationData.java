package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationData {

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("order_id")
    @Expose
    private int order_id;

    @SerializedName("public_order_id")
    @Expose
    private int public_order_id;

    @SerializedName("destination_address")
    @Expose
    private String destination_address;

    @SerializedName("pickup_address_ar")
    @Expose
    private String pickup_address_ar;

    @SerializedName("pickup_address_en")
    @Expose
    private String pickup_address_en;

    @SerializedName("created_at")
    @Expose
    private long created_at;

    @SerializedName("type")
    @Expose
    private String type;

    public String getMsg() {
        return msg;
    }

    public String getTitle() {
        return title;
    }

    public int getOrder_id() {
        return order_id;
    }

    public int getPublic_order_id() {
        return public_order_id;
    }

    public String getDestination_address() {
        return destination_address;
    }

    public String getPickup_address_ar() {
        return pickup_address_ar;
    }

    public String getPickup_address_en() {
        return pickup_address_en;
    }

    public long getCreated_at() {
        return created_at;
    }

    public String getType() {
        return type;
    }
}


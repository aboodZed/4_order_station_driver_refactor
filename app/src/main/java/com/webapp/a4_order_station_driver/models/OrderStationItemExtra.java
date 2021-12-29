package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderStationItemExtra {

    @SerializedName("name")
    @Expose
    private String name;

    public String getName() {
        return name;
    }
}

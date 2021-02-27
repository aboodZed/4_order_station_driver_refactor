package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PublicOrderObject {

    @SerializedName("order")
    @Expose
    private PublicOrder publicOrder;

    public PublicOrder getPublicOrder() {
        return publicOrder;
    }

    @Override
    public String toString() {
        return "PublicOrderObject{" +
                "publicOrder=" + publicOrder +
                '}';
    }
}

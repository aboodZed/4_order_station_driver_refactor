package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PublicArrays {

    @SerializedName("orders")
    @Expose
    private PublicOrderList publicOrders;

    @SerializedName("order")
    @Expose
    private PublicOrder publicOrder;

    public PublicOrderList getPublicOrders() {
        return publicOrders;
    }

    public PublicOrder getPublicOrder() {
        return publicOrder;
    }

}

package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PublicOrderListObject {

    @SerializedName("orders")
    @Expose
    private PublicOrderList publicOrders;

    public PublicOrderList getPublicOrders() {
        return publicOrders;
    }

    @Override
    public String toString() {
        return "PublicOrderListObject{" +
                "publicOrders=" + publicOrders +
                '}';
    }
}

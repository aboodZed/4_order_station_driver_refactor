package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderStationList {

    @SerializedName("orders")
    @Expose
    private ArrayList<OrderStation> orders;

    public ArrayList<OrderStation> getOrders() {
        return orders;
    }

    @Override
    public String toString() {
        return "OrderStationList{" +
                "orders=" + orders +
                '}';
    }
}

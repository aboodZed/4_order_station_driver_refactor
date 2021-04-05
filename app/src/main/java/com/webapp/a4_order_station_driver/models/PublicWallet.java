package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PublicWallet {

    @SerializedName("total_orders_amount")
    @Expose
    private String total_orders_amount;

    @SerializedName("total_driver_revenue")
    @Expose
    private double total_driver_revenue;

    @SerializedName("total_app_revenue")
    @Expose
    private double total_app_revenue;

    @SerializedName("totalClientBills")
    @Expose
    private String totalClientBills;

    @SerializedName("all_orders")
    @Expose
    private ArrayList<PublicOrder> publicOrders;

    @SerializedName("wallet")
    @Expose
    private String wallet;

    public String getTotalClientBills() {
        return totalClientBills;
    }

    public String getTotal_orders_amount() {
        return total_orders_amount;
    }

    public double getTotal_driver_revenue() {
        return total_driver_revenue;
    }

    public double getTotal_app_revenue() {
        return total_app_revenue;
    }

    public String getWallet() {
        return wallet;
    }

    public ArrayList<PublicOrder> getPublicOrders() {
        return publicOrders;
    }

    @Override
    public String toString() {
        return "PublicWallet{" +
                "total_orders_amount='" + total_orders_amount + '\'' +
                ", total_driver_revenue=" + total_driver_revenue +
                ", total_app_revenue=" + total_app_revenue +
                ", totalClientBills='" + totalClientBills + '\'' +
                ", publicOrders=" + publicOrders +
                ", wallet='" + wallet + '\'' +
                '}';
    }
}

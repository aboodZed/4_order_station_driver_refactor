package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Arrays {

    @SerializedName("status")
    @Expose
    private boolean status;

    @SerializedName("notifications")
    @Expose
    private ArrayList<Notification> notifications;

    @SerializedName("orders")
    @Expose
    private ArrayList<OrderStation> orders;

    @SerializedName("shop")
    @Expose
    private Shop shop;

    @SerializedName("ratings")
    @Expose
    private Rating ratings;

    @SerializedName("data")
    @Expose
    private Settings settings;

    @SerializedName("countries")
    @Expose
    private ArrayList<Country> countries;

    public boolean isStatus() {
        return status;
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }

    public ArrayList<OrderStation> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<OrderStation> orders) {
        this.orders = orders;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public Rating getRatings() {
        return ratings;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public ArrayList<Country> getCountries() {
        return countries;
    }
}

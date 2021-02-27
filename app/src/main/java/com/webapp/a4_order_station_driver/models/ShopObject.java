package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopObject {

    @SerializedName("shop")
    @Expose
    private Shop shop;

    public Shop getShop() {
        return shop;
    }

    @Override
    public String toString() {
        return "ShopObject{" +
                "shop=" + shop +
                '}';
    }
}

package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderStationItem implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("item_id")
    @Expose
    private int item_id;

    @SerializedName("item_name")
    @Expose
    private String item_name;

    @SerializedName("price")
    @Expose
    private double price;

    @SerializedName("qty")
    @Expose
    private int qty;

    @SerializedName("total")
    @Expose
    private double total;

    @SerializedName("extra_items")
    @Expose
    private ArrayList<OrderStationItemExtra> extra_items;

    public int getId() {
        return id;
    }

    public int getItem_id() {
        return item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public double getPrice() {
        return price;
    }

    public int getQty() {
        return qty;
    }

    public double getTotal() {
        return total;
    }

    public ArrayList<OrderStationItemExtra> getExtra_items() {
        return extra_items;
    }

    @Override
    public String toString() {
        return "TestOrderItem{" +
                "id=" + id +
                ", item_id=" + item_id +
                ", item_name='" + item_name + '\'' +
                ", price=" + price +
                ", qty=" + qty +
                ", total=" + total +
                ", extra_items=" + extra_items +
                '}';
    }
}

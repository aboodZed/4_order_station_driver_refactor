package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderItem implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("order_id")
    @Expose
    private String order_id;

    @SerializedName("item_id")
    @Expose
    private String item_id;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("qty")
    @Expose
    private String qty;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    @SerializedName("total")
    @Expose
    private String total;

    @SerializedName("item")
    @Expose
    private OrderItemItem orderItemItem;

    @SerializedName("extra_items")
    @Expose
    private ArrayList<ExtraItems> extra_items;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public OrderItemItem getOrderItemItem() {
        return orderItemItem;
    }

    public void setOrderItemItem(OrderItemItem orderItemItem) {
        this.orderItemItem = orderItemItem;
    }

    public ArrayList<ExtraItems> getExtra_items() {
        return extra_items;
    }
}

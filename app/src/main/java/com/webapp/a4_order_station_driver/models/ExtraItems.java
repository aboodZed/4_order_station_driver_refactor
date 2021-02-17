package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExtraItems {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name_ar")
    @Expose
    private String name_ar;

    @SerializedName("name_en")
    @Expose
    private String name_en;

    @SerializedName("item_id")
    @Expose
    private String item_id;

    @SerializedName("order_item_id")
    @Expose
    private String order_item_id;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("qty")
    @Expose
    private String qty;

    @SerializedName("total")
    @Expose
    private String total;

    @SerializedName("deleted_at")
    @Expose
    private String deleted_at;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    @SerializedName("name")
    @Expose
    private String name;

    public int getId() {
        return id;
    }

    public String getName_ar() {
        return name_ar;
    }

    public String getName_en() {
        return name_en;
    }

    public String getItem_id() {
        return item_id;
    }

    public String getOrder_item_id() {
        return order_item_id;
    }

    public String getPrice() {
        return price;
    }

    public String getQty() {
        return qty;
    }

    public String getTotal() {
        return total;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getName() {
        return name;
    }
}

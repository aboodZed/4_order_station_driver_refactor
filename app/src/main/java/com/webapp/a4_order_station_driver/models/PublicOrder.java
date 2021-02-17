package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class PublicOrder implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("place_id")
    @Expose
    private String place_id;

    @SerializedName("store_name")
    @Expose
    private String store_name;

    @SerializedName("invoice_number")
    @Expose
    private String invoice_number;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("purchase_invoice_value")
    @Expose
    private String purchase_invoice_value;

    @SerializedName("delivery_cost")
    @Expose
    private String delivery_cost;

    @SerializedName("tax")
    @Expose
    private String tax;

    @SerializedName("total")
    @Expose
    private String total;

    @SerializedName("store_lat")
    @Expose
    private String store_lat;

    @SerializedName("store_lng")
    @Expose
    private String store_lng;

    @SerializedName("store_address")
    @Expose
    private String store_address;

    @SerializedName("destination_lat")
    @Expose
    private String destination_lat;

    @SerializedName("destination_lng")
    @Expose
    private String destination_lng;

    @SerializedName("destination_address")
    @Expose
    private String destination_address;

    @SerializedName("client_id")
    @Expose
    private String client_id;

    @SerializedName("driver_id")
    @Expose
    private String driver_id;

    @SerializedName("note")
    @Expose
    private String note;

    @SerializedName("cancel_reason")
    @Expose
    private String cancel_reason;

    @SerializedName("cancel_reasons_id")
    @Expose
    private String cancel_reasons_id;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    @SerializedName("order_pic")
    @Expose
    private String order_pic;

    @SerializedName("order_pic_url")
    @Expose
    private String order_pic_url;

    @SerializedName("status_translation")
    @Expose
    private String status_translation;

    @SerializedName("client")
    @Expose
    private User client;

    @SerializedName("app_revenue")
    @Expose
    private String app_revenue;

    @SerializedName("driver_revenue")
    @Expose
    private String driver_revenue;

    @SerializedName("client_paid_invoice")
    @Expose
    private String client_paid_invoice;

    @SerializedName("attachments")
    @Expose
    private ArrayList<Attachment> attachmentArrays;

    public ArrayList<Attachment> getAttachmentArrays() {
        return attachmentArrays;
    }

    public String getClient_paid_invoice() {
        return client_paid_invoice;
    }

    public int getId() {
        return id;
    }

    public String getPlace_id() {
        return place_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public String getStatus() {
        return status;
    }

    public String getPurchase_invoice_value() {
        return purchase_invoice_value;
    }

    public String getDelivery_cost() {
        return delivery_cost;
    }

    public String getTax() {
        return tax;
    }

    public String getTotal() {
        return total;
    }

    public String getStore_lat() {
        return store_lat;
    }

    public String getStore_lng() {
        return store_lng;
    }

    public String getStore_address() {
        return store_address;
    }

    public String getDestination_lat() {
        return destination_lat;
    }

    public String getDestination_lng() {
        return destination_lng;
    }

    public String getDestination_address() {
        return destination_address;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public String getNote() {
        return note;
    }

    public String getCancel_reason() {
        return cancel_reason;
    }

    public String getCancel_reasons_id() {
        return cancel_reasons_id;
    }

    public String getOrder_pic() {
        return order_pic;
    }

    public String getOrder_pic_url() {
        return order_pic_url;
    }


    public String getStatus_translation() {
        return status_translation;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getApp_revenue() {
        return app_revenue;
    }

    public String getDriver_revenue() {
        return driver_revenue;
    }

    public User getClient() {
        return client;
    }

    public void setDelivery_cost(String delivery_cost) {
        this.delivery_cost = delivery_cost;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    @Override
    public String toString() {
        return "PublicOrder{" +
                "id=" + id +
                ", place_id='" + place_id + '\'' +
                ", store_name='" + store_name + '\'' +
                ", invoice_number='" + invoice_number + '\'' +
                ", status='" + status + '\'' +
                ", purchase_invoice_value='" + purchase_invoice_value + '\'' +
                ", delivery_cost='" + delivery_cost + '\'' +
                ", tax='" + tax + '\'' +
                ", total='" + total + '\'' +
                ", store_lat='" + store_lat + '\'' +
                ", store_lng='" + store_lng + '\'' +
                ", store_address='" + store_address + '\'' +
                ", destination_lat='" + destination_lat + '\'' +
                ", destination_lng='" + destination_lng + '\'' +
                ", destination_address='" + destination_address + '\'' +
                ", client_id='" + client_id + '\'' +
                ", driver_id='" + driver_id + '\'' +
                ", note='" + note + '\'' +
                ", cancel_reason='" + cancel_reason + '\'' +
                ", cancel_reasons_id='" + cancel_reasons_id + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", order_pic='" + order_pic + '\'' +
                ", order_pic_url='" + order_pic_url + '\'' +
                ", status_translation='" + status_translation + '\'' +
                ", client=" + client +
                ", app_revenue='" + app_revenue + '\'' +
                ", driver_revenue='" + driver_revenue + '\'' +
                ", client_paid_invoice='" + client_paid_invoice + '\'' +
                ", attachmentArrays=" + attachmentArrays +
                '}';
    }
}

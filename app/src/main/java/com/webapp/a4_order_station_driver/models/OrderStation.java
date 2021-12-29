package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderStation extends Order implements Serializable {

    @SerializedName("invoice_number")
    @Expose
    private String invoice_number;

    @SerializedName("status_translation")
    @Expose
    private String status_translation;

    @SerializedName("order_date")
    @Expose
    private String order_date;

    @SerializedName("type_of_receive")
    @Expose
    private String type_of_receive;

    @SerializedName("sub_total_1")
    @Expose
    private double sub_total_1;

    @SerializedName("discount")
    @Expose
    private double discount;

    @SerializedName("sub_total_2")
    @Expose
    private double sub_total_2;

    @SerializedName("tax")
    @Expose
    private double tax;

    @SerializedName("delivery")
    @Expose
    private double delivery;

    @SerializedName("total")
    @Expose
    private double total;

    @SerializedName("items")
    @Expose
    private ArrayList<OrderStationItem> orderItems;

    @SerializedName("store")
    @Expose
    private Store store;

    @SerializedName("customer_address")
    @Expose
    private Customer customer;

    @SerializedName("driver")
    @Expose
    private User driver;

    public String getInvoice_number() {
        return invoice_number;
    }

    public String getStatus_translation() {
        return status_translation;
    }

    public String getOrder_date() {
        return order_date;
    }

    public String getType_of_receive() {
        return type_of_receive;
    }

    public double getSub_total_1() {
        return sub_total_1;
    }

    public double getDiscount() {
        return discount;
    }

    public double getSub_total_2() {
        return sub_total_2;
    }

    public double getTax() {
        return tax;
    }

    public double getDelivery() {
        return delivery;
    }

    public double getTotal() {
        return total;
    }

    public ArrayList<OrderStationItem> getOrderItems() {
        return orderItems;
    }

    public Store getStore() {
        return store;
    }

    public Customer getCustomer() {
        return customer;
    }

    public User getDriver() {
        return driver;
    }

    @Override
    public String toString() {
        return "TestOrder{" +
                "id='" + getId() + "\'" +
                ", status='" + getStatus() + "\'" +
                ", invoice_number='" + invoice_number + '\'' +
                ", status_translation='" + status_translation + '\'' +
                ", order_date='" + order_date + '\'' +
                ", type_of_receive='" + type_of_receive + '\'' +
                ", sub_total_1=" + sub_total_1 +
                ", discount=" + discount +
                ", sub_total_2=" + sub_total_2 +
                ", tax=" + tax +
                ", delivery=" + delivery +
                ", total=" + total +
                ", orderItems=" + orderItems +
                ", store=" + store +
                ", customer=" + customer +
                ", driver=" + driver +
                '}';
    }
}

package com.webapp.mohammad_al_loh.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ongoing {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("driver_id")
    @Expose
    private int driver_id;

    @SerializedName("order_id")
    @Expose
    private int order_id;

    @SerializedName("order_cost")
    @Expose
    private double order_cost;

    @SerializedName("delivery_cost")
    @Expose
    private double delivery_cost;

    @SerializedName("total")
    @Expose
    private double total;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    @SerializedName("payment_type")
    @Expose
    private String payment_type;

    @SerializedName("invoice_number")
    @Expose
    private String invoice_number;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(int driver_id) {
        this.driver_id = driver_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public double getOrder_cost() {
        return order_cost;
    }

    public void setOrder_cost(double order_cost) {
        this.order_cost = order_cost;
    }

    public double getDelivery_cost() {
        return delivery_cost;
    }

    public void setDelivery_cost(double delivery_cost) {
        this.delivery_cost = delivery_cost;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
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

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    @Override
    public String toString() {
        return "Ongoing{" +
                "id=" + id +
                ", driver_id=" + driver_id +
                ", order_id=" + order_id +
                ", order_cost=" + order_cost +
                ", delivery_cost=" + delivery_cost +
                ", total=" + total +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", payment_type='" + payment_type + '\'' +
                ", invoice_number='" + invoice_number + '\'' +
                '}';
    }
}

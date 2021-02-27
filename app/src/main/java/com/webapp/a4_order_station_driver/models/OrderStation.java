package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderStation extends Order implements Serializable {

    @SerializedName("invoice_date")
    @Expose
    private String invoice_date;

    @SerializedName("invoice_number")
    @Expose
    private String invoice_number;

    @SerializedName("reference_number")
    @Expose
    private String reference_number;

    @SerializedName("paid_status")
    @Expose
    private String paid_status;

    @SerializedName("type_of_receive")
    @Expose
    private String type_of_receive;

    @SerializedName("sub_total_1")
    @Expose
    private String sub_total_1;

    @SerializedName("discount")
    @Expose
    private String discount;

    @SerializedName("sub_total_2")
    @Expose
    private String sub_total_2;

    @SerializedName("tax")
    @Expose
    private String tax;

    @SerializedName("delivery")
    @Expose
    private String delivery;

    @SerializedName("total")
    @Expose
    private String total;

    @SerializedName("receive")
    @Expose
    private String receive;

    @SerializedName("delivered_date")
    @Expose
    private String delivered_date;

    @SerializedName("coupon_id")
    @Expose
    private String coupon_id;

    @SerializedName("notes")
    @Expose
    private String notes;

    @SerializedName("user_id")
    @Expose
    private String user_id;

    @SerializedName("driver_id")
    @Expose
    private String driver_id;

    @SerializedName("created_at")
    @Expose
    private String created_at;

    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    @SerializedName("payment_transaction_id")
    @Expose
    private String payment_transaction_id;

    @SerializedName("shop_id")
    @Expose
    private String shop_id;

    @SerializedName("payment_type")
    @Expose
    private String payment_type;

    @SerializedName("destination_lat")
    @Expose
    private String destination_lat;

    @SerializedName("destination_lng")
    @Expose
    private String destination_lng;

    @SerializedName("destination_address")
    @Expose
    private String destination_address;

    @SerializedName("item_count")
    @Expose
    private String item_count;

    @SerializedName("created_timestamp")
    @Expose
    private long order_created_timestamp;

    @SerializedName("shop")
    @Expose
    private Shop shop;

    @SerializedName("client")
    @Expose
    private User user;

    @SerializedName("driver")
    @Expose
    private User driver;

    @SerializedName("order_items")
    @Expose
    private ArrayList<OrderItem> order_items;

    public String getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(String invoice_date) {
        this.invoice_date = invoice_date;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public String getReference_number() {
        return reference_number;
    }

    public void setReference_number(String reference_number) {
        this.reference_number = reference_number;
    }

    public String getPaid_status() {
        return paid_status;
    }

    public void setPaid_status(String paid_status) {
        this.paid_status = paid_status;
    }

    public String getType_of_receive() {
        return type_of_receive;
    }

    public void setType_of_receive(String type_of_receive) {
        this.type_of_receive = type_of_receive;
    }

    public String getSub_total_1() {
        return sub_total_1;
    }

    public void setSub_total_1(String sub_total_1) {
        this.sub_total_1 = sub_total_1;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getSub_total_2() {
        return sub_total_2;
    }

    public void setSub_total_2(String sub_total_2) {
        this.sub_total_2 = sub_total_2;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getReceive() {
        return receive;
    }

    public void setReceive(String receive) {
        this.receive = receive;
    }

    public String getDelivered_date() {
        return delivered_date;
    }

    public void setDelivered_date(String delivered_date) {
        this.delivered_date = delivered_date;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
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

    public String getPayment_transaction_id() {
        return payment_transaction_id;
    }

    public void setPayment_transaction_id(String payment_transaction_id) {
        this.payment_transaction_id = payment_transaction_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public double getDestination_lat() {
        return Double.parseDouble(destination_lat);
    }

    public void setDestination_lat(String destination_lat) {
        this.destination_lat = destination_lat;
    }

    public double getDestination_lng() {
        return Double.parseDouble(destination_lng);
    }

    public void setDestination_lng(String destination_lng) {
        this.destination_lng = destination_lng;
    }

    public String getDestination_address() {
        return destination_address;
    }

    public void setDestination_address(String destination_address) {
        this.destination_address = destination_address;
    }

    public String getItem_count() {
        return item_count;
    }

    public void setItem_count(String item_count) {
        this.item_count = item_count;
    }


    public long getOrder_created_timestamp() {
        return order_created_timestamp;
    }

    public void setOrder_created_timestamp(long order_created_timestamp) {
        this.order_created_timestamp = order_created_timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getDriver() {
        return driver;
    }

    public ArrayList<OrderItem> getOrder_items() {
        return order_items;
    }

    public void setOrder_items(ArrayList<OrderItem> order_items) {
        this.order_items = order_items;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    @Override
    public String toString() {
        return "OrderStation{" +
                ", invoice_date='" + invoice_date + '\'' +
                ", invoice_number='" + invoice_number + '\'' +
                ", reference_number='" + reference_number + '\'' +
                ", paid_status='" + paid_status + '\'' +
                ", type_of_receive='" + type_of_receive + '\'' +
                ", sub_total_1='" + sub_total_1 + '\'' +
                ", discount='" + discount + '\'' +
                ", sub_total_2='" + sub_total_2 + '\'' +
                ", tax='" + tax + '\'' +
                ", delivery='" + delivery + '\'' +
                ", total='" + total + '\'' +
                ", receive='" + receive + '\'' +
                ", delivered_date='" + delivered_date + '\'' +
                ", coupon_id='" + coupon_id + '\'' +
                ", notes='" + notes + '\'' +
                ", user_id='" + user_id + '\'' +
                ", driver_id='" + driver_id + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", payment_transaction_id='" + payment_transaction_id + '\'' +
                ", shop_id='" + shop_id + '\'' +
                ", payment_type='" + payment_type + '\'' +
                ", destination_lat='" + destination_lat + '\'' +
                ", destination_lng='" + destination_lng + '\'' +
                ", destination_address='" + destination_address + '\'' +
                ", item_count='" + item_count + '\'' +
                ", order_created_timestamp=" + order_created_timestamp +
                ", shop=" + shop +
                ", user=" + user +
                ", order_items=" + order_items +
                '}';
    }
}

package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RatingList {

    @SerializedName("current_page")
    @Expose
    private int current_page;

    @SerializedName("data")
    @Expose
    private ArrayList<RatingData> data;

    @SerializedName("first_page_url")
    @Expose
    private String first_page_url;

    @SerializedName("from")
    @Expose
    private String from;

    @SerializedName("last_page")
    @Expose
    private int last_page;

    @SerializedName("last_page_url")
    @Expose
    private String last_page_url;

    @SerializedName("next_page_url")
    @Expose
    private String next_page_url;

    @SerializedName("path")
    @Expose
    private String path;

    @SerializedName("per_page")
    @Expose
    private int per_page;

    @SerializedName("prev_page_url")
    @Expose
    private String prev_page_url;

    @SerializedName("to")
    @Expose
    private String to;

    @SerializedName("total")
    @Expose
    private float total;

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public ArrayList<RatingData> getData() {
        return data;
    }

    public void setData(ArrayList<RatingData> data) {
        this.data = data;
    }

    public String getFirst_page_url() {
        return first_page_url;
    }

    public void setFirst_page_url(String first_page_url) {
        this.first_page_url = first_page_url;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public String getLast_page_url() {
        return last_page_url;
    }

    public void setLast_page_url(String last_page_url) {
        this.last_page_url = last_page_url;
    }

    public String getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(String next_page_url) {
        this.next_page_url = next_page_url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public String getPrev_page_url() {
        return prev_page_url;
    }

    public void setPrev_page_url(String prev_page_url) {
        this.prev_page_url = prev_page_url;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "RatingList{" +
                "current_page=" + current_page +
                ", data=" + data +
                ", first_page_url='" + first_page_url + '\'' +
                ", from='" + from + '\'' +
                ", last_page=" + last_page +
                ", last_page_url='" + last_page_url + '\'' +
                ", next_page_url='" + next_page_url + '\'' +
                ", path='" + path + '\'' +
                ", per_page=" + per_page +
                ", prev_page_url='" + prev_page_url + '\'' +
                ", to='" + to + '\'' +
                ", total=" + total +
                '}';
    }
}

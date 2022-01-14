package com.webapp.mohammad_al_loh.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PublicOrderList {

    @SerializedName("current_page")
    @Expose
    private int current_page;

    @SerializedName("data")
    @Expose
    ArrayList<PublicOrder> data;

    @SerializedName("first_page_url")
    @Expose
    private String first_page_url;

    @SerializedName("from")
    @Expose
    private int from;

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
    private int to;

    @SerializedName("total")
    @Expose
    private int total;

    public int getCurrent_page() {
        return current_page;
    }

    public ArrayList<PublicOrder> getData() {
        return data;
    }

    public String getFirst_page_url() {
        return first_page_url;
    }

    public int getFrom() {
        return from;
    }

    public int getLast_page() {
        return last_page;
    }

    public String getLast_page_url() {
        return last_page_url;
    }

    public String getNext_page_url() {
        return next_page_url;
    }

    public String getPath() {
        return path;
    }

    public int getPer_page() {
        return per_page;
    }

    public String getPrev_page_url() {
        return prev_page_url;
    }

    public int getTo() {
        return to;
    }

    public int getTotal() {
        return total;
    }
}

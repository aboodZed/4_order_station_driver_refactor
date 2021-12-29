package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Orders extends Result<ArrayList<OrderStation>>{

    @SerializedName("has_more_page")
    @Expose
    private boolean more_page;

    @SerializedName("in_progress_order")
    @Expose
    private OrderStation in_progress_order;

    public boolean isMore_page() {
        return more_page;
    }

    public OrderStation getIn_progress_order() {
        return in_progress_order;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "more_page=" + more_page +
                ", in_progress_order=" + in_progress_order +
                '}';
    }
}


package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Rating extends Result<ArrayList<RatingData>> {

    @SerializedName("rate")
    @Expose
    private String rate;

    public String getRate() {
        return rate;
    }

    @Override
    public String toString() {
        return "TestRating{" +
                "rate='" + rate + '\'' +
                '}';
    }
}

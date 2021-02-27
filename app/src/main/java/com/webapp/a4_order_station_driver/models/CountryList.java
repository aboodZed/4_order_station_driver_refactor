package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CountryList {

    @SerializedName("CountryList")
    @Expose
    private ArrayList<Country> countries;

    public ArrayList<Country> getCountries() {
        return countries;
    }

    @Override
    public String toString() {
        return "CountryList{" +
                "CountryList=" + countries +
                '}';
    }
}

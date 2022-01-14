package com.webapp.mohammad_al_loh.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CountryList {

    @SerializedName("countries")
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

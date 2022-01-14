package com.webapp.mohammad_al_loh.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NeighborhoodList {

    @SerializedName("cities")
    @Expose
    private ArrayList<Neighborhood> neighborhoods;

    public ArrayList<Neighborhood> getNeighborhoods() {
        return neighborhoods;
    }

    public void setNeighborhoods(ArrayList<Neighborhood> neighborhoods) {
        this.neighborhoods = neighborhoods;
    }

    @Override
    public String toString() {
        return "NeighborhoodList{" +
                "neighborhoods=" + neighborhoods +
                '}';
    }
}

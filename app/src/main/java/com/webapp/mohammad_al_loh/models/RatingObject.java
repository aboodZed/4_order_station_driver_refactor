package com.webapp.mohammad_al_loh.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RatingObject {

    @SerializedName("ratings")
    @Expose
    private RatingList ratings;

    public RatingList getRatings() {
        return ratings;
    }

    @Override
    public String toString() {
        return "RatingObject{" +
                "ratings=" + ratings +
                '}';
    }
}

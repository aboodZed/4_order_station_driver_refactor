package com.webapp.mohammad_al_loh.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StationWallet extends Message{

    @SerializedName("wallets")
    @Expose
    private ArrayList<Ongoing> ongoings;

    @SerializedName("total_wallet")
    @Expose
    private double wallet;

    public ArrayList<Ongoing> getOngoings() {
        return ongoings;
    }

    public double getWallet() {
        return wallet;
    }
}

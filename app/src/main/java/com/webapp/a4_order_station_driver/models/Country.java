package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Country implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("currency_code")
    @Expose
    private String currency_code;

    @SerializedName("name_en")
    @Expose
    private String name_en;

    @SerializedName("name_ar")
    @Expose
    private String name_ar;

    @SerializedName("phone_code")
    @Expose
    private String phone_code;

    @SerializedName("phone_length")
    @Expose
    private int phone_length;

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public String getName_en() {
        return name_en;
    }

    public String getName_ar() {
        return name_ar;
    }

    public int getPhone_length() {
        return phone_length;
    }

    public String getPhone_code() {
        return phone_code;
    }

    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", currency_code='" + currency_code + '\'' +
                ", name_en='" + name_en + '\'' +
                ", name_ar='" + name_ar + '\'' +
                ", phone_code='" + phone_code + '\'' +
                ", phone_length=" + phone_length +
                '}';
    }
}

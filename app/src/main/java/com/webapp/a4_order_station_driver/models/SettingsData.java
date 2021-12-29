package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SettingsData implements Serializable {

    @SerializedName("facebook_link")
    @Expose
    private String facebook_link;

    @SerializedName("instagram_link")
    @Expose
    private String instagram_link;

    @SerializedName("linkedin_link")
    @Expose
    private String linkedin_link;

    @SerializedName("twitter_link")
    @Expose
    private String twitter_link;

    @SerializedName("mobile")
    @Expose
    private String mobile;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("currency_code")
    @Expose
    private String currency_code;

    @SerializedName("phone_code")
    @Expose
    private String phone_code;

    @SerializedName("phone_length")
    @Expose
    private int phone_length;

    @SerializedName("country_id")
    @Expose
    private int country_id;

    public String getFacebook_link() {
        return facebook_link;
    }

    public String getInstagram_link() {
        return instagram_link;
    }

    public String getLinkedin_link() {
        return linkedin_link;
    }

    public String getTwitter_link() {
        return twitter_link;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public int getPhone_length() {
        return phone_length;
    }

    public int getCountry_id() {
        return country_id;
    }

    @Override
    public String toString() {
        return "SettingsData{" +
                "facebook_link='" + facebook_link + '\'' +
                ", instagram_link='" + instagram_link + '\'' +
                ", linkedin_link='" + linkedin_link + '\'' +
                ", twitter_link='" + twitter_link + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", currency_code='" + currency_code + '\'' +
                ", phone_code='" + phone_code + '\'' +
                ", phone_length=" + phone_length +
                ", country_id=" + country_id +
                '}';
    }
}

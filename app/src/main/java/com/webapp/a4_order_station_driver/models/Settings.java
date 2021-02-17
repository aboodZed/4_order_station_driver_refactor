package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Settings implements Serializable {
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

    @SerializedName("privacy")
    @Expose
    private String privacy;

    @SerializedName("mobile")
    @Expose
    private String mobile;

    @SerializedName("email")
    @Expose
    private String email;

    public String getFacebook_link() {
        return facebook_link;
    }

    public void setFacebook_link(String facebook_link) {
        this.facebook_link = facebook_link;
    }

    public String getInstagram_link() {
        return instagram_link;
    }

    public void setInstagram_link(String instagram_link) {
        this.instagram_link = instagram_link;
    }

    public String getLinkedin_link() {
        return linkedin_link;
    }

    public void setLinkedin_link(String linkedin_link) {
        this.linkedin_link = linkedin_link;
    }

    public String getTwitter_link() {
        return twitter_link;
    }

    public void setTwitter_link(String twitter_link) {
        this.twitter_link = twitter_link;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "facebook_link='" + facebook_link + '\'' +
                ", instagram_link='" + instagram_link + '\'' +
                ", linkedin_link='" + linkedin_link + '\'' +
                ", twitter_link='" + twitter_link + '\'' +
                ", privacy='" + privacy + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

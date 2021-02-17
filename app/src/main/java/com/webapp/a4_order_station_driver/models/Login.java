package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Login extends Message implements Serializable {
    @SerializedName("access_token")
    @Expose
    private String access_token;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("expires_at")
    @Expose
    private String expires_at;

    public String getAccess_token() {
        return "Bearer " + access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getExpires_at() {
        return expires_at;
    }

    public void setExpires_at(String expires_at) {
        this.expires_at = expires_at;
    }

    @Override
    public String toString() {
        return "Login{" +
                "access_token='" + access_token + '\'' +
                ", user=" + user +
                ", expires_at='" + expires_at + '\'' +
                '}';
    }
}

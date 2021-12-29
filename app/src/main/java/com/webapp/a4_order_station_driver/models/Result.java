package com.webapp.a4_order_station_driver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result<T> extends Message {

    @SerializedName(value = "data", alternate = {"app_settings", "wallets", "rate", "cities"})
    @Expose
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result {" +
                "success=" + isSuccess() +
                ", message='" + getMessage() + '\'' +
                ", data=" + data +
                '}';
    }
}

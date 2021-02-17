package com.webapp.a4_order_station_driver.utils.listeners;

public interface RequestListener<T> {

    void onSuccess(T t, String msg);
    void onError(String msg);
    void onFail(String msg);
}

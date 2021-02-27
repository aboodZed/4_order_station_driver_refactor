package com.webapp.a4_order_station_driver.utils.listeners;

public interface DialogView<T> {

    void setData(T t);
    void showDialog(String s);
    void hideDialog();
}

package com.webapp.mohammad_al_loh.utils.listeners;

public interface DialogView<T> {

    void setData(T t);
    void showDialog(String s);
    void hideDialog();
}

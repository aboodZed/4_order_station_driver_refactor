package com.webapp.mohammad_al_loh.utils.listeners;

public interface RequestListener<T> {

    void onSuccess(T t, String msg);
    void onError(String msg);
    void onFail(String msg);
}

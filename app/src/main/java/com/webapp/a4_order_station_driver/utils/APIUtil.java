package com.webapp.a4_order_station_driver.utils;

import android.app.Activity;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIUtil<T> {

    private Activity activity;

    public APIUtil(Activity activity) {
        this.activity = activity;
    }

    public void getData(Call<T> call, RequestListener<T> listener) {
        if (ToolUtil.checkTheInternet()) {
            call.enqueue(new Callback<T>() {
                @Override
                public void onResponse(Call<T> call, Response<T> response) {
                    if (activity != null) {
                        if (response.isSuccessful() && response.body() != null) {
                            listener.onSuccess(response.body(), response.message());
                        } else {
                            listener.onError(ToolUtil.showError(activity
                                    , response.errorBody()));
                        }
                    }
                }

                @Override
                public void onFailure(Call<T> call, Throwable t) {
                    t.printStackTrace();
                    listener.onFail(t.getLocalizedMessage());
                }
            });
        } else {
            listener.onFail(activity.getString(R.string.no_connection));
        }
    }
}

package com.webapp.a4_order_station_driver.utils;

import android.content.Context;
import android.util.Log;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIUtil<T> {

    private Context context;

    public APIUtil(Context context) {
        this.context = context;
    }

    public void getData(Call<T> call, RequestListener<T> listener) {
        if (ToolUtil.checkTheInternet()) {
            call.enqueue(new Callback<T>() {
                @Override
                public void onResponse(Call<T> call, Response<T> response) {
                    if (context != null) {
                        if (response.isSuccessful() && response.body() != null) {
                            listener.onSuccess(response.body(), response.message());
                        } else {
                            listener.onError(ToolUtil.showError(context
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
            listener.onFail(context.getString(R.string.no_connection));
        }
    }
}

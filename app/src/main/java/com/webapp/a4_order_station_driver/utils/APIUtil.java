package com.webapp.a4_order_station_driver.utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIUtil<T> {

    private final Context context;

    public APIUtil(Context context) {
        this.context = context;
    }

    public void getData(Call<T> call, RequestListener<T> listener) {
        if (ToolUtil.checkTheInternet()) {
            call.enqueue(new Callback<T>() {
                @Override
                public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
                    if (context != null) {
                        if (response.isSuccessful() && response.body() != null) {
                            Log.e(getClass().getName() + " : apiRequest", response.body().toString());
                            listener.onSuccess(response.body(), response.message());
                        } else {
                            Log.e(getClass().getName() + " : errorRequest",
                                    ToolUtil.showError(context, response.errorBody()));
                            listener.onError(ToolUtil.showError(context, response.errorBody()));

                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    Log.e(getClass().getName() + " : request Failure",
                            Objects.requireNonNull(t.getLocalizedMessage()));
                    listener.onFail(t.getLocalizedMessage());

                }
            });
        } else {
            listener.onFail(context.getString(R.string.no_connection));
        }
    }
}

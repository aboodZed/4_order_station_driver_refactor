package com.webapp.mohammad_al_loh.utils;

import android.content.Context;
import android.util.Log;

import com.webapp.mohammad_al_loh.R;
import com.webapp.mohammad_al_loh.utils.listeners.RequestListener;

import java.util.Objects;

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
                            Log.e(getClass().getName() + " : request success", response.body().toString());
                            listener.onSuccess(response.body(), response.message());
                        } else {
                            Log.e(getClass().getName() + " : request Error",
                                    ToolUtil.showError(context, response.errorBody()));
                            listener.onError(ToolUtil.showError(context, response.errorBody()));
                        }
                    }
                }

                @Override
                public void onFailure(Call<T> call, Throwable t) {
                    Log.e(getClass().getName() + " : request Failure",
                            Objects.requireNonNull(t.getLocalizedMessage()));
                    t.printStackTrace();
                    listener.onFail(t.getLocalizedMessage());
                }
            });
        } else {
            listener.onFail(context.getString(R.string.no_connection));
        }
    }
}

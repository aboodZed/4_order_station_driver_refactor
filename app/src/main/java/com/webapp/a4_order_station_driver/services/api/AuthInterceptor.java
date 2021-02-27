package com.webapp.a4_order_station_driver.services.api;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.webapp.a4_order_station_driver.feature.login.LoginActivity;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.location.tracking.GPSTracking;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        Response response = chain.proceed(builder.build());
        if (response.code() == 401) {
            AppController.getInstance().getAppSettingsPreferences().clean();
            if (GPSTracking.getInstance(context) != null)
                GPSTracking.getInstance(context).removeMyUpdates();
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        return response;
    }
}
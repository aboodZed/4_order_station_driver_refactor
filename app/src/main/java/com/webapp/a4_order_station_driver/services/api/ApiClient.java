package com.webapp.a4_order_station_driver.services.api;

import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit = null;

    public static Retrofit getRetrofit() {
        OkHttpClient client = getClient();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(AppContent.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    private static OkHttpClient getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .addNetworkInterceptor(new AddHeaderInterceptor())
                .addInterceptor(new AuthInterceptor(AppController.getInstance()))
                .connectTimeout(10, TimeUnit.MINUTES)
                .readTimeout(10, TimeUnit.MINUTES)
                .retryOnConnectionFailure(true)
                .build();
        client.connectionPool().evictAll();
        return client;
    }

    private static class AddHeaderInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            builder.header("X-Requested-With", "XMLHttpRequest");
            //builder.header("Content-Type", "application/json");
            builder.header("Accept", "application/json");
            builder.header("Connection", "close");
            builder.header("Authorization", AppController.getInstance()
                    .getAppSettingsPreferences().getToken());
            /*if (AppController.getInstance().getAppSettingsPreferences().getUser() != null) {
                builder.header("Authorization", AppController.getInstance()
                        .getAppSettingsPreferences().getUser().getToken());
            } else {
                builder.header("Authorization", "");
            }*/
            /*if (AppController.getInstance().getAppSettingsPreferences().getUser().getCountry() != null) {
                builder.header("country_id", AppController.getInstance()
                        .getAppSettingsPreferences().getUser().getCountry().getId() + "");
            } else {
                builder.header("country_id", "");
            }*/
            builder.header("X-Local", AppController.getInstance().getAppSettingsPreferences().getAppLanguage());
            return chain.proceed(builder.build());
        }
    }
}
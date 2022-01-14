package com.webapp.mohammad_al_loh.services.api;

import com.webapp.mohammad_al_loh.utils.AppContent;
import com.webapp.mohammad_al_loh.utils.AppController;

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
            builder.header("Content-Type", "application/json");
            builder.header("Connection", "close");
            if (AppController.getInstance().getAppSettingsPreferences().getLogin() != null) {
                builder.header("Authorization", AppController.getInstance().getAppSettingsPreferences().getLogin().getAccess_token());
            } else {
                builder.header("Authorization", "");
            }
            if (AppController.getInstance().getAppSettingsPreferences().getCountry() != null) {
                builder.header("country_id", AppController.getInstance().getAppSettingsPreferences().getCountry().getId() + "");
            } else {
                builder.header("country_id", "");
            }
            builder.header("X-Local", AppController.getInstance().getAppSettingsPreferences().getAppLanguage());
            return chain.proceed(builder.build());
        }
    }
}
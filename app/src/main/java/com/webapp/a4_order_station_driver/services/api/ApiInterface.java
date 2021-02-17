package com.webapp.a4_order_station_driver.services.api;

import com.webapp.a4_order_station_driver.models.Arrays;
import com.webapp.a4_order_station_driver.models.Login;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.models.OrderStation;
import com.webapp.a4_order_station_driver.models.Privacy;
import com.webapp.a4_order_station_driver.models.PublicArrays;
import com.webapp.a4_order_station_driver.models.PublicWallet;
import com.webapp.a4_order_station_driver.models.ResetCode;
import com.webapp.a4_order_station_driver.models.StationWallet;
import com.webapp.a4_order_station_driver.models.User;
import com.webapp.a4_order_station_driver.models.VerifyCode;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ApiInterface {

    @POST("auth/register")
    Call<Login> SignUp(@Body User user);

    @POST("auth/signUpDeliveryAppStep2")
    Call<User> signUp2(@Body User user);

    @POST("auth/driverLogin")
    Call<Login> login(@QueryMap HashMap<String, String> params);

    @POST("auth/forgetPasswordRequest")
    Call<ResetCode> forgetPassword(@Query("mobile") String mobile);

    @POST("auth/forgetPasswordVerify")
    Call<VerifyCode> verifyCode(@Body ResetCode resetCode);

    @POST("auth/resetPassword")
    Call<Message> resetPassword(@QueryMap HashMap<String, String> params);

    @POST("delivery/isOnline")
    Call<Message> isOnline(@Query("is_online") String isOnline);

    @GET("auth/user")
    Call<User> getUserData();

    @POST("auth/updateProfile")
    Call<User> updateProfile(@Body User user);

    @GET("delivery/walletDetails")
    Call<StationWallet> getWalletDetails();

    @GET("notifications")
    Call<Arrays> getNotifications();

    @GET("delivery/myOrders")
    Call<Arrays> getOrders();

    @GET("delivery/order/{id}")
    Call<OrderStation> getOrderById(@Path("id") int id);

    @GET("shop/{id}")
    Call<Arrays> getShop(@Path("id") int id);

    @POST("delivery/pickupOrder")
    Call<Message> pickupOrder(@Query("order_id") int id);

    @POST("delivery/markOrderDelivered")
    Call<Message> deliveryOrder(@Query("order_id") int id);

    @GET
    Call<Arrays> getRating(@Url String url);

    @POST("contact")
    Call<Message> sendMessage(@QueryMap HashMap<String, String> params);

    @GET
    Call<Arrays> getSettings(@Url String url);

    @GET("clientPrivacy")
    Call<Privacy> getPrivacy();

    @POST("fcmToken")
    Call<Message> fcmToken(@Query("token") String token);

    @GET
    Call<PublicArrays> getPublicOrders(@Url String url);

    @GET("public/order/client/order/{id}")
    Call<PublicArrays> getPublicOrder(@Path("id") int id);

    @POST("public/order/driver/pickupOrder")
    Call<Message> pickupPublicOrder(@Query("order_id") int id);

    @POST("public/order/driver/sendInvoiceValue")
    Call<Message> sendInvoiceValue(@Query("order_id") int id, @Query("invoice_value") double value);

    @POST("public/order/driver/changeToOnTheWay")
    Call<Message> changeToONTheWay(@Query("order_id") int id);

    @POST("public/order/driver/deliveredOrder")
    Call<Message> deliveredPublicOrder(@Query("order_id") int id);

    @GET("public/order/driver/wallet")
    Call<PublicWallet> getPublicWallet();

    @POST("public/order/driver/cancelOrder")
    Call<Message> cancelOrder(@Query("order_id") int id);

    @POST("public/order/client/sendChatNotify")
    Call<Message> sendMessageNotification(@QueryMap HashMap<String, String> params);

    @POST("delivery/updateDriverLocation")
    Call<Message> updateLocation(@QueryMap HashMap<String, String> params);

    @GET("countries-list")
    Call<Arrays> getCountries();
}
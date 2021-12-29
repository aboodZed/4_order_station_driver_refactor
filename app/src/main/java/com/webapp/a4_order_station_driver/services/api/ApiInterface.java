package com.webapp.a4_order_station_driver.services.api;

import com.webapp.a4_order_station_driver.models.AppSettings;
import com.webapp.a4_order_station_driver.models.City;
import com.webapp.a4_order_station_driver.models.Country;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.models.NotificationList;
import com.webapp.a4_order_station_driver.models.Orders;
import com.webapp.a4_order_station_driver.models.PhotoObject;
import com.webapp.a4_order_station_driver.models.PublicOrderObject;
import com.webapp.a4_order_station_driver.models.ResetCode;
import com.webapp.a4_order_station_driver.models.Result;
import com.webapp.a4_order_station_driver.models.StationWallet;
import com.webapp.a4_order_station_driver.models.OrderStation;
import com.webapp.a4_order_station_driver.models.Rating;
import com.webapp.a4_order_station_driver.models.User;
import com.webapp.a4_order_station_driver.models.VerifyCode;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ApiInterface {

    //register
    @POST("driver/auth/register")
    Call<Result<User>> SignUp(@QueryMap HashMap<String, String> params);

    @POST("driver/auth/complete-register")
    Call<Result<User>> signUpStepTwo(@QueryMap HashMap<String, String> params);

    //login
    @POST("driver/auth/login")
    Call<Result<User>> login(@QueryMap HashMap<String, String> params);

    //online
    @POST("driver/profile/change-online-status")
    Call<Message> isOnline(@Query("is_online") boolean isOnline);

    //fcm
    @POST("driver/profile/update-fcm-token")
    Call<Message> fcmToken(@Query("fcm_token") String fcmToken);

    //user data
    @GET("driver/profile/my-data")
    Call<Result<User>> getUserData();

    @POST("driver/profile/update")
    Call<Result<User>> updateProfile(@QueryMap HashMap<String, String> params);

    //change password
    @POST("driver/profile/change-password")
    Call<Result<User>> changePassword(@QueryMap HashMap<String, String> params);

    //reset password
    @POST("driver/auth/forgetPasswordRequest")
    Call<ResetCode> forgetPassword(@QueryMap HashMap<String, String> params);

    @POST("driver/auth/forgetPasswordVerify")
    Call<VerifyCode> verifyCode(@Body ResetCode resetCode);

    @POST("driver/auth/resetPassword")
    Call<Message> resetPassword(@QueryMap HashMap<String, String> params);

    //wallet
    @GET("driver/wallet/details")
    Call<StationWallet> getWalletDetails();

    //notification
    @GET("driver/notifications")
    Call<NotificationList> getNotifications();

    //orders
    @GET("driver/orders")
    Call<Orders> getOrders();

    //4OrderStation
    @GET("driver/orders/details/{id}")
    Call<Result<OrderStation>> getOrderById(@Path("id") int id);

    @POST("driver/orders/pickupOrder")
    Call<Message> pickupOrder(@Query("order_id") int id);

    @POST("driver/orders/markOrderDelivered")
    Call<Message> deliveryOrder(@Query("order_id") int id);

 /*
    @GET("v2/shop/{id}")
    Call<ShopObject> getShop(@Path("id") int id);

    @GET
    Call<PublicOrderListObject> getPublicOrders(@Url String url);
*/
    @GET("driver/public/orders/details/{id}")
    Call<PublicOrderObject> getPublicOrder(@Path("id") int id);

    @POST("driver/public/orders/pickup")
    Call<Message> pickupPublicOrder(@Query("order_id") int id);

    @POST("driver/public/orders/send-invoice-value")
    Call<Message> sendInvoiceValue(@Query("order_id") int id, @Query("invoice_value") double value);

    @POST("driver/public/orders/change-to-on-the-way")
    Call<Message> changeToONTheWay(@Query("order_id") int id);

    @POST("driver/public/orders/change-to-delivered")
    Call<Message> deliveredPublicOrder(@Query("order_id") int id);
/*
    @GET("v2/driver-public-wallet")
    Call<PublicWallet> getPublicWallet();
*/
    @POST("driver/public/orders/cancel-order")
    Call<Message> cancelOrder(@Query("order_id") int id);

    @POST("driver/chat-notify")
    Call<Message> sendMessageNotification(@QueryMap HashMap<String, String> params);



    //test
    @POST("v2/delivery/updateDriverLocation")
    Call<Message> updateLocation(@QueryMap HashMap<String, String> params);
    //test




    @GET
    Call<Rating> getRating(@Url String url);

    @POST("driver/contact-us")
    Call<Message> sendMessage(@QueryMap HashMap<String, String> params);

    @GET("driver/app-settings")
    Call<AppSettings> getSettings();
/*
    @GET("driverPrivacy")
    Call<Privacy> getPrivacy();
*/
    @GET("driver/countries-list")
    Call<Result<ArrayList<Country>>> getCountries();

    @GET("cities/{id}")
    Call<Result<ArrayList<City>>> getCities(@Path("id") int id);

    //upload image
    @Multipart
    @POST("upload-image")
    Call<PhotoObject> uploadImage(@Part MultipartBody.Part avatar);
}
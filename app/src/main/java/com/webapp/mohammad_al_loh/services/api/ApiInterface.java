package com.webapp.mohammad_al_loh.services.api;

import com.webapp.mohammad_al_loh.models.CountryList;
import com.webapp.mohammad_al_loh.models.Login;
import com.webapp.mohammad_al_loh.models.Message;
import com.webapp.mohammad_al_loh.models.NeighborhoodList;
import com.webapp.mohammad_al_loh.models.NotificationList;
import com.webapp.mohammad_al_loh.models.OrderStation;
import com.webapp.mohammad_al_loh.models.OrderStationList;
import com.webapp.mohammad_al_loh.models.Privacy;
import com.webapp.mohammad_al_loh.models.PublicOrderListObject;
import com.webapp.mohammad_al_loh.models.PublicOrderObject;
import com.webapp.mohammad_al_loh.models.PublicWallet;
import com.webapp.mohammad_al_loh.models.RatingObject;
import com.webapp.mohammad_al_loh.models.ResetCode;
import com.webapp.mohammad_al_loh.models.ResultSettings;
import com.webapp.mohammad_al_loh.models.ResultUser;
import com.webapp.mohammad_al_loh.models.ShopObject;
import com.webapp.mohammad_al_loh.models.StationWallet;
import com.webapp.mohammad_al_loh.models.User;
import com.webapp.mohammad_al_loh.models.VerifyCode;

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
    Call<ResetCode> forgetPassword(@QueryMap HashMap<String, String> params);

    @POST("auth/forgetPasswordVerify")
    Call<VerifyCode> verifyCode(@Body ResetCode resetCode);

    @POST("auth/resetPassword")
    Call<Message> resetPassword(@QueryMap HashMap<String, String> params);

    @POST("delivery/isOnline")
    Call<Message> isOnline(@Query("is_online") String isOnline);

    @GET("auth/user")
    Call<ResultUser> getUserData();

    @POST("auth/updateProfile")
    Call<User> updateProfile(@Body User user);

    @GET("delivery/walletDetails")
    Call<StationWallet> getWalletDetails();

    @GET("notifications")
    Call<NotificationList> getNotifications();

    @GET("delivery/myOrders")
    Call<OrderStationList> getOrdersStation();

    @GET("delivery/order/{id}")
    Call<OrderStation> getOrderById(@Path("id") int id);

    @GET("shop/{id}")
    Call<ShopObject> getShop(@Path("id") int id);

    @POST("delivery/pickupOrder")
    Call<Message> pickupOrder(@Query("order_id") int id);

    @POST("delivery/markOrderDelivered")
    Call<Message> deliveryOrder(@Query("order_id") int id);

    @GET
    Call<RatingObject> getRating(@Url String url);

    @POST("contact")
    Call<Message> sendMessage(@QueryMap HashMap<String, String> params);

    @GET
    Call<ResultSettings> getSettings(@Url String url);

    @GET("clientPrivacy")
    Call<Privacy> getPrivacy();

    @POST("fcmToken")
    Call<Message> fcmToken(@Query("token") String token);

    @GET
    Call<PublicOrderListObject> getPublicOrders(@Url String url);

    @GET("general/order/client/order/{id}")
    Call<PublicOrderObject> getPublicOrder(@Path("id") int id);

    @POST("general/order/driver/pickupOrder")
    Call<Message> pickupPublicOrder(@Query("order_id") int id);

    @POST("general/order/driver/sendInvoiceValue")
    Call<Message> sendInvoiceValue(@Query("order_id") int id, @Query("invoice_value") double value);

    @POST("general/order/driver/changeToOnTheWay")
    Call<Message> changeToONTheWay(@Query("order_id") int id);

    @POST("general/order/driver/deliveredOrder")
    Call<Message> deliveredPublicOrder(@Query("order_id") int id);

    @GET("driver-public-wallet")
    Call<PublicWallet> getPublicWallet();

    @POST("general/order/driver/cancelOrder")
    Call<Message> cancelOrder(@Query("order_id") int id);

    @POST("general/order/client/sendChatNotify")
    Call<Message> sendMessageNotification(@QueryMap HashMap<String, String> params);

    @POST("delivery/updateDriverLocation")
    Call<Message> updateLocation(@QueryMap HashMap<String, String> params);

    @GET("countries-list")
    Call<CountryList> getCountries();

    @GET("cities/{id}")
    Call<NeighborhoodList> getNeighborhood(@Path("id") int id);
}
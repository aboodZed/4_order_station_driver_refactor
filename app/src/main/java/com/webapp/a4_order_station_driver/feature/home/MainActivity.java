package com.webapp.a4_order_station_driver.feature.home;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.feature.home.editProfile.EditProfileFragment;
import com.webapp.a4_order_station_driver.models.Login;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.models.OrderStation;
import com.webapp.a4_order_station_driver.models.PublicArrays;
import com.webapp.a4_order_station_driver.models.PublicOrder;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.dialogs.ChatFragment;
import com.webapp.a4_order_station_driver.utils.dialogs.NewOrderDialog;
import com.webapp.a4_order_station_driver.utils.dialogs.NewPublicOrderDialog;
import com.webapp.a4_order_station_driver.utils.dialogs.PublicChatFragment;
import com.webapp.a4_order_station_driver.utils.location.GPSTracking;
import com.webapp.a4_order_station_driver.utils.view.NavigationView;
import com.webapp.a4_order_station_driver.utils.view.Tracking;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView,
        Tracking, OrderViewFragment.Listener, NewOrderFragment.Listener, NewPublicOrderFragment.Listener {

    public static final int page = 200;

    @BindView(R.id.linearLayout) RelativeLayout linearLayout;
    @BindView(R.id.sc_appear) SwitchCompat scAppear;
    @BindView(R.id.iv_notification) ImageView ivNotification;
    @BindView(R.id.fragment_container) FrameLayout fragmentContainer;
    @BindView(R.id.ll_home) LinearLayout llHome;
    @BindView(R.id.iv_ic_home) ImageView ivIcHome;
    @BindView(R.id.tv_text_home) TextView tvTextHome;
    @BindView(R.id.ll_wallet) LinearLayout llWallet;
    @BindView(R.id.iv_ic_wallet) ImageView ivIcWallet;
    @BindView(R.id.tv_text_wallet) TextView tvTextWallet;
    @BindView(R.id.ll_orders) LinearLayout llOrders;
    @BindView(R.id.iv_ic_orders) ImageView ivIcOrders;
    @BindView(R.id.tv_text_orders) TextView tvTextOrders;
    @BindView(R.id.ll_profile) LinearLayout llProfile;
    @BindView(R.id.iv_ic_profile) ImageView ivIcProfile;
    @BindView(R.id.tv_text_profile) TextView tvTextProfile;
    @BindView(R.id.ll_rating) LinearLayout llRating;
    @BindView(R.id.iv_ic_rating) ImageView ivIcRating;
    @BindView(R.id.tv_text_rating) TextView tvTextRating;

    private FragmentManager fragmentManager;
    private HomeFragment homeFragment;
    private NotificationFragment notificationFragment;
    private OrdersFragment ordersFragment;
    private RatingFragment ratingFragment;
    private NewOrderFragment newOrderFragment;
    private OrderViewFragment orderViewFragment;
    private WalletFragment walletFragment;
    private ProfileFragment profileFragment;
    private EditProfileFragment editProfileFragment;
    private NewPublicOrderFragment newPublicOrderFragment;


    private static OrderStation order;
    private static PublicOrder publicOrder;
    public static boolean isLoadingNewOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //view
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initFragment();
    }

    //clicks
    @OnClick(R.id.iv_notification)
    public void notification() {
        navigate(8);
    }

    @OnClick(R.id.ll_home)
    public void home() {
        navigate(1);
    }

    @OnClick(R.id.ll_wallet)
    public void wallet() {
        navigate(2);
    }

    @OnClick(R.id.ll_orders)
    public void orders() {
        navigate(3);
    }

    @OnClick(R.id.ll_profile)
    public void profile() {
        navigate(4);
    }

    @OnClick(R.id.ll_rating)
    public void rating() {
        navigate(5);
    }

    @OnClick(R.id.sc_appear)
    public void setOnline() {
        if (scAppear.isChecked()) {
            onLine("1");
        } else {
            onLine("0");
        }
    }

    //functions

    private void initFragment() {
        notificationFragment = NotificationFragment.newInstance(this, this);
        ratingFragment = RatingFragment.newInstance(this);
        newOrderFragment = NewOrderFragment.newInstance(this, this);
        newOrderFragment.setListener(this);
        newPublicOrderFragment = NewPublicOrderFragment.newInstance(this, this);
        newPublicOrderFragment.setListener(this);
        orderViewFragment = OrderViewFragment.newInstance(this, this);
        orderViewFragment.setListener(this);
        profileFragment = ProfileFragment.newInstance(this);
        editProfileFragment = EditProfileFragment.newInstance();

        //navigation
        if (AppController.getInstance().getAppSettingsPreferences().getLogin().getUser() != null) {
            if (AppController.getInstance().getAppSettingsPreferences().getLogin().getUser().getIs_online().equals("1")) {
                scAppear.setChecked(true);
            }
        }
        if (AppController.getInstance().getAppSettingsPreferences().getTrackingPublicOrder() != null)
            publicOrder = AppController.getInstance().getAppSettingsPreferences().getTrackingPublicOrder();
        if (AppController.getInstance().getAppSettingsPreferences().getTrackingOrder() != null)
            order = AppController.getInstance().getAppSettingsPreferences().getTrackingOrder();

        //dialog
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            try {
                int id;
                String type;
                String jSON = (String) bundle.get("message");
                if (jSON != null) {
                    JSONObject body = new JSONObject(jSON).getJSONObject("data");
                    if (body.getString("msg").contains("new order")) {
                        type = body.getString("type");
                        if (type.equals("public")) {
                            id = body.getInt("public_order_id");
                            if (!isLoadingNewOrder) {
                                createNewOrder(id, type);
                            }
                        } else if (type.equals("4station")) {
                            id = body.getInt("order_id");
                            if (!isLoadingNewOrder) {
                                createNewOrder(id, type);
                            }
                        }
                        navigate(1);
                    } else {
                        type = body.getString("type");
                        if (type.equals("public") || type.equals("4station")) {
                            navigate(3);
                        } else if (type.contains("wallet")) {
                            navigate(2);
                        } else {
                            navigate(1);
                        }
                    }
                } else {
                    if (getIntent().getExtras().getInt("order_id") != -1) {
                        id = getIntent().getExtras().getInt("order_id");
                        type = getIntent().getExtras().getString("type");
                        navigate(1);
                        if (!isLoadingNewOrder) {
                            createNewOrder(id, type);
                        }
                    } else {
                        type = getIntent().getExtras().getString("type");
                        if (type.equals("public")) {
                            String s = getIntent().getExtras().getString("status");
                            navigate(3);
                            if (s.equals("new_message")) {
                                PublicChatFragment publicChatFragment = PublicChatFragment.newInstance(publicOrder, this, this);
                                publicChatFragment.show(getSupportFragmentManager(), "");
                            }
                        } else if (type.equals("4station")) {
                            String s = getIntent().getExtras().getString("status");
                            navigate(3);
                            if (s.equals("new_message")) {
                                ChatFragment chatFragment = ChatFragment.newInstance(order);
                                chatFragment.show(getSupportFragmentManager(), "");
                            }
                        } else if (type.contains("wallet")) {
                            navigate(2);
                        } else {
                            navigate(1);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                isLoadingNewOrder = false;
                navigate(1);
            }
        } else {
            navigate(1);
        }
    }

    private void replaceFragment(Fragment fragment, String s) {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment, s);
        fragmentTransaction.commit();
    }

    @Override
    public void navigate(int page) {
        ivIcHome.setBackgroundResource(R.drawable.ic_home);
        ivIcWallet.setBackgroundResource(R.drawable.ic_wallet);
        ivIcOrders.setBackgroundResource(R.drawable.ic_orders);
        ivIcProfile.setBackgroundResource(R.drawable.ic_profile);
        ivIcRating.setBackgroundResource(R.drawable.ic_rating);
        tvTextHome.setTextColor(getResources().getColor(R.color.white));
        tvTextWallet.setTextColor(getResources().getColor(R.color.white));
        tvTextOrders.setTextColor(getResources().getColor(R.color.white));
        tvTextProfile.setTextColor(getResources().getColor(R.color.white));
        tvTextRating.setTextColor(getResources().getColor(R.color.white));

        switch (page) {
            case 1:
                homeFragment = HomeFragment.newInstance(this);
                replaceFragment(homeFragment, "first");
                ivIcHome.setBackgroundResource(R.drawable.ic_home_blue);
                tvTextHome.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case 2:
                walletFragment = WalletFragment.newInstance(this);
                replaceFragment(walletFragment, "second");
                ivIcWallet.setBackgroundResource(R.drawable.ic_wallet_blue);
                tvTextWallet.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case 3:
                ordersFragment = OrdersFragment.newInstance(this, this);
                replaceFragment(ordersFragment, "third");
                ivIcOrders.setBackgroundResource(R.drawable.ic_orders_blue);
                tvTextOrders.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case 4:
                replaceFragment(profileFragment, "fourth");
                ivIcProfile.setBackgroundResource(R.drawable.ic_profile_blue);
                tvTextProfile.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case 5:
                replaceFragment(ratingFragment, "fifth");
                ivIcRating.setBackgroundResource(R.drawable.ic_rating_blue);
                tvTextRating.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case 6:
                replaceFragment(newOrderFragment, "sixth");
                break;
            case 7:
                replaceFragment(orderViewFragment, "seventh");
                break;
            case 8:
                replaceFragment(notificationFragment, "eighth");
                break;
            case 9:
                replaceFragment(editProfileFragment, "ninth");
                break;
            case 10:
                replaceFragment(newPublicOrderFragment, "tenth");
                break;
        }
    }

    private void onLine(String i) {
        if (ToolUtils.checkTheInternet()) {
            AppController.getInstance().getApi().isOnline(i).enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                    if (response.isSuccessful()) {
                        Login login = AppController.getInstance().getAppSettingsPreferences().getLogin();
                        login.getUser().setIs_online(i);
                        AppController.getInstance().getAppSettingsPreferences().setLogin(login);
                        ToolUtils.showLongToast(response.body().getMassage(), MainActivity.this);
                    } else {
                        ToolUtils.showError(MainActivity.this, response.errorBody());
                        scAppear.setChecked(false);
                    }
                }

                @Override
                public void onFailure(Call<Message> call, Throwable t) {
                    ToolUtils.showLongToast(t.getMessage(), MainActivity.this);
                    scAppear.setChecked(false);
                }
            });
        } else {
            ToolUtils.showLongToast(getString(R.string.no_connection), this);
            scAppear.setChecked(false);
        }
    }

    private void createNewOrder(int s, String type) {
        isLoadingNewOrder = true;
        if (type != null)
            if (type.equals("4station")) {
                AppController.getInstance().getApi().getOrderById(s).enqueue(new Callback<OrderStation>() {
                    @Override
                    public void onResponse(Call<OrderStation> call, Response<OrderStation> response) {
                        if (response.isSuccessful()) {
                            NewOrderDialog dialog = NewOrderDialog.newInstance(response.body());
                            dialog.show(getSupportFragmentManager(), "");
                            dialog.setListener(order -> {
                                setId(order);
                                navigate(6);
                                dialog.dismiss();
                                isLoadingNewOrder = false;
                            });
                        } else {
                            ToolUtils.showError(MainActivity.this, response.errorBody());
                            isLoadingNewOrder = false;
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderStation> call, Throwable t) {
                        t.printStackTrace();
                        isLoadingNewOrder = false;
                    }
                });
            } else {
                AppController.getInstance().getApi().getPublicOrder(s).enqueue(new Callback<PublicArrays>() {
                    @Override
                    public void onResponse(Call<PublicArrays> call, Response<PublicArrays> response) {
                        if (response.isSuccessful()) {
                            NewPublicOrderDialog dialog = NewPublicOrderDialog.newInstance(response.body().getPublicOrder());
                            dialog.show(getSupportFragmentManager(), "");
                            dialog.setListener(new NewPublicOrderDialog.NewPublicOrderListener() {
                                @Override
                                public void viewNewOrder(PublicOrder publicOrder) {
                                    setPublicOrder(publicOrder);
                                    navigate(10);
                                    dialog.dismiss();
                                    isLoadingNewOrder = false;
                                }

                                @Override
                                public void cancel() {
                                    navigate(1);
                                    isLoadingNewOrder = false;
                                }
                            });
                        } else {
                            ToolUtils.showError(MainActivity.this, response.errorBody());
                            isLoadingNewOrder = false;
                        }
                    }

                    @Override
                    public void onFailure(Call<PublicArrays> call, Throwable t) {
                        t.printStackTrace();
                        isLoadingNewOrder = false;
                    }
                });
            }
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.findFragmentById(R.id.fragment_container) instanceof HomeFragment) {
            super.onBackPressed();
        } else if (fragmentManager.findFragmentById(R.id.fragment_container) instanceof EditProfileFragment) {
            navigate(4);
        } else if (fragmentManager.findFragmentById(R.id.fragment_container) instanceof OrderViewFragment) {
            OrdersFragment.page = 0;
            WalletFragment.page = 0;
            navigate(3);
        } else if (fragmentManager.findFragmentById(R.id.fragment_container) instanceof NewPublicOrderFragment) {
            OrdersFragment.page = 1;
            WalletFragment.page = 1;
            navigate(3);
        } else {
            navigate(1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final Fragment fragmentInFrame = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragmentInFrame instanceof EditProfileFragment) {
            fragmentInFrame.onActivityResult(requestCode, resultCode, data);
        }
    }

    public static void setId(OrderStation o) {
        order = o;
        publicOrder = null;
        isLoadingNewOrder = false;
    }

    public static void setPublicOrder(PublicOrder o) {
        order = null;
        publicOrder = o;
        isLoadingNewOrder = false;
    }

    @Override
    public void setDataInOrderView() {
        orderViewFragment.setData(order);
    }

    @Override
    public void setDataInNewOrder() {
        newOrderFragment.setData(order);
    }

    @Override
    public void setDataInNewPublicOrder() {
        newPublicOrderFragment.setData(publicOrder);
    }

    @Override
    public void startGPSTracking() {
        if (publicOrder == null) {
            GPSTracking.getInstance(this, order).startGPSTracking();
        } else {
            GPSTracking.getInstance(this, publicOrder).startGPSTracking();
        }
    }

    @Override
    public void endGPSTracking() {
        if (publicOrder == null) {
            GPSTracking.getInstance(this, order).removeUpdates();
        } else {
            GPSTracking.getInstance(this, publicOrder).removeUpdates();
        }
    }
}

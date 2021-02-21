package com.webapp.a4_order_station_driver.feature.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.ActivityMainBinding;
import com.webapp.a4_order_station_driver.feature.main.editProfile.EditProfileFragment;
import com.webapp.a4_order_station_driver.feature.main.hame.HomeFragment;
import com.webapp.a4_order_station_driver.feature.main.natification.NotificationFragment;
import com.webapp.a4_order_station_driver.feature.main.newOrders.NewOrderFragment;
import com.webapp.a4_order_station_driver.feature.main.newOrders.NewPublicOrderFragment;
import com.webapp.a4_order_station_driver.feature.main.orders.OrdersFragment;
import com.webapp.a4_order_station_driver.feature.main.profile.ProfileFragment;
import com.webapp.a4_order_station_driver.feature.main.rate.RatingFragment;
import com.webapp.a4_order_station_driver.feature.main.viewOrders.OrderViewFragment;
import com.webapp.a4_order_station_driver.feature.main.wallets.WalletFragment;
import com.webapp.a4_order_station_driver.models.Login;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.models.OrderStation;
import com.webapp.a4_order_station_driver.models.PublicArrays;
import com.webapp.a4_order_station_driver.models.PublicOrder;
import com.webapp.a4_order_station_driver.utils.APIUtils;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.NavigateUtils;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.dialogs.ChatFragment;
import com.webapp.a4_order_station_driver.utils.dialogs.NewOrderDialog;
import com.webapp.a4_order_station_driver.utils.dialogs.NewPublicOrderDialog;
import com.webapp.a4_order_station_driver.utils.dialogs.PublicChatFragment;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;
import com.webapp.a4_order_station_driver.utils.location.GPSTracking;
import com.webapp.a4_order_station_driver.utils.view.Tracking;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends BaseActivity implements
        Tracking, OrderViewFragment.Listener, NewOrderFragment.Listener
        , NewPublicOrderFragment.Listener {

    public static final int page = 200;

    public static final String online = "1";
    public static final String offline = "0";

    private ActivityMainBinding binding;

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
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.setRootView(binding.getRoot());
        super.onCreate(savedInstanceState);
        click();
        data();
    }

    private void click() {
        binding.ivNotification.setOnClickListener(view -> navigate(NotificationFragment.page));
        binding.llHome.setOnClickListener(view -> navigate(HomeFragment.page));
        binding.llWallet.setOnClickListener(view -> navigate(WalletFragment.page));
        binding.llOrders.setOnClickListener(view -> navigate(OrdersFragment.page));
        binding.llProfile.setOnClickListener(view -> navigate(ProfileFragment.page));
        binding.llRating.setOnClickListener(view -> navigate(RatingFragment.page));
        binding.scAppear.setOnClickListener(view -> setOnline());
    }

    public void setOnline() {
        if (binding.scAppear.isChecked()) {
            updateUserStatus(online);
        } else {
            updateUserStatus(offline);
        }
    }

    //functions
    private void data() {
        //navigation
        if (AppController.getInstance().getAppSettingsPreferences().getLogin().getUser() != null) {
            if (AppController.getInstance().getAppSettingsPreferences().getLogin().getUser().getIs_online().equals("1")) {
                binding.scAppear.setChecked(true);
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
                        navigate(HomeFragment.page);
                    } else {
                        type = body.getString("type");
                        if (type.equals("public") || type.equals("4station")) {
                            navigate(OrdersFragment.page);
                        } else if (type.contains("wallet")) {
                            navigate(WalletFragment.page);
                        } else {
                            navigate(HomeFragment.page);
                        }
                    }
                } else {
                    if (getIntent().getExtras().getInt("order_id") != -1) {
                        id = getIntent().getExtras().getInt("order_id");
                        type = getIntent().getExtras().getString("type");
                        navigate(HomeFragment.page);
                        if (!isLoadingNewOrder) {
                            createNewOrder(id, type);
                        }
                    } else {
                        type = getIntent().getExtras().getString("type");
                        if (type.equals("public")) {
                            String s = getIntent().getExtras().getString("status");
                            navigate(OrdersFragment.page);
                            if (s.equals("new_message")) {
                                PublicChatFragment publicChatFragment = PublicChatFragment
                                        .newInstance(publicOrder, this, this);
                                publicChatFragment.show(getSupportFragmentManager(), "");
                            }
                        } else if (type.equals("4station")) {
                            String s = getIntent().getExtras().getString("status");
                            navigate(OrdersFragment.page);
                            if (s.equals("new_message")) {
                                ChatFragment chatFragment = ChatFragment.newInstance(order);
                                chatFragment.show(getSupportFragmentManager(), "");
                            }
                        } else if (type.contains("wallet")) {
                            navigate(WalletFragment.page);
                        } else {
                            navigate(HomeFragment.page);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                isLoadingNewOrder = false;
                navigate(HomeFragment.page);
            }
        } else {
            navigate(HomeFragment.page);
        }
    }

    @Override
    public void navigate(int page) {
        binding.ivIcHome.setBackgroundResource(R.drawable.ic_home);
        binding.ivIcWallet.setBackgroundResource(R.drawable.ic_wallet);
        binding.ivIcOrders.setBackgroundResource(R.drawable.ic_orders);
        binding.ivIcProfile.setBackgroundResource(R.drawable.ic_profile);
        binding.ivIcRating.setBackgroundResource(R.drawable.ic_rating);
        binding.tvTextHome.setTextColor(getResources().getColor(R.color.white));
        binding.tvTextWallet.setTextColor(getResources().getColor(R.color.white));
        binding.tvTextOrders.setTextColor(getResources().getColor(R.color.white));
        binding.tvTextProfile.setTextColor(getResources().getColor(R.color.white));
        binding.tvTextRating.setTextColor(getResources().getColor(R.color.white));

        switch (page) {
            case HomeFragment.page://1
                homeFragment = HomeFragment.newInstance();
                new NavigateUtils().replaceFragment(getSupportFragmentManager()
                        , homeFragment, R.id.fragment_container);
                binding.ivIcHome.setBackgroundResource(R.drawable.ic_home_blue);
                binding.tvTextHome.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case WalletFragment.page://2
                walletFragment = WalletFragment.newInstance();
                new NavigateUtils().replaceFragment(getSupportFragmentManager()
                        , walletFragment, R.id.fragment_container);
                binding.ivIcWallet.setBackgroundResource(R.drawable.ic_wallet_blue);
                binding.tvTextWallet.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case OrdersFragment.page://3
                ordersFragment = OrdersFragment.newInstance(this, this);
                new NavigateUtils().replaceFragment(getSupportFragmentManager()
                        , ordersFragment, R.id.fragment_container);
                binding.ivIcOrders.setBackgroundResource(R.drawable.ic_orders_blue);
                binding.tvTextOrders.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case ProfileFragment.page://4
                profileFragment = ProfileFragment.newInstance(this);
                new NavigateUtils().replaceFragment(getSupportFragmentManager()
                        , profileFragment, R.id.fragment_container);
                binding.ivIcProfile.setBackgroundResource(R.drawable.ic_profile_blue);
                binding.tvTextProfile.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case RatingFragment.page://5
                ratingFragment = RatingFragment.newInstance();
                new NavigateUtils().replaceFragment(getSupportFragmentManager()
                        , ratingFragment, R.id.fragment_container);
                binding.ivIcRating.setBackgroundResource(R.drawable.ic_rating_blue);
                binding.tvTextRating.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case 6://6
                newOrderFragment = NewOrderFragment.newInstance(this, this);
                newOrderFragment.setListener(this);
                new NavigateUtils().replaceFragment(getSupportFragmentManager()
                        , newOrderFragment, R.id.fragment_container);
                break;
            case 7://7
                orderViewFragment = OrderViewFragment.newInstance(this, this);
                orderViewFragment.setListener(this);
                new NavigateUtils().replaceFragment(getSupportFragmentManager()
                        , orderViewFragment, R.id.fragment_container);
                break;
            case NotificationFragment.page://8
                notificationFragment = NotificationFragment.newInstance(this, this);
                new NavigateUtils().replaceFragment(getSupportFragmentManager()
                        , notificationFragment, R.id.fragment_container);
                break;
            case EditProfileFragment.page://9
                editProfileFragment = EditProfileFragment.newInstance();
                new NavigateUtils().replaceFragment(getSupportFragmentManager()
                        , editProfileFragment, R.id.fragment_container);
                break;
            case 10://10
                newPublicOrderFragment = NewPublicOrderFragment.newInstance(this, this);
                newPublicOrderFragment.setListener(this);
                new NavigateUtils().replaceFragment(getSupportFragmentManager()
                        , newPublicOrderFragment, R.id.fragment_container);
                break;
        }
    }

    private void updateUserStatus(String status) {
        new APIUtils<Message>(this).getData(AppController.getInstance()
                .getApi().isOnline(status), new RequestListener<Message>() {
            @Override
            public void onSuccess(Message message, String msg) {
                Login login = AppController.getInstance().getAppSettingsPreferences().getLogin();
                login.getUser().setIs_online(status);
                AppController.getInstance().getAppSettingsPreferences().setLogin(login);
                ToolUtils.showLongToast(message.getMassage(), MainActivity.this);
            }

            @Override
            public void onError(String msg) {
                ToolUtils.showLongToast(msg, MainActivity.this);
                if (status.equals(online)) {
                    binding.scAppear.setChecked(false);
                } else {
                    binding.scAppear.setChecked(true);
                }
            }

            @Override
            public void onFail(String msg) {
                ToolUtils.showLongToast(msg, MainActivity.this);
                if (status.equals(online)) {
                    binding.scAppear.setChecked(false);
                } else {
                    binding.scAppear.setChecked(true);
                }
            }
        });
    }

    private void createNewOrder(int id, String type) {
        isLoadingNewOrder = true;
        if (type != null)
            if (type.equals(AppContent.TYPE_ORDER_4STATION)) {
                new APIUtils<OrderStation>(this).getData(AppController.getInstance()
                        .getApi().getOrderById(id), new RequestListener<OrderStation>() {
                    @Override
                    public void onSuccess(OrderStation orderStation, String msg) {
                        NewOrderDialog dialog = NewOrderDialog.newInstance(orderStation);
                        dialog.show(getSupportFragmentManager(), "");
                        dialog.setListener(order -> {
                            setId(order);
                            navigate(6);
                            dialog.dismiss();
                            isLoadingNewOrder = false;
                        });
                    }

                    @Override
                    public void onError(String msg) {
                        ToolUtils.showLongToast(msg, MainActivity.this);
                        isLoadingNewOrder = false;
                    }

                    @Override
                    public void onFail(String msg) {
                        ToolUtils.showLongToast(msg, MainActivity.this);
                        isLoadingNewOrder = false;
                    }
                });

            } else {
                new APIUtils<PublicArrays>(this).getData(AppController.getInstance()
                        .getApi().getPublicOrder(id), new RequestListener<PublicArrays>() {
                    @Override
                    public void onSuccess(PublicArrays publicArrays, String msg) {
                        NewPublicOrderDialog dialog = NewPublicOrderDialog.newInstance(publicArrays.getPublicOrder());
                        dialog.show(getSupportFragmentManager(), "");
                        dialog.setListener(publicOrder -> {
                            setPublicOrder(publicOrder);
                            navigate(10);
                            dialog.dismiss();
                            isLoadingNewOrder = false;
                        });
                    }

                    @Override
                    public void onError(String msg) {
                        ToolUtils.showLongToast(msg, MainActivity.this);
                        isLoadingNewOrder = false;
                    }

                    @Override
                    public void onFail(String msg) {
                        ToolUtils.showLongToast(msg, MainActivity.this);
                        isLoadingNewOrder = false;
                    }
                });
            }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.fragment_container)
                instanceof HomeFragment) {
            super.onBackPressed();
        } else if (fragmentManager.findFragmentById(R.id.fragment_container)
                instanceof EditProfileFragment) {
            navigate(ProfileFragment.page);
        } else if (fragmentManager.findFragmentById(R.id.fragment_container)
                instanceof OrderViewFragment) {
            OrdersFragment.viewPagerPage = 0;
            WalletFragment.viewPagerPage = 0;
            navigate(OrdersFragment.page);
        } else if (fragmentManager.findFragmentById(R.id.fragment_container)
                instanceof NewPublicOrderFragment) {
            OrdersFragment.viewPagerPage = 1;
            WalletFragment.viewPagerPage = 1;
            navigate(OrdersFragment.page);
        } else {
            navigate(HomeFragment.page);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final Fragment fragmentInFrame = getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
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

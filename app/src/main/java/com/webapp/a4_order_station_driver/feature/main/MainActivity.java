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
import com.webapp.a4_order_station_driver.feature.main.orders.OrdersFragment;
import com.webapp.a4_order_station_driver.feature.main.orders.publicO.OrderPublicFragment;
import com.webapp.a4_order_station_driver.feature.main.orders.station.OrderStationFragment;
import com.webapp.a4_order_station_driver.feature.main.profile.ProfileFragment;
import com.webapp.a4_order_station_driver.feature.main.rate.RatingFragment;
import com.webapp.a4_order_station_driver.feature.main.wallets.station.OrderStationWalletFragment;
import com.webapp.a4_order_station_driver.feature.main.wallets.publicO.PublicWalletFragment;
import com.webapp.a4_order_station_driver.feature.main.wallets.WalletFragment;
import com.webapp.a4_order_station_driver.feature.order.chat.ChatFragment;
import com.webapp.a4_order_station_driver.feature.order.newPublicOrder.NewPublicOrderFragment;
import com.webapp.a4_order_station_driver.feature.order.orderStationView.OrderStationViewFragment;
import com.webapp.a4_order_station_driver.feature.order.publicOrderView.PublicOrderViewFragment;
import com.webapp.a4_order_station_driver.models.Order;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.NavigateUtil;
import com.webapp.a4_order_station_driver.utils.dialogs.NewOrderStationDialog;
import com.webapp.a4_order_station_driver.utils.dialogs.NewPublicOrderDialog;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;

import org.json.JSONObject;

public class MainActivity extends BaseActivity {

    public static final int page = 200;

    public static final String online = "1";
    public static final String offline = "0";

    private ActivityMainBinding binding;

    /*private static OrderStation orderStation;
    private static PublicOrder publicOrder;*/

    private MainPresenter presenter;

    public static boolean isLoadingNewOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.setRootView(binding.getRoot());
        super.onCreate(savedInstanceState);
        presenter = new MainPresenter(this);
        data();
        click();
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
            presenter.updateState(online, binding);
        } else {
            presenter.updateState(offline, binding);
        }
    }

    //functions
    private void data() {
        if (AppController.getInstance().getAppSettingsPreferences().getLogin().getUser() != null) {
            if (AppController.getInstance().getAppSettingsPreferences().getLogin().getUser().getIs_online().equals("1")) {
                binding.scAppear.setChecked(true);
            }
        }
        /*if (AppController.getInstance().getAppSettingsPreferences().getTrackingOrder() != null)
            publicOrder = AppController.getInstance().getAppSettingsPreferences().getTrackingOrder();
        if (AppController.getInstance().getAppSettingsPreferences().getTrackingOrder() != null)
            orderStation = AppController.getInstance().getAppSettingsPreferences().getTrackingOrder();
*/
        //dialog
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            try {
                String message = (String) bundle.get(AppContent.FIREBASE_MESSAGE);
                JSONObject body = new JSONObject(message).getJSONObject(AppContent.FIREBASE_DATA);

                int id;
                String msg = body.getString(AppContent.FIREBASE_MSG);
                String type = body.getString(AppContent.FIREBASE_TYPE);

                switch (type) {
                    case AppContent.TYPE_ORDER_4STATION:
                        id = body.getInt(AppContent.ORDER_Id);
                        break;
                    case AppContent.TYPE_ORDER_PUBLIC:
                        id = body.getInt(AppContent.PUBLIC_ORDER_Id);
                        break;
                    default:
                        id = -1;
                }

                checkNavigate(bundle);

                if (msg.contains(AppContent.NEW_ORDER)) {
                    if (!isLoadingNewOrder) {
                        createNewOrder(id, type);
                    }
                } else if (type.equals(AppContent.TYPE_ORDER_PUBLIC)
                        || type.equals(AppContent.TYPE_ORDER_4STATION)) {
                    navigate(OrdersFragment.page);
                    if (!body.isNull(AppContent.FIREBASE_STATUS)) {
                        if (body.getString(AppContent.FIREBASE_STATUS).equals(AppContent.NEW_MESSAGE)) {
                            if (type.equals(AppContent.TYPE_ORDER_PUBLIC)) {
                                new NavigateUtil().openOrder(this, new Order(id, type), PublicOrderViewFragment.page, true);
                            } else {
                                new NavigateUtil().openOrder(this, new Order(id, type), ChatFragment.page, true);
                            }
                        }
                    }
                } else if (type.contains(AppContent.WALLET)) {
                    navigate(WalletFragment.page);
                } else if (type.equals(AppContent.RATE)) {
                    navigate(RatingFragment.page);
                } else {
                    checkNavigate(bundle);
                }

                /*if (jSON != null) {
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
                                PublicOrderViewFragment publicChatFragment = PublicOrderViewFragment
                                        .newInstance(publicOrder, this, this);
                                publicChatFragment.show(getSupportFragmentManager(), "");
                            }
                        } else if (type.equals("4station")) {
                            String s = getIntent().getExtras().getString("status");
                            navigate(OrdersFragment.page);
                            if (s.equals("new_message")) {
                                ChatFragment chatFragment = ChatFragment.newInstance(orderStation);
                                chatFragment.show(getSupportFragmentManager(), "");
                            }
                        } else if (type.contains("wallet")) {
                            navigate(WalletFragment.page);
                        } else {
                            navigate(HomeFragment.page);
                        }
                    }
                }*/
            } catch (Exception e) {
                e.printStackTrace();
                isLoadingNewOrder = false;
                checkNavigate(bundle);
            }
        } else {
            checkNavigate(bundle);
        }
    }

    private void checkNavigate(Bundle bundle) {
        if (bundle != null && bundle.containsKey(AppContent.PAGE)) {
            navigate(bundle.getInt(AppContent.PAGE));
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
                HomeFragment homeFragment = HomeFragment.newInstance(this);
                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , homeFragment, R.id.fragment_container);
                binding.ivIcHome.setBackgroundResource(R.drawable.ic_home_blue);
                binding.tvTextHome.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case WalletFragment.page://2
                WalletFragment walletFragment = WalletFragment.newInstance();
                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , walletFragment, R.id.fragment_container);
                binding.ivIcWallet.setBackgroundResource(R.drawable.ic_wallet_blue);
                binding.tvTextWallet.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case OrdersFragment.page://3
                OrdersFragment ordersFragment = OrdersFragment.newInstance(this);
                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , ordersFragment, R.id.fragment_container);
                binding.ivIcOrders.setBackgroundResource(R.drawable.ic_orders_blue);
                binding.tvTextOrders.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case ProfileFragment.page://4
                ProfileFragment profileFragment = ProfileFragment.newInstance(this);
                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , profileFragment, R.id.fragment_container);
                binding.ivIcProfile.setBackgroundResource(R.drawable.ic_profile_blue);
                binding.tvTextProfile.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case RatingFragment.page://5
                RatingFragment ratingFragment = RatingFragment.newInstance();
                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , ratingFragment, R.id.fragment_container);
                binding.ivIcRating.setBackgroundResource(R.drawable.ic_rating_blue);
                binding.tvTextRating.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            /*case 6://6
                newOrderFragment = NewOrderStationFragment.newInstance(this, this);
                newOrderFragment.setListener(this);
                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , newOrderFragment, R.id.fragment_container);
                break;
            case 7://7
                orderViewFragment = OrderStationViewFragment.newInstance(this, this);
                orderViewFragment.setListener(this);
                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , orderViewFragment, R.id.fragment_container);
                break;*/
            case NotificationFragment.page://8
                NotificationFragment notificationFragment = NotificationFragment.newInstance(this);
                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , notificationFragment, R.id.fragment_container);
                break;
            case EditProfileFragment.page://9
                EditProfileFragment editProfileFragment = EditProfileFragment.newInstance(this);
                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , editProfileFragment, R.id.fragment_container);
                break;
           /* case 10://10
                newPublicOrderFragment = NewPublicOrderFragment.newInstance(this, this);
                newPublicOrderFragment.setListener(this);
                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , newPublicOrderFragment, R.id.fragment_container);
                break;*/
        }
    }

    private void createNewOrder(int id, String type) {
        isLoadingNewOrder = true;
        if (type != null)
            if (type.equals(AppContent.TYPE_ORDER_4STATION)) {
                NewOrderStationDialog dialog = NewOrderStationDialog.newInstance(id);
                /*@Override
                public void viewNewOrder(OrderStation order) {
                        setId(order);
                navigate(6);
                dialog.dismiss();
                    isLoadingNewOrder = false;
                }*/
                dialog.show(getSupportFragmentManager(), "");
                dialog.setListener(() -> isLoadingNewOrder = false);
            } else {
                NewPublicOrderDialog dialog = NewPublicOrderDialog.newInstance(id);
                dialog.show(getSupportFragmentManager(), "");
                /*@Override
                public void viewNewOrder(PublicOrder publicOrder) {
                    setPublicOrder(publicOrder);
                    navigate(10);
                    dialog.dismiss();
                    isLoadingNewOrder = false;
                }*/
                dialog.setListener(() -> isLoadingNewOrder = false);
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
                instanceof OrderStationViewFragment) {
            OrdersFragment.viewPagerPage = OrderStationFragment.viewPagerPage;
            WalletFragment.viewPagerPage = OrderStationWalletFragment.viewPagerPage;
            navigate(OrdersFragment.page);
        } else if (fragmentManager.findFragmentById(R.id.fragment_container)
                instanceof NewPublicOrderFragment) {
            OrdersFragment.viewPagerPage = OrderPublicFragment.viewPagerPage;
            WalletFragment.viewPagerPage = PublicWalletFragment.viewPagerPage;
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

   /* public static void setId(OrderStation o) {
        orderStation = o;
        publicOrder = null;
        isLoadingNewOrder = false;
    }

    public static void setPublicOrder(PublicOrder o) {
        orderStation = null;
        publicOrder = o;
        isLoadingNewOrder = false;
    }

    @Override
    public void setDataInOrderView() {
        orderViewFragment.setData(orderStation);
    }

    @Override
    public void setDataInNewOrder() {
        newOrderFragment.setData(orderStation);
    }

    @Override
    public void setDataInNewPublicOrder() {
        newPublicOrderFragment.data(publicOrder);
    }*/

   /* @Override
    public void startGPSTracking() {
        if (publicOrder == null) {
            new OrderGPSTracking(this, orderStation).startGPSTracking();
            //GPSTracking.getInstance(this, order).startGPSTracking();
        } else {
            new OrderGPSTracking(this, publicOrder).startGPSTracking();
            //GPSTracking.getInstance(this, publicOrder).startGPSTracking();
        }
    }

    @Override
    public void endGPSTracking() {
        if (publicOrder == null) {
            //GPSTracking.getInstance(this, orderStation).removeUpdates();
            new OrderGPSTracking(this, orderStation).removeUpdates();
        } else {
            new OrderGPSTracking(this, publicOrder).removeUpdates();
            //GPSTracking.getInstance(this, publicOrder).removeUpdates();
        }
    }*/
}

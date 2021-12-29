package com.webapp.a4_order_station_driver.feature.main;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GravityCompat;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.ActivityMain2Binding;
import com.webapp.a4_order_station_driver.feature.data.DataActivity;
import com.webapp.a4_order_station_driver.feature.data.contact.ContactFragment;
import com.webapp.a4_order_station_driver.feature.data.natification.NotificationFragment;
import com.webapp.a4_order_station_driver.feature.data.privacy.PrivacyPolicyFragment;
import com.webapp.a4_order_station_driver.feature.data.rate.RatingFragment;
import com.webapp.a4_order_station_driver.feature.main.home.HomeFragment;
import com.webapp.a4_order_station_driver.feature.main.orders.OrdersFragment;
import com.webapp.a4_order_station_driver.feature.main.profile.ProfileFragment;
import com.webapp.a4_order_station_driver.feature.main.wallets.WalletFragment;
import com.webapp.a4_order_station_driver.feature.order.chat.ChatFragment;
import com.webapp.a4_order_station_driver.feature.order.publicOrderView.PublicOrderViewFragment;
import com.webapp.a4_order_station_driver.models.Order;
import com.webapp.a4_order_station_driver.models.User;
import com.webapp.a4_order_station_driver.utils.APIImageUtil;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.NavigateUtil;
import com.webapp.a4_order_station_driver.utils.dialogs.NewOrderStationDialog;
import com.webapp.a4_order_station_driver.utils.dialogs.NewPublicOrderDialog;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.language.AppLanguageUtil;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity2 extends BaseActivity implements DialogView<Boolean> {

    public static final int page = 200;

    public static boolean isLoadingNewOrder;

    private ActivityMain2Binding binding;
    private MainPresenter presenter;

    private SwitchCompat onOffOnline;
    private TextView tvStatus;
    private CircleImageView civ_avatar;
    private TextView tv_user_name;
    private RatingBar rb_user_rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        super.setRootView(binding.getRoot());
        super.onCreate(savedInstanceState);

        //toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout
                , binding.appBarMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
        binding.appBarMain.toolbar.setNavigationIcon(R.drawable.ic_menu);
        toggle.setToolbarNavigationClickListener(view -> binding.drawerLayout.openDrawer(GravityCompat.START));
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //presenter
        presenter = new MainPresenter(this, this);
        //navigate(HomeFragment.page);
        data();
        //define clicking
        click();
        dataOfNavigationHeader();
    }

    private void data() {
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

    private void createNewOrder(int id, String type) {
        isLoadingNewOrder = true;
        if (type != null)
            if (type.equals(AppContent.TYPE_ORDER_4STATION)) {
                NewOrderStationDialog dialog = NewOrderStationDialog.newInstance(id);
                dialog.show(getSupportFragmentManager(), "");
                dialog.setListener(new NewOrderStationDialog.NewOrderListener() {
                    @Override
                    public void allowLoadNewOrder() {
                        isLoadingNewOrder = false;
                    }

                    @Override
                    public void cancel() {
                        allowLoadNewOrder();
                        navigate(HomeFragment.page);
                    }
                });
            } else {
                NewPublicOrderDialog dialog = NewPublicOrderDialog.newInstance(id);
                dialog.show(getSupportFragmentManager(), "");
                dialog.setListener(new NewPublicOrderDialog.NewPublicOrderListener() {
                    @Override
                    public void allowLoadNewOrder() {
                        isLoadingNewOrder = false;
                    }

                    @Override
                    public void cancel() {
                        allowLoadNewOrder();
                        navigate(HomeFragment.page);
                    }
                });
            }
    }

    private void click() {
        //menu
        Menu menu = binding.navView.getMenu();
        MenuItem home = menu.findItem(R.id.nav_home);
        MenuItem rating = menu.findItem(R.id.nav_rating);
        MenuItem contact = menu.findItem(R.id.nav_contact);
        MenuItem privacy = menu.findItem(R.id.nav_privacy);
        MenuItem logout = menu.findItem(R.id.nav_log_out);

        //home
        home.setOnMenuItemClickListener(menuItem -> {
            navigate(HomeFragment.page);
            return false;
        });

        //rating
        rating.setOnMenuItemClickListener(menuItem -> {
            navigate(RatingFragment.page);
            return false;
        });

        //contact
        contact.setOnMenuItemClickListener(menuItem -> {
            navigate(ContactFragment.page);
            return false;
        });

        //privacy
        privacy.setOnMenuItemClickListener(menuItem -> {
            navigate(PrivacyPolicyFragment.page);
            return false;
        });

        //language toggle
        MenuItem language = menu.findItem(R.id.nav_language);
        View actionView = language.getActionView();
        SwitchCompat aSwitch = actionView.findViewById(R.id.s_language);

        aSwitch.setChecked(AppController.getInstance().getAppSettingsPreferences().getAppLanguage().equals(AppLanguageUtil.ARABIC));

        aSwitch.setOnClickListener(view -> {
            if (aSwitch.isChecked()) {
                AppLanguageUtil.getInstance().setAppLanguage(MainActivity2.this, AppLanguageUtil.ARABIC);
            } else {
                AppLanguageUtil.getInstance().setAppLanguage(MainActivity2.this, AppLanguageUtil.English);
            }
            recreate();
        });

        //logout
        logout.setOnMenuItemClickListener(menuItem -> {
            presenter.logout();
            return false;
        });

        //bottom bar
        binding.appBarMain.ivNotification.setOnClickListener(view -> navigate(NotificationFragment.page));
        binding.appBarMain.llHome.setOnClickListener(view -> navigate(HomeFragment.page));
        binding.appBarMain.llWallet.setOnClickListener(view -> navigate(WalletFragment.page));
        binding.appBarMain.llOrders.setOnClickListener(view -> navigate(OrdersFragment.page));
        binding.appBarMain.llProfile.setOnClickListener(view -> navigate(ProfileFragment.page));
    }

    public void dataOfNavigationHeader() {
        View v = binding.navView.getHeaderView(0);
        civ_avatar = v.findViewById(R.id.iv_user_image);
        tv_user_name = v.findViewById(R.id.tv_user_name);
        rb_user_rating = v.findViewById(R.id.rb_review);
        onOffOnline = v.findViewById(R.id.s_on_off_line);
        tvStatus = v.findViewById(R.id.tv_status);

        //header data
        User user = AppController.getInstance().getAppSettingsPreferences().getUser();
        APIImageUtil.loadImage(this, v.findViewById(R.id.pb_wait), user.getAvatar_url(), civ_avatar);
        tv_user_name.setText(user.getName());
        rb_user_rating.setRating(user.getRate());
        //onOffOnline.setChecked(AppController.getInstance().getAppSettingsPreferences().getUser().getIs_online());
        setStatus(tvStatus);

        //click on header
        onOffOnline.setOnClickListener(view -> {
            presenter.updateState(onOffOnline.isChecked());
        });
    }

    private void setStatus(TextView tvStatus) {
        onOffOnline.setChecked(AppController.getInstance().getAppSettingsPreferences().getUser().isOnline());
        if (onOffOnline.isChecked()) {
            tvStatus.setText(getString(R.string.online));
            tvStatus.setTextColor(getColor(R.color.green));
        } else {
            tvStatus.setText(getString(R.string.offline));
            tvStatus.setTextColor(getColor(R.color.red));
        }
    }

    public void navigate(int page) {
        binding.appBarMain.ivIcHome.setBackgroundResource(R.drawable.ic_home_inactive);
        binding.appBarMain.ivIcWallet.setBackgroundResource(R.drawable.ic_wallet_inactive);
        binding.appBarMain.ivIcOrders.setBackgroundResource(R.drawable.ic_order_inactive);
        binding.appBarMain.ivIcProfile.setBackgroundResource(R.drawable.ic_profile_inactive);
        binding.appBarMain.tvTextHome.setTextColor(getColor(R.color.hint));
        binding.appBarMain.tvTextWallet.setTextColor(getColor(R.color.hint));
        binding.appBarMain.tvTextOrders.setTextColor(getColor(R.color.hint));
        binding.appBarMain.tvTextProfile.setTextColor(getColor(R.color.hint));

        switch (page) {
            case HomeFragment.page://1
                //createNewOrder(28, AppContent.TYPE_ORDER_4STATION);
                HomeFragment homeFragment = HomeFragment.newInstance();
                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , homeFragment, R.id.nav_host_fragment_content_main);
                binding.appBarMain.ivIcHome.setBackgroundResource(R.drawable.ic_home_active);
                binding.appBarMain.tvTextHome.setTextColor(getColor(R.color.colorPrimary));
                binding.appBarMain.tvPageTitle.setText(R.string.home);
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                break;

            case WalletFragment.page://2
                WalletFragment walletFragment = WalletFragment.newInstance(this);
                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , walletFragment, R.id.nav_host_fragment_content_main);
                binding.appBarMain.ivIcWallet.setBackgroundResource(R.drawable.ic_wallet_active);
                binding.appBarMain.tvTextWallet.setTextColor(getColor(R.color.colorPrimary));
                binding.appBarMain.tvPageTitle.setText(R.string.wallet);
                break;

            case OrdersFragment.page://3
                OrdersFragment ordersFragment = OrdersFragment.newInstance(this);
                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , ordersFragment, R.id.nav_host_fragment_content_main);
                binding.appBarMain.ivIcOrders.setBackgroundResource(R.drawable.ic_order_active);
                binding.appBarMain.tvTextOrders.setTextColor(getColor(R.color.colorPrimary));
                binding.appBarMain.tvPageTitle.setText(R.string.orders);
                break;

            case ProfileFragment.page://4
                ProfileFragment profileFragment = ProfileFragment.newInstance(this);
                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , profileFragment, R.id.nav_host_fragment_content_main);
                binding.appBarMain.ivIcProfile.setBackgroundResource(R.drawable.ic_profile_active);
                binding.appBarMain.tvTextProfile.setTextColor(getColor(R.color.colorPrimary));
                binding.appBarMain.tvPageTitle.setText(R.string.profile);
                break;

            case RatingFragment.page:
                new NavigateUtil().activityIntentWithPage(MainActivity2.this, DataActivity.class
                        , true, RatingFragment.page);
                break;

            case ContactFragment.page:
                new NavigateUtil().activityIntentWithPage(MainActivity2.this, DataActivity.class
                        , true, ContactFragment.page);
                break;

            case PrivacyPolicyFragment.page:
                new NavigateUtil().activityIntentWithPage(MainActivity2.this, DataActivity.class
                        , true, PrivacyPolicyFragment.page);
                break;

            case NotificationFragment.page://8
                new NavigateUtil().activityIntentWithPage(MainActivity2.this, DataActivity.class
                        , true, NotificationFragment.page);
                break;
        }
    }

    @Override
    public void setData(Boolean is_success) {
        if (is_success) {
            if (onOffOnline.isChecked()) {
                tvStatus.setText(getString(R.string.online));
                tvStatus.setTextColor(getColor(R.color.green));
            } else {
                tvStatus.setText(getString(R.string.offline));
                tvStatus.setTextColor(getColor(R.color.red));
            }
        } else {
            if (onOffOnline.isChecked()) {
                onOffOnline.setChecked(false);
            } else {
                onOffOnline.setChecked(true);
            }
        }
    }

    @Override
    public void showDialog(String s) {
        WaitDialogFragment.newInstance().show(getSupportFragmentManager(), "");

    }

    @Override
    public void hideDialog() {
        WaitDialogFragment.newInstance().dismiss();
    }
}
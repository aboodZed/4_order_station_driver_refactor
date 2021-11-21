package com.webapp.a4_order_station_driver.feature.main;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.ActivityMain2Binding;
import com.webapp.a4_order_station_driver.feature.data.DataActivity;
import com.webapp.a4_order_station_driver.feature.data.contact.ContactFragment;
import com.webapp.a4_order_station_driver.feature.data.natification.NotificationFragment;
import com.webapp.a4_order_station_driver.feature.data.privacy.PrivacyPolicyFragment;
import com.webapp.a4_order_station_driver.feature.data.rate.RatingFragment;
import com.webapp.a4_order_station_driver.feature.login.LoginActivity;
import com.webapp.a4_order_station_driver.feature.main.editProfile.EditProfileFragment;
import com.webapp.a4_order_station_driver.feature.main.home.HomeFragment;
import com.webapp.a4_order_station_driver.feature.main.orders.OrdersFragment;
import com.webapp.a4_order_station_driver.feature.main.orders.publicO.OrderPublicFragment;
import com.webapp.a4_order_station_driver.feature.main.profile.ProfileFragment;
import com.webapp.a4_order_station_driver.feature.main.wallets.WalletFragment;
import com.webapp.a4_order_station_driver.utils.NavigateUtil;
import com.webapp.a4_order_station_driver.utils.language.AppLanguageUtil;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;

public class MainActivity2 extends BaseActivity {

    //private AppBarConfiguration mAppBarConfiguration;
    private ActivityMain2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        super.setRootView(binding.getRoot());
        super.onCreate(savedInstanceState);

        /*setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

          View headerView = navigationView.getHeaderView(0);
       ImageView user_image = headerView.findViewById(R.id.iv_user_image);
        user_image.setImageDrawable(getDrawable(R.drawable.img_user));
         */


        //toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,  binding.drawerLayout
                ,binding.appBarMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
        binding.appBarMain.toolbar.setNavigationIcon(R.drawable.ic_menu);
        toggle.setToolbarNavigationClickListener(view -> binding.drawerLayout.openDrawer(Gravity.LEFT));
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        /*
        binding.appBarMain.toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
        */

        //start direction
        navigate(HomeFragment.page);
        //define clicking
        click();
    }

    private void click() {
        Menu menu = binding.navView.getMenu();
        MenuItem home = menu.findItem(R.id.nav_home);
        MenuItem rating = menu.findItem(R.id.nav_rating);
        MenuItem contact = menu.findItem(R.id.nav_contact);
        MenuItem privacy = menu.findItem(R.id.nav_privacy);

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

        MenuItem language = menu.findItem(R.id.nav_language);
        View actionView = language.getActionView();
        Switch aSwitch = (Switch) actionView.findViewById(R.id.s_language);
        aSwitch.setOnClickListener(view -> {
            if (aSwitch.isChecked()){
                AppLanguageUtil.getInstance().setAppLanguage(MainActivity2.this, AppLanguageUtil.ARABIC);
            }else {
                AppLanguageUtil.getInstance().setAppLanguage(MainActivity2.this, AppLanguageUtil.English);
            }
            recreate();
        });

        //notification
        binding.appBarMain.ivNotification.setOnClickListener(view -> navigate(NotificationFragment.page));
        binding.appBarMain.llHome.setOnClickListener(view -> navigate(HomeFragment.page));
        binding.appBarMain.llWallet.setOnClickListener(view -> navigate(WalletFragment.page));
        binding.appBarMain.llOrders.setOnClickListener(view -> navigate(OrdersFragment.page));
        binding.appBarMain.llProfile.setOnClickListener(view -> navigate(ProfileFragment.page));
    }

    public void navigate(int page) {
        binding.appBarMain.ivIcHome.setBackgroundResource(R.drawable.ic_home_inactive);
        binding.appBarMain.ivIcWallet.setBackgroundResource(R.drawable.ic_wallet_inactive);
        binding.appBarMain.ivIcOrders.setBackgroundResource(R.drawable.ic_order_inactive);
        binding.appBarMain.ivIcProfile.setBackgroundResource(R.drawable.ic_profile_inactive);
        binding.appBarMain.tvTextHome.setTextColor(getResources().getColor(R.color.hint));
        binding.appBarMain.tvTextWallet.setTextColor(getResources().getColor(R.color.hint));
        binding.appBarMain.tvTextOrders.setTextColor(getResources().getColor(R.color.hint));
        binding.appBarMain.tvTextProfile.setTextColor(getResources().getColor(R.color.hint));

        switch (page) {
            case HomeFragment.page://1
                HomeFragment homeFragment = HomeFragment.newInstance();
                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , homeFragment, R.id.nav_host_fragment_content_main);
                binding.appBarMain.ivIcHome.setBackgroundResource(R.drawable.ic_home_active);
                binding.appBarMain.tvTextHome.setTextColor(getResources().getColor(R.color.colorPrimary));
                binding.appBarMain.tvPageTitle.setText(R.string.home);
                break;

            case WalletFragment.page://2
                WalletFragment walletFragment = WalletFragment.newInstance();
                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , walletFragment, R.id.nav_host_fragment_content_main);
                binding.appBarMain.ivIcWallet.setBackgroundResource(R.drawable.ic_wallet_active);
                binding.appBarMain.tvTextWallet.setTextColor(getResources().getColor(R.color.colorPrimary));
                binding.appBarMain.tvPageTitle.setText(R.string.wallet);
                break;

            case OrdersFragment.page://3
                OrdersFragment ordersFragment = OrdersFragment.newInstance(this);
                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , ordersFragment, R.id.nav_host_fragment_content_main);
                binding.appBarMain.ivIcOrders.setBackgroundResource(R.drawable.ic_order_active);
                binding.appBarMain.tvTextOrders.setTextColor(getResources().getColor(R.color.colorPrimary));
                binding.appBarMain.tvPageTitle.setText(R.string.orders);
                break;

            case ProfileFragment.page://4
                ProfileFragment profileFragment = ProfileFragment.newInstance(this);
                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , profileFragment, R.id.nav_host_fragment_content_main);
                binding.appBarMain.ivIcProfile.setBackgroundResource(R.drawable.ic_profile_active);
                binding.appBarMain.tvTextProfile.setTextColor(getResources().getColor(R.color.colorPrimary));
                binding.appBarMain.tvPageTitle.setText(R.string.profile);
                break;

            case RatingFragment.page:
                new NavigateUtil().activityIntentWithPage(MainActivity2.this, DataActivity.class
                        ,true, RatingFragment.page);
                break;

            case ContactFragment.page:
                new NavigateUtil().activityIntentWithPage(MainActivity2.this, DataActivity.class
                        ,true, ContactFragment.page);
                break;

            case PrivacyPolicyFragment.page:
                new NavigateUtil().activityIntentWithPage(MainActivity2.this, DataActivity.class
                        ,true, PrivacyPolicyFragment.page);
                break;

            case NotificationFragment.page://8
                new NavigateUtil().activityIntentWithPage(MainActivity2.this, DataActivity.class
                        ,true, NotificationFragment.page);
                break;

            case EditProfileFragment.page://9
                /*
                EditProfileFragment editProfileFragment = EditProfileFragment.newInstance(this);
                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , editProfileFragment, R.id.fragment_container);
                 */
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main_activity2, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        /*NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();*/
        return true;
    }
}
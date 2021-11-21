package com.webapp.a4_order_station_driver.feature.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.ActivityOrderBinding;
import com.webapp.a4_order_station_driver.feature.main.MainActivity;
import com.webapp.a4_order_station_driver.feature.main.orders.OrdersFragment;
import com.webapp.a4_order_station_driver.feature.order.newOrderStation.NewOrderStationFragment;
import com.webapp.a4_order_station_driver.feature.order.newPublicOrder.NewPublicOrderFragment;
import com.webapp.a4_order_station_driver.feature.order.orderStationView.OrderStationViewFragment;
import com.webapp.a4_order_station_driver.models.Order;
import com.webapp.a4_order_station_driver.models.OrderStation;
import com.webapp.a4_order_station_driver.models.PublicOrder;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.NavigateUtil;
import com.webapp.a4_order_station_driver.feature.order.chat.ChatFragment;
import com.webapp.a4_order_station_driver.feature.order.publicOrderView.PublicOrderViewFragment;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;

import java.util.Objects;

public class OrderActivity extends BaseActivity {

    public final static int page = 500;

    private ActivityOrderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        super.setRootView(binding.getRoot());
        super.onCreate(savedInstanceState);

        setData();
        click();
    }

    private void click() {
        //binding.ivBack.setOnClickListener(view -> onBackPressed());
    }

    private void setData() {
        navigate(Objects.requireNonNull(getIntent().getExtras()).getInt(AppContent.PAGE));
    }

    @Override
    public void navigate(int page) {
        switch (page) {
            case NewOrderStationFragment.page://6 new order station
                NewOrderStationFragment newOrderFragment = NewOrderStationFragment
                        .newInstance(this, (OrderStation) Objects.requireNonNull(getIntent()
                                .getExtras()).getSerializable(AppContent.ORDER_OBJECT));

                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , newOrderFragment, R.id.fragment_container);
                break;
            case NewPublicOrderFragment.page://10 new public order
                NewPublicOrderFragment newPublicOrderFragment = NewPublicOrderFragment
                        .newInstance(this, (PublicOrder) Objects.requireNonNull(getIntent()
                                .getExtras()).getSerializable(AppContent.ORDER_OBJECT));

                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , newPublicOrderFragment, R.id.fragment_container);
                break;
            case OrderStationViewFragment.page://7 order station view
                OrderStationViewFragment orderViewFragment = OrderStationViewFragment
                        .newInstance(this, (Order) Objects.requireNonNull(getIntent()
                                .getExtras()).getSerializable(AppContent.ORDER_OBJECT));

                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , orderViewFragment, R.id.fragment_container);
                break;
            case PublicOrderViewFragment.page: //public order view and chat
                PublicOrderViewFragment publicChatFragment = PublicOrderViewFragment
                        .newInstance(this, (Order) Objects.requireNonNull(getIntent()
                                .getExtras()).getSerializable(AppContent.ORDER_OBJECT));

                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , publicChatFragment, R.id.fragment_container);
                break;
            case ChatFragment.page: //order station chat
                ChatFragment chatFragment = ChatFragment.newInstance((Order) Objects
                        .requireNonNull(getIntent().getExtras())
                        .getSerializable(AppContent.ORDER_OBJECT));

                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , chatFragment, R.id.fragment_container);
                break;
            case OrdersFragment.page:
                new NavigateUtil().activityIntentWithPage(this, MainActivity.class, false, page);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof ChatFragment) {
            navigate(OrderStationViewFragment.page);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = this.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        /*if (fragment instanceof PublicOrderViewFragment) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }*/
    }
}
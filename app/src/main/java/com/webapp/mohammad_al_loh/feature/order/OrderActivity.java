package com.webapp.mohammad_al_loh.feature.order;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.webapp.mohammad_al_loh.R;
import com.webapp.mohammad_al_loh.databinding.ActivityOrderBinding;
import com.webapp.mohammad_al_loh.feature.main.MainActivity;
import com.webapp.mohammad_al_loh.feature.main.orders.OrdersFragment;
import com.webapp.mohammad_al_loh.feature.order.newOrderStation.NewOrderStationFragment;
import com.webapp.mohammad_al_loh.feature.order.newPublicOrder.NewPublicOrderFragment;
import com.webapp.mohammad_al_loh.feature.order.orderStationView.OrderStationViewFragment;
import com.webapp.mohammad_al_loh.models.Order;
import com.webapp.mohammad_al_loh.models.OrderStation;
import com.webapp.mohammad_al_loh.models.PublicOrder;
import com.webapp.mohammad_al_loh.utils.AppContent;
import com.webapp.mohammad_al_loh.utils.NavigateUtil;
import com.webapp.mohammad_al_loh.feature.order.chat.ChatFragment;
import com.webapp.mohammad_al_loh.feature.order.publicOrderView.PublicOrderViewFragment;
import com.webapp.mohammad_al_loh.utils.language.BaseActivity;

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
        binding.ivBack.setOnClickListener(view -> onBackPressed());
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
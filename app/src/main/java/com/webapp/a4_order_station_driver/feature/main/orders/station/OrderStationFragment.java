package com.webapp.a4_order_station_driver.feature.main.orders.station;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.webapp.a4_order_station_driver.databinding.FragmentOrderStationBinding;
import com.webapp.a4_order_station_driver.feature.main.adapter.OrderStationAdapter;
import com.webapp.a4_order_station_driver.models.OrderStationList;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;

public class OrderStationFragment extends Fragment implements DialogView<OrderStationList> {

    public static final int viewPagerPage = 0;

    private FragmentOrderStationBinding binding;

    private OrderStationAdapter ordersAdapter;
    private Activity baseActivity;
    private OrderStationPresenter presenter;

    public OrderStationFragment(Activity baseActivity) {
        this.baseActivity = baseActivity;
    }

    public static OrderStationFragment newInstance(Activity baseActivity) {
        return new OrderStationFragment(baseActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrderStationBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        initRecycleView();
        presenter = new OrderStationPresenter(baseActivity, this);
    }

    private void initRecycleView() {
        ordersAdapter = new OrderStationAdapter(getActivity(), baseActivity);
        binding.rvOrders.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvOrders.setItemAnimator(new DefaultItemAnimator());
        binding.rvOrders.setAdapter(ordersAdapter);
    }

    @Override
    public void setData(OrderStationList orderStationList) {
        ordersAdapter.addAll(orderStationList.getOrders());
    }

    @Override
    public void showDialog(String s) {
        binding.avi.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDialog() {
        binding.avi.setVisibility(View.GONE);
    }
}

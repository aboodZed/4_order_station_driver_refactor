package com.webapp.a4_order_station_driver.feature.main.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.webapp.a4_order_station_driver.databinding.FragmentOrderStationBinding;
import com.webapp.a4_order_station_driver.feature.main.adapter.OrdersAdapter;
import com.webapp.a4_order_station_driver.models.Arrays;
import com.webapp.a4_order_station_driver.models.OrderStation;
import com.webapp.a4_order_station_driver.utils.APIUtils;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

import java.util.ArrayList;

public class OrderStationFragment extends Fragment {

    private FragmentOrderStationBinding binding;

    private OrdersAdapter ordersAdapter;
    private BaseActivity baseActivity;

    public OrderStationFragment(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    public static OrderStationFragment newInstance(BaseActivity baseActivity) {
        return new OrderStationFragment(baseActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_order_station, container, false);
        binding = FragmentOrderStationBinding.inflate(getLayoutInflater());
        getData();
        return binding.getRoot();
    }

    private void getData() {
        new APIUtils<Arrays>(getActivity()).getData(AppController.getInstance()
                .getApi().getOrders(), new RequestListener<Arrays>() {
            @Override
            public void onSuccess(Arrays arrays, String msg) {
                initRecycleView(arrays.getOrders());
                binding.avi.setVisibility(View.GONE);
            }

            @Override
            public void onError(String msg) {
                ToolUtils.showLongToast(msg, getActivity());
                binding.avi.setVisibility(View.GONE);
            }

            @Override
            public void onFail(String msg) {
                ToolUtils.showLongToast(msg, getActivity());
                binding.avi.setVisibility(View.GONE);
            }
        });
    }


    public void initRecycleView(ArrayList<OrderStation> orders) {
        ordersAdapter = new OrdersAdapter(orders, getActivity(), baseActivity);
        binding.rvOrders.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvOrders.setItemAnimator(new DefaultItemAnimator());
        binding.rvOrders.setAdapter(ordersAdapter);
    }
}

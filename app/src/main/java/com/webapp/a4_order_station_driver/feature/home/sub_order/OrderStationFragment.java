package com.webapp.a4_order_station_driver.feature.home.sub_order;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wang.avi.AVLoadingIndicatorView;
import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.feature.home.adapter.OrdersAdapter;
import com.webapp.a4_order_station_driver.models.Arrays;
import com.webapp.a4_order_station_driver.models.OrderStation;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.view.NavigationView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderStationFragment extends Fragment {

    @BindView(R.id.rv_orders) RecyclerView rvOrders;
    @BindView(R.id.avi) AVLoadingIndicatorView avi;

    private OrdersAdapter ordersAdapter;
    private static NavigationView view;

    public static OrderStationFragment newInstance(NavigationView navigationView) {
        view = navigationView;
        return new OrderStationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_station, container, false);
        ButterKnife.bind(this, view);
        getData();
        return view;
    }

    private void getData() {
        if (ToolUtils.checkTheInternet()) {
            AppController.getInstance().getApi().getOrders().enqueue(new Callback<Arrays>() {
                @Override
                public void onResponse(Call<Arrays> call, Response<Arrays> response) {
                    if (response.isSuccessful()) {
                        initRecycleView(response.body().getOrders());
                        avi.setVisibility(View.GONE);
                    } else {
                        ToolUtils.showError(getActivity(), response.errorBody());
                        avi.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<Arrays> call, Throwable t) {
                    t.printStackTrace();
                    avi.setVisibility(View.GONE);
                }
            });
        } else {
            avi.setVisibility(View.GONE);
            ToolUtils.showLongToast(getString(R.string.no_connection), getActivity());
        }
    }


    public void initRecycleView(ArrayList<OrderStation> orders) {
        ordersAdapter = new OrdersAdapter(orders, getActivity(), view);
        rvOrders.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvOrders.setItemAnimator(new DefaultItemAnimator());
        rvOrders.setAdapter(ordersAdapter);
    }
}

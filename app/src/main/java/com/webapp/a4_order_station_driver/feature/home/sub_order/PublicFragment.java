package com.webapp.a4_order_station_driver.feature.home.sub_order;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wang.avi.AVLoadingIndicatorView;
import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.feature.home.adapter.PublicOrderAdapter;
import com.webapp.a4_order_station_driver.models.PublicOrderList;
import com.webapp.a4_order_station_driver.models.PublicArrays;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.view.NavigationView;
import com.webapp.a4_order_station_driver.utils.view.Tracking;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublicFragment extends Fragment {

    @BindView(R.id.rv_orders) RecyclerView rvOrders;
    @BindView(R.id.avi) AVLoadingIndicatorView avi;

    private PublicOrderAdapter publicOrderAdapter;
    private static NavigationView view;
    private static Tracking tracking;
    private boolean loadingMoreItems;
    private PublicOrderList orderList;

    public static PublicFragment newInstance(NavigationView navigationView, Tracking t) {
        view = navigationView;
        tracking = t;
        return new PublicFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_public, container, false);
        ButterKnife.bind(this, view);
        initRecycleView();
        getData("public/order/driver/ordersList");
        return view;
    }

    private void getData(String s) {
        if (ToolUtils.checkTheInternet()) {
            AppController.getInstance().getApi().getPublicOrders(s)
                    .enqueue(new Callback<PublicArrays>() {
                @Override
                public void onResponse(Call<PublicArrays> call, Response<PublicArrays> response) {
                    loadingMoreItems = false;
                    avi.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        orderList = response.body().getPublicOrders();
                        publicOrderAdapter.addAll(orderList.getData());
                    } else {
                        ToolUtils.showError(getActivity(), response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<PublicArrays> call, Throwable t) {
                    t.printStackTrace();
                    loadingMoreItems = false;
                    avi.setVisibility(View.GONE);
                }
            });
        } else {
            avi.setVisibility(View.GONE);
            ToolUtils.showLongToast(getString(R.string.no_connection), getActivity());
        }
    }

    public void initRecycleView() {
        publicOrderAdapter = new PublicOrderAdapter(getActivity(), view, tracking);
        rvOrders.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvOrders.setItemAnimator(new DefaultItemAnimator());
        rvOrders.setAdapter(publicOrderAdapter);
        rvOrders.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && !TextUtils.isEmpty(orderList.getNext_page_url()) && !loadingMoreItems) {
                    avi.setVisibility(View.VISIBLE);
                    loadingMoreItems = true;
                    getData(orderList.getNext_page_url());
                }
            }
        });
    }
}

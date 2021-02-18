package com.webapp.a4_order_station_driver.feature.home.sub_order;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.webapp.a4_order_station_driver.databinding.FragmentPublicBinding;
import com.webapp.a4_order_station_driver.feature.home.adapter.PublicOrderAdapter;
import com.webapp.a4_order_station_driver.models.PublicArrays;
import com.webapp.a4_order_station_driver.models.PublicOrderList;
import com.webapp.a4_order_station_driver.utils.APIUtils;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;
import com.webapp.a4_order_station_driver.utils.view.NavigationView;
import com.webapp.a4_order_station_driver.utils.view.Tracking;

public class PublicFragment extends Fragment {

    private FragmentPublicBinding binding;

    private PublicOrderAdapter publicOrderAdapter;
    private NavigationView navigationView;
    private Tracking tracking;
    private boolean loadingMoreItems;
    private PublicOrderList orderList;

    public PublicFragment(NavigationView navigationView, Tracking tracking) {
        this.navigationView = navigationView;
        this.tracking = tracking;
    }

    public static PublicFragment newInstance(NavigationView navigationView, Tracking t) {
        return new PublicFragment(navigationView, t);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_public, container, false);
        binding = FragmentPublicBinding.inflate(getLayoutInflater());
        initRecycleView();
        getData("public/order/driver/ordersList");
        return binding.getRoot();
    }

    private void getData(String s) {
        new APIUtils<PublicArrays>(getActivity()).getData(AppController.getInstance()
                .getApi().getPublicOrders(s), new RequestListener<PublicArrays>() {
            @Override
            public void onSuccess(PublicArrays publicArrays, String msg) {
                loadingMoreItems = false;
                binding.avi.setVisibility(View.GONE);
                orderList = publicArrays.getPublicOrders();
                publicOrderAdapter.addAll(orderList.getData());
            }

            @Override
            public void onError(String msg) {
                loadingMoreItems = false;
                binding.avi.setVisibility(View.GONE);
                ToolUtils.showLongToast(msg, getActivity());
            }

            @Override
            public void onFail(String msg) {
                loadingMoreItems = false;
                binding.avi.setVisibility(View.GONE);
                ToolUtils.showLongToast(msg, getActivity());
            }
        });
    }

    public void initRecycleView() {
        publicOrderAdapter = new PublicOrderAdapter(getActivity(), navigationView, tracking);
        binding.rvOrders.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvOrders.setItemAnimator(new DefaultItemAnimator());
        binding.rvOrders.setAdapter(publicOrderAdapter);
        binding.rvOrders.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)
                        && !TextUtils.isEmpty(orderList.getNext_page_url()) && !loadingMoreItems) {

                    binding.avi.setVisibility(View.VISIBLE);
                    loadingMoreItems = true;
                    getData(orderList.getNext_page_url());
                }
            }
        });
    }
}

package com.webapp.a4_order_station_driver.feature.main.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.webapp.a4_order_station_driver.databinding.FragmentOrdersBinding;
import com.webapp.a4_order_station_driver.feature.main.adapter.OrderStationAdapter;
import com.webapp.a4_order_station_driver.models.Orders;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;

public class OrdersFragment extends Fragment implements DialogView<Orders> {

    public static final int page = 203;

    private FragmentOrdersBinding binding;

    private OrderStationAdapter ordersAdapter;
    private static BaseActivity activity;

    public static OrdersFragment newInstance(BaseActivity baseActivity) {
        activity = baseActivity;
        return new OrdersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrdersBinding.inflate(getLayoutInflater());
        new OrdersPresenter(activity,this);
        //setupViewPager();
        return binding.getRoot();
    }
/*
    private void setupViewPager() {

        SectionPageAdapter sectionPageAdapter = new SectionPageAdapter(getChildFragmentManager());
        sectionPageAdapter.addFragment(OrderStationFragment.newInstance(baseActivity), getString(R.string.order_station));
        sectionPageAdapter.addFragment(OrderPublicFragment.newInstance(baseActivity), getString(R.string.public_order));
        binding.vpTabs.setAdapter(sectionPageAdapter);
        binding.tlTabs.setupWithViewPager(binding.vpTabs);
        binding.vpTabs.setCurrentItem(viewPagerPage);

    }
*/
    private void initRecycleView() {
        ordersAdapter = new OrderStationAdapter(activity);
        binding.rvOrdersOthers.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvOrdersOthers.setItemAnimator(new DefaultItemAnimator());
        binding.rvOrdersOthers.setAdapter(ordersAdapter);
    }

    @Override
    public void setData(Orders list) {
        initRecycleView();
        ordersAdapter.addAll(list.getData());
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

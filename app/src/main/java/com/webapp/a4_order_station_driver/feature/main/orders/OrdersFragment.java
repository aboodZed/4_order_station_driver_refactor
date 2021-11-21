package com.webapp.a4_order_station_driver.feature.main.orders;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.FragmentOrdersBinding;
import com.webapp.a4_order_station_driver.feature.main.adapter.SectionPageAdapter;
import com.webapp.a4_order_station_driver.feature.main.orders.publicO.OrderPublicFragment;
import com.webapp.a4_order_station_driver.feature.main.orders.station.OrderStationFragment;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.location.tracking.OrderGPSTracking;

public class OrdersFragment extends Fragment {

    public static final int page = 203;

    private FragmentOrdersBinding binding;

    private Activity baseActivity;
    public static int viewPagerPage = 0;

    public OrdersFragment(Activity baseActivity) {
        this.baseActivity = baseActivity;
    }

    public static OrdersFragment newInstance(Activity baseActivity) {
        return new OrdersFragment(baseActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrdersBinding.inflate(getLayoutInflater());
        //setupViewPager();
        return binding.getRoot();
    }

    private void setupViewPager() {
        /*
        SectionPageAdapter sectionPageAdapter = new SectionPageAdapter(getChildFragmentManager());
        sectionPageAdapter.addFragment(OrderStationFragment.newInstance(baseActivity), getString(R.string.order_station));
        sectionPageAdapter.addFragment(OrderPublicFragment.newInstance(baseActivity), getString(R.string.public_order));
        binding.vpTabs.setAdapter(sectionPageAdapter);
        binding.tlTabs.setupWithViewPager(binding.vpTabs);
        binding.vpTabs.setCurrentItem(viewPagerPage);
        */
    }
}

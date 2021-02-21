package com.webapp.a4_order_station_driver.feature.main.orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.FragmentOrdersBinding;
import com.webapp.a4_order_station_driver.feature.main.adapter.SectionPageAdapter;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.view.Tracking;

public class OrdersFragment extends Fragment {

    public static final int page = 203;

    private FragmentOrdersBinding binding;

    private BaseActivity baseActivity;
    private Tracking tracking;
    public static int viewPagerPage = 0;

    public OrdersFragment(BaseActivity baseActivity, Tracking tracking) {
        this.baseActivity = baseActivity;
        this.tracking = tracking;
    }

    public static OrdersFragment newInstance(BaseActivity baseActivity, Tracking tracking) {
        return new OrdersFragment(baseActivity, tracking);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View v = inflater.inflate(R.layout.fragment_orders, container, false);
        binding = FragmentOrdersBinding.inflate(getLayoutInflater());
        setupViewPager();
        return binding.getRoot();
    }

    private void setupViewPager() {
        SectionPageAdapter sectionPageAdapter = new SectionPageAdapter(getChildFragmentManager());
        sectionPageAdapter.addFragment(OrderStationFragment.newInstance(baseActivity), getString(R.string.order_station));
        sectionPageAdapter.addFragment(PublicFragment.newInstance(baseActivity, tracking), getString(R.string.public_order));
        binding.vpTabs.setAdapter(sectionPageAdapter);
        binding.tlTabs.setupWithViewPager(binding.vpTabs);
        binding.vpTabs.setCurrentItem(viewPagerPage);
    }
}

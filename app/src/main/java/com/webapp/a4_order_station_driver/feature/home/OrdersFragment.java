package com.webapp.a4_order_station_driver.feature.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.feature.home.adapter.SectionPageAdapter;
import com.webapp.a4_order_station_driver.feature.home.sub_order.OrderStationFragment;
import com.webapp.a4_order_station_driver.feature.home.sub_order.PublicFragment;
import com.webapp.a4_order_station_driver.utils.view.NavigationView;
import com.webapp.a4_order_station_driver.utils.view.Tracking;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrdersFragment extends Fragment {

    @BindView(R.id.tv_title) TextView tvTitle;
    @BindView(R.id.tl_tabs) TabLayout tlTabs;
    @BindView(R.id.vp_tabs) ViewPager vpTabs;

    private static NavigationView view;
    private static Tracking tracking;
    public static int page = 0;

    public static OrdersFragment newInstance(NavigationView navigationView, Tracking t) {
        view = navigationView;
        tracking = t;
        return new OrdersFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_orders, container, false);
        ButterKnife.bind(this, v);
        setupViewPager();
        return v;
    }

    private void setupViewPager() {
        SectionPageAdapter sectionPageAdapter = new SectionPageAdapter(getChildFragmentManager());
        sectionPageAdapter.addFragment(OrderStationFragment.newInstance(view), getString(R.string.order_station));
        sectionPageAdapter.addFragment(PublicFragment.newInstance(view, tracking), getString(R.string.public_order));
        vpTabs.setAdapter(sectionPageAdapter);
        tlTabs.setupWithViewPager(vpTabs);
        vpTabs.setCurrentItem(page);
    }
}

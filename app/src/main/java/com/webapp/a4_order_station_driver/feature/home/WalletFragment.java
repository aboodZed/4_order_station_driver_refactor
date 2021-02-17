package com.webapp.a4_order_station_driver.feature.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.feature.home.adapter.SectionPageAdapter;
import com.webapp.a4_order_station_driver.feature.home.sub_wallet.OrderStationWalletFragment;
import com.webapp.a4_order_station_driver.feature.home.sub_wallet.PublicWalletFragment;
import com.webapp.a4_order_station_driver.utils.view.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalletFragment extends Fragment {

    @BindView(R.id.tl_tabs) TabLayout tlTabs;
    @BindView(R.id.vp_tabs) ViewPager vpTabs;

    private static NavigationView view;
    public static int page = 0;

    public static WalletFragment newInstance(NavigationView navigationView) {
        WalletFragment fragment = new WalletFragment();
        view = navigationView;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_wallet, container, false);
        ButterKnife.bind(this, v);
        setupViewPager(vpTabs);
        return v;
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionPageAdapter sectionPageAdapter = new SectionPageAdapter(getChildFragmentManager());
        sectionPageAdapter.addFragment(OrderStationWalletFragment.newInstance(), getString(R.string.order_station));
        sectionPageAdapter.addFragment(PublicWalletFragment.newInstance(), getString(R.string.public_order));
        viewPager.setAdapter(sectionPageAdapter);
        tlTabs.setupWithViewPager(vpTabs);
        viewPager.setCurrentItem(page);
    }
}

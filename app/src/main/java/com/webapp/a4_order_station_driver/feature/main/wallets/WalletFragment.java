package com.webapp.a4_order_station_driver.feature.main.wallets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.FragmentWalletBinding;
import com.webapp.a4_order_station_driver.feature.main.adapter.SectionPageAdapter;
import com.webapp.a4_order_station_driver.feature.main.wallets.publicO.PublicWalletFragment;
import com.webapp.a4_order_station_driver.feature.main.wallets.station.OrderStationWalletFragment;

public class WalletFragment extends Fragment {

    public static final int page = 202;

    private FragmentWalletBinding binding;

    public static int viewPagerPage = 0;

    public static WalletFragment newInstance() {
        WalletFragment fragment = new WalletFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWalletBinding.inflate(getLayoutInflater());
        setupViewPager(binding.vpTabs);
        return binding.getRoot();
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionPageAdapter sectionPageAdapter = new SectionPageAdapter(getChildFragmentManager());
        sectionPageAdapter.addFragment(OrderStationWalletFragment.newInstance(), getString(R.string.order_station));
        sectionPageAdapter.addFragment(PublicWalletFragment.newInstance(), getString(R.string.public_order));
        viewPager.setAdapter(sectionPageAdapter);
        binding.tlTabs.setupWithViewPager(binding.vpTabs);
        viewPager.setCurrentItem(viewPagerPage);
    }
}

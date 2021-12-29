package com.webapp.a4_order_station_driver.feature.main.wallets;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.webapp.a4_order_station_driver.databinding.FragmentWalletBinding;
import com.webapp.a4_order_station_driver.feature.main.adapter.WalletAdapter;
import com.webapp.a4_order_station_driver.models.Ongoing;
import com.webapp.a4_order_station_driver.models.StationWallet;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;

import java.util.ArrayList;

public class WalletFragment extends Fragment implements DialogView<StationWallet> {

    public static final int page = 202;

    private FragmentWalletBinding binding;
    private WalletPresenter presenter;

    public static int viewPagerPage = 0;

    @SuppressLint("StaticFieldLeak")
    private static BaseActivity activity;
    private WalletAdapter walletAdapter;

    public static WalletFragment newInstance(BaseActivity baseActivity) {
        activity = baseActivity;
        return new WalletFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWalletBinding.inflate(getLayoutInflater());
        presenter = new WalletPresenter(activity, this);
        //setupViewPager(binding.vpTabs);
        return binding.getRoot();
    }

    private void setupViewPager(ViewPager viewPager) {
        /*
        SectionPageAdapter sectionPageAdapter = new SectionPageAdapter(getChildFragmentManager());
        sectionPageAdapter.addFragment(OrderStationWalletFragment.newInstance(), getString(R.string.order_station));
        sectionPageAdapter.addFragment(PublicWalletFragment.newInstance(), getString(R.string.public_order));
        viewPager.setAdapter(sectionPageAdapter);
        binding.tlTabs.setupWithViewPager(binding.vpTabs);
        viewPager.setCurrentItem(viewPagerPage);
        */
    }

    private void initRecycleView(ArrayList<Ongoing> ongoings) {
        walletAdapter = new WalletAdapter(ongoings, activity);
        binding.rvBalance.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvBalance.setItemAnimator(new DefaultItemAnimator());
        binding.rvBalance.setAdapter(walletAdapter);
    }

    @Override
    public void setData(StationWallet stationWallet) {
        binding.tvTotalBalance.setText(String.valueOf(stationWallet.getWallet()));
        binding.tvCurrency.setText(AppController.getInstance().getAppSettingsPreferences()
                .getUser().getCountry().getCurrency_code());
        initRecycleView(stationWallet.getOngoings());
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

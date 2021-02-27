package com.webapp.a4_order_station_driver.feature.main.wallets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.webapp.a4_order_station_driver.databinding.FragmentOrderStationWalletBinding;
import com.webapp.a4_order_station_driver.feature.main.adapter.WalletAdapter;
import com.webapp.a4_order_station_driver.models.Ongoing;
import com.webapp.a4_order_station_driver.models.StationWallet;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.formatter.DecimalFormatterManager;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

import java.util.ArrayList;

public class OrderStationWalletFragment extends Fragment {

    public static final int viewPagerPage = 0;

    private FragmentOrderStationWalletBinding binding;

    private WalletAdapter walletAdapter;

    public static OrderStationWalletFragment newInstance() {
        OrderStationWalletFragment fragment = new OrderStationWalletFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // View view = inflater.inflate(R.layout.fragment_order_station_wallet, container, false);
        binding = FragmentOrderStationWalletBinding.inflate(getLayoutInflater());
        getData();
        return binding.getRoot();
    }

    private void getData() {
        new APIUtil<StationWallet>(getActivity()).getData(AppController.getInstance()
                .getApi().getWalletDetails(), new RequestListener<StationWallet>() {
            @Override
            public void onSuccess(StationWallet stationWallet, String msg) {
                setData(stationWallet);
                binding.avi.setVisibility(View.GONE);
            }

            @Override
            public void onError(String msg) {
                binding.avi.setVisibility(View.GONE);
                ToolUtil.showLongToast(msg, getActivity());
            }

            @Override
            public void onFail(String msg) {
                binding.avi.setVisibility(View.GONE);
                ToolUtil.showLongToast(msg, getActivity());
            }
        });
    }

    private void setData(StationWallet stationWallet) {
        initRecycleView(stationWallet.getOngoings());
        binding.tvTotalBalance.setText(DecimalFormatterManager.getFormatterInstance()
                .format(stationWallet.getWallet()) + " " + AppController.getInstance()
                .getAppSettingsPreferences().getCountry().getCurrency_code());
    }

    private void initRecycleView(ArrayList<Ongoing> ongoings) {
        walletAdapter = new WalletAdapter(ongoings);
        binding.rvBalance.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvBalance.setItemAnimator(new DefaultItemAnimator());
        binding.rvBalance.setAdapter(walletAdapter);
    }
}

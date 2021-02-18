package com.webapp.a4_order_station_driver.feature.home.sub_wallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.webapp.a4_order_station_driver.databinding.FragmentPublicWalletBinding;
import com.webapp.a4_order_station_driver.feature.home.adapter.PublicWalletAdapter;
import com.webapp.a4_order_station_driver.models.PublicOrder;
import com.webapp.a4_order_station_driver.models.PublicWallet;
import com.webapp.a4_order_station_driver.utils.APIUtils;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.formatter.DecimalFormatterManager;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

import java.util.ArrayList;

public class PublicWalletFragment extends Fragment {

    private FragmentPublicWalletBinding binding;

    private PublicWalletAdapter publicWalletAdapter;

    public static PublicWalletFragment newInstance() {
        PublicWalletFragment fragment = new PublicWalletFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_public_wallet, container, false);
        binding = FragmentPublicWalletBinding.inflate(getLayoutInflater());
        getData();
        return binding.getRoot();
    }

    private void getData() {
        new APIUtils<PublicWallet>(getActivity()).getData(AppController.getInstance()
                .getApi().getPublicWallet(), new RequestListener<PublicWallet>() {
            @Override
            public void onSuccess(PublicWallet publicWallet, String msg) {
                setData(publicWallet);
                binding.avi.setVisibility(View.GONE);
            }

            @Override
            public void onError(String msg) {
                ToolUtils.showLongToast(msg, getActivity());
                binding.avi.setVisibility(View.GONE);
            }

            @Override
            public void onFail(String msg) {
                ToolUtils.showLongToast(msg, getActivity());
                binding.avi.setVisibility(View.GONE);
            }
        });
    }


    private void setData(PublicWallet publicWallet) {
        String currency = AppController.getInstance().getAppSettingsPreferences().getCountry().getCurrency_code();

        binding.tvTotalBalance.setText(DecimalFormatterManager.getFormatterInstance()
                .format(Double.parseDouble(publicWallet.getWallet())
                        + Double.parseDouble(publicWallet.getTotalClientBills())) + " " + currency);
        binding.tvTotalOrdersPrice.setText(DecimalFormatterManager.getFormatterInstance()
                .format(Double.parseDouble(publicWallet.getTotal_orders_amount())) + " " + currency);
        binding.tvDelegateDues.setText(DecimalFormatterManager.getFormatterInstance()
                .format(publicWallet.getTotal_driver_revenue()) + " " + currency);
        binding.tvAppDues.setText(DecimalFormatterManager.getFormatterInstance()
                .format(publicWallet.getTotal_app_revenue()) + " " + currency);
        binding.tvDriverBills.setText(DecimalFormatterManager.getFormatterInstance()
                .format(Double.parseDouble(publicWallet.getTotalClientBills())) + " " + currency);
        initRecycleView(publicWallet.getPublicOrders());
    }

    private void initRecycleView(ArrayList<PublicOrder> publicOrders) {
        publicWalletAdapter = new PublicWalletAdapter(publicOrders);
        binding.rvBalance.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvBalance.setItemAnimator(new DefaultItemAnimator());
        binding.rvBalance.setAdapter(publicWalletAdapter);
    }
}

package com.webapp.a4_order_station_driver.feature.home.sub_wallet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;
import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.feature.home.adapter.PublicWalletAdapter;
import com.webapp.a4_order_station_driver.models.PublicOrder;
import com.webapp.a4_order_station_driver.models.PublicWallet;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.formatter.DecimalFormatterManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublicWalletFragment extends Fragment {

    @BindView(R.id.tv_total_balance) TextView tvTotalBalance;
    @BindView(R.id.tv_total_orders_price) TextView tvTotalOrdersPrice;
    @BindView(R.id.tv_delegate_dues) TextView tvDelegateDues;
    @BindView(R.id.tv_app_dues) TextView tvAppDues;
    @BindView(R.id.tv_driver_bills) TextView tvDriverBills;
    @BindView(R.id.rv_balance) RecyclerView rvBalance;
    @BindView(R.id.avi) AVLoadingIndicatorView avi;

    private PublicWalletAdapter publicWalletAdapter;

    public static PublicWalletFragment newInstance() {
        PublicWalletFragment fragment = new PublicWalletFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_public_wallet, container, false);
        ButterKnife.bind(this, view);
        setData();
        return view;
    }

    private void setData() {
        String currency = AppController.getInstance().getAppSettingsPreferences().getCountry().getCurrency_code();

        if (ToolUtils.checkTheInternet()) {
            AppController.getInstance().getApi().getPublicWallet().enqueue(new Callback<PublicWallet>() {
                @Override
                public void onResponse(Call<PublicWallet> call, Response<PublicWallet> response) {
                    if (response.isSuccessful()) {
                        tvTotalBalance.setText(DecimalFormatterManager.getFormatterInstance()
                                .format(Double.parseDouble(response.body().getWallet()) + Double.parseDouble(response.body().getTotalClientBills())) + " " + currency);
                        tvTotalOrdersPrice.setText(DecimalFormatterManager.getFormatterInstance()
                                .format(Double.parseDouble(response.body().getTotal_orders_amount())) + " " + currency);
                        tvDelegateDues.setText(DecimalFormatterManager.getFormatterInstance()
                                .format(response.body().getTotal_driver_revenue()) + " " + currency);
                        tvAppDues.setText(DecimalFormatterManager.getFormatterInstance()
                                .format(response.body().getTotal_app_revenue()) + " " + currency);
                        tvDriverBills.setText(DecimalFormatterManager.getFormatterInstance()
                                .format(Double.parseDouble(response.body().getTotalClientBills())) + " " + currency);
                        initRecycleView(response.body().getPublicOrders());
                    } else {
                        ToolUtils.showError(getActivity(), response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<PublicWallet> call, Throwable t) {
                    ToolUtils.showLongToast(t.getMessage(), getActivity());
                }
            });
        } else {
            ToolUtils.showLongToast(getString(R.string.no_connection), getActivity());
        }
    }

    private void initRecycleView(ArrayList<PublicOrder> publicOrders) {
        publicWalletAdapter = new PublicWalletAdapter(publicOrders);
        rvBalance.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvBalance.setItemAnimator(new DefaultItemAnimator());
        rvBalance.setAdapter(publicWalletAdapter);
        avi.setVisibility(View.GONE);
    }
}

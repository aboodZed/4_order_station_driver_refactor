package com.webapp.a4_order_station_driver.feature.home.sub_wallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wang.avi.AVLoadingIndicatorView;
import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.feature.home.adapter.WalletAdapter;
import com.webapp.a4_order_station_driver.models.Ongoing;
import com.webapp.a4_order_station_driver.models.StationWallet;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.formatter.DecimalFormatterManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderStationWalletFragment extends Fragment {

    @BindView(R.id.tv_total_balance) TextView tvTotalBalance;
    @BindView(R.id.rv_balance) RecyclerView rvBalance;
    @BindView(R.id.avi) AVLoadingIndicatorView avi;

    private WalletAdapter walletAdapter;

    public static OrderStationWalletFragment newInstance() {
        OrderStationWalletFragment fragment = new OrderStationWalletFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_station_wallet, container, false);
        ButterKnife.bind(this, view);
        setData();
        return view;
    }


    private void setData() {
        if (ToolUtils.checkTheInternet()) {
            AppController.getInstance().getApi().getWalletDetails().enqueue(new Callback<StationWallet>() {
                @Override
                public void onResponse(Call<StationWallet> call, Response<StationWallet> response) {
                    if (response.isSuccessful()) {
                        initRecycleView(response.body().getOngoings());
                        tvTotalBalance.setText(DecimalFormatterManager.getFormatterInstance()
                                .format(response.body().getWallet()) + " " + AppController.getInstance()
                        .getAppSettingsPreferences().getCountry().getCurrency_code());
                        avi.setVisibility(View.GONE);
                    } else {
                        ToolUtils.showError(getActivity(), response.errorBody());
                        avi.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<StationWallet> call, Throwable t) {
                    t.printStackTrace();
                    avi.setVisibility(View.GONE);
                }
            });
        } else {
            ToolUtils.showLongToast(getString(R.string.no_connection), getActivity());
        }
    }


    private void initRecycleView(ArrayList<Ongoing> ongoings) {
        walletAdapter = new WalletAdapter(ongoings);
        rvBalance.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvBalance.setItemAnimator(new DefaultItemAnimator());
        rvBalance.setAdapter(walletAdapter);
    }
}

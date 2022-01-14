package com.webapp.mohammad_al_loh.feature.main.orders.station;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.webapp.mohammad_al_loh.databinding.FragmentOrderStationBinding;
import com.webapp.mohammad_al_loh.feature.main.adapter.OrderStationAdapter;
import com.webapp.mohammad_al_loh.models.OrderStationList;
import com.webapp.mohammad_al_loh.utils.language.BaseActivity;
import com.webapp.mohammad_al_loh.utils.listeners.DialogView;

public class OrderStationFragment extends Fragment implements DialogView<OrderStationList> {

    public static final int viewPagerPage = 0;

    private FragmentOrderStationBinding binding;

    private OrderStationAdapter ordersAdapter;
    private BaseActivity baseActivity;
    private OrderStationPresenter presenter;

    public OrderStationFragment(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    public static OrderStationFragment newInstance(BaseActivity baseActivity) {
        return new OrderStationFragment(baseActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrderStationBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        initRecycleView();
        presenter = new OrderStationPresenter(baseActivity, this);
    }

    private void initRecycleView() {
        ordersAdapter = new OrderStationAdapter(getActivity(), baseActivity);
        binding.rvOrders.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvOrders.setItemAnimator(new DefaultItemAnimator());
        binding.rvOrders.setAdapter(ordersAdapter);
    }

    @Override
    public void setData(OrderStationList orderStationList) {
        ordersAdapter.addAll(orderStationList.getOrders());
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

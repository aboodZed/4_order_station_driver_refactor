package com.webapp.mohammad_al_loh.feature.main.orders.publicO;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.webapp.mohammad_al_loh.databinding.FragmentPublicBinding;
import com.webapp.mohammad_al_loh.feature.main.adapter.PublicOrderAdapter;
import com.webapp.mohammad_al_loh.models.PublicOrderList;
import com.webapp.mohammad_al_loh.models.PublicOrderListObject;
import com.webapp.mohammad_al_loh.utils.language.BaseActivity;
import com.webapp.mohammad_al_loh.utils.listeners.DialogView;

public class OrderPublicFragment extends Fragment implements DialogView<PublicOrderListObject> {

    public static final int viewPagerPage = 1;

    private FragmentPublicBinding binding;

    private PublicOrderAdapter publicOrderAdapter;
    private BaseActivity baseActivity;
    private boolean loadingMoreItems;
    private PublicOrderList orderList;
    private OrderPublicPresenter presenter;

    public OrderPublicFragment(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    public static OrderPublicFragment newInstance(BaseActivity baseActivity) {
        return new OrderPublicFragment(baseActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPublicBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        initRecycleView();
        presenter = new OrderPublicPresenter(baseActivity, this);
    }

    public void initRecycleView() {
        publicOrderAdapter = new PublicOrderAdapter(baseActivity);
        binding.rvOrders.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvOrders.setItemAnimator(new DefaultItemAnimator());
        binding.rvOrders.setAdapter(publicOrderAdapter);
        binding.rvOrders.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && !TextUtils
                        .isEmpty(orderList.getNext_page_url()) && !loadingMoreItems) {
                    presenter.getData(orderList.getNext_page_url());
                }
            }
        });
    }

    @Override
    public void setData(PublicOrderListObject publicOrderListObject) {
        orderList = publicOrderListObject.getPublicOrders();
        publicOrderAdapter.addAll(orderList.getData());
    }

    @Override
    public void showDialog(String s) {
        binding.avi.setVisibility(View.VISIBLE);
        loadingMoreItems = true;
    }

    @Override
    public void hideDialog() {
        loadingMoreItems = false;
        binding.avi.setVisibility(View.GONE);
    }
}

package com.webapp.a4_order_station_driver.feature.order;

import android.os.Bundle;

import com.webapp.a4_order_station_driver.databinding.ActivityOrderBinding;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;

public class OrderActivity extends BaseActivity {

    private ActivityOrderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        super.setRootView(binding.getRoot());
        super.onCreate(savedInstanceState);
    }

    @Override
    public void navigate(int page) {

    }
}
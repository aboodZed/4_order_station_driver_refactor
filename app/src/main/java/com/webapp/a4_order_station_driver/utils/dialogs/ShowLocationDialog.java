package com.webapp.a4_order_station_driver.utils.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;

import com.google.android.gms.maps.model.LatLng;
import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.FragmentShowLocationBinding;
import com.webapp.a4_order_station_driver.models.PublicOrder;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.NavigateUtils;

public class ShowLocationDialog extends DialogFragment {

    private FragmentShowLocationBinding binding;

    private PublicOrder order;

    public static ShowLocationDialog newInstance(PublicOrder order) {
        ShowLocationDialog fragment = new ShowLocationDialog();
        Bundle args = new Bundle();
        args.putSerializable(AppContent.INPUT_ORDER, order);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_show_location, container, false);
        binding = FragmentShowLocationBinding.inflate(getLayoutInflater());
        if (getArguments() != null) {
            order = (PublicOrder) getArguments().getSerializable(AppContent.INPUT_ORDER);
            binding.tvStoreName.setText(order.getStore_name());
            binding.tvCustomerAddress.setText(order.getDestination_address());
        }
        click();
        return binding.getRoot();
    }

    private void click() {
        binding.clStoreLocation.setOnClickListener(view -> {
            new NavigateUtils().setLocation(getActivity()
                    , new LatLng(Double.parseDouble(order.getStore_lat())
                            , Double.parseDouble(order.getStore_lng())));
        });
        binding.clCustomerLocation.setOnClickListener(view ->new NavigateUtils().setLocation(getActivity()
                , new LatLng(Double.parseDouble(order.getDestination_lat())
                        , Double.parseDouble(order.getDestination_lng()))));
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.transparent);
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        setCancelable(false);
        super.onResume();
        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if ((keyCode == android.view.KeyEvent.KEYCODE_BACK)) {
                dismiss();
                return true;
            } else return false;
        });
    }
}

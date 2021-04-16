package com.webapp.a4_order_station_driver.utils.dialogs;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.DialogUpdateNeighborhoodBinding;
import com.webapp.a4_order_station_driver.feature.register.adapter.SpinnerAdapter;
import com.webapp.a4_order_station_driver.models.Neighborhood;
import com.webapp.a4_order_station_driver.models.NeighborhoodList;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

import java.util.ArrayList;

public class UpdateNeighborhoodDialog extends DialogFragment {

    private DialogUpdateNeighborhoodBinding binding;
    private SpinnerAdapter spinnerAdapter;
    private BaseActivity baseActivity;

    public UpdateNeighborhoodDialog(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogUpdateNeighborhoodBinding.inflate(getLayoutInflater());
        //data();
        //test
        ArrayList<Neighborhood> neighborhoods = new ArrayList<>();
        neighborhoods.add(new Neighborhood(1, "غزة"));
        neighborhoods.add(new Neighborhood(2, "رفح"));
        neighborhoods.add(new Neighborhood(3, "خانيونس"));
        NeighborhoodList neighborhoodList = new NeighborhoodList();
        neighborhoodList.setNeighborhoods(neighborhoods);
        setData(neighborhoodList);
        //test
        click();
        return binding.getRoot();
    }

    private void click() {
        binding.btnNext.setOnClickListener((View.OnClickListener) view -> {
            ToolUtil.showLongToast(((Neighborhood) binding.spNeighborhood
                    .getSelectedItem()).toString(), baseActivity);
            dismiss();
        });
    }

    private void data() {
        WaitDialogFragment.newInstance().show(getChildFragmentManager(), "");
        new APIUtil<NeighborhoodList>(baseActivity).getData(AppController.getInstance().getApi()
                .getNeighborhood(0), new RequestListener<NeighborhoodList>() {
            @Override
            public void onSuccess(NeighborhoodList neighborhoodList, String msg) {
                setData(neighborhoodList);
                WaitDialogFragment.newInstance().dismiss();
            }

            @Override
            public void onError(String msg) {
                WaitDialogFragment.newInstance().dismiss();
                ToolUtil.showLongToast(msg, baseActivity);
            }

            @Override
            public void onFail(String msg) {
                WaitDialogFragment.newInstance().dismiss();
                ToolUtil.showLongToast(msg, baseActivity);
            }
        });
    }

    private void setData(NeighborhoodList neighborhoodList) {
        spinnerAdapter = new SpinnerAdapter(baseActivity, neighborhoodList.getNeighborhoods());
        binding.spNeighborhood.setAdapter(spinnerAdapter);
        binding.spNeighborhood.getSelectedItem();
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.transparent);
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        setCancelable(false);
        super.onResume();
        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                UpdateNeighborhoodDialog.this.dismiss();
                return true;
            } else return false;
        });
    }
}

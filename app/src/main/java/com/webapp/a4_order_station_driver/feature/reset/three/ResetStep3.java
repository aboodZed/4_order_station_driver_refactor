package com.webapp.a4_order_station_driver.feature.reset.three;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.webapp.a4_order_station_driver.databinding.FragmentResetStep3Binding;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;

public class ResetStep3 extends Fragment implements DialogView<Message> {

    public static final int page = 403;

    private FragmentResetStep3Binding binding;

    private BaseActivity baseActivity;

    private ResetStep3Presenter presenter;

    public ResetStep3(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
        presenter = new ResetStep3Presenter(baseActivity, this);
    }

    public static ResetStep3 newInstance(BaseActivity baseActivity) {
        ResetStep3 fragment = new ResetStep3(baseActivity);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentResetStep3Binding.inflate(getLayoutInflater());
        click();
        return binding.getRoot();
    }

    private void click() {
        binding.btnConfirm.setOnClickListener(view -> presenter.ValidInput(binding.etEnterPassword, binding.etEnterConfirmPassword));
    }

    @Override
    public void setData(Message message) {

    }

    @Override
    public void showDialog(String s) {
        WaitDialogFragment.newInstance().show(getChildFragmentManager(), "");
    }

    @Override
    public void hideDialog() {
        WaitDialogFragment.newInstance().dismiss();
    }
}

package com.webapp.a4_order_station_driver.feature.reset.one;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.FragmentResetStep1Binding;
import com.webapp.a4_order_station_driver.feature.reset.two.ResetStep2;
import com.webapp.a4_order_station_driver.models.ResetCode;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;

import java.util.HashMap;

import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetStep1 extends Fragment implements DialogView<ResetCode> {

    public static final int page = 401;

    private FragmentResetStep1Binding binding;

    private ResetStep1Presenter presenter;
    private BaseActivity baseActivity;
    private ResetCode resetCode;

    public ResetStep1(BaseActivity baseActivity) {
        presenter = new ResetStep1Presenter(baseActivity, this);
    }

    public static ResetStep1 newInstance(BaseActivity baseActivity) {
        ResetStep1 fragment = new ResetStep1(baseActivity);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentResetStep1Binding.inflate(getLayoutInflater());
        //View v = inflater.inflate(R.layout.fragment_reset_step1, container, false);
        //ButterKnife.bind(this, v);
        //tvCode.setText(AppController.getInstance().getAppSettingsPreferences().getCountry().getPhone_code());
        click();
        return binding.getRoot();
    }

    private void click() {
        binding.btnSend.setOnClickListener(view -> presenter.validInput(binding.etEnterPhone));
    }

    public ResetCode getData() {
        return resetCode;
    }

    @Override
    public void setData(ResetCode resetCode) {
        this.resetCode = resetCode;
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

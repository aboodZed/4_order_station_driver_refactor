package com.webapp.a4_order_station_driver.feature.reset.two;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.annotations.NotNull;
import com.poovam.pinedittextfield.PinField;
import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.FragmentResetStep2Binding;
import com.webapp.a4_order_station_driver.models.ResetCode;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;

import java.util.HashMap;

public class ResetStep2 extends Fragment
        implements PinField.OnTextCompleteListener, TextWatcher
        , DialogView<HashMap<String,String>> {

    public static final int page = 402;

    private FragmentResetStep2Binding binding;

    private ResetStep2Presenter presenter;
    private HashMap<String,String> map;
    private BaseActivity baseActivity;
    private String code;

    public ResetStep2(BaseActivity baseActivity, HashMap<String,String> map) {
        this.baseActivity = baseActivity;
        presenter = new ResetStep2Presenter(baseActivity, this, map);
    }

    public static ResetStep2 newInstance(BaseActivity baseActivity, HashMap<String,String> map) {
        ResetStep2 fragment = new ResetStep2(baseActivity, map);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentResetStep2Binding.inflate(getLayoutInflater());
        initListener();
        click();
        return binding.getRoot();
    }

    private void click() {
//        binding.btnResend.setOnClickListener(view -> presenter.reSend(resetCode.getMobile()));
//        binding.btnConfirm.setOnClickListener(view -> presenter.conform(resetCode, verificationCode, countDownTimer));
        binding.btnConfirm.setOnClickListener(v -> presenter.verify(code));
    }

    //function
    private void initListener() {
        binding.lfCode.setOnTextCompleteListener(this);
        binding.lfCode.addTextChangedListener(this);
        binding.lfCode.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
         /*binding.btnResend.setVisibility(View.INVISIBLE);
       countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                binding.tvResendText.setText(getString(R.string.resend) + " " + (millisUntilFinished / 1000) + " sec");
            }

            public void onFinish() {
                binding.btnResend.setVisibility(View.VISIBLE);
            }
        }.start();*/
    }

    @Override
    public void setData(HashMap<String,String> map) {
    }

    @Override
    public void showDialog(String s) {
        WaitDialogFragment.newInstance().show(getChildFragmentManager(), "");
    }

    @Override
    public void hideDialog() {
        WaitDialogFragment.newInstance().dismiss();
    }

    //overrides
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        code = editable.toString();
    }

    @Override
    public boolean onTextComplete(@NotNull String s) {
        code = s;
        return false;
    }
}

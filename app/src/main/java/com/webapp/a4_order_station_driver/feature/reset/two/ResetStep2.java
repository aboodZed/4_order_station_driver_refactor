package com.webapp.a4_order_station_driver.feature.reset.two;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.FragmentResetStep2Binding;
import com.webapp.a4_order_station_driver.feature.reset.three.ResetStep3;
import com.webapp.a4_order_station_driver.models.Login;
import com.webapp.a4_order_station_driver.models.ResetCode;
import com.webapp.a4_order_station_driver.models.VerifyCode;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.poovam.pinedittextfield.PinField;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;

import org.jetbrains.annotations.NotNull;

import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetStep2 extends Fragment
        implements PinField.OnTextCompleteListener, TextWatcher
        , DialogView<ResetCode> {

    public static final int page = 402;

    private FragmentResetStep2Binding binding;

    private ResetStep2Presenter presenter;
    private String verificationCode;
    private BaseActivity baseActivity;
    private ResetCode resetCode;
    private CountDownTimer countDownTimer;

    public ResetStep2(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
        presenter = new ResetStep2Presenter(baseActivity, this);
    }

    public static ResetStep2 newInstance(BaseActivity baseActivity) {
        ResetStep2 fragment = new ResetStep2(baseActivity);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentResetStep2Binding.inflate(getLayoutInflater());
        /*View v = inflater.inflate(R.layout.fragment_reset_step2, container, false);
        ButterKnife.bind(this, v);*/
        initListener();
        click();
        return binding.getRoot();
    }

    private void click() {
        binding.btnResend.setOnClickListener(view -> presenter.reSend(resetCode.getMobile()));
        binding.btnConfirm.setOnClickListener(view -> presenter.conform(resetCode, verificationCode, countDownTimer));
    }

    //function
    private void initListener() {
        binding.lfCode.setOnTextCompleteListener(this);
        binding.lfCode.addTextChangedListener(this);
        binding.lfCode.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        binding.btnResend.setVisibility(View.INVISIBLE);
        countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                binding.tvResendText.setText(getString(R.string.resend) + " " + (millisUntilFinished / 1000) + " sec");
            }

            public void onFinish() {
                binding.btnResend.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    @Override
    public void setData(ResetCode data) {
        resetCode = data;
        Log.e("reset_code",resetCode.toString());
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
        verificationCode = editable.toString();
    }

    @Override
    public boolean onTextComplete(@NotNull String s) {
        verificationCode = s;
        return false;
    }
}

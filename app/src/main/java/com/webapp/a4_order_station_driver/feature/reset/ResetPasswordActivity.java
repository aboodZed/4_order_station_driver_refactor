package com.webapp.a4_order_station_driver.feature.reset;

import android.os.Bundle;
import android.view.WindowManager;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.ActivityResetPasswordBinding;
import com.webapp.a4_order_station_driver.feature.main.MainActivity;
import com.webapp.a4_order_station_driver.feature.login.LoginActivity;
import com.webapp.a4_order_station_driver.feature.reset.one.ResetStep1;
import com.webapp.a4_order_station_driver.feature.reset.three.ResetStep3;
import com.webapp.a4_order_station_driver.feature.reset.two.ResetStep2;
import com.webapp.a4_order_station_driver.utils.NavigateUtil;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;

public class ResetPasswordActivity extends BaseActivity {

    public static final int page = 400;

    private ActivityResetPasswordBinding binding;

    private ResetStep1 resetStep1;
    private ResetStep2 resetStep2;
    private ResetStep3 resetStep3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        super.setRootView(binding.getRoot());
        super.onCreate(savedInstanceState);
        //view
        initFragments();
        click();
    }

    private void click() {
        //binding.ivBack.setOnClickListener(view -> back());
    }

    //Clicks
    public void back() {
        if (getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container) instanceof ResetStep1) {
            navigate(LoginActivity.page);
        } else if (getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container) instanceof ResetStep2) {
            navigate(ResetStep1.page);
        } else if (getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container) instanceof ResetStep3) {
            navigate(ResetStep2.page);
        }
    }

    //functions
    private void initFragments() {
        navigate(ResetStep1.page);
    }

    @Override
    public void navigate(int page) {
        switch (page) {
            case ResetStep1.page:
                resetStep1 = ResetStep1.newInstance(this);
                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , resetStep1, R.id.fragment_container);
                break;
            case ResetStep2.page:
                resetStep2 = ResetStep2.newInstance(this);
                //resetStep2.setData(resetStep1.getData());
                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , resetStep2, R.id.fragment_container);
                break;
            case ResetStep3.page:
                resetStep3 = ResetStep3.newInstance(this);
                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , resetStep3, R.id.fragment_container);
                break;
            case MainActivity.page:
                new NavigateUtil().activityIntent(this, MainActivity.class, false);
                break;
            case LoginActivity.page:
                new NavigateUtil().activityIntent(this, LoginActivity.class, false);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }
}

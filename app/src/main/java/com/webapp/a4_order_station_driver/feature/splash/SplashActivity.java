package com.webapp.a4_order_station_driver.feature.splash;

import android.os.Bundle;
import android.view.WindowManager;

import com.webapp.a4_order_station_driver.databinding.ActivitySplachBinding;
import com.webapp.a4_order_station_driver.feature.login.LoginActivity;
import com.webapp.a4_order_station_driver.feature.main.MainActivity2;
import com.webapp.a4_order_station_driver.utils.NavigateUtil;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;

public class SplashActivity extends BaseActivity {

    private ActivitySplachBinding binding;
    private SplashPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivitySplachBinding.inflate(getLayoutInflater());
        super.setRootView(binding.getRoot());
        super.onCreate(savedInstanceState);
        // Hide the status bar.
        /*iew decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                , WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);*/
        //test
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //presenter
        presenter = new SplashPresenter(this);
    }

    @Override
    public void navigate(int page) {
        switch (page) {
            case MainActivity2.page:
                new NavigateUtil().activityIntent(this
                        , MainActivity2.class, false);
                break;
            case LoginActivity.page:
                new NavigateUtil().activityIntent(this
                        , LoginActivity.class, false);
                break;
        }
    }
}

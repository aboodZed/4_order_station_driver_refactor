package com.webapp.a4_order_station_driver.feature.splash;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.webapp.a4_order_station_driver.databinding.ActivitySplachBinding;
import com.webapp.a4_order_station_driver.feature.main.MainActivity;
import com.webapp.a4_order_station_driver.feature.login.LoginActivity;
import com.webapp.a4_order_station_driver.utils.NavigateUtil;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
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
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                , WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //presenter
        presenter = new SplashPresenter(this);
    }

    @Override
    public void navigate(int page) {
        switch (page) {
            case MainActivity.page:
                new NavigateUtil().activityIntent(this
                        , MainActivity.class, false);
                break;
            case LoginActivity.page:
                new NavigateUtil().activityIntent(this
                        , LoginActivity.class, false);
                break;
        }
    }
}

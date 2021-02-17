package com.webapp.a4_order_station_driver.feature.register;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.ActivityRegisterBinding;
import com.webapp.a4_order_station_driver.feature.home.MainActivity;
import com.webapp.a4_order_station_driver.feature.login.LoginActivity;
import com.webapp.a4_order_station_driver.feature.register.one.RegisterStep_1;
import com.webapp.a4_order_station_driver.feature.register.two.RegisterStep_2;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.NavigateUtils;
import com.webapp.a4_order_station_driver.utils.PermissionUtil;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;

public class RegisterActivity extends BaseActivity {

    public static final int page = 300;

    private ActivityRegisterBinding binding;

    private RegisterStep_1 registerStep1;
    private RegisterStep_2 registerStep2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        super.setRootView(binding.getRoot());
        super.onCreate(savedInstanceState);
        //view
        initFragment();
        click();
    }

    //functions
    private void initFragment() {
        registerStep1 = RegisterStep_1.newInstance(this);
        registerStep2 = RegisterStep_2.newInstance(this);
        if (!PermissionUtil.isPermissionGranted(MediaStore.ACTION_IMAGE_CAPTURE, this)) {
            PermissionUtil.requestPermission(this, Manifest.permission.CAMERA
                    , AppContent.REQUEST_PERMISSIONS_R_W_STORAGE_CAMERA);
        }
        navigate(RegisterStep_1.page);
    }


    private void click() {
        setOnClickListeners(new View[]{binding.btnNext, binding.ivBack},
                view -> {
                    switch (view.getId()) {
                        case R.id.btn_next:
                            next();
                            break;
                        case R.id.iv_back:
                            back();
                            break;
                    }
                });
    }

    //clicks
    public void next() {
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof RegisterStep_1) {
            registerStep1.signUp();
        } else {
            registerStep2.signUp();
        }
    }

    public void back() {
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof RegisterStep_2) {
            navigate(RegisterStep_1.page);
        } else {
            navigate(LoginActivity.page);
        }
    }

    @Override
    public void navigate(int page) {
        switch (page) {
            case RegisterStep_1.page:
                binding.tvPage.setText("1/2");
                new NavigateUtils().replaceFragment(getSupportFragmentManager(), registerStep1, R.id.fragment_container);
                break;
            case RegisterStep_2.page:
                binding.tvPage.setText("2/2");
                new NavigateUtils().replaceFragment(getSupportFragmentManager(), registerStep2, R.id.fragment_container);
                break;
            case MainActivity.page:
                new NavigateUtils().activityIntent(this, MainActivity.class, false);
                break;
            case LoginActivity.page:
                new NavigateUtils().activityIntent(this, LoginActivity.class, false);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case AppContent.REQUEST_PERMISSIONS_R_W_STORAGE_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.permission_garnted, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(this, R.string.permission_denial, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final Fragment fragmentInFrame = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragmentInFrame instanceof RegisterStep_2) {
            fragmentInFrame.onActivityResult(requestCode, resultCode, data);
        }
    }
}

package com.webapp.mohammad_al_loh.feature.register;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.webapp.mohammad_al_loh.R;
import com.webapp.mohammad_al_loh.databinding.ActivityRegisterBinding;
import com.webapp.mohammad_al_loh.feature.main.MainActivity;
import com.webapp.mohammad_al_loh.feature.login.LoginActivity;
import com.webapp.mohammad_al_loh.feature.register.one.RegisterStepOneFragment;
import com.webapp.mohammad_al_loh.feature.register.two.RegisterStepTwoFragment;
import com.webapp.mohammad_al_loh.utils.AppContent;
import com.webapp.mohammad_al_loh.utils.NavigateUtil;
import com.webapp.mohammad_al_loh.utils.PermissionUtil;
import com.webapp.mohammad_al_loh.utils.language.BaseActivity;

public class RegisterActivity extends BaseActivity {

    public static final int page = 300;

    private ActivityRegisterBinding binding;

    private RegisterStepOneFragment registerStep1;
    private RegisterStepTwoFragment registerStep2;
    private RegisterPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        super.setRootView(binding.getRoot());
        super.onCreate(savedInstanceState);
        //view
        presenter = new RegisterPresenter(this);
        initFragment();
        click();
    }

    //functions
    private void initFragment() {
        registerStep1 = RegisterStepOneFragment.newInstance(this);
        registerStep2 = RegisterStepTwoFragment.newInstance(this);
        if (!PermissionUtil.isPermissionGranted(MediaStore.ACTION_IMAGE_CAPTURE, this)) {
            PermissionUtil.requestPermission(this, Manifest.permission.CAMERA
                    , AppContent.REQUEST_PERMISSIONS_R_W_STORAGE_CAMERA);
        }
        navigate(RegisterStepOneFragment.page);
    }


    private void click() {
        binding.btnNext.setOnClickListener(view -> next());
        binding.ivBack.setOnClickListener(view -> back());
    }

    //clicks
    public void next() {
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof RegisterStepOneFragment) {
            registerStep1.signUp();
        } else {
            registerStep2.signUp();
        }
    }

    public void back() {
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof RegisterStepTwoFragment) {
            navigate(RegisterStepOneFragment.page);
        } else {
            navigate(LoginActivity.page);
        }
    }

    @Override
    public void navigate(int page) {
        switch (page) {
            case RegisterStepOneFragment.page:
                binding.tvPage.setText("1/2");
                new NavigateUtil().replaceFragment(getSupportFragmentManager(), registerStep1, R.id.fragment_container);
                break;
            case RegisterStepTwoFragment.page:
                binding.tvPage.setText("2/2");
                new NavigateUtil().replaceFragment(getSupportFragmentManager(), registerStep2, R.id.fragment_container);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        presenter.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}

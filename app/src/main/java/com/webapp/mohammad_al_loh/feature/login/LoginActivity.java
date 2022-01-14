package com.webapp.mohammad_al_loh.feature.login;

import android.os.Bundle;

import com.webapp.mohammad_al_loh.databinding.ActivityLoginBinding;
import com.webapp.mohammad_al_loh.feature.main.MainActivity;
import com.webapp.mohammad_al_loh.feature.register.RegisterActivity;
import com.webapp.mohammad_al_loh.feature.reset.ResetPasswordActivity;
import com.webapp.mohammad_al_loh.models.Login;
import com.webapp.mohammad_al_loh.utils.AppController;
import com.webapp.mohammad_al_loh.utils.NavigateUtil;
import com.webapp.mohammad_al_loh.utils.dialogs.CountryFragment;
import com.webapp.mohammad_al_loh.utils.dialogs.WaitDialogFragment;
import com.webapp.mohammad_al_loh.utils.language.BaseActivity;
import com.webapp.mohammad_al_loh.utils.listeners.DialogView;

public class LoginActivity extends BaseActivity implements DialogView<Login> {

    public static final int page = 100;

    private ActivityLoginBinding binding;
    private LoginPresenter presenter;
    private CountryFragment countryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        super.setRootView(binding.getRoot());
        super.onCreate(savedInstanceState);
        //presenter
        presenter = new LoginPresenter(this, this);
        //view
        //data();
        showCountries();
        //on click
        click();
    }

    private void data() {
        binding.tvCode.setText(AppController.getInstance()
                .getAppSettingsPreferences().getCountry().getPhone_code());
    }

    private void click() {
        binding.btnLogin.setOnClickListener(view -> presenter.checkInput(binding.etEnterPhone, binding.etEnterPassword));
        binding.tvForget.setOnClickListener(view -> navigate(ResetPasswordActivity.page));
        binding.btnSignUp.setOnClickListener(view -> navigate(RegisterActivity.page));
    }

    private void showCountries() {
        countryFragment = CountryFragment.newInstance();
        countryFragment.show(getSupportFragmentManager(), "");
        countryFragment.setCountryListener(() -> {
            binding.tvCode.setText(AppController.getInstance()
                    .getAppSettingsPreferences().getCountry().getPhone_code());
        });
    }

    @Override
    public void onBackPressed() {
        showCountries();
    }

    @Override
    public void setData(Login login) {
    }

    @Override
    public void showDialog(String s) {
        WaitDialogFragment.newInstance().show(getSupportFragmentManager(), "");
    }

    @Override
    public void hideDialog() {
        WaitDialogFragment.newInstance().dismiss();
    }


    @Override
    public void navigate(int page) {
        switch (page) {
            case MainActivity.page:
                new NavigateUtil().activityIntent(this, MainActivity.class, false);
                break;
            case RegisterActivity.page:
                new NavigateUtil().activityIntent(this, RegisterActivity.class, false);
                break;
            case ResetPasswordActivity.page:
                new NavigateUtil().activityIntent(this, ResetPasswordActivity.class, false);
                break;
        }
    }
}

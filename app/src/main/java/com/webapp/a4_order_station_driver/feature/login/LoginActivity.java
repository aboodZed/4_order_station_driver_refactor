package com.webapp.a4_order_station_driver.feature.login;

import android.os.Bundle;

import com.webapp.a4_order_station_driver.databinding.ActivityLoginBinding;
import com.webapp.a4_order_station_driver.feature.main.MainActivity;
import com.webapp.a4_order_station_driver.feature.register.RegisterActivity;
import com.webapp.a4_order_station_driver.feature.reset.ResetPasswordActivity;
import com.webapp.a4_order_station_driver.models.Login;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.NavigateUtils;
import com.webapp.a4_order_station_driver.utils.dialogs.CountryFragment;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;

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
        showCountries();
        //on click
        click();
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
            /*if (AppController.getInstance().getAppSettingsPreferences().getLogin() != null) {
                if (AppController.getInstance().getAppSettingsPreferences().getLogin().getUser()
                        .getCountry_id() != AppController.getInstance().getAppSettingsPreferences()
                        .getCountry().getId()) {
                    goToSignUp();
                }
            }*/
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
                new NavigateUtils().activityIntent(this, MainActivity.class, false);
                break;
            case RegisterActivity.page:
                new NavigateUtils().activityIntent(this, RegisterActivity.class, false);
                break;
            case ResetPasswordActivity.page:
                new NavigateUtils().activityIntent(this, ResetPasswordActivity.class, false);
                break;
        }
    }
}

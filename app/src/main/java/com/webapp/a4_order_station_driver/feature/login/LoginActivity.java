package com.webapp.a4_order_station_driver.feature.login;

import android.os.Bundle;
import android.view.View;

import com.webapp.a4_order_station_driver.databinding.ActivityLoginBinding;
import com.webapp.a4_order_station_driver.feature.main.MainActivity2;
import com.webapp.a4_order_station_driver.feature.register.RegisterActivity;
import com.webapp.a4_order_station_driver.feature.register.two.RegisterStepTwoFragment;
import com.webapp.a4_order_station_driver.feature.reset.ResetPasswordActivity;
import com.webapp.a4_order_station_driver.models.Login;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.NavigateUtil;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.language.AppLanguageUtil;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;

public class LoginActivity extends BaseActivity implements DialogView<Login> {

    public static final int page = 100;

    private ActivityLoginBinding binding;

    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        super.setRootView(binding.getRoot());
        super.onCreate(savedInstanceState);
        //presenter
        presenter = new LoginPresenter(this, this);
        //view
        data();
        //showCountries();
        //on click
        click();
    }

    private void data() {
        binding.tvCode.setText(AppController.getInstance()
                .getAppSettingsPreferences().getSettings().getData().getPhone_code());
    }

    private void click() {
        binding.btnLogin.setOnClickListener(view -> presenter.checkInput(binding.etEnterPhone, binding.etEnterPassword));
        binding.tvForget.setOnClickListener(view -> navigate(ResetPasswordActivity.page));
        binding.btnSignUp.setOnClickListener(view -> navigate(RegisterActivity.page));

        if (AppController.getInstance().getAppSettingsPreferences().getAppLanguage().equals(AppLanguageUtil.ARABIC)) {
            binding.tvArabic.setVisibility(View.GONE);
            binding.tvEnglish.setVisibility(View.VISIBLE);
        } else {
            binding.tvArabic.setVisibility(View.VISIBLE);
            binding.tvEnglish.setVisibility(View.GONE);
        }

        binding.tvArabic.setOnClickListener(view -> {
            AppLanguageUtil.getInstance().setAppLanguage(LoginActivity.this, AppLanguageUtil.ARABIC);
            binding.tvArabic.setVisibility(View.GONE);
            binding.tvEnglish.setVisibility(View.VISIBLE);
            recreate();
        });

        binding.tvEnglish.setOnClickListener(view -> {
            AppLanguageUtil.getInstance().setAppLanguage(LoginActivity.this, AppLanguageUtil.English);
            binding.tvArabic.setVisibility(View.VISIBLE);
            binding.tvEnglish.setVisibility(View.GONE);
            recreate();
        });
    }

    /*private void showCountries() {
        countryFragment = CountryFragment.newInstance();
        countryFragment.show(getSupportFragmentManager(), "");
        countryFragment.setCountryListener(() -> {
            binding.tvCode.setText(AppController.getInstance()
                    .getAppSettingsPreferences().getUser().getCountry().getPhone_code());
        });
    }*/

   /*@Override
    public void onBackPressed() {
        showCountries();
    }*/

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
            case MainActivity2.page:
                new NavigateUtil().activityIntent(this, MainActivity2.class, false);
                break;
            case RegisterActivity.page:
                new NavigateUtil().activityIntent(this, RegisterActivity.class, false);
                break;
            case RegisterStepTwoFragment.page:
                new NavigateUtil().activityIntentWithPage(this, RegisterActivity.class
                        , true, RegisterStepTwoFragment.page);
                break;
            case ResetPasswordActivity.page:
                new NavigateUtil().activityIntent(this, ResetPasswordActivity.class, false);
                break;
        }
    }
}

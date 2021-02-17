package com.webapp.a4_order_station_driver.feature.register.one;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.feature.register.two.RegisterStep_2;
import com.webapp.a4_order_station_driver.models.Login;
import com.webapp.a4_order_station_driver.models.User;
import com.webapp.a4_order_station_driver.utils.APIUtils;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

public class RegisterStep_1Presenter {

    private BaseActivity baseActivity;
    private DialogView<Login> dialogView;

    public RegisterStep_1Presenter(BaseActivity baseActivity, DialogView<Login> dialogView) {
        this.baseActivity = baseActivity;
        this.dialogView = dialogView;
    }

    public void validInput(EditText etEnterName, EditText etEnterEmail, EditText etEnterAddress
            , EditText etEnterPhone, EditText etEnterPassword, EditText etEnterConfirmPassword
            , CheckBox cbAgreeTerms, ImageView ivEnterImage, boolean saveImage) {

        String name = etEnterName.getText().toString().trim();
        String email = etEnterEmail.getText().toString().trim();
        String mobile = etEnterPhone.getText().toString().trim();
        String password = etEnterPassword.getText().toString().trim();
        String confirmPassword = etEnterConfirmPassword.getText().toString().trim();
        String address = etEnterAddress.getText().toString().trim();
        int phone_length = AppController.getInstance().getAppSettingsPreferences().getCountry().getPhone_length();
        if (!saveImage) {
            ToolUtils.showLongToast(baseActivity.getString(R.string.fill_photos), baseActivity);
            return;
        }
        if (TextUtils.isEmpty(name)) {
            etEnterName.setError(baseActivity.getString(R.string.empty_error));
            return;
        }
        if (TextUtils.isEmpty(email)) {
            etEnterEmail.setError(baseActivity.getString(R.string.empty_error));
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEnterEmail.setError(baseActivity.getString(R.string.un_match_pattern));
            return;
        }
        if (TextUtils.isEmpty(mobile)) {
            etEnterPhone.setError(baseActivity.getString(R.string.empty_error));
            return;
        }
        if (mobile.length() != phone_length) {
            etEnterPhone.setError("phone number must be " + phone_length + " digits");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etEnterPassword.setError(baseActivity.getString(R.string.empty_error));
            return;
        }
        if (password.length() < 6) {
            etEnterPassword.setError(baseActivity.getString(R.string.length_password_error));
            return;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            etEnterConfirmPassword.setError(baseActivity.getString(R.string.empty_error));
            return;
        }
        if (confirmPassword.length() < 6) {
            etEnterConfirmPassword.setError(baseActivity.getString(R.string.length_password_error));
            return;
        }
        if (!password.equals(confirmPassword)) {
            etEnterConfirmPassword.setError(baseActivity.getString(R.string.match_error));
            return;
        }
        if (TextUtils.isEmpty(address)) {
            etEnterAddress.setError(baseActivity.getString(R.string.empty_error));
            return;
        }
        if (!cbAgreeTerms.isChecked()) {
            cbAgreeTerms.setError(baseActivity.getString(R.string.check_box));
            return;
        }

        User user = new User(name, ToolUtils.bitmapToBase64(ToolUtils.getBitmapFromImageView(ivEnterImage)),
                AppController.getInstance().getAppSettingsPreferences()
                        .getCountry().getPhone_code() + mobile, email, address,
                "delivery_driver", password, confirmPassword, AppController.getInstance()
                .getAppSettingsPreferences().getCountry().getId());

        Log.e("country_id", user.getCountry_id() + "");
        onNextStep_1(user);
    }

    private void onNextStep_1(User user) {
        dialogView.showDialog("");

        new APIUtils<Login>(baseActivity).getData(AppController.getInstance().getApi().SignUp(user)
                , new RequestListener<Login>() {
                    @Override
                    public void onSuccess(Login login, String msg) {
                        AppController.getInstance().getAppSettingsPreferences().setLogin(login);
                        AppController.getInstance().getAppSettingsPreferences().setIsLogin(true);
                        AppController.getInstance().getAppSettingsPreferences().setPassword(user.getPassword());
                        dialogView.hideDialog();

                        baseActivity.navigate(RegisterStep_2.page);
                    }

                    @Override
                    public void onError(String msg) {
                        dialogView.hideDialog();
                        ToolUtils.showLongToast(msg, baseActivity);
                    }

                    @Override
                    public void onFail(String msg) {
                        dialogView.hideDialog();
                        ToolUtils.showLongToast(msg, baseActivity);
                    }
                });
    }
}

package com.webapp.a4_order_station_driver.feature.login;

import android.content.Intent;
import android.widget.EditText;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.feature.main.MainActivity;
import com.webapp.a4_order_station_driver.models.Login;
import com.webapp.a4_order_station_driver.services.firebase.GenerateFCMService;
import com.webapp.a4_order_station_driver.utils.APIUtils;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

import java.util.HashMap;

public class LoginPresenter {

    private BaseActivity baseActivity;
    private DialogView<Login> dialogView;

    public LoginPresenter(BaseActivity baseActivity, DialogView<Login> dialogView) {
        this.baseActivity = baseActivity;
        this.dialogView = dialogView;
    }

    public void checkInput(EditText etEnterPhone, EditText etEnterPassword) {
        int phone_length = AppController.getInstance().getAppSettingsPreferences().getCountry().getPhone_length();

        if (etEnterPhone.getText().toString().trim().equals("")) {
            etEnterPhone.setError(baseActivity.getString(R.string.empty_error));
            return;
        }
        if (etEnterPhone.getText().toString().trim().length() != phone_length) {
            etEnterPhone.setError("phone number must be " + phone_length + " digits");
            return;
        }
        if (etEnterPassword.getText().toString().trim().equals("")) {
            etEnterPassword.setError(baseActivity.getString(R.string.empty_error));
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", AppController.getInstance().getAppSettingsPreferences().getCountry()
                .getPhone_code() + etEnterPhone.getText().toString().trim());
        map.put("password", etEnterPassword.getText().toString().trim());
        map.put("country_id", AppController.getInstance().getAppSettingsPreferences()
                .getCountry().getId() + "");
        map.put("remember_me", "True");

        login(map);
    }

    private void login(HashMap<String, String> map) {
        dialogView.showDialog("");
        new APIUtils<Login>(baseActivity).getData(AppController.getInstance().getApi().login(map),
                new RequestListener<Login>() {
                    @Override
                    public void onSuccess(Login login, String msg) {
                        dialogView.hideDialog();
                        if (login != null && login.getSuccess()) {
                            AppController.getInstance().getAppSettingsPreferences().setLogin(login);
                            AppController.getInstance().getAppSettingsPreferences().setIsLogin(true);
                            AppController.getInstance().getAppSettingsPreferences().setPassword(map.get("password"));
                            //service
                            Intent service = new Intent(baseActivity, GenerateFCMService.class);
                            baseActivity.startService(service);
                            //navigation
                            baseActivity.navigate(MainActivity.page);
                        } else {
                            ToolUtils.showLongToast(login.getMassage(), baseActivity);
                        }
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

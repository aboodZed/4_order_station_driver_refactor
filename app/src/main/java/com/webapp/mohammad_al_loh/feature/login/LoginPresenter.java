package com.webapp.mohammad_al_loh.feature.login;

import android.content.Intent;
import android.util.Log;
import android.widget.EditText;

import com.webapp.mohammad_al_loh.R;
import com.webapp.mohammad_al_loh.feature.main.MainActivity;
import com.webapp.mohammad_al_loh.models.Login;
import com.webapp.mohammad_al_loh.services.firebase.GenerateFCMService;
import com.webapp.mohammad_al_loh.utils.APIUtil;
import com.webapp.mohammad_al_loh.utils.AppController;
import com.webapp.mohammad_al_loh.utils.ToolUtil;
import com.webapp.mohammad_al_loh.utils.language.BaseActivity;
import com.webapp.mohammad_al_loh.utils.listeners.DialogView;
import com.webapp.mohammad_al_loh.utils.listeners.RequestListener;

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
        new APIUtil<Login>(baseActivity).getData(AppController.getInstance().getApi().login(map),
                new RequestListener<Login>() {
                    @Override
                    public void onSuccess(Login login, String msg) {
                        dialogView.hideDialog();
                        if (login != null && login.getSuccess()) {
                            Log.e("sharedPreferences",login.toString());
                            AppController.getInstance().getAppSettingsPreferences().setLogin(login);
                            AppController.getInstance().getAppSettingsPreferences().setIsLogin(true);
                            AppController.getInstance().getAppSettingsPreferences().setPassword(map.get("password"));
                            //service
                            Intent service = new Intent(baseActivity, GenerateFCMService.class);
                            baseActivity.startService(service);
                            //navigation
                            baseActivity.navigate(MainActivity.page);
                        } else {
                            ToolUtil.showLongToast(login.getMassage(), baseActivity);
                        }
                    }

                    @Override
                    public void onError(String msg) {
                        dialogView.hideDialog();
                        ToolUtil.showLongToast(msg, baseActivity);
                    }

                    @Override
                    public void onFail(String msg) {
                        dialogView.hideDialog();
                        ToolUtil.showLongToast(msg, baseActivity);
                    }
                });
    }
}

package com.webapp.a4_order_station_driver.feature.login;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.feature.main.MainActivity2;
import com.webapp.a4_order_station_driver.feature.register.two.RegisterStepTwoFragment;
import com.webapp.a4_order_station_driver.models.Login;
import com.webapp.a4_order_station_driver.models.Result;
import com.webapp.a4_order_station_driver.models.User;
import com.webapp.a4_order_station_driver.services.firebase.GenerateFCMService;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

import java.util.HashMap;
import java.util.Map;

public class LoginPresenter {

    private BaseActivity baseActivity;
    private DialogView<Login> dialogView;

    public LoginPresenter(BaseActivity baseActivity, DialogView<Login> dialogView) {
        this.baseActivity = baseActivity;
        this.dialogView = dialogView;
    }

    public void checkInput(EditText etEnterPhone, EditText etEnterPassword) {
        int phone_length = AppController.getInstance().getAppSettingsPreferences()
                .getSettings().getData().getPhone_length();

        if (etEnterPhone.getText().toString().trim().equals("")) {
            etEnterPhone.setError(baseActivity.getString(R.string.empty_error));
            return;
        }
        if (etEnterPhone.getText().toString().trim().length() != phone_length) {
            etEnterPhone.setError(baseActivity.getString(R.string.phone_must) + phone_length + baseActivity.getString(R.string.digits));
            return;
        }
        if (etEnterPassword.getText().toString().trim().equals("")) {
            etEnterPassword.setError(baseActivity.getString(R.string.empty_error));
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", AppController.getInstance().getAppSettingsPreferences().getSettings()
                .getData().getPhone_code() + etEnterPhone.getText().toString().trim());
        map.put("password", etEnterPassword.getText().toString().trim());
        map.put("remember_me", "True");
        generateFCMToken(baseActivity, map);
    }


    public void generateFCMToken(Context context, HashMap<String, String> map) {
        dialogView.showDialog("");
        FirebaseApp.initializeApp(context);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("Main", "getInstanceId failed", task.getException());
                return;
            }
            // Get new Instance ID token
            String token = task.getResult();
            Log.e("fcm token", "" + token);
            map.put("fcm_token", token);
            login(map);
        });
    }

    private void login(HashMap<String, String> map) {
        new APIUtil<Result<User>>(baseActivity).getData(AppController.getInstance().getApi().login(map),
                new RequestListener<Result<User>>() {
                    @Override
                    public void onSuccess(Result<User> result, String msg) {
                        dialogView.hideDialog();
                        if (result.isSuccess() && result.getData().isComplete()) {
                            Log.e("sharedPreferences", result.toString());
                            AppController.getInstance().getAppSettingsPreferences().setUser(result.getData());
                            AppController.getInstance().getAppSettingsPreferences().setToken(result.getData().getToken());
                            //navigation
                            baseActivity.navigate(MainActivity2.page);
                        } else if (!result.getData().isComplete()) {
                            baseActivity.navigate(RegisterStepTwoFragment.page);
                        } else {
                            ToolUtil.showLongToast(result.getMessage(), baseActivity);
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

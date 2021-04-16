package com.webapp.a4_order_station_driver.feature.reset.one;

import android.text.TextUtils;
import android.widget.EditText;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.feature.reset.two.ResetStep2;
import com.webapp.a4_order_station_driver.models.ResetCode;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

import java.util.HashMap;

public class ResetStep1Presenter {

    private BaseActivity baseActivity;
    private DialogView<ResetCode> dialogView;

    public ResetStep1Presenter(BaseActivity baseActivity, DialogView<ResetCode> dialogView) {
        this.baseActivity = baseActivity;
        this.dialogView = dialogView;
    }

    public void validInput(EditText etEnterPhone) {
        String mobile = etEnterPhone.getText().toString().trim();
        int phone_length = AppController.getInstance().getAppSettingsPreferences()
                .getCountry().getPhone_length();

        if (TextUtils.isEmpty(mobile)) {
            etEnterPhone.setError(baseActivity.getString(R.string.empty_error));
            return;
        }
        if (mobile.length() != phone_length) {
            etEnterPhone.setError("phone number must be " + phone_length + " digits");
            return;
        }
        //AppController.getInstance().getAppSettingsPreferences()
        //                .getCountry().getPhone_code()
        sendPhone(AppController.getInstance().getAppSettingsPreferences()
                .getCountry().getPhone_code() + mobile);
    }


    private void sendPhone(String mobile) {
        dialogView.showDialog("");

        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("role", "delivery_driver");

        new APIUtil<ResetCode>(baseActivity).getData(AppController.getInstance()
                .getApi().forgetPassword(map), new RequestListener<ResetCode>() {
            @Override
            public void onSuccess(ResetCode resetCode, String msg) {
                dialogView.hideDialog();
                resetCode.setMobile(mobile);
                dialogView.setData(resetCode);
                baseActivity.navigate(ResetStep2.page);
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

package com.webapp.a4_order_station_driver.feature.reset.one;

import android.text.TextUtils;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.feature.reset.two.ResetStep2;
import com.webapp.a4_order_station_driver.models.ResetCode;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.NavigateUtil;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class ResetStep1Presenter {

    private BaseActivity baseActivity;
    private DialogView<String> dialogView;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String TAG = getClass().getName() + ":testsmscode";
    private String verify_id;
    private String phone;

    public ResetStep1Presenter(BaseActivity baseActivity, DialogView<String> dialogView) {
        this.baseActivity = baseActivity;
        this.dialogView = dialogView;
    }

    public void validInput(EditText etEnterPhone) {
        String mobile = etEnterPhone.getText().toString().trim();
        int phone_length = AppController.getInstance().getAppSettingsPreferences()
                .getSettings().getData().getPhone_length();

        if (TextUtils.isEmpty(mobile)) {
            etEnterPhone.setError(baseActivity.getString(R.string.empty_error));
            return;
        }
        if (mobile.length() != phone_length) {
            etEnterPhone.setError("phone number must be " + phone_length + " digits");
            return;
        }
        phone = "+" + AppController.getInstance().getAppSettingsPreferences()
                .getSettings().getData().getPhone_code() + mobile;
        callBackListener();
        sendVerify(phone);

    }

    private void sendVerify(String phoneNumber) {
        dialogView.showDialog("");
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(baseActivity)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks).build(); //callback
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void callBackListener() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                verify_id = verificationId;
                baseActivity.navigate(ResetStep2.page);
            }
        };
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
               // dialogView.setData(resetCode);
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

    public HashMap<String,String> getCredential() {
        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", phone);
        map.put("verify", verify_id);
        return map;
    }
}

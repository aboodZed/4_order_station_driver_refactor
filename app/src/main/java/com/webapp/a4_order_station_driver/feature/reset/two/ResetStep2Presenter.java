package com.webapp.a4_order_station_driver.feature.reset.two;

import android.os.CountDownTimer;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.feature.reset.three.ResetStep3;
import com.webapp.a4_order_station_driver.models.Login;
import com.webapp.a4_order_station_driver.models.ResetCode;
import com.webapp.a4_order_station_driver.models.VerifyCode;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

import java.util.HashMap;
import java.util.Objects;

public class ResetStep2Presenter {

    private BaseActivity baseActivity;
    private DialogView<HashMap<String, String>> dialogView;
    private HashMap<String, String> map;

    public ResetStep2Presenter(BaseActivity baseActivity
            , DialogView<HashMap<String, String>> dialogView,
                               HashMap<String, String> map
    ) {
        this.baseActivity = baseActivity;
        this.dialogView = dialogView;
        this.map = map;
    }

    public void verify(String code) {
        if (TextUtils.isEmpty(code)) {
            ToolUtil.showLongToast(baseActivity.getString(R.string.empty_error), baseActivity);
            return;
        }

        if (code.length() != 6) {
            ToolUtil.showLongToast(baseActivity.getString(R.string.empty_error), baseActivity);
            return;
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(Objects.requireNonNull(map.get("verify")), code);
        signIn(credential);
    }

    private void signIn(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(baseActivity, task -> {
                    if (task.isSuccessful()) {
                        baseActivity.navigate(ResetStep3.page);
                    }else {
                        ToolUtil.showLongToast(Objects.requireNonNull(task.getException())
                                .getLocalizedMessage(),baseActivity);
                    }
                });
    }

    public void reSend(String mobile) {
        dialogView.showDialog("");

        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("role", "delivery_driver");

        new APIUtil<ResetCode>(baseActivity).getData(
                AppController.getInstance().getApi().forgetPassword(map),
                new RequestListener<ResetCode>() {
                    @Override
                    public void onSuccess(ResetCode resetCode, String msg) {
                        dialogView.hideDialog();
                        resetCode.setMobile(mobile);
                        //dialogView.setData(resetCode);
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

    public void conform(ResetCode resetCode, String verify, CountDownTimer countDownTimer) {
        if (verify.equals(String.valueOf(resetCode.getCode()))) {
            dialogView.showDialog("");

            new APIUtil<VerifyCode>(baseActivity).getData(
                    AppController.getInstance().getApi().verifyCode(resetCode)
                    , new RequestListener<VerifyCode>() {
                        @Override
                        public void onSuccess(VerifyCode verifyCode, String msg) {
                            dialogView.hideDialog();
                            /*Login login = AppController.getInstance().getAppSettingsPreferences().getUser();
                            login.setAccess_token(verifyCode.getAccess_token());
                            AppController.getInstance().getAppSettingsPreferences().setUser(login);*/
                            countDownTimer.cancel();
                            baseActivity.navigate(ResetStep3.page);
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
        } else {
            ToolUtil.showLongToast(baseActivity.getString(R.string.re_write_code), baseActivity);
        }
    }

}

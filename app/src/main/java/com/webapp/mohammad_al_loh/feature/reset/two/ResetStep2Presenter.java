package com.webapp.mohammad_al_loh.feature.reset.two;

import android.os.CountDownTimer;
import android.text.TextUtils;

import com.webapp.mohammad_al_loh.R;
import com.webapp.mohammad_al_loh.databinding.FragmentResetStep2Binding;
import com.webapp.mohammad_al_loh.feature.reset.three.ResetStep3;
import com.webapp.mohammad_al_loh.models.Login;
import com.webapp.mohammad_al_loh.models.ResetCode;
import com.webapp.mohammad_al_loh.models.VerifyCode;
import com.webapp.mohammad_al_loh.utils.APIUtil;
import com.webapp.mohammad_al_loh.utils.AppController;
import com.webapp.mohammad_al_loh.utils.ToolUtil;
import com.webapp.mohammad_al_loh.utils.language.BaseActivity;
import com.webapp.mohammad_al_loh.utils.listeners.DialogView;
import com.webapp.mohammad_al_loh.utils.listeners.RequestListener;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class ResetStep2Presenter {

    private BaseActivity baseActivity;
    private DialogView<ResetCode> dialogView;
    private int code;

    public ResetStep2Presenter(BaseActivity baseActivity
            , DialogView<ResetCode> dialogView) {
        this.baseActivity = baseActivity;
        this.dialogView = dialogView;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void generateCode() {
        Random random = new Random();
        int i = random.nextInt(9000) + 1000;
        ToolUtil.showLongToast(String.valueOf(i), baseActivity);
        ResetCode resetCode = new ResetCode();
        resetCode.setCode(i);
        dialogView.setData(resetCode);
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
                        dialogView.setData(resetCode);
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

    public void conform(FragmentResetStep2Binding binding, CountDownTimer countDownTimer) {
        if (TextUtils.isEmpty(binding.lfCode.getText())) {
            binding.lfCode.setError(baseActivity.getString(R.string.empty_error));
            return;
        }

        if (Objects.requireNonNull(binding.lfCode.getText()).toString().equals(String.valueOf(code))) {
            countDownTimer.cancel();
            baseActivity.navigate(ResetStep3.page);
        }
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
                            Login login = AppController.getInstance().getAppSettingsPreferences().getLogin();
                            login.setAccess_token(verifyCode.getAccess_token());
                            AppController.getInstance().getAppSettingsPreferences().setLogin(login);
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

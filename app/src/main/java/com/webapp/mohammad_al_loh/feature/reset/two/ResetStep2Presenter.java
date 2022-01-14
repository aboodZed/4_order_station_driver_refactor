package com.webapp.mohammad_al_loh.feature.reset.two;

import android.os.CountDownTimer;

import com.webapp.mohammad_al_loh.R;
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

public class ResetStep2Presenter {

    private BaseActivity baseActivity;
    private DialogView<ResetCode> dialogView;

    public ResetStep2Presenter(BaseActivity baseActivity
            , DialogView<ResetCode> dialogView) {
        this.baseActivity = baseActivity;
        this.dialogView = dialogView;
    }

    public void reSend(String mobile) {
        dialogView.showDialog("");

        HashMap<String,String> map = new HashMap<>();
        map.put("mobile",mobile);
        map.put("role","delivery_driver");

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

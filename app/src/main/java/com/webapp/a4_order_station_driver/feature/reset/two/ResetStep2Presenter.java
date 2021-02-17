package com.webapp.a4_order_station_driver.feature.reset.two;

import android.os.CountDownTimer;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.feature.reset.three.ResetStep3;
import com.webapp.a4_order_station_driver.models.Login;
import com.webapp.a4_order_station_driver.models.ResetCode;
import com.webapp.a4_order_station_driver.models.VerifyCode;
import com.webapp.a4_order_station_driver.utils.APIUtils;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

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

        new APIUtils<ResetCode>(baseActivity).getData(
                AppController.getInstance().getApi().forgetPassword(mobile),
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
                        ToolUtils.showLongToast(msg, baseActivity);
                    }

                    @Override
                    public void onFail(String msg) {
                        dialogView.hideDialog();
                        ToolUtils.showLongToast(msg, baseActivity);
                    }
                });
    }

    public void conform(ResetCode resetCode, String verify, CountDownTimer countDownTimer) {
        if (verify.equals(String.valueOf(resetCode.getCode()))) {
            dialogView.showDialog("");

            new APIUtils<VerifyCode>(baseActivity).getData(
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
                            ToolUtils.showLongToast(msg, baseActivity);

                        }

                        @Override
                        public void onFail(String msg) {
                            dialogView.hideDialog();
                            ToolUtils.showLongToast(msg, baseActivity);
                        }
                    });
        } else {
            ToolUtils.showLongToast(baseActivity.getString(R.string.re_write_code), baseActivity);
        }
    }
}

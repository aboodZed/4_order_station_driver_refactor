package com.webapp.a4_order_station_driver.feature.reset.one;

import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.feature.reset.two.ResetStep2;
import com.webapp.a4_order_station_driver.models.ResetCode;
import com.webapp.a4_order_station_driver.utils.APIUtils;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

public class ResetStep1Presenter {

    private BaseActivity baseActivity;
    private DialogView<ResetCode> dialogView;

    public ResetStep1Presenter(BaseActivity baseActivity, DialogView<ResetCode> dialogView) {
        this.baseActivity = baseActivity;
        this.dialogView = dialogView;
    }

    public void validInput(EditText etEnterPhone) {
        String mobile = etEnterPhone.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            etEnterPhone.setError(baseActivity.getString(R.string.empty_error));
            return;
        }
        sendPhone(mobile);
    }


    private void sendPhone(String mobile) {
        dialogView.showDialog("");

        new APIUtils<ResetCode>(baseActivity).getData(AppController.getInstance()
                .getApi().forgetPassword(mobile), new RequestListener<ResetCode>() {
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
                ToolUtils.showLongToast(msg, baseActivity);
            }

            @Override
            public void onFail(String msg) {
                dialogView.hideDialog();
                ToolUtils.showLongToast(msg, baseActivity);
            }
        });
        /*AppController.getInstance().getApi().forgetPassword(/*AppController.getInstance()
                .getAppSettingsPreferences().getCountry().getPhone_code() + mobile)
                .enqueue(new Callback<ResetCode>() {
                    @Override
                    public void onResponse(Call<ResetCode> call, Response<ResetCode> response) {
                        if (response.isSuccessful()) {
                            code = response.body().getCode();
                            //ToolUtils.notificationBuilder(getActivity(), response.body());
                            WaitDialogFragment.newInstance().dismiss();
                            baseActivity.navigate(ResetStep2.page);
                        } else {
                            ToolUtils.showError(getActivity(), response.errorBody());
                            WaitDialogFragment.newInstance().dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResetCode> call, Throwable t) {
                        t.printStackTrace();
                        ToolUtils.showLongToast(getString(R.string.error), getActivity());
                        WaitDialogFragment.newInstance().dismiss();
                    }
                });*/
    }
}

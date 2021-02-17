package com.webapp.a4_order_station_driver.feature.reset.three;

import android.text.TextUtils;
import android.widget.EditText;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.feature.login.LoginActivity;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.utils.APIUtils;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetStep3Presenter {

    private BaseActivity baseActivity;
    private DialogView<Message> dialogView;

    public ResetStep3Presenter(BaseActivity baseActivity, DialogView<Message> dialogView) {
        this.baseActivity = baseActivity;
        this.dialogView = dialogView;
    }

    public void ValidInput(EditText etEnterPassword, EditText etEnterConfirmPassword) {
        String password = etEnterPassword.getText().toString().trim();
        String confirmPassword = etEnterConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(password)) {
            etEnterPassword.setError(baseActivity.getString(R.string.empty_error));
            return;
        }
        if (password.length() < 6) {
            etEnterPassword.setError(baseActivity.getString(R.string.length_password_error));
            return;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            etEnterConfirmPassword.setError(baseActivity.getString(R.string.empty_error));
            return;
        }
        if (confirmPassword.length() < 6) {
            etEnterConfirmPassword.setError(baseActivity.getString(R.string.length_password_error));
            return;
        }
        if (!password.equals(confirmPassword)) {
            etEnterConfirmPassword.setError(baseActivity.getString(R.string.match_error));
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("password", password);
        map.put("password_confirmation", confirmPassword);
        saveNewPassword(map);
    }

    private void saveNewPassword(HashMap<String, String> hashMap) {
        dialogView.showDialog("");

        new APIUtils<Message>(baseActivity).getData(AppController.getInstance().getApi()
                .resetPassword(hashMap), new RequestListener<Message>() {
            @Override
            public void onSuccess(Message message, String msg) {
                dialogView.hideDialog();
                AppController.getInstance().getAppSettingsPreferences().setPassword(hashMap.get("password"));
                baseActivity.navigate(LoginActivity.page);
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
        /*if (ToolUtils.checkTheInternet()) {
            WaitDialogFragment.newInstance().show(getFragmentManager(), "");
            AppController.getInstance().getApi().resetPassword(hashMap).enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                    WaitDialogFragment.newInstance().dismiss();
                    if (response.isSuccessful()) {
                        ToolUtils.showLongToast(response.body().getMassage(), getActivity());
                        AppController.getInstance().getAppSettingsPreferences().setPassword(hashMap.get("password"));
                        baseActivity.navigate(LoginActivity.page);
                    } else {
                        ToolUtils.showError(getActivity(), response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<Message> call, Throwable t) {
                    t.printStackTrace();
                    ToolUtils.showLongToast(getString(R.string.error), getActivity());
                    WaitDialogFragment.newInstance().dismiss();
                }
            });
        }*/
    }
}

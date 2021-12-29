package com.webapp.a4_order_station_driver.feature.data.changePassword;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.FragmentChangePasswordBinding;
import com.webapp.a4_order_station_driver.feature.main.profile.ProfileFragment;
import com.webapp.a4_order_station_driver.models.Result;
import com.webapp.a4_order_station_driver.models.User;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

import java.util.HashMap;

public class ChangePasswordFragment extends Fragment {

    public static final int page = 606;

    private FragmentChangePasswordBinding binding;

    private BaseActivity baseActivity;

    public ChangePasswordFragment(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    public static ChangePasswordFragment newInstance(BaseActivity baseActivity) {
        ChangePasswordFragment fragment = new ChangePasswordFragment(baseActivity);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChangePasswordBinding.inflate(inflater);
        click();
        return binding.getRoot();
    }

    private void click() {
        binding.btnChangePassword.setOnClickListener(view -> validate());
    }

    private void validate() {
        String oldPassword = binding.etEnterOldPassword.getText().toString().trim();
        String newPassword = binding.etEnterNewPassword.getText().toString().trim();
        String confirmPassword = binding.etEnterConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(oldPassword)) {
            binding.etEnterOldPassword.setError(getString(R.string.empty_error));
            return;
        }

        if (TextUtils.isEmpty(newPassword)) {
            binding.etEnterNewPassword.setError(getString(R.string.empty_error));
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            binding.etEnterConfirmPassword.setError(getString(R.string.empty_error));
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            binding.etEnterConfirmPassword.setError(getString(R.string.match_error));
            return;
        }

        changePassword(oldPassword, newPassword);
    }

    private void changePassword(String oldPassword, String newPassword) {
        WaitDialogFragment.newInstance().show(getChildFragmentManager(), "");
        HashMap<String, String> map = new HashMap<>();
        map.put("old_password", oldPassword);
        map.put("password", newPassword);
        map.put("password_confirmation", newPassword);

        new APIUtil<Result<User>>(getActivity()).getData(AppController.getInstance().getApi()
                .changePassword(map), new RequestListener<Result<User>>() {
            @Override
            public void onSuccess(Result<User> result, String msg) {
                WaitDialogFragment.newInstance().dismiss();
                Log.e(getClass().getName() + " changePassword", result.toString());
                if (result.isSuccess()) {
                    AppController.getInstance().getAppSettingsPreferences().setUser(result.getData());
                    AppController.getInstance().getAppSettingsPreferences().setToken(result.getData().getToken());
                    baseActivity.navigate(ProfileFragment.page);
                }else {
                    onError(result.getMessage());
                }
            }

            @Override
            public void onError(String msg) {
                WaitDialogFragment.newInstance().dismiss();
                ToolUtil.showLongToast(msg, baseActivity);
            }

            @Override
            public void onFail(String msg) {
                WaitDialogFragment.newInstance().dismiss();
                ToolUtil.showLongToast(msg, baseActivity);
            }
        });
    }
}
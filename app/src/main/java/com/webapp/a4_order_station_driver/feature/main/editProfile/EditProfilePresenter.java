package com.webapp.a4_order_station_driver.feature.main.editProfile;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Patterns;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.FragmentEditProfileBinding;
import com.webapp.a4_order_station_driver.models.Login;
import com.webapp.a4_order_station_driver.models.User;
import com.webapp.a4_order_station_driver.utils.APIUtils;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

class EditProfilePresenter {

    private Activity activity;
    private DialogView<User> dialogView;

    public EditProfilePresenter(Activity activity, DialogView<User> dialogView) {
        this.activity = activity;
        this.dialogView = dialogView;
    }

    public void validInput(FragmentEditProfileBinding binding) {

        String email = binding.etEnterEmail.getText().toString().trim();
        String mobile = binding.etEnterPhone.getText().toString().trim();
        String address = binding.etEnterAddress.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            binding.etEnterEmail.setError(activity.getString(R.string.empty_error));
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEnterEmail.setError(activity.getString(R.string.un_match_pattern));
            return;
        }
        if (TextUtils.isEmpty(mobile)) {
            binding.etEnterPhone.setError(activity.getString(R.string.empty_error));
            return;
        }
        if (TextUtils.isEmpty(address)) {
            binding.etEnterAddress.setError(activity.getString(R.string.empty_error));
            return;
        }
        User user = new User(
                ToolUtils.bitmapToBase64(ToolUtils.getBitmapFromImageView(binding.ivDriverAvatar)),
                ToolUtils.bitmapToBase64(ToolUtils.getBitmapFromImageView(binding.ivInsuranceLicense)),
                ToolUtils.bitmapToBase64(ToolUtils.getBitmapFromImageView(binding.ivYourLicense)),
                ToolUtils.bitmapToBase64(ToolUtils.getBitmapFromImageView(binding.ivVehicleLicense)),
                ToolUtils.bitmapToBase64(ToolUtils.getBitmapFromImageView(binding.ivIdPic)),
                ToolUtils.bitmapToBase64(ToolUtils.getBitmapFromImageView(binding.ivYourVehicle)),
                AppController.getInstance().getAppSettingsPreferences().getPassword(),
                AppController.getInstance().getAppSettingsPreferences().getPassword(),
                mobile, email, address);
        upload(user);
    }

    private void upload(User user) {
        new APIUtils<User>(activity)
                .getData(AppController.getInstance().getApi().updateProfile(user),
                        new RequestListener<User>() {
                            @Override
                            public void onSuccess(User user, String msg) {
                                Login login = AppController.getInstance().getAppSettingsPreferences().getLogin();
                                login.setUser(user);
                                AppController.getInstance().getAppSettingsPreferences().setLogin(login);
                                WaitDialogFragment.newInstance().dismiss();
                            }

                            @Override
                            public void onError(String msg) {
                                ToolUtils.showLongToast(msg, activity);
                                WaitDialogFragment.newInstance().dismiss();
                            }

                            @Override
                            public void onFail(String msg) {
                                WaitDialogFragment.newInstance().dismiss();
                                ToolUtils.showLongToast(msg, activity);
                            }
                        });
    }
}

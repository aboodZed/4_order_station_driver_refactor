package com.webapp.a4_order_station_driver.feature.main.editProfile;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Patterns;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.FragmentEditProfileBinding;
import com.webapp.a4_order_station_driver.feature.main.profile.ProfileFragment;
import com.webapp.a4_order_station_driver.models.Login;
import com.webapp.a4_order_station_driver.models.User;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.Photo.PhotoTakerManager;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

import static android.app.Activity.RESULT_OK;

class EditProfilePresenter {

    private BaseActivity baseActivity;
    private DialogView<User> dialogView;
    private PhotoTakerManager photoTakerManager;
    private int requestCode;

    public EditProfilePresenter(BaseActivity baseActivity, DialogView<User> dialogView, PhotoTakerManager photoTakerManager) {
        this.baseActivity = baseActivity;
        this.dialogView = dialogView;
        this.photoTakerManager = photoTakerManager;
    }

    public void validInput(FragmentEditProfileBinding binding, String[] images) {

        String email = binding.etEnterEmail.getText().toString().trim();
        String mobile = binding.etEnterPhone.getText().toString().trim();
        String address = binding.etEnterAddress.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            binding.etEnterEmail.setError(baseActivity.getString(R.string.empty_error));
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEnterEmail.setError(baseActivity.getString(R.string.un_match_pattern));
            return;
        }
        if (TextUtils.isEmpty(mobile)) {
            binding.etEnterPhone.setError(baseActivity.getString(R.string.empty_error));
            return;
        }
        if (TextUtils.isEmpty(address)) {
            binding.etEnterAddress.setError(baseActivity.getString(R.string.empty_error));
            return;
        }
        User user = new User(
                images[EditProfileFragment.AVATAR], images[EditProfileFragment.VEHICLE_INSURANCE],
                images[EditProfileFragment.YOUR_LICENSE], images[EditProfileFragment.VEHICLE_LICENSE]
                , images[EditProfileFragment.IDENTITY], images[EditProfileFragment.VEHICLE_IMAGE],
                AppController.getInstance().getAppSettingsPreferences().getPassword(),
                AppController.getInstance().getAppSettingsPreferences().getPassword(),
                mobile, email, address);
        upload(user);
    }

    private void upload(User user) {
        dialogView.showDialog("");
        new APIUtil<User>(baseActivity)
                .getData(AppController.getInstance().getApi().updateProfile(user),
                        new RequestListener<User>() {
                            @Override
                            public void onSuccess(User user, String msg) {
                                Login login = AppController.getInstance().getAppSettingsPreferences().getLogin();
                                login.setUser(user);
                                AppController.getInstance().getAppSettingsPreferences().setLogin(login);
                                dialogView.hideDialog();
                                baseActivity.navigate(ProfileFragment.page);
                            }

                            @Override
                            public void onError(String msg) {
                                ToolUtil.showLongToast(msg, baseActivity);
                                dialogView.hideDialog();
                            }

                            @Override
                            public void onFail(String msg) {
                                ToolUtil.showLongToast(msg, baseActivity);
                                dialogView.hideDialog();
                            }
                        });
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public void onActivityResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AppContent.REQUEST_IMAGE_AVATAR_UPLOAD:
                case AppContent.REQUEST_IMAGE_VEHICLE_UPLOAD:
                case AppContent.REQUEST_IMAGE_VEHICLE_LICENSE_UPLOAD:
                case AppContent.REQUEST_IMAGE_VEHICLE_INSURANCE_UPLOAD:
                case AppContent.REQUEST_IMAGE_IDENTITY_UPLOAD:
                case AppContent.REQUEST_IMAGE_YOUR_LICENSE_UPLOAD:
                    photoTakerManager.processGalleryPhoto(baseActivity, data);
                    break;
                case AppContent.REQUEST_IMAGE_AVATAR_CAMERA:
                case AppContent.REQUEST_IMAGE_VEHICLE_CAMERA:
                case AppContent.REQUEST_IMAGE_VEHICLE_LICENSE_CAMERA:
                case AppContent.REQUEST_IMAGE_VEHICLE_INSURANCE_CAMERA:
                case AppContent.REQUEST_IMAGE_IDENTITY_CAMERA:
                case AppContent.REQUEST_IMAGE_YOUR_LICENSE_CAMERA:
                    photoTakerManager.processCameraPhoto(baseActivity);
                    break;
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case AppContent.REQUEST_PERMISSIONS_R_W_STORAGE_CAMERA:
                    ToolUtil.showLongToast(baseActivity.getString(R.string.permission_garnted), baseActivity);
                    break;
            }
        } else {
            ToolUtil.showLongToast(baseActivity.getString(R.string.permission_denial), baseActivity);
        }
    }
}

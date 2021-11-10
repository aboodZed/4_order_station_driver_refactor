package com.webapp.a4_order_station_driver.feature.register.one;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.CheckBox;
import android.widget.EditText;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.feature.register.two.RegisterStepTwoFragment;
import com.webapp.a4_order_station_driver.models.Login;
import com.webapp.a4_order_station_driver.models.NeighborhoodList;
import com.webapp.a4_order_station_driver.models.User;
import com.webapp.a4_order_station_driver.utils.APIImageUtil;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.Photo.PhotoTakerManager;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

public class RegisterStepOnePresenter {

    private BaseActivity baseActivity;
    private DialogView<NeighborhoodList> dialogView;
    private PhotoTakerManager photoTakerManager;
    private int requestCode;

    public RegisterStepOnePresenter(BaseActivity baseActivity
            , DialogView<NeighborhoodList> dialogView, PhotoTakerManager photoTakerManager) {
        this.baseActivity = baseActivity;
        this.dialogView = dialogView;
        this.photoTakerManager = photoTakerManager;
    }

    public void validInput(EditText etEnterName, EditText etEnterEmail, EditText etEnterAddress
            , EditText etEnterPhone, EditText etEnterPassword, EditText etEnterConfirmPassword
            , Bitmap bitmap, boolean saveImage, int city_id) {

        String name = etEnterName.getText().toString().trim();
        String email = etEnterEmail.getText().toString().trim();
        String mobile = etEnterPhone.getText().toString().trim();
        String password = etEnterPassword.getText().toString().trim();
        String confirmPassword = etEnterConfirmPassword.getText().toString().trim();
        String address = etEnterAddress.getText().toString().trim();
        int phone_length = AppController.getInstance().getAppSettingsPreferences().getCountry().getPhone_length();

        if (!saveImage) {
            ToolUtil.showLongToast(baseActivity.getString(R.string.fill_photos), baseActivity);
            return;
        }
        if (TextUtils.isEmpty(name)) {
            etEnterName.setError(baseActivity.getString(R.string.empty_error));
            return;
        }
        if (TextUtils.isEmpty(email)) {
            etEnterEmail.setError(baseActivity.getString(R.string.empty_error));
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEnterEmail.setError(baseActivity.getString(R.string.un_match_pattern));
            return;
        }
        if (TextUtils.isEmpty(mobile)) {
            etEnterPhone.setError(baseActivity.getString(R.string.empty_error));
            return;
        }
        if (mobile.length() != phone_length) {
            etEnterPhone.setError("phone number must be " + phone_length + " digits");
            return;
        }
        if (TextUtils.isEmpty(address)) {
            etEnterAddress.setError(baseActivity.getString(R.string.empty_error));
            return;
        }
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
       /* if (!cbAgreeTerms.isChecked()) {
            cbAgreeTerms.setError(baseActivity.getString(R.string.check_box));
            return;
        }*/

        User user = new User(name, APIImageUtil.bitmapToBase64(bitmap),
                /*AppController.getInstance().getAppSettingsPreferences()
                        .getCountry().getPhone_code() +*/ mobile, email, address,
                "delivery_driver", password, confirmPassword, AppController.getInstance()
                .getAppSettingsPreferences().getCountry().getId(), city_id);

        Log.e("country_id", user.getCountry_id() + "");
        finishStepOne(user);
    }

    private void finishStepOne(User user) {
        dialogView.showDialog("");

        new APIUtil<Login>(baseActivity).getData(AppController.getInstance().getApi().SignUp(user)
                , new RequestListener<Login>() {
                    @Override
                    public void onSuccess(Login login, String msg) {
                        AppController.getInstance().getAppSettingsPreferences().setLogin(login);
                        AppController.getInstance().getAppSettingsPreferences().setIsLogin(true);
                        AppController.getInstance().getAppSettingsPreferences().setPassword(user.getPassword());
                        dialogView.hideDialog();

                        baseActivity.navigate(RegisterStepTwoFragment.page);
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

    public void getNeighborhood() {
        dialogView.showDialog("");
        new APIUtil<NeighborhoodList>(baseActivity).getData(AppController.getInstance().getApi()
                .getNeighborhood(AppController.getInstance().getAppSettingsPreferences().getCountry().getId())
                , new RequestListener<NeighborhoodList>() {
            @Override
            public void onSuccess(NeighborhoodList neighborhoodList, String msg) {
                dialogView.hideDialog();
                dialogView.setData(neighborhoodList);
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

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public void onActivityResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AppContent.REQUEST_STUDIO:
                    photoTakerManager.processGalleryPhoto(baseActivity, data);
                    break;
                case AppContent.REQUEST_CAMERA:
                    photoTakerManager.processCameraPhoto(baseActivity);
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            photoTakerManager.deleteLastTakenPhoto();
        }
    }
/*
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
    }*/
}

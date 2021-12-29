package com.webapp.a4_order_station_driver.feature.register.one;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.feature.register.two.RegisterStepTwoFragment;
import com.webapp.a4_order_station_driver.models.City;
import com.webapp.a4_order_station_driver.models.Result;
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

import java.util.ArrayList;
import java.util.HashMap;

public class RegisterStepOnePresenter implements DialogView<String> {


    private BaseActivity baseActivity;
    private DialogView<ArrayList<City>> dialogView;
    private PhotoTakerManager photoTakerManager;
    private int requestCode;
    private String fileName;

    public RegisterStepOnePresenter(BaseActivity baseActivity
            , DialogView<ArrayList<City>> dialogView, PhotoTakerManager photoTakerManager) {
        this.baseActivity = baseActivity;
        this.dialogView = dialogView;
        this.photoTakerManager = photoTakerManager;
    }

    public void validInput(EditText etEnterName, EditText etEnterEmail, EditText etEnterAddress
            , EditText etEnterPhone, EditText etEnterPassword, EditText etEnterConfirmPassword, int city_id) {

        String name = etEnterName.getText().toString().trim();
        String email = etEnterEmail.getText().toString().trim();
        String mobile = etEnterPhone.getText().toString().trim();
        String password = etEnterPassword.getText().toString().trim();
        String confirmPassword = etEnterConfirmPassword.getText().toString().trim();
        String address = etEnterAddress.getText().toString().trim();
        int phone_length = AppController.getInstance().getAppSettingsPreferences()
                .getSettings().getData().getPhone_length();

        if (TextUtils.isEmpty(fileName)) {
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
            etEnterPhone.setError(baseActivity.getString(R.string.phone_must) + phone_length + baseActivity.getString(R.string.digits));
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

        /*User user = new User(name, fileName,/*APIImageUtil.bitmapToBase64(bitmap),
                AppController.getInstance().getAppSettingsPreferences()
                        .getCountry().getPhone_code() + mobile, email, address,
                "delivery_driver", password, confirmPassword, AppController.getInstance()
                .getAppSettingsPreferences().getCountry().getId(), city_id);

        Log.e("country_id", user.getCountry_id() + "");*/
        finishStepOne(User.mapUploadStepOne(name, AppController.getInstance()
                        .getAppSettingsPreferences().getSettings().getData().getPhone_code() + mobile
                , email, password, address, AppController.getInstance().getAppSettingsPreferences()
                        .getSettings().getData().getCountry_id(), city_id, fileName));
    }

    private void finishStepOne(HashMap<String, String> map) {
        dialogView.showDialog("");

        new APIUtil<Result<User>>(baseActivity).getData(AppController.getInstance().getApi().SignUp(map)
                , new RequestListener<Result<User>>() {
                    @Override
                    public void onSuccess(Result<User> result, String msg) {
                        Log.e(getClass().getName() + "user", result.getData().toString());
                        AppController.getInstance().getAppSettingsPreferences().setUser(result.getData());
                        AppController.getInstance().getAppSettingsPreferences().setToken(result.getData().getToken());
                        //AppController.getInstance().getAppSettingsPreferences().setPassword(user.getPassword());
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

    public void getCities() {
        dialogView.showDialog("");
        new APIUtil<Result<ArrayList<City>>>(baseActivity).getData(AppController.getInstance().getApi()
                        .getCities(AppController.getInstance().getAppSettingsPreferences().getSettings()
                                .getData().getCountry_id())
                , new RequestListener<Result<ArrayList<City>>>() {
                    @Override
                    public void onSuccess(Result<ArrayList<City>> result, String msg) {
                        dialogView.hideDialog();
                        dialogView.setData(result.getData());
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

    public void uploadImage(Bitmap bitmap) {
        APIImageUtil.uploadImage(baseActivity, this, bitmap);
    }

    @Override
    public void setData(String s) {
        fileName = s;
    }

    @Override
    public void showDialog(String s) {
        dialogView.showDialog(s);
    }

    @Override
    public void hideDialog() {
        dialogView.hideDialog();
    }
}

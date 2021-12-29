package com.webapp.a4_order_station_driver.feature.register.two;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.FragmentRegisterStep2Binding;
import com.webapp.a4_order_station_driver.feature.login.LoginActivity;
import com.webapp.a4_order_station_driver.feature.main.MainActivity2;
import com.webapp.a4_order_station_driver.models.Result;
import com.webapp.a4_order_station_driver.models.User;
import com.webapp.a4_order_station_driver.services.firebase.GenerateFCMService;
import com.webapp.a4_order_station_driver.utils.APIImageUtil;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.Photo.PhotoTakerManager;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

import java.util.HashMap;

public class RegisterStepTwoPresenter {

    private BaseActivity baseActivity;
    private DialogView<String> dialogView;
    private PhotoTakerManager photoTakerManager;
    private int requestCode;

    public RegisterStepTwoPresenter(BaseActivity baseActivity
            , DialogView<String> dialogView, PhotoTakerManager photoTakerManager) {
        this.baseActivity = baseActivity;
        this.dialogView = dialogView;
        this.photoTakerManager = photoTakerManager;
    }

    public void validInput(FragmentRegisterStep2Binding binding, HashMap<String, String> map) {
        String plate = binding.etVehiclePlate.getText().toString().trim();
        String type = binding.etVehicleType.getText().toString().trim();

        if (map.size() < 5) {
            ToolUtil.showLongToast(baseActivity.getString(R.string.fill_photos), baseActivity);
            return;
        }

        if (TextUtils.isEmpty(type)) {
            binding.etVehicleType.setError(baseActivity.getString(R.string.empty_error));
            return;
        }
        if (TextUtils.isEmpty(plate)) {
            binding.etVehiclePlate.setError(baseActivity.getString(R.string.empty_error));
            return;
        }

        if (!binding.cbAgreeTerms.isChecked()) {
            binding.cbAgreeTerms.setError(baseActivity.getString(R.string.accept_terms));
            return;
        }
        map.put("vehicle_plate",plate);
        map.put("vehicle_type", type);
        generateFCMToken(baseActivity, map);
    }

    public void generateFCMToken(Context context, HashMap<String, String> map) {
        dialogView.showDialog("");
        FirebaseApp.initializeApp(context);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("Main", "getInstanceId failed", task.getException());
                return;
            }
            // Get new Instance ID token
            String token = task.getResult();
            Log.e("fcm token", "" + token);
            map.put("fcm_token", token);
            finishStepTwo(map);
        });
    }

    public void finishStepTwo(HashMap<String, String> map) {
        new APIUtil<Result<User>>(baseActivity).getData(AppController.getInstance().getApi().signUpStepTwo(map),
                new RequestListener<Result<User>>() {
                    @Override
                    public void onSuccess(Result<User> result, String msg) {
                        dialogView.hideDialog();
                        AppController.getInstance().getAppSettingsPreferences().setUser(result.getData());
                        //service
                        Intent service = new Intent(baseActivity, GenerateFCMService.class);
                        baseActivity.startService(service);
                        //main
                        baseActivity.navigate(MainActivity2.page);
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
                case AppContent.REQUEST_IMAGE_VEHICLE_UPLOAD:
                case AppContent.REQUEST_IMAGE_VEHICLE_LICENSE_UPLOAD:
                case AppContent.REQUEST_IMAGE_VEHICLE_INSURANCE_UPLOAD:
                case AppContent.REQUEST_IMAGE_IDENTITY_UPLOAD:
                case AppContent.REQUEST_IMAGE_YOUR_LICENSE_UPLOAD:
                    photoTakerManager.processGalleryPhoto(baseActivity, data);
                    break;
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

    public void uploadImage(Bitmap bitmap) {
        APIImageUtil.uploadImage(baseActivity, dialogView, bitmap);
    }

    public void goLoginPage() {
        baseActivity.navigate(LoginActivity.page);
    }
}

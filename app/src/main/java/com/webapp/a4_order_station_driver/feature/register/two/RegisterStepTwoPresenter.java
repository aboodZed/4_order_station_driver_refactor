package com.webapp.a4_order_station_driver.feature.register.two;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.feature.main.MainActivity;
import com.webapp.a4_order_station_driver.models.Login;
import com.webapp.a4_order_station_driver.models.User;
import com.webapp.a4_order_station_driver.services.firebase.GenerateFCMService;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.PermissionUtil;
import com.webapp.a4_order_station_driver.utils.Photo.PhotoTakerManager;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

import static android.app.Activity.RESULT_OK;

public class RegisterStepTwoPresenter {

    private BaseActivity baseActivity;
    private DialogView<User> dialogView;
    private PhotoTakerManager photoTakerManager;

    public RegisterStepTwoPresenter(BaseActivity baseActivity
            , DialogView<User> dialogView, PhotoTakerManager photoTakerManager) {
        this.baseActivity = baseActivity;
        this.dialogView = dialogView;
        this.photoTakerManager = photoTakerManager;
    }

    public void validInput( String images[], EditText etVehiclePlate, EditText etVehicleType) {
        String plate = etVehiclePlate.getText().toString().trim();
        String type = etVehicleType.getText().toString().trim();

        for (int i = 0; i < images.length; i++) {
            if (TextUtils.isEmpty(images[i])) {
                ToolUtil.showLongToast(baseActivity.getString(R.string.fill_photos), baseActivity);
                return;
            }
        }
        if (TextUtils.isEmpty(type)) {
            etVehicleType.setError(baseActivity.getString(R.string.empty_error));
            return;
        }
        if (TextUtils.isEmpty(plate)) {
            etVehiclePlate.setError(baseActivity.getString(R.string.empty_error));
            return;
        }

        User user = new User(images[0], images[1], images[2], images[3], images[4], plate, type);

        finishStepTwo(user);
    }

    public void finishStepTwo(User user) {
        dialogView.showDialog("");
        new APIUtil<User>(baseActivity).getData(AppController.getInstance().getApi().signUp2(user),
                new RequestListener<User>() {
                    @Override
                    public void onSuccess(User user, String msg) {
                        dialogView.hideDialog();
                        //update user
                        Login login = AppController.getInstance().getAppSettingsPreferences().getLogin();
                        login.setUser(user);
                        AppController.getInstance().getAppSettingsPreferences().setLogin(login);
                        AppController.getInstance().getAppSettingsPreferences().setIsLogin(true);
                        //service
                        Intent service = new Intent(baseActivity, GenerateFCMService.class);
                        baseActivity.startService(service);

                        baseActivity.navigate(MainActivity.page);
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
}

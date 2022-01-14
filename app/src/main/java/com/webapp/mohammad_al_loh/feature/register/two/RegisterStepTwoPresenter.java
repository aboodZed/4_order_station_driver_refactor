package com.webapp.mohammad_al_loh.feature.register.two;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;

import com.webapp.mohammad_al_loh.R;
import com.webapp.mohammad_al_loh.feature.main.MainActivity;
import com.webapp.mohammad_al_loh.models.Login;
import com.webapp.mohammad_al_loh.models.User;
import com.webapp.mohammad_al_loh.services.firebase.GenerateFCMService;
import com.webapp.mohammad_al_loh.utils.APIUtil;
import com.webapp.mohammad_al_loh.utils.AppContent;
import com.webapp.mohammad_al_loh.utils.AppController;
import com.webapp.mohammad_al_loh.utils.Photo.PhotoTakerManager;
import com.webapp.mohammad_al_loh.utils.ToolUtil;
import com.webapp.mohammad_al_loh.utils.language.BaseActivity;
import com.webapp.mohammad_al_loh.utils.listeners.DialogView;
import com.webapp.mohammad_al_loh.utils.listeners.RequestListener;

public class RegisterStepTwoPresenter {

    private BaseActivity baseActivity;
    private DialogView<User> dialogView;
    private PhotoTakerManager photoTakerManager;
    private int requestCode;

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
}

package com.webapp.a4_order_station_driver.feature.register.two;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.feature.home.MainActivity;
import com.webapp.a4_order_station_driver.models.Login;
import com.webapp.a4_order_station_driver.models.User;
import com.webapp.a4_order_station_driver.services.firebase.GenerateFCMService;
import com.webapp.a4_order_station_driver.utils.APIUtils;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

public class RegisterStep_2Presenter {

    private BaseActivity baseActivity;
    private DialogView<User> dialogView;

    public RegisterStep_2Presenter(BaseActivity baseActivity, DialogView<User> dialogView) {
        this.baseActivity = baseActivity;
        this.dialogView = dialogView;
    }

    public void validInput(boolean saveImage[], String images[], EditText etVehiclePlate, EditText etVehicleType) {
        String plate = etVehiclePlate.getText().toString().trim();
        String type = etVehicleType.getText().toString().trim();

        for (int i = 0; i < saveImage.length; i++) {
            if (!saveImage[i]) {
                ToolUtils.showLongToast(baseActivity.getString(R.string.fill_photos), baseActivity);
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

        finishStep_2(user);
    }

    public void finishStep_2(User user) {
        dialogView.showDialog("");
        new APIUtils<User>(baseActivity).getData(AppController.getInstance().getApi().signUp2(user),
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
                        ToolUtils.showLongToast(msg, baseActivity);
                    }

                    @Override
                    public void onFail(String msg) {
                        dialogView.hideDialog();
                        ToolUtils.showLongToast(msg, baseActivity);
                    }
                });
    }
}

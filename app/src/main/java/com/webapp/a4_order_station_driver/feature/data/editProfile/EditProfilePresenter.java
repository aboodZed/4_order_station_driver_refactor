package com.webapp.a4_order_station_driver.feature.data.editProfile;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Patterns;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.FragmentEditProfileBinding;
import com.webapp.a4_order_station_driver.feature.main.profile.ProfileFragment;
import com.webapp.a4_order_station_driver.feature.register.adapter.SpinnerAdapter;
import com.webapp.a4_order_station_driver.models.City;
import com.webapp.a4_order_station_driver.models.Login;
import com.webapp.a4_order_station_driver.models.Result;
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

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.http.QueryMap;

class EditProfilePresenter {

    private BaseActivity baseActivity;
    private DialogView<String> dialogView;
    private PhotoTakerManager photoTakerManager;
    private int requestCode;

    public EditProfilePresenter(BaseActivity baseActivity, DialogView<String> dialogView, PhotoTakerManager photoTakerManager) {
        this.baseActivity = baseActivity;
        this.dialogView = dialogView;
        this.photoTakerManager = photoTakerManager;
    }

    public void validInput(FragmentEditProfileBinding binding, HashMap<String, String> map) {

        String name = binding.etEnterName.getText().toString().trim();
        String email = binding.etEnterEmail.getText().toString().trim();
        String address = binding.etEnterAddress.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            binding.etEnterName.setError(baseActivity.getString(R.string.empty_error));
            return;
        }
        if (TextUtils.isEmpty(email)) {
            binding.etEnterEmail.setError(baseActivity.getString(R.string.empty_error));
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEnterEmail.setError(baseActivity.getString(R.string.un_match_pattern));
            return;
        }
        if (TextUtils.isEmpty(address)) {
            binding.etEnterAddress.setError(baseActivity.getString(R.string.empty_error));
            return;
        }
        //fill data
        map.put("name", name);
        map.put("email", email);
        map.put("address", address);
        City city = (City) binding.spNeighborhood.getSelectedItem();
        map.put("city_id", String.valueOf(city.getId()));

        updateProfile(map);
    }

    public void getCities(FragmentEditProfileBinding binding) {
        dialogView.showDialog("");
        new APIUtil<Result<ArrayList<City>>>(baseActivity).getData(AppController.getInstance()
                        .getApi().getCities(AppController.getInstance().getAppSettingsPreferences().
                                getSettings().getData().getCountry_id())
                , new RequestListener<Result<ArrayList<City>>>() {
                    @Override
                    public void onSuccess(Result<ArrayList<City>> result, String msg) {
                        dialogView.hideDialog();
                        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(baseActivity, result.getData());
                        binding.spNeighborhood.setAdapter(spinnerAdapter);
                        for (int i = 0; i < result.getData().size(); i++) {
                            if (result.getData().get(i).getId() == AppController.getInstance()
                                    .getAppSettingsPreferences().getUser().getCity().getId()) {
                                binding.spNeighborhood.setSelection(i);
                            }
                        }
                        //dialogView.setData(result.getData());
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

    private void updateProfile(HashMap<String, String> params) {
        dialogView.showDialog("");
        new APIUtil<Result<User>>(baseActivity).getData(AppController.getInstance().getApi()
                .updateProfile(params), new RequestListener<Result<User>>() {
            @Override
            public void onSuccess(Result<User> result, String msg) {
                AppController.getInstance().getAppSettingsPreferences().setUser(result.getData());
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

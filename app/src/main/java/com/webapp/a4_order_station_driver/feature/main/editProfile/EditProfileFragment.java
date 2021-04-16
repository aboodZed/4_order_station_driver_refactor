package com.webapp.a4_order_station_driver.feature.main.editProfile;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.FragmentEditProfileBinding;
import com.webapp.a4_order_station_driver.models.User;
import com.webapp.a4_order_station_driver.utils.APIImageUtil;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.PermissionUtil;
import com.webapp.a4_order_station_driver.utils.Photo.PhotoTakerManager;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.dialogs.ItemSelectImageDialogFragment;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

import static android.app.Activity.RESULT_OK;

public class EditProfileFragment extends Fragment implements DialogView<User>, RequestListener<Bitmap> {

    public static final int page = 209;

    private FragmentEditProfileBinding binding;
    private EditProfilePresenter presenter;
    private ItemSelectImageDialogFragment itemSelectImageDialogFragment;
    private PhotoTakerManager photoTakerManager;
    private BaseActivity baseActivity;

    private String[] images = new String[6];
    private int process_image;

    protected static final int AVATAR = 0;
    protected static final int VEHICLE_IMAGE = 5;
    protected static final int VEHICLE_LICENSE = 3;
    protected static final int VEHICLE_INSURANCE = 1;
    protected static final int IDENTITY = 4;
    protected static final int YOUR_LICENSE = 2;

    public EditProfileFragment(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    public static EditProfileFragment newInstance(BaseActivity baseActivity) {
        return new EditProfileFragment(baseActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(getLayoutInflater());
        photoTakerManager = new PhotoTakerManager(this);
        presenter = new EditProfilePresenter(baseActivity, this, photoTakerManager);
        data();
        click();
        return binding.getRoot();
    }

    private void click() {
        binding.ivEditDriverAvatar.setOnClickListener(view -> {
            request(AppContent.REQUEST_IMAGE_AVATAR_UPLOAD, AppContent.REQUEST_IMAGE_AVATAR_CAMERA);
            process_image = AVATAR;
        });
        binding.ivEditIdPic.setOnClickListener(view -> {
            request(AppContent.REQUEST_IMAGE_IDENTITY_UPLOAD, AppContent.REQUEST_IMAGE_IDENTITY_CAMERA);
            process_image = IDENTITY;
        });
        binding.ivEditYourVehicle.setOnClickListener(view -> {
            request(AppContent.REQUEST_IMAGE_VEHICLE_UPLOAD, AppContent.REQUEST_IMAGE_VEHICLE_CAMERA);
            process_image = VEHICLE_IMAGE;
        });
        binding.ivEditInsuranceLicense.setOnClickListener(view -> {
            request(AppContent.REQUEST_IMAGE_VEHICLE_INSURANCE_UPLOAD, AppContent.REQUEST_IMAGE_VEHICLE_INSURANCE_CAMERA);
            process_image = VEHICLE_INSURANCE;
        });
        binding.ivEditVehicleLicense.setOnClickListener(view -> {
            request(AppContent.REQUEST_IMAGE_VEHICLE_LICENSE_UPLOAD, AppContent.REQUEST_IMAGE_VEHICLE_LICENSE_CAMERA);
            process_image = VEHICLE_LICENSE;
        });
        binding.ivEditYourLicense.setOnClickListener(view -> {
            request(AppContent.REQUEST_IMAGE_YOUR_LICENSE_UPLOAD, AppContent.REQUEST_IMAGE_YOUR_LICENSE_CAMERA);
            process_image = YOUR_LICENSE;
        });

        binding.btnOk.setOnClickListener(view -> presenter.validInput(binding, images));
    }

    //functions
    private void data() {
        User user = AppController.getInstance().getAppSettingsPreferences().getLogin().getUser();
        APIImageUtil.loadImage(getContext(), binding.pbWaitAvater, user.getAvatar_url(), binding.ivDriverAvatar);
        APIImageUtil.loadImage(getContext(), binding.pbWaitIdPic, user.getId_pic_url(), binding.ivIdPic);
        APIImageUtil.loadImage(getContext(), binding.pbWaitYourLicense, user.getDrive_license_url(), binding.ivYourLicense);
        APIImageUtil.loadImage(getContext(), binding.pbWaitYourVehicle, user.getVehicle_pic_url(), binding.ivYourVehicle);
        APIImageUtil.loadImage(getContext(), binding.pbWaitVehicleLicense, user.getVehicle_license_url(), binding.ivVehicleLicense);
        APIImageUtil.loadImage(getContext(), binding.pbWaitInsuranceLicense, user.getInsurance_license_url(), binding.ivInsuranceLicense);
        binding.etEnterEmail.setText(user.getEmail());
        binding.etEnterPhone.setText(user.getMobile());
        binding.etEnterAddress.setText(user.getAddress());
        Log.e(getClass().getName() + " : userData", user.toString());
        if (PermissionUtil.isPermissionGranted(MediaStore.ACTION_IMAGE_CAPTURE, getContext()))
            PermissionUtil.requestPermission(getActivity(), Manifest.permission.CAMERA
                    , AppContent.REQUEST_PERMISSIONS_R_W_STORAGE_CAMERA);
    }

    private void request(int request_upload, int request_camera) {
        itemSelectImageDialogFragment = ItemSelectImageDialogFragment.newInstance();
        itemSelectImageDialogFragment.setListener(new ItemSelectImageDialogFragment.Listener() {
            @Override
            public void onGalleryClicked() {
                photoTakerManager.galleryRequest(requireActivity(), request_upload);
            }

            @Override
            public void onCameraClicked() {
                photoTakerManager.cameraRequest(requireActivity(), request_camera);
            }
        });
        itemSelectImageDialogFragment.show(getChildFragmentManager(), "");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        presenter.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void setData(User user) {

    }

    @Override
    public void showDialog(String s) {
        WaitDialogFragment.newInstance().show(getChildFragmentManager(), "");
    }

    @Override
    public void hideDialog() {
        WaitDialogFragment.newInstance().dismiss();
    }

    @Override
    public void onSuccess(Bitmap bitmap, String msg) {
        switch (process_image) {
            case AVATAR:
                binding.ivDriverAvatar.setImageBitmap(bitmap);
                images[AVATAR] = APIImageUtil.bitmapToBase64(bitmap);
                binding.pbWaitAvater.setVisibility(View.GONE);
                break;
            case VEHICLE_IMAGE:
                binding.ivYourVehicle.setImageBitmap(bitmap);
                images[VEHICLE_IMAGE] = APIImageUtil.bitmapToBase64(bitmap);
                binding.pbWaitYourVehicle.setVisibility(View.GONE);
                break;
            case VEHICLE_LICENSE:
                binding.ivVehicleLicense.setImageBitmap(bitmap);
                images[VEHICLE_LICENSE] = APIImageUtil.bitmapToBase64(bitmap);
                binding.pbWaitVehicleLicense.setVisibility(View.GONE);
                break;
            case VEHICLE_INSURANCE:
                binding.ivInsuranceLicense.setImageBitmap(bitmap);
                images[VEHICLE_INSURANCE] = APIImageUtil.bitmapToBase64(bitmap);
                binding.pbWaitInsuranceLicense.setVisibility(View.GONE);
                break;
            case IDENTITY:
                binding.ivIdPic.setImageBitmap(bitmap);
                images[IDENTITY] = APIImageUtil.bitmapToBase64(bitmap);
                binding.pbWaitIdPic.setVisibility(View.GONE);
                break;
            case YOUR_LICENSE:
                binding.ivYourLicense.setImageBitmap(bitmap);
                images[YOUR_LICENSE] = APIImageUtil.bitmapToBase64(bitmap);
                binding.pbWaitYourLicense.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onError(String msg) {

    }

    @Override
    public void onFail(String msg) {

    }
}

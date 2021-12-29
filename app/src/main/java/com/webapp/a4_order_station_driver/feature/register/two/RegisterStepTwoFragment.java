package com.webapp.a4_order_station_driver.feature.register.two;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.webapp.a4_order_station_driver.databinding.FragmentRegisterStep2Binding;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.Photo.PhotoTakerManager;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.dialogs.ItemSelectImageDialogFragment;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

import java.util.HashMap;

public class RegisterStepTwoFragment extends Fragment implements DialogView<String>, RequestListener<Bitmap> {

    public static final int page = 302;

    private FragmentRegisterStep2Binding binding;
    private RegisterStepTwoPresenter presenter;
    private ItemSelectImageDialogFragment itemSelectImageDialogFragment;
    private PhotoTakerManager photoTakerManager;
    private HashMap<String, String> map = new HashMap<>();
    private int process_image;

    private final int VEHICLE = 0;
    private final int VEHICLE_LICENSE = 1;
    private final int VEHICLE_INSURANCE = 2;
    private final int IDENTITY = 3;
    private final int YOUR_LICENSE = 4;

    private ActivityResultLauncher<Intent> launcher;

    public RegisterStepTwoFragment(BaseActivity baseActivity) {
        photoTakerManager = new PhotoTakerManager(this);
        presenter = new RegisterStepTwoPresenter(baseActivity, this, photoTakerManager);
    }

    public static RegisterStepTwoFragment newInstance(BaseActivity baseActivity) {
        return new RegisterStepTwoFragment(baseActivity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterStep2Binding.inflate(getLayoutInflater());

        click();
        onActivityResulting();
        return binding.getRoot();
    }

    private void onActivityResulting() {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                , result -> presenter.onActivityResult(result.getResultCode(), result.getData()));
    }

    private void click() {
        binding.ivVehicle.setOnClickListener(view -> {
            process_image = VEHICLE;
            request(AppContent.REQUEST_IMAGE_VEHICLE_UPLOAD, AppContent.REQUEST_IMAGE_VEHICLE_CAMERA);
        });

        binding.ivVehicleLicense.setOnClickListener(view -> {
            process_image = VEHICLE_LICENSE;
            request(AppContent.REQUEST_IMAGE_VEHICLE_LICENSE_UPLOAD, AppContent.REQUEST_IMAGE_VEHICLE_LICENSE_CAMERA);
        });

        binding.ivVehicleInsurance.setOnClickListener(view -> {
            process_image = VEHICLE_INSURANCE;
            request(AppContent.REQUEST_IMAGE_VEHICLE_INSURANCE_UPLOAD, AppContent.REQUEST_IMAGE_VEHICLE_INSURANCE_CAMERA);
        });

        binding.ivIdentity.setOnClickListener(view -> {
            process_image = IDENTITY;
            request(AppContent.REQUEST_IMAGE_IDENTITY_UPLOAD, AppContent.REQUEST_IMAGE_IDENTITY_CAMERA);
        });

        binding.ivYourLicense.setOnClickListener(view -> {
            process_image = YOUR_LICENSE;
            request(AppContent.REQUEST_IMAGE_YOUR_LICENSE_UPLOAD, AppContent.REQUEST_IMAGE_YOUR_LICENSE_CAMERA);
        });

        binding.btnRegister.setOnClickListener(view -> presenter.validInput(binding, map));
        binding.btnSignIn.setOnClickListener(view -> presenter.goLoginPage());
    }

    private void request(int request_upload, int request_camera) {
        itemSelectImageDialogFragment = ItemSelectImageDialogFragment.newInstance();
        itemSelectImageDialogFragment.setListener(new ItemSelectImageDialogFragment.Listener() {
            @Override
            public void onGalleryClicked() {
                presenter.setRequestCode(request_upload);
                photoTakerManager.galleryRequestLauncher(getActivity(), launcher);
            }

            @Override
            public void onCameraClicked() {
                presenter.setRequestCode(request_camera);
                photoTakerManager.cameraRequestLauncher(getActivity(), launcher);
            }
        });
        itemSelectImageDialogFragment.show(getChildFragmentManager(), "");
    }

    @Override
    public void setData(String fileName) {
        switch (process_image) {
            case VEHICLE:
                map.put("vehicle_pic", fileName);
                break;
            case VEHICLE_LICENSE:
                map.put("vehicle_license", fileName);
                break;
            case VEHICLE_INSURANCE:
                map.put("insurance_license", fileName);
                break;
            case IDENTITY:
                map.put("id_pic", fileName);
                break;
            case YOUR_LICENSE:
                map.put("drive_license", fileName);
                break;
        }
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
        presenter.uploadImage(bitmap);
        switch (process_image) {
            case VEHICLE:
                binding.ivVehicle.setImageBitmap(bitmap);
                break;
            case VEHICLE_LICENSE:
                binding.ivVehicleLicense.setImageBitmap(bitmap);
                break;
            case VEHICLE_INSURANCE:
                binding.ivVehicleInsurance.setImageBitmap(bitmap);
                break;
            case IDENTITY:
                binding.ivIdentity.setImageBitmap(bitmap);
                break;
            case YOUR_LICENSE:
                binding.ivYourLicense.setImageBitmap(bitmap);
                break;
        }
    }

    @Override
    public void onError(String msg) {
        ToolUtil.showLongToast(msg, getActivity());
    }

    @Override
    public void onFail(String msg) {
        ToolUtil.showLongToast(msg, getActivity());
    }
}

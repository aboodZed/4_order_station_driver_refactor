package com.webapp.a4_order_station_driver.feature.register.two;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.FragmentRegisterStep2Binding;
import com.webapp.a4_order_station_driver.models.User;
import com.webapp.a4_order_station_driver.utils.APIImageUtil;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.Photo.PhotoTakerManager;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class RegisterStepTwoFragment extends Fragment implements DialogView<User>, RequestListener<Bitmap> {

    public static final int page = 302;

    private FragmentRegisterStep2Binding binding;
    private RegisterStepTwoPresenter presenter;
    private PhotoTakerManager photoTakerManager;
    private String[] images = new String[5];
    private int process_image;

    private final int VEHICLE_IMAGE = 0;
    private final int VEHICLE_LICENSE = 1;
    private final int VEHICLE_INSURANCE = 2;
    private final int IDENTITY = 3;
    private final int YOUR_LICENSE = 4;

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
        return binding.getRoot();
    }

    public void signUp() {
        presenter.validInput(images, binding.etVehiclePlate, binding.etVehicleType);
    }

    private void click() {
        binding.ivVehicleUpload.setOnClickListener(view ->
        {
            photoTakerManager.galleryRequest(requireActivity()
                    , AppContent.REQUEST_IMAGE_VEHICLE_UPLOAD);
            process_image = VEHICLE_IMAGE;
        });

        binding.ivVehicleLicenseUpload.setOnClickListener(view ->
        {
            photoTakerManager.galleryRequest(requireActivity()
                    , AppContent.REQUEST_IMAGE_VEHICLE_LICENSE_UPLOAD);
            process_image = VEHICLE_LICENSE;
        });

        binding.ivVehicleInsuranceUpload.setOnClickListener(view ->
        {
            photoTakerManager.galleryRequest(requireActivity()
                    , AppContent.REQUEST_IMAGE_VEHICLE_INSURANCE_UPLOAD);
            process_image = VEHICLE_INSURANCE;
        });

        binding.ivIdentityUpload.setOnClickListener(view ->
        {
            photoTakerManager.galleryRequest(requireActivity()
                    , AppContent.REQUEST_IMAGE_IDENTITY_UPLOAD);
            process_image = IDENTITY;
        });

        binding.ivYourLicenseUpload.setOnClickListener(view ->
        {
            photoTakerManager.galleryRequest(requireActivity()
                    , AppContent.REQUEST_IMAGE_YOUR_LICENSE_UPLOAD);
            process_image = YOUR_LICENSE;
        });


        binding.ivVehicleCamera.setOnClickListener(view ->
        {
            photoTakerManager.cameraRequest(requireActivity()
                    , AppContent.REQUEST_IMAGE_VEHICLE_CAMERA);
            process_image = VEHICLE_IMAGE;
        });

        binding.ivVehicleLicenseCamera.setOnClickListener(view ->
        {
            photoTakerManager.cameraRequest(requireActivity()
                    , AppContent.REQUEST_IMAGE_VEHICLE_LICENSE_CAMERA);
            process_image = VEHICLE_LICENSE;
        });

        binding.ivVehicleInsuranceCamera.setOnClickListener(view ->
        {
            photoTakerManager.cameraRequest(requireActivity()
                    , AppContent.REQUEST_IMAGE_VEHICLE_INSURANCE_CAMERA);
            process_image = VEHICLE_INSURANCE;
        });

        binding.ivIdentityCamera.setOnClickListener(view ->
        {
            photoTakerManager.cameraRequest(requireActivity()
                    , AppContent.REQUEST_IMAGE_IDENTITY_CAMERA);
            process_image = IDENTITY;
        });

        binding.ivYourLicenseCamera.setOnClickListener(view ->
        {
            photoTakerManager.cameraRequest(requireActivity()
                    , AppContent.REQUEST_IMAGE_YOUR_LICENSE_CAMERA);
            process_image = YOUR_LICENSE;
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        presenter.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
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
            case VEHICLE_IMAGE:
                binding.ivVehicle.setImageBitmap(bitmap);
                images[VEHICLE_IMAGE] = APIImageUtil.bitmapToBase64(bitmap);
                break;
            case VEHICLE_LICENSE:
                binding.ivVehicleLicense.setImageBitmap(bitmap);
                images[VEHICLE_LICENSE] = APIImageUtil.bitmapToBase64(bitmap);
                break;
            case VEHICLE_INSURANCE:
                binding.ivVehicleInsurance.setImageBitmap(bitmap);
                images[VEHICLE_INSURANCE] = APIImageUtil.bitmapToBase64(bitmap);
                break;
            case IDENTITY:
                binding.ivIdentity.setImageBitmap(bitmap);
                images[IDENTITY] = APIImageUtil.bitmapToBase64(bitmap);
                break;
            case YOUR_LICENSE:
                binding.ivYourLicense.setImageBitmap(bitmap);
                images[YOUR_LICENSE] = APIImageUtil.bitmapToBase64(bitmap);
                break;
        }
    }

    @Override
    public void onError(String msg) {

    }

    @Override
    public void onFail(String msg) {
        ToolUtil.showLongToast(msg, getActivity());
    }
}

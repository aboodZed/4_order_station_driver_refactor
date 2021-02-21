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
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class RegisterStep_2 extends Fragment implements DialogView<User> {

    public static final int page = 302;

    private FragmentRegisterStep2Binding binding;
    private RegisterStep_2Presenter presenter;

    private boolean[] saveImage = new boolean[5];
    private String[] images = new String[5];

    public RegisterStep_2(BaseActivity baseActivity) {
        presenter = new RegisterStep_2Presenter(baseActivity, this);
    }

    public static RegisterStep_2 newInstance(BaseActivity baseActivity) {
        RegisterStep_2 fragment = new RegisterStep_2(baseActivity);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View v = inflater.inflate(R.layout.fragment_register_step_2, container, false);
        binding = FragmentRegisterStep2Binding.inflate(getLayoutInflater());
        click();
        return binding.getRoot();
    }

    public void signUp() {
        presenter.validInput(saveImage, images, binding.etVehiclePlate, binding.etVehicleType);
    }

    private void click() {
        binding.ivVehicleUpload.setOnClickListener(view -> setIvVehicleUpload());
        binding.ivVehicleLicenseUpload.setOnClickListener(view -> setIvVehicleLicenseUpload());
        binding.ivVehicleInsuranceUpload.setOnClickListener(view -> setIvVehicleInsuranceUpload());
        binding.ivIdentityUpload.setOnClickListener(view -> setIvIdentityUpload());
        binding.ivYourLicenseUpload.setOnClickListener(view -> setIvYourLicenseUpload());

        binding.ivVehicleCamera.setOnClickListener(view -> setIvVehicleCamera());
        binding.ivVehicleLicenseCamera.setOnClickListener(view -> setIvVehicleLicenseCamera());
        binding.ivVehicleInsuranceCamera.setOnClickListener(view -> setIvVehicleInsuranceCamera());
        binding.ivIdentityCamera.setOnClickListener(view -> setIvIdentityCamera());
        binding.ivYourLicenseCamera.setOnClickListener(view -> setIvYourLicenseCamera());
    }

    void setIvVehicleUpload() {
        if (ToolUtils.checkTheInternet()) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, AppContent.REQUEST_IMAGE_VEHICLE_UPLOAD);
        }
    }

    void setIvVehicleLicenseUpload() {
        if (ToolUtils.checkTheInternet()) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, AppContent.REQUEST_IMAGE_VEHICLE_LICENSE_UPLOAD);
        }
    }

    void setIvVehicleInsuranceUpload() {
        if (ToolUtils.checkTheInternet()) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, AppContent.REQUEST_IMAGE_VEHICLE_INSURANCE_UPLOAD);
        }
    }

    void setIvIdentityUpload() {
        if (ToolUtils.checkTheInternet()) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, AppContent.REQUEST_IMAGE_IDENTITY_UPLOAD);
        }
    }

    void setIvYourLicenseUpload() {
        if (ToolUtils.checkTheInternet()) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, AppContent.REQUEST_IMAGE_YOUR_LICENSE_UPLOAD);
        }
    }

    void setIvVehicleCamera() {
        if (ToolUtils.checkTheInternet()) {
            Intent photoPickerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (photoPickerIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
                getActivity().startActivityForResult(photoPickerIntent, AppContent.REQUEST_IMAGE_VEHICLE_CAMERA);
            }
        }
    }

    void setIvVehicleLicenseCamera() {
        if (ToolUtils.checkTheInternet()) {
            Intent photoPickerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(photoPickerIntent, AppContent.REQUEST_IMAGE_VEHICLE_LICENSE_CAMERA);
        }
    }

    void setIvVehicleInsuranceCamera() {
        if (ToolUtils.checkTheInternet()) {
            Intent photoPickerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(photoPickerIntent, AppContent.REQUEST_IMAGE_VEHICLE_INSURANCE_CAMERA);
        }
    }

    void setIvIdentityCamera() {
        if (ToolUtils.checkTheInternet()) {
            Intent photoPickerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(photoPickerIntent, AppContent.REQUEST_IMAGE_IDENTITY_CAMERA);
        }
    }

    void setIvYourLicenseCamera() {
        if (ToolUtils.checkTheInternet()) {
            Intent photoPickerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(photoPickerIntent, AppContent.REQUEST_IMAGE_YOUR_LICENSE_CAMERA);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {
            if (requestCode == AppContent.REQUEST_IMAGE_VEHICLE_UPLOAD) {
                try {
                    //code
                    Uri uri = data.getData();
                    binding.ivVehicle.setImageURI(uri);
                    images[0] = ToolUtils.bitmapToBase64(ToolUtils.getBitmapFromImageView(binding.ivVehicle));
                    saveImage[0] = true;
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == AppContent.REQUEST_IMAGE_VEHICLE_CAMERA) {
                try {
                    //code
                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                    binding.ivVehicle.setImageBitmap(imageBitmap);
                    images[0] = ToolUtils.bitmapToBase64(imageBitmap);
                    saveImage[0] = true;
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == AppContent.REQUEST_IMAGE_VEHICLE_LICENSE_UPLOAD) {
                try {
                    //code
                    Uri uri = data.getData();
                    binding.ivVehicleLicense.setImageURI(uri);
                    images[1] = ToolUtils.bitmapToBase64(ToolUtils.getBitmapFromImageView(binding.ivVehicleLicense));
                    saveImage[1] = true;
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == AppContent.REQUEST_IMAGE_VEHICLE_LICENSE_CAMERA) {
                try {
                    //code
                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                    binding.ivVehicleLicense.setImageBitmap(imageBitmap);
                    images[1] = ToolUtils.bitmapToBase64(imageBitmap);
                    saveImage[1] = true;

                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == AppContent.REQUEST_IMAGE_VEHICLE_INSURANCE_UPLOAD) {
                try {
                    //code
                    Uri uri = data.getData();
                    binding.ivVehicleInsurance.setImageURI(uri);
                    images[2] = ToolUtils.bitmapToBase64(ToolUtils.getBitmapFromImageView(binding.ivVehicleInsurance));
                    saveImage[2] = true;
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == AppContent.REQUEST_IMAGE_VEHICLE_INSURANCE_CAMERA) {
                try {
                    //code
                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                    binding.ivVehicleInsurance.setImageBitmap(imageBitmap);
                    images[2] = ToolUtils.bitmapToBase64(imageBitmap);
                    saveImage[2] = true;
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == AppContent.REQUEST_IMAGE_IDENTITY_UPLOAD) {
                try {
                    //code
                    Uri uri = data.getData();
                    binding.ivIdentity.setImageURI(uri);
                    images[3] = ToolUtils.bitmapToBase64(ToolUtils.getBitmapFromImageView(binding.ivIdentity));
                    saveImage[3] = true;
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == AppContent.REQUEST_IMAGE_IDENTITY_CAMERA) {
                try {
                    //code
                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                    binding.ivIdentity.setImageBitmap(imageBitmap);
                    images[3] = ToolUtils.bitmapToBase64(imageBitmap);
                    saveImage[3] = true;
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == AppContent.REQUEST_IMAGE_YOUR_LICENSE_UPLOAD) {
                try {
                    //code
                    Uri uri = data.getData();
                    binding.ivYourLicense.setImageURI(uri);
                    images[4] = ToolUtils.bitmapToBase64(ToolUtils.getBitmapFromImageView(binding.ivYourLicense));
                    saveImage[4] = true;
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == AppContent.REQUEST_IMAGE_YOUR_LICENSE_CAMERA) {
                try {
                    //code
                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                    binding.ivYourLicense.setImageBitmap(imageBitmap);
                    images[4] = ToolUtils.bitmapToBase64(imageBitmap);
                    saveImage[4] = true;
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                }
            }
        }
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
}

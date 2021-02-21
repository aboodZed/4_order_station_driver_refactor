package com.webapp.a4_order_station_driver.feature.main.editProfile;

import android.Manifest;
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
import com.webapp.a4_order_station_driver.databinding.FragmentEditProfileBinding;
import com.webapp.a4_order_station_driver.models.User;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.PermissionUtil;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.dialogs.ItemSelectImageDialogFragment;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;

import static android.app.Activity.RESULT_OK;

public class EditProfileFragment extends Fragment implements DialogView<User> {

    public static final int page = 209;

    private FragmentEditProfileBinding binding;
    private EditProfilePresenter presenter;
    private ItemSelectImageDialogFragment itemSelectImageDialogFragment
            = ItemSelectImageDialogFragment.newInstance();

    public static EditProfileFragment newInstance() {
        EditProfileFragment fragment = new EditProfileFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        binding = FragmentEditProfileBinding.inflate(getLayoutInflater());
        presenter = new EditProfilePresenter(getActivity(), this);
        data();
        click();
        return binding.getRoot();
    }

    private void click() {
        binding.ivEditDriverAvatar.setOnClickListener(view ->
                request(AppContent.REQUEST_IMAGE_AVATAR_UPLOAD, AppContent.REQUEST_IMAGE_AVATAR_CAMERA));
        binding.ivEditIdPic.setOnClickListener(view ->
                request(AppContent.REQUEST_IMAGE_IDENTITY_UPLOAD, AppContent.REQUEST_IMAGE_IDENTITY_CAMERA));
        binding.ivEditYourVehicle.setOnClickListener(view ->
                request(AppContent.REQUEST_IMAGE_VEHICLE_UPLOAD, AppContent.REQUEST_IMAGE_VEHICLE_CAMERA));
        binding.ivEditInsuranceLicense.setOnClickListener(view ->
                request(AppContent.REQUEST_IMAGE_VEHICLE_INSURANCE_UPLOAD, AppContent.REQUEST_IMAGE_VEHICLE_INSURANCE_CAMERA));
        binding.ivEditVehicleLicense.setOnClickListener(view ->
                request(AppContent.REQUEST_IMAGE_VEHICLE_LICENSE_UPLOAD, AppContent.REQUEST_IMAGE_VEHICLE_LICENSE_CAMERA));
        binding.ivEditYourLicense.setOnClickListener(view ->
                request(AppContent.REQUEST_IMAGE_YOUR_LICENSE_UPLOAD, AppContent.REQUEST_IMAGE_YOUR_LICENSE_CAMERA));
        binding.btnOk.setOnClickListener(view -> presenter.validInput(binding));
    }

    //functions
    private void data() {
        User user = AppController.getInstance().getAppSettingsPreferences().getLogin().getUser();
        ToolUtils.loadImage(getContext(), binding.pbWaitAvater, user.getAvatar_url(), binding.ivDriverAvatar);
        ToolUtils.loadImage(getContext(), binding.pbWaitIdPic, user.getId_pic_url(), binding.ivIdPic);
        ToolUtils.loadImage(getContext(), binding.pbWaitYourLicense, user.getDrive_license_url(), binding.ivYourLicense);
        ToolUtils.loadImage(getContext(), binding.pbWaitYourVehicle, user.getVehicle_pic_url(), binding.ivYourVehicle);
        ToolUtils.loadImage(getContext(), binding.pbWaitVehicleLicense, user.getVehicle_license_url(), binding.ivVehicleLicense);
        ToolUtils.loadImage(getContext(), binding.pbWaitInsuranceLicense, user.getInsurance_license_url(), binding.ivInsuranceLicense);
        binding.etEnterEmail.setText(user.getEmail());
        binding.etEnterPhone.setText(user.getMobile());
        binding.etEnterAddress.setText(user.getAddress());

        if (PermissionUtil.isPermissionGranted(MediaStore.ACTION_IMAGE_CAPTURE, getContext()))
            PermissionUtil.requestPermission(getActivity(), Manifest.permission.CAMERA
                    , AppContent.REQUEST_PERMISSIONS_R_W_STORAGE_CAMERA);
    }

    private void request(int request_upload, int request_camera) {
        itemSelectImageDialogFragment.setListener(new ItemSelectImageDialogFragment.Listener() {
            @Override
            public void onGalleryClicked() {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, request_upload);
            }

            @Override
            public void onCameraClicked() {
                Intent photoPickerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoPickerIntent, request_camera);
            }
        });
        itemSelectImageDialogFragment.show(getFragmentManager(), "");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {
            if (requestCode == AppContent.REQUEST_IMAGE_AVATAR_UPLOAD) {
                try {
                    //code
                    Uri uri = data.getData();
                    binding.ivDriverAvatar.setImageURI(uri);
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == AppContent.REQUEST_IMAGE_AVATAR_CAMERA) {
                try {
                    //code
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    binding.ivDriverAvatar.setImageBitmap(bitmap);
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == AppContent.REQUEST_IMAGE_VEHICLE_UPLOAD) {
                try {
                    //code
                    Uri uri = data.getData();
                    binding.ivYourVehicle.setImageURI(uri);
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == AppContent.REQUEST_IMAGE_VEHICLE_CAMERA) {
                try {
                    //code
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    binding.ivYourVehicle.setImageBitmap(bitmap);
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
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == AppContent.REQUEST_IMAGE_VEHICLE_INSURANCE_UPLOAD) {
                try {
                    //code
                    Uri uri = data.getData();
                    binding.ivInsuranceLicense.setImageURI(uri);
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == AppContent.REQUEST_IMAGE_VEHICLE_INSURANCE_CAMERA) {
                try {
                    //code
                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                    binding.ivInsuranceLicense.setImageBitmap(imageBitmap);
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == AppContent.REQUEST_IMAGE_IDENTITY_UPLOAD) {
                try {
                    //code
                    Uri uri = data.getData();
                    binding.ivIdPic.setImageURI(uri);
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == AppContent.REQUEST_IMAGE_IDENTITY_CAMERA) {
                try {
                    //code
                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                    binding.ivIdPic.setImageBitmap(imageBitmap);
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
        WaitDialogFragment.newInstance().show(getFragmentManager(), "");
    }

    @Override
    public void hideDialog() {
        WaitDialogFragment.newInstance().dismiss();
    }
}

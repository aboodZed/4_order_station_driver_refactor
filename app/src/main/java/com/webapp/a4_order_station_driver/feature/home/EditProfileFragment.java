package com.webapp.a4_order_station_driver.feature.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.models.Login;
import com.webapp.a4_order_station_driver.models.User;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.dialogs.ItemSelectImageDialogFragment;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.view.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class EditProfileFragment extends Fragment {

    @BindView(R.id.iv_driver_avatar) CircleImageView ivDriverAvatar;
    @BindView(R.id.iv_edit_driver_avatar) ImageView ivEditDriverAvatar;
    @BindView(R.id.pb_wait_avater) ProgressBar pbWaitAvater;
    @BindView(R.id.et_enter_email) EditText etEnterEmail;
    @BindView(R.id.et_enter_phone) EditText etEnterPhone;
    @BindView(R.id.et_enter_address) EditText etEnterAddress;
    @BindView(R.id.iv_id_pic) CircleImageView ivIdPic;
    @BindView(R.id.iv_edit_id_pic) ImageView ivEditIdPic;
    @BindView(R.id.pb_wait_id_pic) ProgressBar pbWaitIdPic;
    @BindView(R.id.iv_your_vehicle) CircleImageView ivYourVehicle;
    @BindView(R.id.iv_edit_your_vehicle) ImageView ivEditYourVehicle;
    @BindView(R.id.pb_wait_your_vehicle) ProgressBar pbWaitYourVehicle;
    @BindView(R.id.iv_insurance_license) CircleImageView ivInsuranceLicense;
    @BindView(R.id.iv_edit_insurance_license) ImageView ivEditInsuranceLicense;
    @BindView(R.id.pb_wait_insurance_license) ProgressBar pbWaitInsuranceLicense;
    @BindView(R.id.iv_vehicle_license) CircleImageView ivVehicleLicense;
    @BindView(R.id.iv_edit_vehicle_license) ImageView ivEditVehicleLicense;
    @BindView(R.id.pb_wait_vehicle_license) ProgressBar pbWaitVehicleLicense;
    @BindView(R.id.iv_your_license) CircleImageView ivYourLicense;
    @BindView(R.id.iv_edit_your_license) ImageView ivEditYourLicense;
    @BindView(R.id.pb_wait_your_license) ProgressBar pbWaitYourLicense;
    @BindView(R.id.btn_ok) Button btnOk;

    private static NavigationView view;
    private ItemSelectImageDialogFragment itemSelectImageDialogFragment = ItemSelectImageDialogFragment.newInstance();

    public static EditProfileFragment newInstance(NavigationView navigationView) {
        EditProfileFragment fragment = new EditProfileFragment();
        view = navigationView;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.bind(this, view);
        setData();
        return view;
    }

    //Clicks

    @OnClick(R.id.iv_edit_driver_avatar)
    public void editAvatar() {
        request(AppContent.REQUEST_IMAGE_AVATAR_UPLOAD, AppContent.REQUEST_IMAGE_AVATAR_CAMERA);
    }

    @OnClick(R.id.iv_edit_id_pic)
    public void editID() {
        request(AppContent.REQUEST_IMAGE_IDENTITY_UPLOAD, AppContent.REQUEST_IMAGE_IDENTITY_CAMERA);
    }

    @OnClick(R.id.iv_edit_your_vehicle)
    public void editVehicle() {
        request(AppContent.REQUEST_IMAGE_VEHICLE_UPLOAD, AppContent.REQUEST_IMAGE_VEHICLE_CAMERA);
    }

    @OnClick(R.id.iv_edit_insurance_license)
    public void editInsuranceLicense() {
        request(AppContent.REQUEST_IMAGE_VEHICLE_INSURANCE_UPLOAD, AppContent.REQUEST_IMAGE_VEHICLE_INSURANCE_CAMERA);
    }

    @OnClick(R.id.iv_edit_vehicle_license)
    public void editVehicleLicense() {
        request(AppContent.REQUEST_IMAGE_VEHICLE_LICENSE_UPLOAD, AppContent.REQUEST_IMAGE_VEHICLE_LICENSE_CAMERA);
    }

    @OnClick(R.id.iv_edit_your_license)
    public void setIvEditYourLicense() {
        request(AppContent.REQUEST_IMAGE_YOUR_LICENSE_UPLOAD, AppContent.REQUEST_IMAGE_YOUR_LICENSE_CAMERA);
    }

    @OnClick(R.id.btn_ok)
    public void save() {
        validInput();
    }

    //functions

    private void setData() {
        User user = AppController.getInstance().getAppSettingsPreferences().getLogin().getUser();
        ToolUtils.loadImage(getContext(), pbWaitAvater, user.getAvatar_url(), ivDriverAvatar);
        ToolUtils.loadImage(getContext(), pbWaitIdPic, user.getId_pic_url(), ivIdPic);
        ToolUtils.loadImage(getContext(), pbWaitYourLicense, user.getDrive_license_url(), ivYourLicense);
        ToolUtils.loadImage(getContext(), pbWaitYourVehicle, user.getVehicle_pic_url(), ivYourVehicle);
        ToolUtils.loadImage(getContext(), pbWaitVehicleLicense, user.getVehicle_license_url(), ivVehicleLicense);
        ToolUtils.loadImage(getContext(), pbWaitInsuranceLicense, user.getInsurance_license_url(), ivInsuranceLicense);
        etEnterEmail.setText(user.getEmail());
        etEnterPhone.setText(user.getMobile());
        etEnterAddress.setText(user.getAddress());
        if (!checkPermissionFromDevice()) {
            requestPermission();
        }
    }

    private void validInput() {

        String email = etEnterEmail.getText().toString().trim();
        String mobile = etEnterPhone.getText().toString().trim();
        String address = etEnterAddress.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEnterEmail.setError(getString(R.string.empty_error));
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEnterEmail.setError(getString(R.string.un_match_pattern));
            return;
        }
        if (TextUtils.isEmpty(mobile)) {
            etEnterPhone.setError(getString(R.string.empty_error));
            return;
        }
        if (TextUtils.isEmpty(address)) {
            etEnterAddress.setError(getString(R.string.empty_error));
            return;
        }
        User user = new User(
                ToolUtils.bitmapToBase64(ToolUtils.getBitmapFromImageView(ivDriverAvatar)),
                ToolUtils.bitmapToBase64(ToolUtils.getBitmapFromImageView(ivInsuranceLicense)),
                ToolUtils.bitmapToBase64(ToolUtils.getBitmapFromImageView(ivYourLicense)),
                ToolUtils.bitmapToBase64(ToolUtils.getBitmapFromImageView(ivVehicleLicense)),
                ToolUtils.bitmapToBase64(ToolUtils.getBitmapFromImageView(ivIdPic)),
                ToolUtils.bitmapToBase64(ToolUtils.getBitmapFromImageView(ivYourVehicle)),
                AppController.getInstance().getAppSettingsPreferences().getPassword(),
                AppController.getInstance().getAppSettingsPreferences().getPassword(),
                mobile, email, address);
        upload(user);
    }

    private void upload(User user) {
        if (ToolUtils.checkTheInternet()) {
            btnOk.setClickable(false);
            WaitDialogFragment.newInstance().show(getFragmentManager(), "");
            AppController.getInstance().getApi().updateProfile(user).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        Login login = AppController.getInstance().getAppSettingsPreferences().getLogin();
                        login.setUser(response.body());
                        AppController.getInstance().getAppSettingsPreferences().setLogin(login);
                        WaitDialogFragment.newInstance().dismiss();
                        view.navigate(4);
                    } else {
                        ToolUtils.showError(getActivity(), response.errorBody());
                        WaitDialogFragment.newInstance().dismiss();
                        btnOk.setClickable(true);
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    t.printStackTrace();
                    WaitDialogFragment.newInstance().dismiss();
                    btnOk.setClickable(true);
                }
            });
        } else {
            ToolUtils.showLongToast(getString(R.string.no_connection), getActivity());
        }
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

    public boolean checkPermissionFromDevice() {
        int read_image_capture_result = ContextCompat.checkSelfPermission(getContext(), MediaStore.ACTION_IMAGE_CAPTURE);
        return read_image_capture_result == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.CAMERA
        }, AppContent.REQUEST_PERMISSIONS_R_W_STORAGE_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data) {
            if (requestCode == AppContent.REQUEST_IMAGE_AVATAR_UPLOAD) {
                try {
                    //code
                    Uri uri = data.getData();
                    ivDriverAvatar.setImageURI(uri);
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == AppContent.REQUEST_IMAGE_AVATAR_CAMERA) {
                try {
                    //code
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    ivDriverAvatar.setImageBitmap(bitmap);
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == AppContent.REQUEST_IMAGE_VEHICLE_UPLOAD) {
                try {
                    //code
                    Uri uri = data.getData();
                    ivYourVehicle.setImageURI(uri);
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == AppContent.REQUEST_IMAGE_VEHICLE_CAMERA) {
                try {
                    //code
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    ivYourVehicle.setImageBitmap(bitmap);
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == AppContent.REQUEST_IMAGE_VEHICLE_LICENSE_UPLOAD) {
                try {
                    //code
                    Uri uri = data.getData();
                    ivVehicleLicense.setImageURI(uri);
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == AppContent.REQUEST_IMAGE_VEHICLE_LICENSE_CAMERA) {
                try {
                    //code
                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                    ivVehicleLicense.setImageBitmap(imageBitmap);
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == AppContent.REQUEST_IMAGE_VEHICLE_INSURANCE_UPLOAD) {
                try {
                    //code
                    Uri uri = data.getData();
                    ivInsuranceLicense.setImageURI(uri);
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == AppContent.REQUEST_IMAGE_VEHICLE_INSURANCE_CAMERA) {
                try {
                    //code
                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                    ivInsuranceLicense.setImageBitmap(imageBitmap);
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == AppContent.REQUEST_IMAGE_IDENTITY_UPLOAD) {
                try {
                    //code
                    Uri uri = data.getData();
                    ivIdPic.setImageURI(uri);
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == AppContent.REQUEST_IMAGE_IDENTITY_CAMERA) {
                try {
                    //code
                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                    ivIdPic.setImageBitmap(imageBitmap);
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == AppContent.REQUEST_IMAGE_YOUR_LICENSE_UPLOAD) {
                try {
                    //code
                    Uri uri = data.getData();
                    ivYourLicense.setImageURI(uri);
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == AppContent.REQUEST_IMAGE_YOUR_LICENSE_CAMERA) {
                try {
                    //code
                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                    ivYourLicense.setImageBitmap(imageBitmap);
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}

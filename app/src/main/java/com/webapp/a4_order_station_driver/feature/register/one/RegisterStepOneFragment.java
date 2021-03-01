package com.webapp.a4_order_station_driver.feature.register.one;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.webapp.a4_order_station_driver.databinding.FragmentRegisterStep1Binding;
import com.webapp.a4_order_station_driver.models.Login;
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

public class RegisterStepOneFragment extends Fragment implements RequestListener<Bitmap>, DialogView<Login> {

    public static final int page = 301;

    private FragmentRegisterStep1Binding binding;

    private boolean saveImage;
    private ItemSelectImageDialogFragment itemSelectImageDialogFragment;
    private RegisterStepOnePresenter presenter;
    private Bitmap bitmap;
    private PhotoTakerManager photoTakerManager;

    public static RegisterStepOneFragment newInstance(BaseActivity baseActivity) {
        return new RegisterStepOneFragment(baseActivity);
    }

    public RegisterStepOneFragment(BaseActivity baseActivity) {
        photoTakerManager = new PhotoTakerManager(this);
        presenter = new RegisterStepOnePresenter(baseActivity, this, photoTakerManager);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterStep1Binding.inflate(getLayoutInflater());

        setInformation();
        click();

        return binding.getRoot();
    }

    private void setInformation() {
        binding.tvCode.setText(AppController.getInstance().getAppSettingsPreferences().getCountry().getPhone_code());
        if (AppController.getInstance().getAppSettingsPreferences().getAppLanguage().equals("en")) {
            binding.etCountry.setText(AppController.getInstance().getAppSettingsPreferences().getCountry().getName_en());
        } else {
            binding.etCountry.setText(AppController.getInstance().getAppSettingsPreferences().getCountry().getName_ar());
        }
    }

    private void click() {
        binding.ivEnterImage.setOnClickListener(view -> enterImage());
    }

    public void signUp() {
        presenter.validInput(binding.etEnterName, binding.etEnterEmail, binding.etEnterAddress
                , binding.etEnterPhone, binding.etEnterPassword, binding.etEnterConfirmPassword
                , binding.cbAgreeTerms, bitmap, saveImage);
    }

    public void enterImage() {
        itemSelectImageDialogFragment = ItemSelectImageDialogFragment.newInstance();
        itemSelectImageDialogFragment.setListener(new ItemSelectImageDialogFragment.Listener() {
            @Override
            public void onGalleryClicked() {
                photoTakerManager.galleryRequest(requireActivity(), AppContent.REQUEST_STUDIO);
            }

            @Override
            public void onCameraClicked() {
                photoTakerManager.cameraRequest(requireActivity(), AppContent.REQUEST_CAMERA);
            }
        });
        itemSelectImageDialogFragment.show(getChildFragmentManager(), "");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        presenter.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

            /*if (requestCode == AppContent.REQUEST_STUDIO) {

                Uri uri = data.getData();
                binding.ivEnterImage.setImageURI(uri);
                saveImage = true;
            } else if (requestCode == AppContent.REQUEST_CAMERA) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                binding.ivEnterImage.setImageBitmap(imageBitmap);
                saveImage = true;
            }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        presenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void setData(Login login) {

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
        binding.ivEnterImage.setImageBitmap(bitmap);
        this.bitmap = bitmap;
        saveImage = true;
    }

    @Override
    public void onError(String msg) {
    }

    @Override
    public void onFail(String msg) {
        ToolUtil.showLongToast(msg, requireActivity());
    }
}

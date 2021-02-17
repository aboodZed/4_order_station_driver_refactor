package com.webapp.a4_order_station_driver.feature.register.one;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.FragmentRegisterStep1Binding;
import com.webapp.a4_order_station_driver.feature.register.RegisterActivity;
import com.webapp.a4_order_station_driver.feature.register.two.RegisterStep_2;
import com.webapp.a4_order_station_driver.models.Login;
import com.webapp.a4_order_station_driver.models.User;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.PhotoTakerManager;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.dialogs.ItemSelectImageDialogFragment;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.webapp.a4_order_station_driver.utils.AppContent.REQUEST_STUDIO;

public class RegisterStep_1 extends Fragment implements PhotoTakerManager.PhotoListener, DialogView<Login> {

    public static final int page = 301;

    private FragmentRegisterStep1Binding binding;

    private boolean saveImage;
    private PhotoTakerManager photoTakerManager;
    private ItemSelectImageDialogFragment itemSelectImageDialogFragment = ItemSelectImageDialogFragment.newInstance();
    private RegisterStep_1Presenter presenter;

    public static RegisterStep_1 newInstance(BaseActivity baseActivity) {
        RegisterStep_1 fragment = new RegisterStep_1(baseActivity);
        return fragment;
    }

    public RegisterStep_1(BaseActivity baseActivity) {
        presenter = new RegisterStep_1Presenter(baseActivity, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterStep1Binding.inflate(getLayoutInflater());
        photoTakerManager = new PhotoTakerManager(this);

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
                , binding.cbAgreeTerms, binding.ivEnterImage, saveImage);
    }

    public void enterImage() {
        itemSelectImageDialogFragment.setListener(new ItemSelectImageDialogFragment.Listener() {
            @Override
            public void onGalleryClicked() {
                Intent galleryPictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (galleryPictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivityForResult(galleryPictureIntent, REQUEST_STUDIO);
                }
            }

            @Override
            public void onCameraClicked() {
                Intent photoPickerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoPickerIntent, AppContent.REQUEST_CAMERA);
            }
        });
        itemSelectImageDialogFragment.show(getChildFragmentManager(), "");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_STUDIO) {
                Uri uri = data.getData();
                binding.ivEnterImage.setImageURI(uri);
                saveImage = true;
            } else if (requestCode == AppContent.REQUEST_CAMERA) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                binding.ivEnterImage.setImageBitmap(imageBitmap);
                saveImage = true;
            }
        }
    }

    @Override
    public void onTakePhotoFailure() {

    }

    @Override
    public void onTakePhotoSuccess(Bitmap bitmap) {
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
}

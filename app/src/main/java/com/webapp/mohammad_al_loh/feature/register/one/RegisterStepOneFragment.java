package com.webapp.mohammad_al_loh.feature.register.one;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.webapp.mohammad_al_loh.databinding.FragmentRegisterStep1Binding;
import com.webapp.mohammad_al_loh.feature.register.adapter.SpinnerAdapter;
import com.webapp.mohammad_al_loh.models.Neighborhood;
import com.webapp.mohammad_al_loh.models.NeighborhoodList;
import com.webapp.mohammad_al_loh.utils.AppContent;
import com.webapp.mohammad_al_loh.utils.AppController;
import com.webapp.mohammad_al_loh.utils.Photo.PhotoTakerManager;
import com.webapp.mohammad_al_loh.utils.ToolUtil;
import com.webapp.mohammad_al_loh.utils.dialogs.ItemSelectImageDialogFragment;
import com.webapp.mohammad_al_loh.utils.dialogs.WaitDialogFragment;
import com.webapp.mohammad_al_loh.utils.language.AppLanguageUtil;
import com.webapp.mohammad_al_loh.utils.language.BaseActivity;
import com.webapp.mohammad_al_loh.utils.listeners.DialogView;
import com.webapp.mohammad_al_loh.utils.listeners.RequestListener;

public class RegisterStepOneFragment extends Fragment implements RequestListener<Bitmap>
        , DialogView<NeighborhoodList> {

    public static final int page = 301;

    private FragmentRegisterStep1Binding binding;

    private boolean saveImage;
    private ItemSelectImageDialogFragment itemSelectImageDialogFragment;
    private RegisterStepOnePresenter presenter;
    private Bitmap bitmap;
    private PhotoTakerManager photoTakerManager;
    private SpinnerAdapter spinnerAdapter;

    private ActivityResultLauncher<Intent> launcher;

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

        data();
        click();
        onActivityResulting();

        return binding.getRoot();
    }

    private void onActivityResulting() {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> presenter.onActivityResult(result.getResultCode(), result.getData()));
    }

    private void data() {
        binding.tvCode.setText(AppController.getInstance().getAppSettingsPreferences().getCountry().getPhone_code());
        if (AppController.getInstance().getAppSettingsPreferences().getAppLanguage().equals(AppLanguageUtil.English)) {
            binding.etCountry.setText(AppController.getInstance().getAppSettingsPreferences().getCountry().getName_en());
        } else {
            binding.etCountry.setText(AppController.getInstance().getAppSettingsPreferences().getCountry().getName_ar());
        }
        presenter.getNeighborhood();
    }

    private void click() {
        binding.ivEnterImage.setOnClickListener(view -> enterImage());
    }

    public void signUp() {
        presenter.validInput(binding.etEnterName, binding.etEnterEmail, binding.etEnterAddress
                , binding.etEnterPhone, binding.etEnterPassword, binding.etEnterConfirmPassword
                , binding.cbAgreeTerms, bitmap, saveImage
                ,((Neighborhood) binding.spNeighborhood.getSelectedItem()).getId());
    }

    public void enterImage() {
        itemSelectImageDialogFragment = ItemSelectImageDialogFragment.newInstance();
        itemSelectImageDialogFragment.setListener(new ItemSelectImageDialogFragment.Listener() {
            @Override
            public void onGalleryClicked() {
                presenter.setRequestCode(AppContent.REQUEST_STUDIO);
                photoTakerManager.galleryRequestLauncher(getActivity(), launcher);
            }

            @Override
            public void onCameraClicked() {
                presenter.setRequestCode(AppContent.REQUEST_CAMERA);
                photoTakerManager.cameraRequestLauncher(getActivity(), launcher);
            }
        });
        itemSelectImageDialogFragment.show(getChildFragmentManager(), "");
    }

    @Override
    public void setData(NeighborhoodList neighborhoodList) {
        spinnerAdapter = new SpinnerAdapter(requireContext(), neighborhoodList.getNeighborhoods());
        binding.spNeighborhood.setAdapter(spinnerAdapter);
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

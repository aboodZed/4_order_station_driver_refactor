package com.webapp.mohammad_al_loh.feature.main.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.webapp.mohammad_al_loh.databinding.FragmentProfileBinding;
import com.webapp.mohammad_al_loh.feature.main.editProfile.EditProfileFragment;
import com.webapp.mohammad_al_loh.models.Message;
import com.webapp.mohammad_al_loh.models.User;
import com.webapp.mohammad_al_loh.utils.APIImageUtil;
import com.webapp.mohammad_al_loh.utils.AppController;
import com.webapp.mohammad_al_loh.utils.dialogs.ContactFragment;
import com.webapp.mohammad_al_loh.utils.dialogs.LanguageDialog;
import com.webapp.mohammad_al_loh.utils.dialogs.PrivacyPolicyFragment;
import com.webapp.mohammad_al_loh.utils.dialogs.WaitDialogFragment;
import com.webapp.mohammad_al_loh.utils.language.BaseActivity;
import com.webapp.mohammad_al_loh.utils.listeners.DialogView;

public class ProfileFragment extends Fragment implements DialogView<Message> {

    public static final int page = 204;

    private FragmentProfileBinding binding;

    private BaseActivity baseActivity;
    private ProfilePresenter presenter;

    public ProfileFragment(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
        presenter = new ProfilePresenter(baseActivity, this);
    }

    public static ProfileFragment newInstance(BaseActivity baseActivity) {
        return new ProfileFragment(baseActivity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater());
        data();
        click();
        return binding.getRoot();
    }

    private void click() {
        binding.tvPrivacy.setOnClickListener(view -> privacy());
        binding.ivEditProfile.setOnClickListener(view -> editProfile());
        binding.tvContact.setOnClickListener(view -> contact());
        binding.tvLanguage.setOnClickListener(view -> changeLanguage());
        binding.btnLogout.setOnClickListener(view -> presenter.logout());
    }

    public void privacy() {
        PrivacyPolicyFragment privacyPolicyFragment = PrivacyPolicyFragment.newInstance();
        privacyPolicyFragment.show(getChildFragmentManager(), "");
    }

    public void editProfile() {
        baseActivity.navigate(EditProfileFragment.page);
    }

    public void contact() {
        ContactFragment contactFragment = ContactFragment.newInstance();
        contactFragment.show(getChildFragmentManager(), "");
    }

    public void changeLanguage() {
        LanguageDialog languageDialog = new LanguageDialog(baseActivity);
        languageDialog.show(getChildFragmentManager(), "");
    }


    //functions
    private void data() {
        User user = AppController.getInstance().getAppSettingsPreferences().getLogin().getUser();
        Log.e("usercountryid", user.getCountry_id() + "");
        Log.e("id", user.getId() + "");
        APIImageUtil.loadImage(getContext(), binding.pbWait, user.getAvatar_url(), binding.ivDriverAvatar);
        binding.tvDriverName.setText(user.getName());
        binding.rbUser.setRating(user.getRate());
    }

    @Override
    public void setData(Message message) {

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

package com.webapp.a4_order_station_driver.feature.main.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.webapp.a4_order_station_driver.databinding.FragmentProfileBinding;
import com.webapp.a4_order_station_driver.feature.login.LoginActivity;
import com.webapp.a4_order_station_driver.feature.main.MainActivity;
import com.webapp.a4_order_station_driver.feature.main.editProfile.EditProfileFragment;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.models.MyLocation;
import com.webapp.a4_order_station_driver.models.User;
import com.webapp.a4_order_station_driver.utils.APIImageUtil;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.NavigateUtil;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.dialogs.ContactFragment;
import com.webapp.a4_order_station_driver.utils.dialogs.LanguageDialog;
import com.webapp.a4_order_station_driver.utils.dialogs.PrivacyPolicyFragment;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;
import com.webapp.a4_order_station_driver.utils.location.tracking.GPSTracking;

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

package com.webapp.a4_order_station_driver.feature.main.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.webapp.a4_order_station_driver.databinding.FragmentProfileBinding;
import com.webapp.a4_order_station_driver.feature.login.LoginActivity;
import com.webapp.a4_order_station_driver.feature.main.MainActivity;
import com.webapp.a4_order_station_driver.feature.main.editProfile.EditProfileFragment;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.models.User;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.NavigateUtil;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.dialogs.ContactFragment;
import com.webapp.a4_order_station_driver.utils.dialogs.LanguageDialog;
import com.webapp.a4_order_station_driver.utils.dialogs.PrivacyPolicyFragment;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;
import com.webapp.a4_order_station_driver.utils.location.tracking.GPSTracking;

public class ProfileFragment extends Fragment {

    public static final int page = 204;

    private FragmentProfileBinding binding;

    private BaseActivity baseActivity;

    public ProfileFragment(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    public static ProfileFragment newInstance(BaseActivity baseActivity) {
        ProfileFragment fragment = new ProfileFragment(baseActivity);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View v = inflater.inflate(R.layout.fragment_profile, container, false);
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
        binding.btnLogout.setOnClickListener(view -> logout());
    }

    public void privacy() {
        PrivacyPolicyFragment privacyPolicyFragment = PrivacyPolicyFragment.newInstance();
        privacyPolicyFragment.show(getFragmentManager(), "");
    }

    public void editProfile() {
        baseActivity.navigate(EditProfileFragment.page);
    }

    public void contact() {
        ContactFragment contactFragment = ContactFragment.newInstance();
        contactFragment.show(getFragmentManager(), "");
    }

    public void changeLanguage() {
        LanguageDialog languageDialog = new LanguageDialog();
        languageDialog.show(getFragmentManager(), "");
    }

    public void logout() {
        WaitDialogFragment.newInstance().show(getFragmentManager(), "");
        new APIUtil<Message>(getActivity()).getData(AppController.getInstance()
                .getApi().isOnline(MainActivity.offline), new RequestListener<Message>() {
            @Override
            public void onSuccess(Message message, String msg) {
                AppController.getInstance().getAppSettingsPreferences().setIsLogin(false);
                GPSTracking.getInstance(getContext()).removeMyUpdates();
                new NavigateUtil().activityIntent(getActivity(), LoginActivity.class, false);
                WaitDialogFragment.newInstance().dismiss();
            }

            @Override
            public void onError(String msg) {
                ToolUtil.showLongToast(msg, getActivity());
                WaitDialogFragment.newInstance().dismiss();
            }

            @Override
            public void onFail(String msg) {
                ToolUtil.showLongToast(msg, getActivity());
                WaitDialogFragment.newInstance().dismiss();
            }
        });
    }

    //functions
    private void data() {
        User user = AppController.getInstance().getAppSettingsPreferences().getLogin().getUser();
        Log.e("usercountryid", user.getCountry_id() + "");
        ToolUtil.loadImage(getContext(), binding.pbWait, user.getAvatar_url(), binding.ivDriverAvatar);
        binding.tvDriverName.setText(user.getName());
        binding.rbUser.setRating(user.getRate());
    }
}

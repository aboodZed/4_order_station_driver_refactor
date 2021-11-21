package com.webapp.a4_order_station_driver.feature.main.profile;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.webapp.a4_order_station_driver.databinding.FragmentProfileBinding;
import com.webapp.a4_order_station_driver.feature.data.DataActivity;
import com.webapp.a4_order_station_driver.feature.data.rate.RatingFragment;
import com.webapp.a4_order_station_driver.feature.main.editProfile.EditProfileFragment;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.models.User;
import com.webapp.a4_order_station_driver.utils.APIImageUtil;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.feature.data.contact.ContactFragment;
import com.webapp.a4_order_station_driver.utils.NavigateUtil;
import com.webapp.a4_order_station_driver.utils.dialogs.LanguageDialog;
import com.webapp.a4_order_station_driver.feature.data.privacy.PrivacyPolicyFragment;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;

public class ProfileFragment extends Fragment implements DialogView<Message> {

    public static final int page = 204;

    private FragmentProfileBinding binding;

    private Activity baseActivity;
    private ProfilePresenter presenter;

    public ProfileFragment(Activity baseActivity) {
        this.baseActivity = baseActivity;
        presenter = new ProfilePresenter(baseActivity, this);
    }

    public static ProfileFragment newInstance(Activity baseActivity) {
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
        /*binding.tvPrivacy.setOnClickListener(view -> privacy());
        binding.ivEditProfile.setOnClickListener(view -> editProfile());
        binding.tvContact.setOnClickListener(view -> contact());
        binding.tvLanguage.setOnClickListener(view -> changeLanguage());
        binding.btnLogout.setOnClickListener(view -> presenter.logout());*/
    }

    public void privacy() {
        new NavigateUtil().activityIntentWithPage(requireActivity(), DataActivity.class,true, PrivacyPolicyFragment.page);
    }

    public void editProfile() {
        //baseActivity.navigate(EditProfileFragment.page);
    }

    public void contact() {
        new NavigateUtil().activityIntentWithPage(requireActivity(), DataActivity.class,true, ContactFragment.page);

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
        /*APIImageUtil.loadImage(getContext(), binding.pbWait, user.getAvatar_url(), binding.ivDriverAvatar);
        binding.tvDriverName.setText(user.getName());
        binding.rbUser.setRating(user.getRate());*/
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

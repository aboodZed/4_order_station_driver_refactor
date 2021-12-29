package com.webapp.a4_order_station_driver.feature.main.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.webapp.a4_order_station_driver.databinding.FragmentProfileBinding;
import com.webapp.a4_order_station_driver.feature.data.DataActivity;
import com.webapp.a4_order_station_driver.feature.data.changePassword.ChangePasswordFragment;
import com.webapp.a4_order_station_driver.feature.data.contact.ContactFragment;
import com.webapp.a4_order_station_driver.feature.data.editProfile.EditProfileFragment;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.models.User;
import com.webapp.a4_order_station_driver.utils.APIImageUtil;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.NavigateUtil;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;

public class ProfileFragment extends Fragment implements DialogView<Message> {

    public static final int page = 204;

    private FragmentProfileBinding binding;

    private static BaseActivity activity;
    private ProfilePresenter presenter;

    public static ProfileFragment newInstance(BaseActivity baseActivity) {
        activity = baseActivity;
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater());
        presenter = new ProfilePresenter(activity, this);
        data();
        click();
        return binding.getRoot();
    }

    private void click() {
        binding.tvChangePassword.setOnClickListener(view -> new NavigateUtil().activityIntentWithPage(
                activity, DataActivity.class, true, ChangePasswordFragment.page));

        binding.tvEdit.setOnClickListener(view -> new NavigateUtil().activityIntentWithPage(
                activity, DataActivity.class, true, EditProfileFragment.page));
    }

    public void contact() {
        new NavigateUtil().activityIntentWithPage(requireActivity(), DataActivity.class, true, ContactFragment.page);
    }


    //functions
    private void data() {
        User user = AppController.getInstance().getAppSettingsPreferences().getUser();
        Log.e("usercountryid", user.getCountry().getId() + "");
        Log.e("id", user.getId() + "");
        APIImageUtil.loadImage(getContext(), binding.pbWait, user.getAvatar_url(), binding.ivDriverAvatar);
        binding.tvDriverName.setText(user.getName());
        binding.tvUserName.setText(user.getName());
        binding.tvPhone.setText(user.getMobile());
        binding.tvEmail.setText(user.getEmail());
        binding.tvAddress.setText(user.getAddress());
        binding.tvArea.setText(user.getCity().getName());
        //binding.rbUser.setRating(user.getRate());
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

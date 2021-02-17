package com.webapp.a4_order_station_driver.feature.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.feature.login.LoginActivity;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.models.Settings;
import com.webapp.a4_order_station_driver.models.User;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.NavigateUtils;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.dialogs.ContactFragment;
import com.webapp.a4_order_station_driver.utils.dialogs.LanguageDialog;
import com.webapp.a4_order_station_driver.utils.dialogs.PrivacyPolicyFragment;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.location.GPSTracking;
import com.webapp.a4_order_station_driver.utils.view.NavigationView;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    @BindView(R.id.iv_driver_avatar) CircleImageView ivDriverAvatar;
    @BindView(R.id.pb_wait) ProgressBar pbWait;
    @BindView(R.id.tv_driver_name) TextView tvDriverName;
    @BindView(R.id.rb_user) RatingBar rbUser;
    @BindView(R.id.iv_edit_profile) ImageView ivEditProfile;
    @BindView(R.id.tv_privacy) TextView tvPrivacy;
    @BindView(R.id.tv_contact) TextView tvContact;
    @BindView(R.id.tv_language) TextView tvLanguage;
    @BindView(R.id.btn_logout) Button btnLogout;

    private static NavigationView view;
    private Settings settings;

    public static ProfileFragment newInstance(NavigationView navigationView) {
        ProfileFragment fragment = new ProfileFragment();
        view = navigationView;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, v);
        setData(AppController.getInstance().getAppSettingsPreferences().getLogin().getUser());
        return v;
    }

    //clicks
    @OnClick(R.id.tv_privacy)
    public void privacy() {
        PrivacyPolicyFragment privacyPolicyFragment = PrivacyPolicyFragment.newInstance();
        privacyPolicyFragment.show(getFragmentManager(), "");
    }

    @OnClick(R.id.iv_edit_profile)
    public void editProfile() {
        view.navigate(9);
    }

    @OnClick(R.id.tv_contact)
    public void contact() {
        ContactFragment contactFragment = ContactFragment.newInstance();
        contactFragment.show(getFragmentManager(), "");
    }

    @OnClick(R.id.tv_language)
    public void changeLanguage() {
        LanguageDialog languageDialog = new LanguageDialog();
        languageDialog.show(getFragmentManager(), "");
    }

    @OnClick(R.id.btn_logout)
    public void logout() {
        if (ToolUtils.checkTheInternet()) {
            WaitDialogFragment.newInstance().show(getFragmentManager(), "");
            AppController.getInstance().getApi().isOnline("0").enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                    if (response.isSuccessful()) {
                        AppController.getInstance().getAppSettingsPreferences().setIsLogin(false);
                        GPSTracking.getInstance(getContext()).removeMyUpdates();
                        new NavigateUtils().activityIntent(getActivity(), LoginActivity.class, false);
                    } else {
                        ToolUtils.showError(getActivity(), response.errorBody());
                    }
                    WaitDialogFragment.newInstance().dismiss();
                }

                @Override
                public void onFailure(Call<Message> call, Throwable t) {
                    ToolUtils.showLongToast(getString(R.string.error), getActivity());
                    WaitDialogFragment.newInstance().dismiss();
                }
            });
        }
    }

    //functions
    private void setData(@NotNull User user) {
        Log.e("usercountryid", user.getCountry_id() + "");
        ToolUtils.loadImage(getContext(), pbWait, user.getAvatar_url(), ivDriverAvatar);
        tvDriverName.setText(user.getName());
        rbUser.setRating(user.getRate());
    }
}

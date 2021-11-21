package com.webapp.a4_order_station_driver.feature.data;

import android.os.Bundle;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.ActivityDataBinding;
import com.webapp.a4_order_station_driver.feature.data.changePassword.ChangePasswordFragment;
import com.webapp.a4_order_station_driver.feature.data.contact.ContactFragment;
import com.webapp.a4_order_station_driver.feature.data.natification.NotificationFragment;
import com.webapp.a4_order_station_driver.feature.data.privacy.PrivacyPolicyFragment;
import com.webapp.a4_order_station_driver.feature.data.rate.RatingFragment;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.NavigateUtil;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;

public class DataActivity extends BaseActivity {

    public static final int page = 600;

    private ActivityDataBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityDataBinding.inflate(getLayoutInflater());
        super.setRootView(binding.getRoot());
        super.onCreate(savedInstanceState);
        navigate(getIntent().getExtras().getInt(AppContent.PAGE,0));
    }

    @Override
    public void navigate(int page) {
        switch (page){
            case RatingFragment.page:
                RatingFragment ratingFragment = RatingFragment.newInstance();
                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , ratingFragment, R.id.fragment_container);
                binding.tvPageTitle.setText(getString(R.string.rating));
                break;
            case ContactFragment.page:
                ContactFragment contactFragment = ContactFragment.newInstance();
                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , contactFragment, R.id.fragment_container);
                binding.tvPageTitle.setText(getString(R.string.contact_us));
                break;
            case PrivacyPolicyFragment.page:
                PrivacyPolicyFragment privacyPolicyFragment = PrivacyPolicyFragment.newInstance();
                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , privacyPolicyFragment, R.id.fragment_container);
                binding.tvPageTitle.setText(getString(R.string.privacy_policy));
                break;
            case NotificationFragment.page:
                NotificationFragment notificationFragment = NotificationFragment.newInstance(this);
                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , notificationFragment, R.id.fragment_container);
                binding.tvPageTitle.setText(getString(R.string.notification));
                break;
            case ChangePasswordFragment.page:
                ChangePasswordFragment changePasswordFragment = ChangePasswordFragment.newInstance();
                new NavigateUtil().replaceFragment(getSupportFragmentManager()
                        , changePasswordFragment, R.id.fragment_container);
                binding.tvPageTitle.setText(getString(R.string.notification));
                break;
        }
    }
}
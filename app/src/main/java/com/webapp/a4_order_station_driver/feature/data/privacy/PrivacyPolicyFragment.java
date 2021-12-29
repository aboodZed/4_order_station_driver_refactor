package com.webapp.a4_order_station_driver.feature.data.privacy;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.webapp.a4_order_station_driver.databinding.FragmentPrivacyPolicyBinding;
import com.webapp.a4_order_station_driver.models.AppSettings;
import com.webapp.a4_order_station_driver.utils.AppController;

public class PrivacyPolicyFragment extends Fragment {

    public static final int page = 603;

    private FragmentPrivacyPolicyBinding binding;

    public static PrivacyPolicyFragment newInstance() {
        PrivacyPolicyFragment fragment = new PrivacyPolicyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPrivacyPolicyBinding.inflate(getLayoutInflater());
        data();
        //click();
        return binding.getRoot();
    }

    private void click() {
        //binding.ivBack.setOnClickListener(view -> dismiss());
    }

    private void data() {
        AppSettings settings = AppController.getInstance().getAppSettingsPreferences().getSettings();
        binding.tvTitle.setText(settings.getPrivacy_title());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvPrivacy.setText(Html.fromHtml(settings.getPrivacy_content(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            binding.tvPrivacy.setText(Html.fromHtml(settings.getPrivacy_content()));
        }
       /* new APIUtil<Privacy>(getActivity()).getData(AppController.getInstance()
                .getApi().getPrivacy(), new RequestListener<Privacy>() {
            @Override
            public void onSuccess(Privacy privacy, String msg) {
                binding.pbWait.setVisibility(View.GONE);

                binding.tvTitle.setText(privacy.getPrivacy_title());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.tvPrivacy.setText(Html.fromHtml(privacy.getPrivacy_content(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    binding.tvPrivacy.setText(Html.fromHtml(privacy.getPrivacy_content()));
                }
            }

            @Override
            public void onError(String msg) {
                ToolUtil.showLongToast(msg, getActivity());
                binding.pbWait.setVisibility(View.GONE);
            }

            @Override
            public void onFail(String msg) {
                ToolUtil.showLongToast(msg, getActivity());
                binding.pbWait.setVisibility(View.GONE);
            }
        });*/
    }
/*
    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER);
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        setCancelable(false);
        super.onResume();
        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                PrivacyPolicyFragment.this.dismiss();
                return true;
            } else return false;
        });
    }
    */
}

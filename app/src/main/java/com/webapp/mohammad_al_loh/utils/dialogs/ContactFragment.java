package com.webapp.mohammad_al_loh.utils.dialogs;

import static com.webapp.mohammad_al_loh.utils.AppContent.PHONE_CALL_CODE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.webapp.mohammad_al_loh.R;
import com.webapp.mohammad_al_loh.databinding.FragmentContactBinding;
import com.webapp.mohammad_al_loh.models.ResultSettings;
import com.webapp.mohammad_al_loh.models.SettingsData;
import com.webapp.mohammad_al_loh.utils.APIUtil;
import com.webapp.mohammad_al_loh.utils.AppController;
import com.webapp.mohammad_al_loh.utils.NavigateUtil;
import com.webapp.mohammad_al_loh.utils.PermissionUtil;
import com.webapp.mohammad_al_loh.utils.ToolUtil;
import com.webapp.mohammad_al_loh.utils.listeners.RequestListener;

public class ContactFragment extends DialogFragment {

    private FragmentContactBinding binding;
    private SettingsData settings;

    public static ContactFragment newInstance() {
        ContactFragment fragment = new ContactFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentContactBinding.inflate(getLayoutInflater());
        data();
        click();
        return binding.getRoot();
    }

    private void data() {
        String url = "app-content?country_id=" + AppController.getInstance()
                .getAppSettingsPreferences().getCountry().getId();

        WaitDialogFragment.newInstance().show(getChildFragmentManager(), "");
        new APIUtil<ResultSettings>(getActivity()).getData(AppController.getInstance().getApi()
                .getSettings(url), new RequestListener<ResultSettings>() {
            @Override
            public void onSuccess(ResultSettings result, String msg) {
                ContactFragment.this.settings = result.getSettingsData();
                setDate();
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

    private void click() {
        binding.ivBack.setOnClickListener(view -> dismiss());
        binding.btnSend.setOnClickListener(view -> openSendMessage());
        binding.llEmail.setOnClickListener(view -> openEmail());
        binding.llCall.setOnClickListener(view -> phone());
        binding.ivFacebook.setOnClickListener(view -> facebook());
        binding.ivInstagram.setOnClickListener(view -> instagram());
        binding.ivTwitter.setOnClickListener(view -> twitter());
        binding.ivLinkin.setOnClickListener(view -> linkedin());
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        setCancelable(false);
        super.onResume();
        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                ContactFragment.this.dismiss();
                return true;
            } else return false;
        });
    }

    //clicks
    public void back() {
        this.dismiss();
    }

    public void openSendMessage() {
        SendMessageDialog dialog = SendMessageDialog.newInstance();
        dialog.show(getChildFragmentManager(), "");
    }

    public void openEmail() {
        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", settings.getEmail(), null));
        startActivity(Intent.createChooser(i, getString(R.string.send_email)));
    }

    void phone() {
        if (PermissionUtil.isPermissionGranted(Manifest.permission.CALL_PHONE, getActivity())) {
            new NavigateUtil().makeCall(getActivity(), settings.getMobile());
        } else {
            PermissionUtil.requestPermission(getActivity(), Manifest.permission.CALL_PHONE, PHONE_CALL_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != PHONE_CALL_CODE) {
            return;
        }

        if ((grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            new NavigateUtil().makeCall(getActivity(), settings.getMobile());
        }
    }

    void facebook() {
        new NavigateUtil().openLink(getActivity(), settings.getFacebook_link());
    }

    void twitter() {
        new NavigateUtil().openLink(getActivity(), settings.getTwitter_link());
    }

    void instagram() {
        new NavigateUtil().openLink(getActivity(), settings.getInstagram_link());
    }

    void linkedin() {
        new NavigateUtil().openLink(getActivity(), settings.getLinkedin_link());
    }

    private void setDate() {
        binding.tvEmail.setText(settings.getEmail());
        binding.tvPhone.setText(settings.getMobile());
    }
}

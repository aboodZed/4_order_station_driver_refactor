package com.webapp.a4_order_station_driver.utils.dialogs;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.DialogLanguageBinding;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.language.AppLanguageUtil;

public class LanguageDialog extends DialogFragment {

    private DialogLanguageBinding binding;

    public static LanguageDialog newInstance() {
        LanguageDialog fragment = new LanguageDialog();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.dialog_language, container, false);
        binding = DialogLanguageBinding.inflate(getLayoutInflater());
        data();
        click();
        return binding.getRoot();
    }

    private void click() {
        binding.tvArabic.setOnClickListener(view -> arabic());
        binding.tvEnglish.setOnClickListener(view -> english());
        binding.btnCancel.setOnClickListener(view -> dismiss());
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.8);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.transparent);
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        setCancelable(false);
        super.onResume();
        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                LanguageDialog.this.dismiss();
                return true;
            } else return false;
        });
    }

    //clicks
    public void arabic() {
        AppLanguageUtil.getInstance().setAppLanguage(getContext(), AppLanguageUtil.ARABIC);
        dismiss();
        getActivity().recreate();
    }

    public void english() {
        AppLanguageUtil.getInstance().setAppLanguage(getContext(), AppLanguageUtil.English);
        dismiss();
        getActivity().recreate();
    }

    private void data() {
        binding.tvArabic.setBackgroundResource(R.drawable.dark_gray_button);
        binding.tvEnglish.setBackgroundResource(R.drawable.dark_gray_button);
        if (AppController.getInstance().getAppSettingsPreferences()
                .getAppLanguage().equals(AppLanguageUtil.English)) {
            binding.tvArabic.setBackgroundResource(R.drawable.blue_button);
        } else {
            binding.tvEnglish.setBackgroundResource(R.drawable.blue_button);
        }
    }
}

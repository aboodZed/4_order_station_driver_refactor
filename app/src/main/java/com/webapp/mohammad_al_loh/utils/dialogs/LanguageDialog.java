package com.webapp.mohammad_al_loh.utils.dialogs;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.webapp.mohammad_al_loh.R;
import com.webapp.mohammad_al_loh.databinding.DialogLanguageBinding;
import com.webapp.mohammad_al_loh.feature.main.MainActivity;
import com.webapp.mohammad_al_loh.feature.main.profile.ProfileFragment;
import com.webapp.mohammad_al_loh.utils.AppController;
import com.webapp.mohammad_al_loh.utils.NavigateUtil;
import com.webapp.mohammad_al_loh.utils.language.AppLanguageUtil;
import com.webapp.mohammad_al_loh.utils.language.BaseActivity;

public class LanguageDialog extends DialogFragment {

    private DialogLanguageBinding binding;
    private BaseActivity baseActivity;

    public LanguageDialog(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        new NavigateUtil().activityIntentWithPage(baseActivity, MainActivity.class, false, ProfileFragment.page);
    }

    public void english() {
        AppLanguageUtil.getInstance().setAppLanguage(getContext(), AppLanguageUtil.English);
        dismiss();
        new NavigateUtil().activityIntentWithPage(baseActivity, MainActivity.class, false, ProfileFragment.page);
    }

    private void data() {
        binding.tvArabic.setBackgroundResource(R.drawable.dark_gray_button);
        binding.tvEnglish.setBackgroundResource(R.drawable.dark_gray_button);
        if (AppController.getInstance().getAppSettingsPreferences()
                .getAppLanguage().equals(AppLanguageUtil.English)) {
            binding.tvEnglish.setBackgroundResource(R.drawable.blue_button);
        } else {
            binding.tvArabic.setBackgroundResource(R.drawable.blue_button);
        }
    }
}

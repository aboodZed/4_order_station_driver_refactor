package com.webapp.mohammad_al_loh.feature.reset.one;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.webapp.mohammad_al_loh.databinding.FragmentResetStep1Binding;
import com.webapp.mohammad_al_loh.models.ResetCode;
import com.webapp.mohammad_al_loh.utils.AppController;
import com.webapp.mohammad_al_loh.utils.dialogs.WaitDialogFragment;
import com.webapp.mohammad_al_loh.utils.language.BaseActivity;
import com.webapp.mohammad_al_loh.utils.listeners.DialogView;

public class ResetStep1 extends Fragment implements DialogView<ResetCode> {

    public static final int page = 401;

    private FragmentResetStep1Binding binding;

    private ResetStep1Presenter presenter;
    private BaseActivity baseActivity;
    private ResetCode resetCode;

    public ResetStep1(BaseActivity baseActivity) {
        presenter = new ResetStep1Presenter(baseActivity, this);
    }

    public static ResetStep1 newInstance(BaseActivity baseActivity) {
        return new ResetStep1(baseActivity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentResetStep1Binding.inflate(getLayoutInflater());
        data();
        click();
        return binding.getRoot();
    }

    private void data() {
        binding.tvCode.setText(AppController.getInstance()
                .getAppSettingsPreferences().getCountry().getPhone_code());
    }

    private void click() {
        // binding.btnSend.setOnClickListener(view -> presenter.validInput(binding.etEnterPhone));
         binding.btnSend.setOnClickListener(view -> presenter.generateCode());
    }

    public ResetCode getData() {
        return resetCode;
    }

    @Override
    public void setData(ResetCode resetCode) {
        this.resetCode = resetCode;
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

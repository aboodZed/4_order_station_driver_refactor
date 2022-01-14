package com.webapp.mohammad_al_loh.utils.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.webapp.mohammad_al_loh.R;
import com.webapp.mohammad_al_loh.databinding.FragmentWaitBinding;

public class WaitDialogFragment extends DialogFragment {

    private FragmentWaitBinding binding;

    private static WaitDialogFragment fragment;

    public static WaitDialogFragment newInstance() {
        if (fragment == null) {
            fragment = new WaitDialogFragment();
            return fragment;
        } else return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWaitBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.transparent);
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        setCancelable(false);
        super.onResume();
    }
}

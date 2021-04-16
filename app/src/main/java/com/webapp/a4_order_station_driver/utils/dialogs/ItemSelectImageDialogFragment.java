package com.webapp.a4_order_station_driver.utils.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.FragmentItemImageDialogBinding;


public class ItemSelectImageDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private FragmentItemImageDialogBinding binding;

    private static final String ARG_DIALOG_TITLE = "title";
    private Listener mListener;

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public static ItemSelectImageDialogFragment newInstance() {
        final ItemSelectImageDialogFragment fragment = new ItemSelectImageDialogFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentItemImageDialogBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.flBackground.setOnClickListener(this);
        binding.selectImageCamera.setOnClickListener(this);
        binding.selectImageGallery.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            mListener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_image_camera:
                if (mListener != null) {
                    dismiss();
                    mListener.onCameraClicked();
                }
                break;
            case R.id.select_image_gallery:
                if (mListener != null) {
                    dismiss();
                    mListener.onGalleryClicked();
                }
                break;
            case R.id.fl_background:
                dismiss();
        }
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = (int) (getResources().getDisplayMetrics().widthPixels);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.transparent);
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        setCancelable(false);
        super.onResume();
        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if ((keyCode == android.view.KeyEvent.KEYCODE_BACK)) {
                dismiss();
                return true;
            } else return false;
        });
    }

    public interface Listener {
        void onGalleryClicked();

        void onCameraClicked();
    }
}
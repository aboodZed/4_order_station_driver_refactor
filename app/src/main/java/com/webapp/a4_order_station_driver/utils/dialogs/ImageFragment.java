package com.webapp.a4_order_station_driver.utils.dialogs;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.FragmentImageBinding;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageFragment extends DialogFragment {

    private FragmentImageBinding binding;

    private static final String IMG = "img";

    public static ImageFragment newInstance(Bitmap bitmap) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putParcelable(IMG, bitmap);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentImageBinding.inflate(getLayoutInflater());
        data();
        return binding.getRoot();
    }

    private void data() {
        if (getArguments() != null && getArguments().getParcelable(IMG) != null) {
            binding.ivImage.setImageBitmap(getArguments().getParcelable(IMG));
            new PhotoViewAttacher(binding.ivImage).update();
        }
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        params.height = (int) (getResources().getDisplayMetrics().heightPixels * 0.9);
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.transparent);
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        setCancelable(false);
        super.onResume();
        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                this.dismiss();
                return true;
            } else return false;
        });
    }
}
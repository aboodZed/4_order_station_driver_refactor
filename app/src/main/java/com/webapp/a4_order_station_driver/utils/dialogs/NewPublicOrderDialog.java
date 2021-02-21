package com.webapp.a4_order_station_driver.utils.dialogs;

import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.DialogNewOrderBinding;
import com.webapp.a4_order_station_driver.models.PublicOrder;
import com.webapp.a4_order_station_driver.utils.AppContent;

public class NewPublicOrderDialog extends DialogFragment {

    private DialogNewOrderBinding binding;

    private static NewPublicOrderDialog dialog;
    private NewPublicOrderListener listener;
    private PublicOrder publicOrder;

    public static NewPublicOrderDialog newInstance(PublicOrder publicOrder) {
        Bundle args = new Bundle();
        args.putSerializable(AppContent.INPUT_ORDER, publicOrder);
        dialog = new NewPublicOrderDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.dialog_new_order, container, false);
        binding = DialogNewOrderBinding.inflate(getLayoutInflater());
        data();
        click();
        return binding.getRoot();
    }

    private void click() {
        binding.btnView.setOnClickListener(view -> listener.viewNewOrder(publicOrder));
        binding.btnCancel.setOnClickListener(view -> dismiss());
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER);
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        setCancelable(false);
        super.onResume();
        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                NewPublicOrderDialog.this.dismiss();
                return true;
            } else return false;
        });
    }

    public void setListener(NewPublicOrderListener listener) {
        this.listener = listener;
    }

    private void data() {
        if (getArguments() != null) {
            publicOrder = (PublicOrder) getArguments().getSerializable(AppContent.INPUT_ORDER);
            binding.tvFrom.setText(publicOrder.getStore_name());
            binding.tvTo.setText(publicOrder.getClient().getName());
            binding.tvPickupLocation.setText(publicOrder.getStore_address());
            binding.tvDestLocation.setText(publicOrder.getDestination_address());
        }
    }

    public interface NewPublicOrderListener {
        void viewNewOrder(PublicOrder publicOrder);
    }
}

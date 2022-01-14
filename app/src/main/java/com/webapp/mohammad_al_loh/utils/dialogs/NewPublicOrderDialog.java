package com.webapp.mohammad_al_loh.utils.dialogs;

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

import com.webapp.mohammad_al_loh.R;
import com.webapp.mohammad_al_loh.databinding.DialogNewOrderBinding;
import com.webapp.mohammad_al_loh.feature.order.newPublicOrder.NewPublicOrderFragment;
import com.webapp.mohammad_al_loh.models.PublicOrder;
import com.webapp.mohammad_al_loh.models.PublicOrderObject;
import com.webapp.mohammad_al_loh.utils.APIUtil;
import com.webapp.mohammad_al_loh.utils.AppContent;
import com.webapp.mohammad_al_loh.utils.AppController;
import com.webapp.mohammad_al_loh.utils.NavigateUtil;
import com.webapp.mohammad_al_loh.utils.ToolUtil;
import com.webapp.mohammad_al_loh.utils.listeners.RequestListener;

public class NewPublicOrderDialog extends DialogFragment {

    private DialogNewOrderBinding binding;

    private static NewPublicOrderDialog dialog;
    private NewPublicOrderListener listener;
    private PublicOrder publicOrder;

    public static NewPublicOrderDialog newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt(AppContent.ORDER_Id, id);
        dialog = new NewPublicOrderDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogNewOrderBinding.inflate(getLayoutInflater());
        getData(getArguments().getInt(AppContent.ORDER_Id, -1));
        click();
        return binding.getRoot();
    }

    private void click() {
        binding.btnView.setOnClickListener(view -> {
            dismiss();
            new NavigateUtil().openOrder(getContext(), publicOrder
                    , NewPublicOrderFragment.page, true);
            listener.allowLoadNewOrder();
        });
        binding.btnCancel.setOnClickListener(view ->
        {
            dismiss();
            listener.cancel();
        });
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

    private void getData(int id) {
        if (id != -1) {
            WaitDialogFragment.newInstance().show(getChildFragmentManager(), "");
            new APIUtil<PublicOrderObject>(getActivity()).getData(AppController.getInstance()
                    .getApi().getPublicOrder(id), new RequestListener<PublicOrderObject>() {
                @Override
                public void onSuccess(PublicOrderObject publicOrderObject, String msg) {
                    WaitDialogFragment.newInstance().dismiss();
                    NewPublicOrderDialog.this.publicOrder = publicOrderObject.getPublicOrder();
                    data();
                }

                @Override
                public void onError(String msg) {
                    WaitDialogFragment.newInstance().dismiss();
                    ToolUtil.showLongToast(msg, getActivity());
                    listener.allowLoadNewOrder();
                    dismiss();
                }

                @Override
                public void onFail(String msg) {
                    WaitDialogFragment.newInstance().dismiss();
                    ToolUtil.showLongToast(msg, getActivity());
                    listener.allowLoadNewOrder();
                    dismiss();
                }
            });
        } else {
            listener.allowLoadNewOrder();
            dismiss();
        }
    }

    private void data() {
        binding.tvFrom.setText(publicOrder.getStore_name());
        binding.tvTo.setText(publicOrder.getClient().getName());
        binding.tvPickupLocation.setText(publicOrder.getStore_address());
        binding.tvDestLocation.setText(publicOrder.getDestination_address());
    }

    public interface NewPublicOrderListener {
        void allowLoadNewOrder();

        void cancel();
    }
}

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
import com.webapp.a4_order_station_driver.feature.order.newOrderStation.NewOrderStationFragment;
import com.webapp.a4_order_station_driver.models.OrderStation;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.NavigateUtil;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.language.AppLanguageUtil;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

public class NewOrderStationDialog extends DialogFragment {

    private DialogNewOrderBinding binding;

    private NewOrderListener listener;
    private OrderStation orderStation;

    public static NewOrderStationDialog newInstance(int id) {
        Bundle args = new Bundle();
        args.putInt(AppContent.ORDER_Id, id);
        NewOrderStationDialog dialog = new NewOrderStationDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.dialog_new_order, container, false);
        binding = DialogNewOrderBinding.inflate(getLayoutInflater());
        click();
        getData(getArguments().getInt(AppContent.ORDER_Id, -1));
        return binding.getRoot();
    }

    private void click() {
        binding.btnView.setOnClickListener(view -> {
            new NavigateUtil().openOrder(getContext(), orderStation
                    , NewOrderStationFragment.page, true);
            listener.allowLoadNewOrder();
        });
        //listener.viewNewOrder(orderStation)

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
                NewOrderStationDialog.this.dismiss();
                return true;
            } else return false;
        });
    }

    public void setListener(NewOrderListener listener) {
        this.listener = listener;
    }

    private void getData(int id) {
        if (id != -1) {
            new APIUtil<OrderStation>(getActivity()).getData(AppController.getInstance()
                    .getApi().getOrderById(id), new RequestListener<OrderStation>() {
                @Override
                public void onSuccess(OrderStation orderStation, String msg) {
                    NewOrderStationDialog.this.orderStation = orderStation;
                    data();
                }

                @Override
                public void onError(String msg) {
                    ToolUtil.showLongToast(msg, getActivity());
                    listener.allowLoadNewOrder();
                    dismiss();
                }

                @Override
                public void onFail(String msg) {
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
        if (AppController.getInstance().getAppSettingsPreferences()
                .getAppLanguage().equals(AppLanguageUtil.English)) {

            binding.tvPickupLocation.setText(orderStation.getShop().getAddress_en());
            binding.tvFrom.setText(orderStation.getShop().getName_en());
        } else {
            binding.tvPickupLocation.setText(orderStation.getShop().getAddress_ar());
            binding.tvFrom.setText(orderStation.getShop().getName_ar());
        }
        binding.tvTo.setText(orderStation.getUser().getName());
        binding.tvDestLocation.setText(orderStation.getUser().getAddress());
    }

    public interface NewOrderListener {
        void allowLoadNewOrder();
    }
}

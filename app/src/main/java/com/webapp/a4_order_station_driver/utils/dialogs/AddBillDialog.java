package com.webapp.a4_order_station_driver.utils.dialogs;

import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.FragmentAddBillDialogBinding;
import com.webapp.a4_order_station_driver.feature.order.publicOrderView.PublicOrderViewFragment;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.models.PublicOrder;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

public class AddBillDialog extends BottomSheetDialogFragment implements DialogView<Message> {

    private static final String ENTER = "enter";
    private FragmentAddBillDialogBinding binding;
    private PublicOrder publicOrder;

    public static AddBillDialog newInstance(PublicOrder publicOrder, String s) {
        AddBillDialog fragment = new AddBillDialog();
        Bundle args = new Bundle();
        args.putSerializable(AppContent.ORDER_OBJECT, publicOrder);
        args.putString(ENTER, s);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_add_bill_dialog, container, false);
        binding = FragmentAddBillDialogBinding.inflate(getLayoutInflater());
        data();
        click();
        return binding.getRoot();
    }

    private void click() {
        binding.btnSend.setOnClickListener(view -> send());
    }

    private void data() {
        if (getArguments() != null) {
            publicOrder = (PublicOrder) getArguments().get(AppContent.ORDER_OBJECT);
            String m = getArguments().getString(ENTER);
            binding.tvEnter.setText(m);
            binding.tvMessage.setText(m);
            if (PublicOrderViewFragment.s != 0 && m.equals(getString(R.string.show_bill))) {
                binding.etEnterPrice.setText(PublicOrderViewFragment.s + "");
                binding.etEnterPrice.setFocusable(false);
                binding.btnSend.setVisibility(View.GONE);
            }
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

    public void send() {
        if (TextUtils.isEmpty(binding.etEnterPrice.getText())) {
            binding.etEnterPrice.setError(getString(R.string.empty_error));
            return;
        }

        double price = Double.parseDouble(binding.etEnterPrice.getText().toString().trim());

        if (PublicOrderViewFragment.s == 0) {
            PublicOrderViewFragment.s = price;
            dismiss();
            AddBillDialog addBillDialog = AddBillDialog
                    .newInstance(publicOrder, getString(R.string.re_enter_bill_price));
            addBillDialog.show(getFragmentManager(), "");
        } else {
            uploadBill(price);
        }
    }

    private void uploadBill(double price) {
        if (PublicOrderViewFragment.s != price) {
            binding.etEnterPrice.setError(getString(R.string.not_match));
            return;
        }
        showDialog("");
        new APIUtil<Message>(getActivity())
                .getData(AppController.getInstance().getApi()
                                .sendInvoiceValue(publicOrder.getId(), price)
                        , new RequestListener<Message>() {
                            @Override
                            public void onSuccess(Message message, String msg) {
                                setData(message);
                            }

                            @Override
                            public void onError(String msg) {
                                ToolUtil.showLongToast(msg, getActivity());
                                hideDialog();
                            }

                            @Override
                            public void onFail(String msg) {
                                ToolUtil.showLongToast(msg, getActivity());
                                hideDialog();
                            }
                        });

    }

    @Override
    public void setData(Message message) {
        hideDialog();
        dismiss();
    }

    @Override
    public void showDialog(String s) {
        WaitDialogFragment.newInstance().show(getFragmentManager(), "");
    }

    @Override
    public void hideDialog() {
        WaitDialogFragment.newInstance().dismiss();
    }
}

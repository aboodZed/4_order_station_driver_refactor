package com.webapp.a4_order_station_driver.utils.dialogs;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.FragmentBillDailogBinding;
import com.webapp.a4_order_station_driver.feature.main.orders.OrdersFragment;
import com.webapp.a4_order_station_driver.feature.main.wallets.WalletFragment;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.models.PublicOrder;
import com.webapp.a4_order_station_driver.utils.APIUtils;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

public class BillDialog extends BottomSheetDialogFragment {

    private PublicOrder publicOrder;
    private static BillDialog fragment;
    private Listener listener;
    private FragmentBillDailogBinding binding;

    public static BillDialog newInstance(PublicOrder order) {
        if (fragment != null) {
            fragment.dismiss();
        }
        fragment = new BillDialog();
        Bundle args = new Bundle();
        args.putSerializable(AppContent.INPUT_ORDER, order);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_bill_dailog, container, false);
        binding = FragmentBillDailogBinding.inflate(getLayoutInflater());
        data();
        click();
        return binding.getRoot();
    }

    private void data() {
        if (getArguments() != null) {
            publicOrder = (PublicOrder) getArguments().get(AppContent.INPUT_ORDER);
            if (publicOrder.getStatus().equals(AppContent.TO_STORE_STATUS)) {
                binding.tvDelivery.setVisibility(View.INVISIBLE);
                binding.scOnWay.setClickable(false);

                if (publicOrder.getClient_paid_invoice().equals("1")) {
                    binding.scOnWay.setClickable(true);
                    binding.btnCancel.setVisibility(View.GONE);
                }

            } else if (publicOrder.getStatus().equals(AppContent.TO_CLIENT_STATUS)) {
                binding.scOnWay.setChecked(true);
                binding.scOnWay.setClickable(false);

                if (publicOrder.getClient_paid_invoice().equals("1")) {
                    binding.btnCancel.setVisibility(View.GONE);
                    binding.tvDelivery.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void click() {
        binding.tvAddBill.setOnClickListener(view -> addPrice());
        binding.tvDelivery.setOnClickListener(view -> delivery());
        binding.scOnWay.setOnClickListener(view -> onWay());
        binding.btnCancel.setOnClickListener(view -> cancelOrder());
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
            if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                this.dismiss();
                return true;
            } else return false;
        });
    }

    public void addPrice() {
        if (PublicChatFragment.s == 0) {
            dismiss();
            AddBillDialog addBillDialog = AddBillDialog.newInstance(publicOrder, getString(R.string.enter_bill_price));
            addBillDialog.show(getFragmentManager(), "");
        } else {
            dismiss();
            AddBillDialog addBillDialog = AddBillDialog.newInstance(publicOrder, getString(R.string.show_bill));
            addBillDialog.show(getFragmentManager(), "");
        }
    }

    public void delivery() {
        if (AppController.getInstance().getAppSettingsPreferences()
                .getPayType().equals(AppContent.ON_DELIVERY_STATUS)) {
            CompletePayDialog.newInstance(publicOrder, listener)
                    .show(getFragmentManager(), "");
        } else {
            WaitDialogFragment.newInstance().show(getFragmentManager(), "");
            new APIUtils<Message>(getActivity())
                    .getData(AppController.getInstance().getApi()
                                    .deliveredPublicOrder(publicOrder.getId())
                            , new RequestListener<Message>() {
                                @Override
                                public void onSuccess(Message message, String msg) {
                                    PublicChatFragment.s = 0;
                                    OrdersFragment.viewPagerPage = 0;
                                    WalletFragment.viewPagerPage = 0;
                                    listener.wayToCustomer();
                                }

                                @Override
                                public void onError(String msg) {
                                    showError(msg);
                                }

                                @Override
                                public void onFail(String msg) {
                                    showError(msg);
                                }
                            });

        }
    }

    public void onWay() {
        if (binding.scOnWay.isChecked()) {
            WaitDialogFragment.newInstance().show(getFragmentManager(), "");
            new APIUtils<Message>(getActivity()).getData(AppController.getInstance()
                            .getApi().changeToONTheWay(publicOrder.getId())
                    , new RequestListener<Message>() {
                        @Override
                        public void onSuccess(Message message, String msg) {
                            listener.wayToCustomer();
                        }

                        @Override
                        public void onError(String msg) {
                            showError(msg);
                        }

                        @Override
                        public void onFail(String msg) {
                            showError(msg);
                        }
                    });
        }
    }

    public void cancelOrder() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.cancel))
                .setMessage(getString(R.string.cancel_massage))
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    WaitDialogFragment.newInstance().show(getChildFragmentManager(), "");
                    new APIUtils<Message>(getActivity()).getData(AppController
                                    .getInstance().getApi().cancelOrder(publicOrder.getId())
                            , new RequestListener<Message>() {
                                @Override
                                public void onSuccess(Message message, String msg) {
                                    PublicChatFragment.s = 0;
                                    OrdersFragment.viewPagerPage = 0;
                                    WalletFragment.viewPagerPage = 0;
                                    ToolUtils.showLongToast(message.getMassage(), getActivity());
                                    listener.wayToCustomer();
                                }

                                @Override
                                public void onError(String msg) {
                                    showError(msg);
                                }

                                @Override
                                public void onFail(String msg) {
                                    showError(msg);
                                }
                            });

                })
                .setNegativeButton(R.string.no, null)
                .setIcon(R.drawable.ic_privacy)
                .show();
    }

    private void showError(String msg) {
        ToolUtils.showLongToast(msg, getActivity());
        WaitDialogFragment.newInstance().dismiss();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void wayToCustomer();
    }
}                                                                                                                                     
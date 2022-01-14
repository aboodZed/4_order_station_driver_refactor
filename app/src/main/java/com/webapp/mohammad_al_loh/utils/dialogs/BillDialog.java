package com.webapp.mohammad_al_loh.utils.dialogs;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.webapp.mohammad_al_loh.R;
import com.webapp.mohammad_al_loh.databinding.FragmentBillDailogBinding;
import com.webapp.mohammad_al_loh.feature.main.orders.OrdersFragment;
import com.webapp.mohammad_al_loh.feature.main.orders.station.OrderStationFragment;
import com.webapp.mohammad_al_loh.feature.main.wallets.station.OrderStationWalletFragment;
import com.webapp.mohammad_al_loh.feature.main.wallets.WalletFragment;
import com.webapp.mohammad_al_loh.feature.order.publicOrderView.PublicOrderViewFragment;
import com.webapp.mohammad_al_loh.models.Message;
import com.webapp.mohammad_al_loh.models.PublicOrder;
import com.webapp.mohammad_al_loh.utils.APIUtil;
import com.webapp.mohammad_al_loh.utils.AppContent;
import com.webapp.mohammad_al_loh.utils.AppController;
import com.webapp.mohammad_al_loh.utils.ToolUtil;
import com.webapp.mohammad_al_loh.utils.listeners.RequestListener;

public class BillDialog extends BottomSheetDialogFragment {

    private PublicOrder publicOrder;
    private Listener listener;
    private FragmentBillDailogBinding binding;

    public static BillDialog newInstance(PublicOrder order) {
        BillDialog fragment = new BillDialog();
        Bundle args = new Bundle();
        args.putSerializable(AppContent.ORDER_OBJECT, order);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBillDailogBinding.inflate(getLayoutInflater());
        data();
        click();
        return binding.getRoot();
    }

    private void data() {
        if (getArguments() != null) {
            publicOrder = (PublicOrder) getArguments().get(AppContent.ORDER_OBJECT);
            if (publicOrder.getStatus().equals(AppContent.TO_STORE_STATUS)) {
                binding.tvDelivery.setVisibility(View.INVISIBLE);
                binding.scOnWay.setEnabled(false);

                if (publicOrder.getClient_paid_invoice().equals("1")) {
                    binding.scOnWay.setEnabled(true);
                    binding.btnCancel.setVisibility(View.GONE);
                }

            } else if (publicOrder.getStatus().equals(AppContent.TO_CLIENT_STATUS)) {
                binding.scOnWay.setChecked(true);
                binding.scOnWay.setEnabled(false);

                if (publicOrder.getClient_paid_invoice().equals("1")) {
                    binding.btnCancel.setVisibility(View.GONE);
                    binding.tvDelivery.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void click() {
        binding.tvAddBill.setOnClickListener(view -> {
            dismiss();
            listener.addBill();
        });
        binding.tvDelivery.setOnClickListener(view -> delivery());
        binding.scOnWay.setOnClickListener(view -> onWay());
        binding.btnCancel.setOnClickListener(view -> cancelOrder());
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = getResources().getDisplayMetrics().widthPixels;
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

    public void onWay() {
        if (binding.scOnWay.isChecked()) {
            WaitDialogFragment.newInstance().show(getChildFragmentManager(), "");
            new APIUtil<Message>(getActivity()).getData(AppController.getInstance()
                            .getApi().changeToONTheWay(publicOrder.getId())
                    , new RequestListener<Message>() {
                        @Override
                        public void onSuccess(Message message, String msg) {
                            listener.updatePublicOrder();
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

    public void delivery() {
        if (AppController.getInstance().getAppSettingsPreferences()
                .getPayType().equals(AppContent.ON_DELIVERY_STATUS)) {
            CompletePayDialog.newInstance(publicOrder, listener)
                    .show(getChildFragmentManager(), "");
        } else {
            WaitDialogFragment.newInstance().show(getChildFragmentManager(), "");
            new APIUtil<Message>(getActivity()).getData(AppController.getInstance().getApi()
                    .deliveredPublicOrder(publicOrder.getId()), new RequestListener<Message>() {
                @Override
                public void onSuccess(Message message, String msg) {
                    PublicOrderViewFragment.billPrice = 0;
                    OrdersFragment.viewPagerPage = OrderStationFragment.viewPagerPage;
                    WalletFragment.viewPagerPage = OrderStationWalletFragment.viewPagerPage;
                    listener.updatePublicOrder();
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
                    new APIUtil<Message>(getActivity()).getData(AppController
                                    .getInstance().getApi().cancelOrder(publicOrder.getId())
                            , new RequestListener<Message>() {
                                @Override
                                public void onSuccess(Message message, String msg) {
                                    PublicOrderViewFragment.billPrice = 0;
                                    OrdersFragment.viewPagerPage = OrderStationFragment.viewPagerPage;
                                    WalletFragment.viewPagerPage = OrderStationWalletFragment.viewPagerPage;
                                    ToolUtil.showLongToast(message.getMassage(), getActivity());
                                    listener.updatePublicOrder();
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
        ToolUtil.showLongToast(msg, getActivity());
        WaitDialogFragment.newInstance().dismiss();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void updatePublicOrder();

        void addBill();
    }
}                                                                                                                                     
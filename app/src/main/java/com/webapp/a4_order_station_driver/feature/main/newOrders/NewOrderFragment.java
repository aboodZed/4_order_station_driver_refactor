package com.webapp.a4_order_station_driver.feature.main.newOrders;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.model.LatLng;
import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.FragmentNewOrderBinding;
import com.webapp.a4_order_station_driver.feature.main.adapter.OrderItemsAdapter;
import com.webapp.a4_order_station_driver.feature.main.hame.HomeFragment;
import com.webapp.a4_order_station_driver.feature.main.orders.OrdersFragment;
import com.webapp.a4_order_station_driver.feature.main.wallets.WalletFragment;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.models.OrderItem;
import com.webapp.a4_order_station_driver.models.OrderStation;
import com.webapp.a4_order_station_driver.utils.APIUtils;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.NavigateUtils;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.formatter.DecimalFormatterManager;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;
import com.webapp.a4_order_station_driver.utils.view.Tracking;

import java.util.ArrayList;

public class NewOrderFragment extends Fragment {

    private FragmentNewOrderBinding binding;

    private BaseActivity baseActivity;
    private Tracking tracking;
    private OrderItemsAdapter orderItemsAdapter;
    private OrderStation order;
    private Listener listener;

    public NewOrderFragment(BaseActivity baseActivity, Tracking tracking) {
        this.baseActivity = baseActivity;
        this.tracking = tracking;
    }

    public static NewOrderFragment newInstance(BaseActivity baseActivity, Tracking tracking) {
        NewOrderFragment fragment = new NewOrderFragment(baseActivity, tracking);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View v = inflater.inflate(R.layout.fragment_new_order, container, false);
        binding = FragmentNewOrderBinding.inflate(getLayoutInflater());
        listener.setDataInNewOrder();
        click();
        return binding.getRoot();
    }

    private void click() {
        binding.btnAccept.setOnClickListener(view -> accept());
        binding.btnReject.setOnClickListener(view -> baseActivity.navigate(HomeFragment.page));
        binding.ivLocationShop.setOnClickListener(view -> new NavigateUtils()
                .setLocation(getActivity(), new LatLng(order.getShop().getLat(), order.getShop().getLng())));
        binding.ivLocationClient.setOnClickListener(view -> new NavigateUtils()
                .setLocation(getActivity(), new LatLng(order.getDestination_lat(), order.getDestination_lng())));
    }

    //clicks
    public void accept() {
        WaitDialogFragment.newInstance().show(getFragmentManager(), "");
        new APIUtils<Message>(getActivity()).getData(AppController.getInstance()
                .getApi().pickupOrder(order.getId()), new RequestListener<Message>() {
            @Override
            public void onSuccess(Message message, String msg) {
                WaitDialogFragment.newInstance().dismiss();
                AppController.getInstance().getAppSettingsPreferences().setTrackingOrder(order);
                tracking.startGPSTracking();
                ToolUtils.showLongToast(getString(R.string.closeApp), getActivity());
                OrdersFragment.viewPagerPage = 0;
                WalletFragment.viewPagerPage = 0;
                baseActivity.navigate(OrdersFragment.page);
            }

            @Override
            public void onError(String msg) {
                ToolUtils.showLongToast(msg, getActivity());
                WaitDialogFragment.newInstance().dismiss();
            }

            @Override
            public void onFail(String msg) {
                ToolUtils.showLongToast(msg, getActivity());
                WaitDialogFragment.newInstance().dismiss();
            }
        });
    }

    //function
    public void setData(OrderStation order) {
        String currency = AppController.getInstance().getAppSettingsPreferences().getCountry().getCurrency_code();
        if (ToolUtils.checkTheInternet()) {
            WaitDialogFragment.newInstance().show(getFragmentManager(), "");
            this.order = order;
            binding.tvOrderId.setText(getString(R.string.order) + "#" + order.getInvoice_number());
            if (!TextUtils.isEmpty(order.getSub_total_1()))
                binding.tvSubTotalBefore.setText(DecimalFormatterManager.getFormatterInstance()
                        .format(Double.parseDouble(order.getSub_total_1())) + " " + currency);
            if (!TextUtils.isEmpty(order.getDiscount()))
                if (Integer.parseInt(order.getDiscount()) <= 0) {
                    binding.tvDiscount.setText(DecimalFormatterManager.getFormatterInstance()
                            .format(Double.parseDouble(order.getDiscount())) + " " + currency);
                } else {
                    binding.tvDiscount.setText("-" + DecimalFormatterManager.getFormatterInstance()
                            .format(Double.parseDouble(order.getDiscount())) + " " + currency);
                }
            if (!TextUtils.isEmpty(order.getSub_total_2()))
                binding.tvSubTotalAfter.setText(DecimalFormatterManager.getFormatterInstance()
                        .format(Double.parseDouble(order.getSub_total_2())) + " " + currency);
            if (!TextUtils.isEmpty(order.getTax()))
                binding.tvTaxes.setText(DecimalFormatterManager.getFormatterInstance()
                        .format(Double.parseDouble(order.getTax())) + " " + currency);
            if (!TextUtils.isEmpty(order.getDelivery()))
                binding.tvDelivery.setText(DecimalFormatterManager.getFormatterInstance()
                        .format(Double.parseDouble(order.getDelivery())) + " " + currency);
            if (!TextUtils.isEmpty(order.getTotal()))
                binding.tvTotal.setText(DecimalFormatterManager.getFormatterInstance()
                        .format(Double.parseDouble(order.getTotal())) + " " + currency);
            if (!TextUtils.isEmpty(order.getType_of_receive()))
                binding.tvReceive.setText(order.getType_of_receive());
            binding.tvDatetime.setText(ToolUtils.getTime(order.getOrder_created_timestamp()) + " " +
                    ToolUtils.getDate(order.getOrder_created_timestamp()));
//shop info
            ToolUtils.loadImage(getContext(), binding.pbWaitCoImage, order.getShop().getLogo_url(), binding.ivCoImage);
            if (AppController.getInstance().getAppSettingsPreferences().getAppLanguage().equals("en")) {
                binding.tvCoName.setText(order.getShop().getName_en());
                binding.tvOrderCoName.setText(order.getShop().getName_en());
                binding.tvOrderCoAddress.setText(order.getShop().getAddress_en());
                binding.tvCoAddress.setText(order.getShop().getAddress_en());
            } else {
                binding.tvCoName.setText(order.getShop().getName_ar());
                binding.tvOrderCoName.setText(order.getShop().getName_ar());
                binding.tvOrderCoAddress.setText(order.getShop().getAddress_ar());
                binding.tvCoAddress.setText(order.getShop().getAddress_ar());
            }
//user info
            ToolUtils.loadImage(getContext(), binding.pbWaitReciverImage
                    , order.getUser().getAvatar_url(), binding.ivReceiverImage);
            binding.tvReceiverName.setText(order.getUser().getName());
            binding.tvReceiverAddress.setText(order.getUser().getAddress());
            WaitDialogFragment.newInstance().dismiss();
            initRecycleView(order.getOrder_items());

        } else {
            ToolUtils.showLongToast(getString(R.string.no_connection), getActivity());
        }
    }

    private void initRecycleView(ArrayList<OrderItem> orderItems) {
        orderItemsAdapter = new OrderItemsAdapter(orderItems);
        binding.rvOrderItem.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvOrderItem.setItemAnimator(new DefaultItemAnimator());
        binding.rvOrderItem.setAdapter(orderItemsAdapter);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void setDataInNewOrder();
    }
}

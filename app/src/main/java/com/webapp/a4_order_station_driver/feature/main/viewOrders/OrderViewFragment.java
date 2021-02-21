package com.webapp.a4_order_station_driver.feature.main.viewOrders;

import android.Manifest;
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
import com.webapp.a4_order_station_driver.databinding.FragmentOrderViewBinding;
import com.webapp.a4_order_station_driver.feature.main.adapter.OrderItemsAdapter;
import com.webapp.a4_order_station_driver.feature.main.hame.HomeFragment;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.models.OrderItem;
import com.webapp.a4_order_station_driver.models.OrderStation;
import com.webapp.a4_order_station_driver.utils.APIUtils;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.NavigateUtils;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.dialogs.ChatFragment;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.formatter.DecimalFormatterManager;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;
import com.webapp.a4_order_station_driver.utils.PermissionUtil;
import com.webapp.a4_order_station_driver.utils.view.Tracking;

import java.util.ArrayList;

import static com.webapp.a4_order_station_driver.utils.AppContent.PHONE_CALL_CODE;

public class OrderViewFragment extends Fragment {

    private FragmentOrderViewBinding binding;

    private BaseActivity baseActivity;
    private Tracking tracking;
    private OrderItemsAdapter orderItemsAdapter;
    private OrderStation order;
    private Listener listener;

    public OrderViewFragment(BaseActivity baseActivity, Tracking tracking) {
        this.baseActivity = baseActivity;
        this.tracking = tracking;
    }

    public static OrderViewFragment newInstance(BaseActivity baseActivity, Tracking tracking) {
        OrderViewFragment fragment = new OrderViewFragment(baseActivity, tracking);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View v = inflater.inflate(R.layout.fragment_order_view, container, false);
        binding = FragmentOrderViewBinding.inflate(getLayoutInflater());
        listener.setDataInOrderView();
        click();
        return binding.getRoot();
    }

    private void click() {
        binding.ivReceiverChat.setOnClickListener(view -> chat());
        binding.btnDone.setOnClickListener(view -> done());
        binding.ivCoCall.setOnClickListener(view -> coCall());
        binding.ivReceiveCall.setOnClickListener(view -> receiverCall());
        binding.ivCoLocation.setOnClickListener(view -> new NavigateUtils()
                .setLocation(getActivity(), new LatLng(order.getShop().getLat(), order.getShop().getLng())));
        binding.ivReceiveLocation.setOnClickListener(view -> new NavigateUtils()
                .setLocation(getActivity(), new LatLng(order.getDestination_lat(), order.getDestination_lng())));
    }

    public void chat() {
        ChatFragment chatFragment = ChatFragment.newInstance(order);
        chatFragment.show(getFragmentManager(), "");
    }

    public void done() {
        WaitDialogFragment.newInstance().show(getFragmentManager(), "");
        new APIUtils<Message>(getActivity()).getData(AppController.getInstance()
                .getApi().deliveryOrder(order.getId()), new RequestListener<Message>() {
            @Override
            public void onSuccess(Message message, String msg) {
                if (tracking != null) {
                    tracking.endGPSTracking();
                }
                WaitDialogFragment.newInstance().dismiss();
                baseActivity.navigate(HomeFragment.page);
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

    public void coCall() {
        if (PermissionUtil.isPermissionGranted(Manifest.permission.CALL_PHONE, getActivity())) {
            new NavigateUtils().makeCall(getActivity(), order.getShop().getMobile());
        } else {
            PermissionUtil.requestPermissionOnFragment(this, Manifest.permission.CALL_PHONE, PHONE_CALL_CODE);
        }
    }

    public void receiverCall() {
        if (PermissionUtil.isPermissionGranted(Manifest.permission.CALL_PHONE, getActivity())) {
            new NavigateUtils().makeCall(getActivity(), order.getUser().getMobile());
        } else {
            PermissionUtil.requestPermissionOnFragment(this, Manifest.permission.CALL_PHONE, PHONE_CALL_CODE);
        }
    }

    //functions
    public void setData(OrderStation order) {
        String currency = AppController.getInstance().getAppSettingsPreferences().getCountry().getCurrency_code();

        if (order.getStatus().equals(AppContent.DELIVERED_STATUS)) {
            binding.btnDone.setClickable(false);
            binding.btnDone.setVisibility(View.GONE);
            binding.ivCoCall.setVisibility(View.GONE);
            binding.ivCoLocation.setVisibility(View.GONE);
            binding.ivReceiveCall.setVisibility(View.GONE);
            binding.ivReceiveLocation.setVisibility(View.GONE);
            binding.ivReceiverChat.setVisibility(View.GONE);
        }
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
//shop info
        ToolUtils.loadImage(getContext(), binding.pbWaitCoImage, order.getShop().getLogo_url(), binding.ivCoImage);
        if (AppController.getInstance().getAppSettingsPreferences().getAppLanguage().equals("en")) {
            binding.tvCoName.setText(order.getShop().getName_en());
            binding.tvOrderCoName.setText(order.getShop().getName_en());
            binding.tvOrderCoAddress.setText(order.getShop().getAddress_en());
        } else {
            binding.tvCoName.setText(order.getShop().getName_ar());
            binding.tvOrderCoName.setText(order.getShop().getName_ar());
            binding.tvOrderCoAddress.setText(order.getShop().getAddress_ar());
        }
//user info
        ToolUtils.loadImage(getContext(), binding.pbWaitReciverImage
                , order.getUser().getAvatar_url(), binding.ivReceiverImage);
        binding.tvReceiverName.setText(order.getUser().getName());
        WaitDialogFragment.newInstance().dismiss();

        new APIUtils<OrderStation>(getActivity()).getData(AppController.getInstance()
                .getApi().getOrderById(order.getId()), new RequestListener<OrderStation>() {
            @Override
            public void onSuccess(OrderStation orderStation, String msg) {
                initRecycleView(orderStation.getOrder_items());

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

    public void initRecycleView(ArrayList<OrderItem> orderItems) {
        orderItemsAdapter = new OrderItemsAdapter(orderItems);
        binding.rvOrderItem.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvOrderItem.setItemAnimator(new DefaultItemAnimator());
        binding.rvOrderItem.setAdapter(orderItemsAdapter);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void setDataInOrderView();
    }
}

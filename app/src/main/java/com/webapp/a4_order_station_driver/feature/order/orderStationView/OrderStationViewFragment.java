package com.webapp.a4_order_station_driver.feature.order.orderStationView;

import static com.webapp.a4_order_station_driver.utils.AppContent.PHONE_CALL_CODE;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.annotations.NotNull;
import com.webapp.a4_order_station_driver.databinding.FragmentOrderViewBinding;
import com.webapp.a4_order_station_driver.feature.main.adapter.OrderItemsAdapter;
import com.webapp.a4_order_station_driver.feature.order.chat.ChatFragment;
import com.webapp.a4_order_station_driver.models.Order;
import com.webapp.a4_order_station_driver.models.OrderStation;
import com.webapp.a4_order_station_driver.models.OrderStationItem;
import com.webapp.a4_order_station_driver.utils.APIImageUtil;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.NavigateUtil;
import com.webapp.a4_order_station_driver.utils.PermissionUtil;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;

import java.util.ArrayList;

public class OrderStationViewFragment extends Fragment implements DialogView<OrderStation> {

    public final static int page = 503;

    private FragmentOrderViewBinding binding;

    private BaseActivity baseActivity;
    private OrderItemsAdapter orderItemsAdapter;
    private OrderStation orderStation;
    private OrderStationViewPresenter presenter;

    public OrderStationViewFragment(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    public static OrderStationViewFragment newInstance(BaseActivity baseActivity, Order order) {
        OrderStationViewFragment fragment = new OrderStationViewFragment(baseActivity);
        Bundle args = new Bundle();
        args.putSerializable(AppContent.ORDER_OBJECT, order);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrderViewBinding.inflate(getLayoutInflater());
        presenter = new OrderStationViewPresenter(baseActivity, this);
        Order order = (Order) requireArguments().getSerializable(AppContent.ORDER_OBJECT);
        if (order != null)
            presenter.getOrderData(order);
        click();
        return binding.getRoot();
    }

    private void click() {
        binding.ivReceiverChat.setOnClickListener(view -> baseActivity.navigate(ChatFragment.page));
        binding.btnDone.setOnClickListener(view -> presenter.finishOrder(orderStation));
        binding.ivCoCall.setOnClickListener(view -> coCall());
        binding.ivReceiveCall.setOnClickListener(view -> receiverCall());
        binding.ivCoLocation.setOnClickListener(view -> new NavigateUtil()
                .setLocation(requireActivity(), new LatLng(orderStation.getStore().getLat(), orderStation.getStore().getLng())));
        binding.ivReceiveLocation.setOnClickListener(view -> new NavigateUtil()
                .setLocation(requireActivity(), new LatLng(orderStation.getCustomer().getLat(), orderStation.getCustomer().getLng())));
    }

    public void coCall() {
        if (PermissionUtil.isPermissionGranted(Manifest.permission.CALL_PHONE, getActivity())) {
            new NavigateUtil().makeCall(requireActivity(), orderStation.getStore().getMobile());
        } else {
            PermissionUtil.requestPermission(getActivity(), Manifest.permission.CALL_PHONE, PHONE_CALL_CODE);
        }
    }

    public void receiverCall() {
        if (PermissionUtil.isPermissionGranted(Manifest.permission.CALL_PHONE, getActivity())) {
            new NavigateUtil().makeCall(requireActivity(), orderStation.getCustomer().getMobile());
        } else {
            PermissionUtil.requestPermission(getActivity(), Manifest.permission.CALL_PHONE, PHONE_CALL_CODE);
        }
    }

    @Override
    public void setData(OrderStation orderStation) {
        String currency = AppController.getInstance().getAppSettingsPreferences().getUser()
                .getCountry().getCurrency_code();
        this.orderStation = orderStation;
        APIImageUtil.loadImage(getContext(), binding.pbWaitReciverImage, this.orderStation.getCustomer().getAvatar_url(), binding.ivReceiverImage);
        APIImageUtil.loadImage(getContext(), binding.pbWaitCoImage, this.orderStation.getStore().getLogo_url(), binding.ivCoImage);

        binding.tvOrderCoName.setText(this.orderStation.getStore().getName());
        binding.tvOrderCoAddress.setText(this.orderStation.getStore().getAddress());

       /* binding.tvDelivery.setText();
        if (this.orderStation.getStatus().equals(AppContent.DELIVERED_STATUS)) {
            binding.btnDone.setClickable(false);
            binding.btnDone.setVisibility(View.GONE);
            binding.ivCoCall.setVisibility(View.GONE);
            binding.ivCoLocation.setVisibility(View.GONE);
            binding.ivReceiveCall.setVisibility(View.GONE);
            binding.ivReceiveLocation.setVisibility(View.GONE);
            binding.ivReceiverChat.setVisibility(View.GONE);
        }
        binding.tvOrderId.setText((getString(R.string.order) + "#" + this.orderStation.getInvoice_number()));
        if (!TextUtils.isEmpty(this.orderStation.getSub_total_1()))
            binding.tv_time.setText((DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(this.orderStation.getSub_total_1())) + " " + currency));
        if (!TextUtils.isEmpty(this.orderStation.getDiscount()))
            if (Integer.parseInt(this.orderStation.getDiscount()) <= 0) {
                binding.tv_delivery_fees.setText((DecimalFormatterManager.getFormatterInstance()
                        .format(Double.parseDouble(this.orderStation.getDiscount())) + " " + currency));
            } else {
                binding.tv_delivery_fees.setText(("-" + DecimalFormatterManager.getFormatterInstance()
                        .format(Double.parseDouble(this.orderStation.getDiscount())) + " " + currency));
            }
        if (!TextUtils.isEmpty(this.orderStation.getSub_total_2()))
            binding.tvSubTotalAfter.setText((DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(this.orderStation.getSub_total_2())) + " " + currency));
        if (!TextUtils.isEmpty(this.orderStation.getTax()))
            binding.tv_vat.setText((DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(this.orderStation.getTax())) + " " + currency));
        if (!TextUtils.isEmpty(this.orderStation.getDelivery()))
            binding.tvDelivery.setText((DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(this.orderStation.getDelivery())) + " " + currency));
        if (!TextUtils.isEmpty(this.orderStation.getTotal()))
            binding.tvTotal.setText((DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(this.orderStation.getTotal())) + " " + currency));
        if (!TextUtils.isEmpty(this.orderStation.getType_of_receive()))
            binding.tvReceive.setText(this.orderStation.getType_of_receive());
//shop info
        APIImageUtil.loadImage(getContext(), binding.pbWaitCoImage, this.orderStation.getShop().getLogo_url(), binding.ivCoImage);
        if (AppController.getInstance().getAppSettingsPreferences().getAppLanguage().equals(AppLanguageUtil.English)) {
            binding.tvCoName.setText(this.orderStation.getShop().getName_en());
            binding.tvOrderCoName.setText(this.orderStation.getShop().getName_en());
            binding.tvOrderCoAddress.setText(this.orderStation.getShop().getAddress_en());
        } else {
            binding.tvCoName.setText(this.orderStation.getShop().getName_ar());
            binding.tvOrderCoName.setText(this.orderStation.getShop().getName_ar());
            binding.tvOrderCoAddress.setText(this.orderStation.getShop().getAddress_ar());
        }
//user info
        APIImageUtil.loadImage(getContext(), binding.pbWaitReciverImage
                , this.orderStation.getUser().getAvatar_url(), binding.ivReceiverImage);
        binding.tvReceiverName.setText(this.orderStation.getUser().getName());*/
        //items
        initRecycleView(orderStation.getOrderItems());
    }

    @Override
    public void showDialog(String s) {
        WaitDialogFragment.newInstance().show(getChildFragmentManager(), "");
    }

    @Override
    public void hideDialog() {
        WaitDialogFragment.newInstance().dismiss();
    }

    public void initRecycleView(ArrayList<OrderStationItem> orderItems) {
        orderItemsAdapter = new OrderItemsAdapter(orderItems);
        binding.rvOrderItem.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvOrderItem.setItemAnimator(new DefaultItemAnimator());
        binding.rvOrderItem.setAdapter(orderItemsAdapter);
    }
}

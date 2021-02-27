package com.webapp.a4_order_station_driver.feature.order.orderStationView;

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
import com.webapp.a4_order_station_driver.models.Order;
import com.webapp.a4_order_station_driver.models.OrderItem;
import com.webapp.a4_order_station_driver.models.OrderStation;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.NavigateUtil;
import com.webapp.a4_order_station_driver.utils.PermissionUtil;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.feature.order.chat.ChatFragment;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.formatter.DecimalFormatterManager;
import com.webapp.a4_order_station_driver.utils.language.AppLanguageUtil;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;
import com.webapp.a4_order_station_driver.utils.location.tracking.OrderGPSTracking;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.webapp.a4_order_station_driver.utils.AppContent.PHONE_CALL_CODE;

public class OrderStationViewFragment extends Fragment implements DialogView<OrderStation> {

    public final static int page = 503;

    private FragmentOrderViewBinding binding;

    private BaseActivity baseActivity;
    private OrderItemsAdapter orderItemsAdapter;
    private OrderStation orderStation;
    private OrderStationViewPresenter presenter;
    //private Listener listener;

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
        //View v = inflater.inflate(R.layout.fragment_order_view, container, false);
        binding = FragmentOrderViewBinding.inflate(getLayoutInflater());
        presenter = new OrderStationViewPresenter(baseActivity, this);
        Order order = (Order) requireArguments().getSerializable(AppContent.ORDER_OBJECT);
        if (order != null)
            presenter.getOrderData(order);

        //listener.setDataInOrderView();
        click();
        return binding.getRoot();
    }

    private void click() {
        binding.ivReceiverChat.setOnClickListener(view -> baseActivity.navigate(ChatFragment.page));
        binding.btnDone.setOnClickListener(view -> presenter.finishOrder(orderStation));
        binding.ivCoCall.setOnClickListener(view -> coCall());
        binding.ivReceiveCall.setOnClickListener(view -> receiverCall());
        binding.ivCoLocation.setOnClickListener(view -> new NavigateUtil()
                .setLocation(requireActivity(), new LatLng(orderStation.getShop().getLat(), orderStation.getShop().getLng())));
        binding.ivReceiveLocation.setOnClickListener(view -> new NavigateUtil()
                .setLocation(requireActivity(), new LatLng(orderStation.getDestination_lat(), orderStation.getDestination_lng())));
    }

    public void coCall() {
        if (PermissionUtil.isPermissionGranted(Manifest.permission.CALL_PHONE, getActivity())) {
            new NavigateUtil().makeCall(requireActivity(), orderStation.getShop().getMobile());
        } else {
            PermissionUtil.requestPermissionOnFragment(this, Manifest.permission.CALL_PHONE, PHONE_CALL_CODE);
        }
    }

    public void receiverCall() {
        if (PermissionUtil.isPermissionGranted(Manifest.permission.CALL_PHONE, getActivity())) {
            new NavigateUtil().makeCall(requireActivity(), orderStation.getUser().getMobile());
        } else {
            PermissionUtil.requestPermissionOnFragment(this, Manifest.permission.CALL_PHONE, PHONE_CALL_CODE);
        }
    }

    @Override
    public void setData(OrderStation orderStation) {
        String currency = AppController.getInstance().getAppSettingsPreferences().getCountry().getCurrency_code();
        this.orderStation = orderStation;

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
            binding.tvSubTotalBefore.setText((DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(this.orderStation.getSub_total_1())) + " " + currency));
        if (!TextUtils.isEmpty(this.orderStation.getDiscount()))
            if (Integer.parseInt(this.orderStation.getDiscount()) <= 0) {
                binding.tvDiscount.setText((DecimalFormatterManager.getFormatterInstance()
                        .format(Double.parseDouble(this.orderStation.getDiscount())) + " " + currency));
            } else {
                binding.tvDiscount.setText(("-" + DecimalFormatterManager.getFormatterInstance()
                        .format(Double.parseDouble(this.orderStation.getDiscount())) + " " + currency));
            }
        if (!TextUtils.isEmpty(this.orderStation.getSub_total_2()))
            binding.tvSubTotalAfter.setText((DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(this.orderStation.getSub_total_2())) + " " + currency));
        if (!TextUtils.isEmpty(this.orderStation.getTax()))
            binding.tvTaxes.setText((DecimalFormatterManager.getFormatterInstance()
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
        ToolUtil.loadImage(getContext(), binding.pbWaitCoImage, this.orderStation.getShop().getLogo_url(), binding.ivCoImage);
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
        ToolUtil.loadImage(getContext(), binding.pbWaitReciverImage
                , this.orderStation.getUser().getAvatar_url(), binding.ivReceiverImage);
        binding.tvReceiverName.setText(this.orderStation.getUser().getName());
        //items
        initRecycleView(orderStation.getOrder_items());
    }

    @Override
    public void showDialog(String s) {
        WaitDialogFragment.newInstance().show(getChildFragmentManager(), "");
    }

    @Override
    public void hideDialog() {
        WaitDialogFragment.newInstance().dismiss();
    }

    public void initRecycleView(ArrayList<OrderItem> orderItems) {
        orderItemsAdapter = new OrderItemsAdapter(orderItems);
        binding.rvOrderItem.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvOrderItem.setItemAnimator(new DefaultItemAnimator());
        binding.rvOrderItem.setAdapter(orderItemsAdapter);
    }

    /*public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void setDataInOrderView();
    }*/
}

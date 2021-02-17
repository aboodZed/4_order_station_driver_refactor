package com.webapp.a4_order_station_driver.feature.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.feature.home.adapter.OrderItemsAdapter;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.models.OrderStation;
import com.webapp.a4_order_station_driver.models.OrderItem;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.formatter.DecimalFormatterManager;
import com.webapp.a4_order_station_driver.utils.view.NavigationView;
import com.webapp.a4_order_station_driver.utils.view.Tracking;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewOrderFragment extends Fragment {

    @BindView(R.id.tv_order_id) TextView tvOrderId;
    @BindView(R.id.tv_datetime) TextView tvDatetime;
    @BindView(R.id.iv_co_image) ImageView ivCoImage;
    @BindView(R.id.pb_wait_co_image) ProgressBar pbWaitCoImage;
    @BindView(R.id.tv_co_name) TextView tvCoName;
    @BindView(R.id.tv_co_address) TextView tvCoAddress;
    @BindView(R.id.iv_location_shop) ImageView ivLocationShop;
    @BindView(R.id.iv_receiver_image) ImageView ivReceiverImage;
    @BindView(R.id.pb_wait_reciver_image) ProgressBar pbWaitReciverImage;
    @BindView(R.id.tv_receiver_name) TextView tvReceiverName;
    @BindView(R.id.tv_receiver_address) TextView tvReceiverAddress;
    @BindView(R.id.iv_location_client) ImageView ivLocationClient;
    @BindView(R.id.tv_order_co_name) TextView tvOrderCoName;
    @BindView(R.id.tv_order_co_address) TextView tvOrderCoAddress;
    @BindView(R.id.rv_order_item) RecyclerView rvOrderItem;
    @BindView(R.id.tv_sub_total_before) TextView tvSubTotalBefore;
    @BindView(R.id.tv_discount) TextView tvDiscount;
    @BindView(R.id.tv_sub_total_after) TextView tvSubTotalAfter;
    @BindView(R.id.tv_taxes) TextView tvTaxes;
    @BindView(R.id.tv_delivery) TextView tvDelivery;
    @BindView(R.id.tv_total) TextView tvTotal;
    @BindView(R.id.tv_receive) TextView tvReceive;
    @BindView(R.id.btn_accept) Button btnAccept;
    @BindView(R.id.btn_reject) Button btnReject;

    private static NavigationView view;
    private static Tracking tracking;
    private OrderItemsAdapter orderItemsAdapter;
    private OrderStation order;
    private Listener listener;

    public static NewOrderFragment newInstance(NavigationView navigationView, Tracking t) {
        view = navigationView;
        tracking = t;
        NewOrderFragment fragment = new NewOrderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_order, container, false);
        ButterKnife.bind(this, v);
        listener.setDataInNewOrder();
        return v;
    }

    //clicks
    @OnClick(R.id.btn_accept)
    public void accept() {
        if (ToolUtils.checkTheInternet()) {
            WaitDialogFragment.newInstance().show(getFragmentManager(), "");
            AppController.getInstance().getApi().pickupOrder(order.getId()).enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                    if (response.isSuccessful()) {
                        WaitDialogFragment.newInstance().dismiss();
                        AppController.getInstance().getAppSettingsPreferences().setTrackingOrder(order);
                        tracking.startGPSTracking();
                        ToolUtils.showLongToast(getString(R.string.closeApp), getActivity());
                        OrdersFragment.page = 0;
                        WalletFragment.page = 0;
                        view.navigate(3);
                    } else {
                        ToolUtils.showError(getActivity(), response.errorBody());
                        WaitDialogFragment.newInstance().dismiss();
                    }
                }

                @Override
                public void onFailure(Call<Message> call, Throwable t) {
                    t.printStackTrace();
                    ToolUtils.showLongToast(getString(R.string.error), getActivity());
                    WaitDialogFragment.newInstance().dismiss();
                }
            });
        }
    }

    @OnClick(R.id.btn_reject)
    public void rejectOrder() {
        view.navigate(1);
    }

    @OnClick(R.id.iv_location_shop)
    public void shopLocation() {
        String format = "geo:0,0?q=" + order.getShop().getLat()
                + "," + order.getShop().getLng() + "( Location title)";
        Uri uri = Uri.parse(format);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @OnClick(R.id.iv_location_client)
    public void clientLocation() {
        String format = "geo:0,0?q=" + order.getDestination_lat()
                + "," + order.getDestination_lng() + "( Location title)";
        Uri uri = Uri.parse(format);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //function
    public void setData(OrderStation order) {
        String currency = AppController.getInstance().getAppSettingsPreferences().getCountry().getCurrency_code();
        if (ToolUtils.checkTheInternet()) {
            WaitDialogFragment.newInstance().show(getFragmentManager(), "");
            this.order = order;
            tvOrderId.setText(getString(R.string.order) + "#" + order.getInvoice_number());
            if (!TextUtils.isEmpty(order.getSub_total_1()))
                tvSubTotalBefore.setText(DecimalFormatterManager.getFormatterInstance()
                        .format(Double.parseDouble(order.getSub_total_1())) + " " + currency);
            if (!TextUtils.isEmpty(order.getDiscount()))
                if (Integer.parseInt(order.getDiscount()) <= 0) {
                    tvDiscount.setText(DecimalFormatterManager.getFormatterInstance()
                            .format(Double.parseDouble(order.getDiscount())) + " " + currency);
                } else {
                    tvDiscount.setText("-" + DecimalFormatterManager.getFormatterInstance()
                            .format(Double.parseDouble(order.getDiscount())) + " " + currency);
                }
            if (!TextUtils.isEmpty(order.getSub_total_2()))
                tvSubTotalAfter.setText(DecimalFormatterManager.getFormatterInstance()
                        .format(Double.parseDouble(order.getSub_total_2())) + " " + currency);
            if (!TextUtils.isEmpty(order.getTax()))
                tvTaxes.setText(DecimalFormatterManager.getFormatterInstance()
                        .format(Double.parseDouble(order.getTax())) + " " + currency);
            if (!TextUtils.isEmpty(order.getDelivery()))
                tvDelivery.setText(DecimalFormatterManager.getFormatterInstance()
                        .format(Double.parseDouble(order.getDelivery())) + " " + currency);
            if (!TextUtils.isEmpty(order.getTotal()))
                tvTotal.setText(DecimalFormatterManager.getFormatterInstance()
                        .format(Double.parseDouble(order.getTotal())) + " " + currency);
            if (!TextUtils.isEmpty(order.getType_of_receive()))
                tvReceive.setText(order.getType_of_receive());
            tvDatetime.setText(ToolUtils.getTime(order.getOrder_created_timestamp()) + " " +
                    ToolUtils.getDate(order.getOrder_created_timestamp()));
//shop info
            ToolUtils.loadImage(getContext(), pbWaitCoImage, order.getShop().getLogo_url(), ivCoImage);
            if (AppController.getInstance().getAppSettingsPreferences().getAppLanguage().equals("en")) {
                tvCoName.setText(order.getShop().getName_en());
                tvOrderCoName.setText(order.getShop().getName_en());
                tvOrderCoAddress.setText(order.getShop().getAddress_en());
                tvCoAddress.setText(order.getShop().getAddress_en());
            } else {
                tvCoName.setText(order.getShop().getName_ar());
                tvOrderCoName.setText(order.getShop().getName_ar());
                tvOrderCoAddress.setText(order.getShop().getAddress_ar());
                tvCoAddress.setText(order.getShop().getAddress_ar());
            }
//user info
            ToolUtils.loadImage(getContext(), pbWaitReciverImage, order.getUser().getAvatar_url(), ivReceiverImage);
            tvReceiverName.setText(order.getUser().getName());
            tvReceiverAddress.setText(order.getUser().getAddress());
            WaitDialogFragment.newInstance().dismiss();
            initRecycleView(order.getOrder_items());

        } else {
            ToolUtils.showLongToast(getString(R.string.no_connection), getActivity());
        }
    }

    private void initRecycleView(ArrayList<OrderItem> orderItems) {
        orderItemsAdapter = new OrderItemsAdapter(orderItems);
        rvOrderItem.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvOrderItem.setItemAnimator(new DefaultItemAnimator());
        rvOrderItem.setAdapter(orderItemsAdapter);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    interface Listener {
        void setDataInNewOrder();
    }
}

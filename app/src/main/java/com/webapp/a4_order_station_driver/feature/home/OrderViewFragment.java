package com.webapp.a4_order_station_driver.feature.home;

import android.Manifest;
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
import com.webapp.a4_order_station_driver.utils.dialogs.ChatFragment;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.formatter.DecimalFormatterManager;
import com.webapp.a4_order_station_driver.utils.location.PermissionUtil;
import com.webapp.a4_order_station_driver.utils.view.NavigationView;
import com.webapp.a4_order_station_driver.utils.view.Tracking;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.webapp.a4_order_station_driver.utils.AppContent.PHONE_CALL_CODE;

public class OrderViewFragment extends Fragment {

    @BindView(R.id.tv_order_id) TextView tvOrderId;
    @BindView(R.id.iv_co_image) ImageView ivCoImage;
    @BindView(R.id.pb_wait_co_image) ProgressBar pbWaitCoImage;
    @BindView(R.id.tv_co_name) TextView tvCoName;
    @BindView(R.id.iv_co_call) ImageView ivCoCall;
    @BindView(R.id.iv_co_location) ImageView ivCoLocation;
    @BindView(R.id.iv_receiver_image) ImageView ivReceiverImage;
    @BindView(R.id.pb_wait_reciver_image) ProgressBar pbWaitReciverImage;
    @BindView(R.id.tv_receiver_name) TextView tvReceiverName;
    @BindView(R.id.iv_receiver_chat) ImageView ivReceiverChat;
    @BindView(R.id.iv_receive_call) ImageView ivReceiveCall;
    @BindView(R.id.iv_receive_location) ImageView ivReceiveLocation;
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
    @BindView(R.id.btn_done) Button btnDone;

    private static NavigationView view;
    private static Tracking tracking;
    private OrderItemsAdapter orderItemsAdapter;
    private OrderStation order;
    private Listener listener;

    public static OrderViewFragment newInstance(NavigationView navigationView, Tracking t) {
        OrderViewFragment fragment = new OrderViewFragment();
        view = navigationView;
        tracking = t;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order_view, container, false);
        ButterKnife.bind(this, v);
        listener.setDataInOrderView();
        return v;
    }

    @OnClick(R.id.iv_receiver_chat)
    public void chat() {
        ChatFragment chatFragment = ChatFragment.newInstance(order);
        chatFragment.show(getFragmentManager(), "");
    }

    @OnClick(R.id.btn_done)
    public void done() {
        if (ToolUtils.checkTheInternet()) {
            WaitDialogFragment.newInstance().show(getFragmentManager(), "");
            AppController.getInstance().getApi().deliveryOrder(order.getId()).enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                    if (response.isSuccessful()) {
                        if (tracking != null) {
                            tracking.endGPSTracking();
                        }
                        WaitDialogFragment.newInstance().dismiss();
                        view.navigate(1);
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

    @OnClick(R.id.iv_co_call)
    public void coCall() {
        if (PermissionUtil.isPermissionGranted(Manifest.permission.CALL_PHONE, getActivity())) {
            makeCall(order.getShop().getMobile());
        } else {
            PermissionUtil.requestPermissionOnFragment(this, Manifest.permission.CALL_PHONE, PHONE_CALL_CODE);
        }
    }

    @OnClick(R.id.iv_co_location)
    public void coLocation() {
        String format = "geo:0,0?q=" + order.getShop().getLat()
                + "," + order.getShop().getLng() + "( Location title)";
        Uri uri = Uri.parse(format);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @OnClick(R.id.iv_receive_call)
    public void receiverCall() {
        if (PermissionUtil.isPermissionGranted(Manifest.permission.CALL_PHONE, getActivity())) {
            makeCall(order.getUser().getMobile());
        } else {
            PermissionUtil.requestPermissionOnFragment(this, Manifest.permission.CALL_PHONE, PHONE_CALL_CODE);
        }
    }

    @OnClick(R.id.iv_receive_location)
    public void reLocation() {
        String format = "geo:0,0?q=" + order.getDestination_lat()
                + "," + order.getDestination_lng() + "( Location title)";
        Uri uri = Uri.parse(format);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //functions
    public void setData(OrderStation order) {
        String currency = AppController.getInstance().getAppSettingsPreferences().getCountry().getCurrency_code();

        if (ToolUtils.checkTheInternet()) {
            if (order.getStatus().equals("delivered")) {
                btnDone.setClickable(false);
                btnDone.setVisibility(View.GONE);
                ivCoCall.setVisibility(View.GONE);
                ivCoLocation.setVisibility(View.GONE);
                ivReceiveCall.setVisibility(View.GONE);
                ivReceiveLocation.setVisibility(View.GONE);
                ivReceiverChat.setVisibility(View.GONE);
            }
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
//shop info
            ToolUtils.loadImage(getContext(), pbWaitCoImage, order.getShop().getLogo_url(), ivCoImage);
            if (AppController.getInstance().getAppSettingsPreferences().getAppLanguage().equals("en")) {
                tvCoName.setText(order.getShop().getName_en());
                tvOrderCoName.setText(order.getShop().getName_en());
                tvOrderCoAddress.setText(order.getShop().getAddress_en());
            } else {
                tvCoName.setText(order.getShop().getName_ar());
                tvOrderCoName.setText(order.getShop().getName_ar());
                tvOrderCoAddress.setText(order.getShop().getAddress_ar());
            }
//user info
            ToolUtils.loadImage(getContext(), pbWaitReciverImage, order.getUser().getAvatar_url(), ivReceiverImage);
            tvReceiverName.setText(order.getUser().getName());
            WaitDialogFragment.newInstance().dismiss();


            AppController.getInstance().getApi().getOrderById(order.getId()).enqueue(new Callback<OrderStation>() {
                @Override
                public void onResponse(Call<OrderStation> call, Response<OrderStation> response) {
                    if (response.isSuccessful()) {
                        initRecycleView(response.body().getOrder_items());
                    } else {
                        ToolUtils.showError(getActivity(), response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<OrderStation> call, Throwable t) {
                    t.printStackTrace();
                    WaitDialogFragment.newInstance().dismiss();
                }
            });
        } else {
            ToolUtils.showLongToast(getString(R.string.no_connection), getActivity());
        }
    }

    public void initRecycleView(ArrayList<OrderItem> orderItems) {
        orderItemsAdapter = new OrderItemsAdapter(orderItems);
        rvOrderItem.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvOrderItem.setItemAnimator(new DefaultItemAnimator());
        rvOrderItem.setAdapter(orderItemsAdapter);
    }

    private void makeCall(String n) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + n));
        startActivity(intent);
    }

    private void setOrder(OrderStation order) {
        this.order = order;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    interface Listener {
        void setDataInOrderView();
    }
}

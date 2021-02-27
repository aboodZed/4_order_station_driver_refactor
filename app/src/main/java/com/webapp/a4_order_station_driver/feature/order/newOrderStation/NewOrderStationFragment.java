package com.webapp.a4_order_station_driver.feature.order.newOrderStation;

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
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.models.OrderItem;
import com.webapp.a4_order_station_driver.models.OrderStation;
import com.webapp.a4_order_station_driver.models.PublicOrder;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.NavigateUtil;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.formatter.DecimalFormatterManager;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;

import java.util.ArrayList;

public class NewOrderStationFragment extends Fragment implements DialogView<Message> {

    public final static int page = 501;

    private FragmentNewOrderBinding binding;

    private BaseActivity baseActivity;
    private OrderItemsAdapter orderItemsAdapter;
    private OrderStation orderStation;
    //private Listener listener;
    private NewOrderStationPresenter presenter;

    public NewOrderStationFragment(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    public static NewOrderStationFragment newInstance(BaseActivity baseActivity, OrderStation orderStation) {
        NewOrderStationFragment fragment = new NewOrderStationFragment(baseActivity);
        Bundle args = new Bundle();
        args.putSerializable(AppContent.ORDER_OBJECT, orderStation);
        fragment.setArguments(args);
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
        presenter = new NewOrderStationPresenter(baseActivity, this);
        //listener.setDataInNewOrder();
        data();
        click();
        return binding.getRoot();
    }

    private void click() {
        binding.btnAccept.setOnClickListener(view -> presenter.accept(orderStation));
        binding.btnReject.setOnClickListener(view -> baseActivity.onBackPressed());

        binding.ivLocationShop.setOnClickListener(view -> new NavigateUtil().setLocation(getActivity()
                , new LatLng(orderStation.getShop().getLat(), orderStation.getShop().getLng())));

        binding.ivLocationClient.setOnClickListener(view -> new NavigateUtil().setLocation(getActivity()
                , new LatLng(orderStation.getDestination_lat(), orderStation.getDestination_lng())));
    }

    //function
    public void data() {
        String currency = AppController.getInstance().getAppSettingsPreferences().getCountry().getCurrency_code();
        orderStation = (OrderStation) requireArguments().getSerializable(AppContent.ORDER_OBJECT);

        binding.tvOrderId.setText((getString(R.string.order) + "#" + orderStation.getInvoice_number()));
        if (!TextUtils.isEmpty(orderStation.getSub_total_1()))
            binding.tvSubTotalBefore.setText((DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(orderStation.getSub_total_1())) + " " + currency));
        if (!TextUtils.isEmpty(orderStation.getDiscount()))
            if (Integer.parseInt(orderStation.getDiscount()) <= 0) {
                binding.tvDiscount.setText((DecimalFormatterManager.getFormatterInstance()
                        .format(Double.parseDouble(orderStation.getDiscount())) + " " + currency));
            } else {
                binding.tvDiscount.setText(("-" + DecimalFormatterManager.getFormatterInstance()
                        .format(Double.parseDouble(orderStation.getDiscount())) + " " + currency));
            }
        if (!TextUtils.isEmpty(orderStation.getSub_total_2()))
            binding.tvSubTotalAfter.setText((DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(orderStation.getSub_total_2())) + " " + currency));
        if (!TextUtils.isEmpty(orderStation.getTax()))
            binding.tvTaxes.setText((DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(orderStation.getTax())) + " " + currency));
        if (!TextUtils.isEmpty(orderStation.getDelivery()))
            binding.tvDelivery.setText((DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(orderStation.getDelivery())) + " " + currency));
        if (!TextUtils.isEmpty(orderStation.getTotal()))
            binding.tvTotal.setText((DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(orderStation.getTotal())) + " " + currency));
        if (!TextUtils.isEmpty(orderStation.getType_of_receive()))
            binding.tvReceive.setText(orderStation.getType_of_receive());
        binding.tvDatetime.setText((ToolUtil.getTime(orderStation.getOrder_created_timestamp()) + " " +
                ToolUtil.getDate(orderStation.getOrder_created_timestamp())));
//shop info
        ToolUtil.loadImage(getContext(), binding.pbWaitCoImage, orderStation.getShop().getLogo_url(), binding.ivCoImage);
        if (AppController.getInstance().getAppSettingsPreferences().getAppLanguage().equals("en")) {
            binding.tvCoName.setText(orderStation.getShop().getName_en());
            binding.tvOrderCoName.setText(orderStation.getShop().getName_en());
            binding.tvOrderCoAddress.setText(orderStation.getShop().getAddress_en());
            binding.tvCoAddress.setText(orderStation.getShop().getAddress_en());
        } else {
            binding.tvCoName.setText(orderStation.getShop().getName_ar());
            binding.tvOrderCoName.setText(orderStation.getShop().getName_ar());
            binding.tvOrderCoAddress.setText(orderStation.getShop().getAddress_ar());
            binding.tvCoAddress.setText(orderStation.getShop().getAddress_ar());
        }
//user info
        ToolUtil.loadImage(getContext(), binding.pbWaitReciverImage
                , orderStation.getUser().getAvatar_url(), binding.ivReceiverImage);
        binding.tvReceiverName.setText(orderStation.getUser().getName());
        binding.tvReceiverAddress.setText(orderStation.getUser().getAddress());
        WaitDialogFragment.newInstance().dismiss();
        initRecycleView(orderStation.getOrder_items());
    }

    private void initRecycleView(ArrayList<OrderItem> orderItems) {
        orderItemsAdapter = new OrderItemsAdapter(orderItems);
        binding.rvOrderItem.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvOrderItem.setItemAnimator(new DefaultItemAnimator());
        binding.rvOrderItem.setAdapter(orderItemsAdapter);
    }

    @Override
    public void setData(Message message) {

    }

    @Override
    public void showDialog(String s) {
        WaitDialogFragment.newInstance().show(getChildFragmentManager(), "");
    }

    @Override
    public void hideDialog() {
        WaitDialogFragment.newInstance().dismiss();
    }

   /* public void setListener(Listener listener) {
        this.listener = listener;
    }


    public interface Listener {
        void setDataInNewOrder();
    }*/
}

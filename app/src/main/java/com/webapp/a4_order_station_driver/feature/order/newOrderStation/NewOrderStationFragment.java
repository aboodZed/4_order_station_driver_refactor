package com.webapp.a4_order_station_driver.feature.order.newOrderStation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.webapp.a4_order_station_driver.databinding.FragmentNewOrderBinding;
import com.webapp.a4_order_station_driver.feature.main.adapter.OrderItemsAdapter;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.models.OrderStation;
import com.webapp.a4_order_station_driver.models.OrderStationItem;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
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
        binding = FragmentNewOrderBinding.inflate(getLayoutInflater());
        presenter = new NewOrderStationPresenter(baseActivity, this);
        data();
        click();
        return binding.getRoot();
    }

    private void click() {
        binding.btnAccept.setOnClickListener(view -> presenter.accept(orderStation));
        binding.btnReject.setOnClickListener(view -> baseActivity.onBackPressed());

        /*binding.ivLocationShop.setOnClickListener(view -> new NavigateUtil().setLocation(getActivity()
                , new LatLng(orderStation.getShop().getLat(), orderStation.getShop().getLng())));

        binding.ivLocationClient.setOnClickListener(view -> new NavigateUtil().setLocation(getActivity()
                , new LatLng(orderStation.getDestination_lat(), orderStation.getDestination_lng())));*/
    }

    //function
    public void data() {
        String currency = AppController.getInstance().getAppSettingsPreferences()
                .getUser().getCountry().getCurrency_code();
        orderStation = (OrderStation) requireArguments().getSerializable(AppContent.ORDER_OBJECT);

        //binding.tvOrderId.setText((getString(R.string.order) + "#" + testOrder.getInvoice_number()));
        binding.tvDelivery.setText((DecimalFormatterManager.getFormatterInstance()
                .format(orderStation.getDelivery()) + " " + currency));

        binding.tvSubTotalAfter.setText((DecimalFormatterManager.getFormatterInstance()
                .format(orderStation.getSub_total_2()) + " " + currency));
        /*if (!TextUtils.isEmpty(testOrder.getDiscount()))
            if (Integer.parseInt(testOrder.getDiscount()) <= 0) {
                binding.tvDiscount.setText((DecimalFormatterManager.getFormatterInstance()
                        .format(Double.parseDouble(testOrder.getDiscount())) + " " + currency));
            } else {
                binding.tvDiscount.setText(("-" + DecimalFormatterManager.getFormatterInstance()
                        .format(Double.parseDouble(testOrder.getDiscount())) + " " + currency));
            }
       if (!TextUtils.isEmpty(testOrder.getSub_total_2()))
            binding.tvSubTotalAfter.setText((DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(testOrder.getSub_total_2())) + " " + currency));
                       binding.tvDelivery.setText((DecimalFormatterManager.getFormatterInstance()
                    .format(testOrder.getDelivery()) + " " + currency));
                    */
        //if (!TextUtils.isEmpty(testOrder.getTax()))
        binding.tvVat.setText((DecimalFormatterManager.getFormatterInstance()
                .format(orderStation.getTax()) + " " + currency));
        //if (!TextUtils.isEmpty(testOrder.getDelivery()))

        //if (!TextUtils.isEmpty(testOrder.getTotal()))
        binding.tvTotal.setText((DecimalFormatterManager.getFormatterInstance()
                .format(orderStation.getTotal()) + " " + currency));
        //if (!TextUtils.isEmpty(testOrder.getType_of_receive()))
        binding.tvReceive.setText(orderStation.getType_of_receive());

        binding.tvTime.setText(orderStation.getOrder_date());
//shop info
        //APIImageUtil.loadImage(getContext(), binding.pbWaitCoImage, testOrder.getShop().getLogo_url(), binding.ivCoImage);
        //if (AppController.getInstance().getAppSettingsPreferences().getAppLanguage().equals(AppLanguageUtil.English)) {
        binding.tvFrom.setText(orderStation.getStore().getName());
        binding.tvPickupLocation.setText(orderStation.getStore().getAddress());
        binding.tvTo.setText(orderStation.getCustomer().getName());
        binding.tvDestLocation.setText(orderStation.getCustomer().getAddress());
        binding.tvOrderCoName.setText(orderStation.getStore().getName());
        binding.tvOrderCoAddress.setText(orderStation.getStore().getAddress());
        /*} else {
            binding.tvCoName.setText(testOrder.getShop().getName_ar());
            binding.tvOrderCoName.setText(testOrder.getShop().getName_ar());
            binding.tvOrderCoAddress.setText(testOrder.getShop().getAddress_ar());
            binding.tvCoAddress.setText(testOrder.getShop().getAddress_ar());
        }
//user info
        APIImageUtil.loadImage(getContext(), binding.pbWaitReciverImage
                , testOrder.getUser().getAvatar_url(), binding.ivReceiverImage);
        binding.tvReceiverName.setText(testOrder.getUser().getName());
        binding.tvReceiverAddress.setText(testOrder.getUser().getAddress());*/
        initRecycleView(orderStation.getOrderItems());

    }

    private void initRecycleView(ArrayList<OrderStationItem> orderItems) {
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

}

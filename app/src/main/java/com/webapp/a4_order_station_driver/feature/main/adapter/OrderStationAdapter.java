package com.webapp.a4_order_station_driver.feature.main.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.ItemOrderBinding;
import com.webapp.a4_order_station_driver.feature.main.MainActivity;
import com.webapp.a4_order_station_driver.feature.order.orderStationView.OrderStationViewFragment;
import com.webapp.a4_order_station_driver.models.OrderStation;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.NavigateUtil;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.formatter.DecimalFormatterManager;
import com.webapp.a4_order_station_driver.utils.language.AppLanguageUtil;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;

import java.util.ArrayList;

public class OrderStationAdapter extends RecyclerView.Adapter<OrderStationAdapter.OrdersHolder> {

    private ArrayList<OrderStation> orders = new ArrayList<>();
    private FragmentActivity activity;
    private BaseActivity baseActivity;

    public OrderStationAdapter(FragmentActivity activity, BaseActivity baseActivity) {
        this.activity = activity;
        this.baseActivity = baseActivity;
    }

    @NonNull
    @Override
    public OrdersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrdersHolder(ItemOrderBinding.inflate(LayoutInflater
                .from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersHolder holder, int position) {
        holder.setData(orders.get(position), baseActivity);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void addAll(ArrayList<OrderStation> arrayList) {
        if (arrayList != null) {
            orders.addAll(arrayList);
            notifyDataSetChanged();
        }
    }

    public class OrdersHolder extends RecyclerView.ViewHolder {

        private ItemOrderBinding binding;

        private OrderStation order;
        private BaseActivity baseActivity;

        public OrdersHolder(ItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            click();
        }

        private void click() {
            binding.rootLayout.setOnClickListener(view -> setId());
        }

        public void setId() {
            new NavigateUtil().openOrder(baseActivity, order, OrderStationViewFragment.page, true);
            /*MainActivity.setId(order);
            baseActivity.navigate(7);*/
        }

        public void setData(OrderStation order, BaseActivity baseActivity) {
            this.baseActivity = baseActivity;
            this.order = order;
            binding.tvTime.setText(ToolUtil.getTime(order.getOrder_created_timestamp()));
            binding.tvDate.setText(ToolUtil.getDate(order.getOrder_created_timestamp()));
            binding.tvNumItems.setText((order.getItem_count() + " " + activity.getString(R.string.items)));
            binding.tvPaymentWay.setText(order.getPayment_type());
            binding.tvReceiverPoint.setText(order.getType_of_receive());
            if (order.getStatus().equals(AppContent.DELIVERED_STATUS)) {
                binding.tvOrderState.setBackgroundResource(R.drawable.green_button);
            } else if (order.getStatus().equals(AppContent.CANCEL_STATUS)) {
                binding.tvOrderState.setBackgroundResource(R.drawable.red_button);
            }
            binding.tvOrderState.setText(order.getStatus());
            binding.tvPrice.setText((DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(order.getTotal())) + " " + AppController
                    .getInstance().getAppSettingsPreferences().getCountry().getCurrency_code()));
            binding.tvReceiverName.setText(order.getUser().getName());
            binding.tvReceiverAddress.setText(order.getUser().getAddress());

            if (AppController.getInstance().getAppSettingsPreferences()
                    .getAppLanguage().equals(AppLanguageUtil.English)) {
                binding.tvCoName.setText(order.getShop().getName_en());
                binding.tvCoAddress.setText(order.getShop().getAddress_en());
            } else {
                binding.tvCoName.setText(order.getShop().getName_ar());
                binding.tvCoAddress.setText(order.getShop().getAddress_ar());
            }
        }
    }
}

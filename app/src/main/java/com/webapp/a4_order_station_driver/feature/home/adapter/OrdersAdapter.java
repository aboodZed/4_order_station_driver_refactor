package com.webapp.a4_order_station_driver.feature.home.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.ItemOrderBinding;
import com.webapp.a4_order_station_driver.feature.home.MainActivity;
import com.webapp.a4_order_station_driver.models.OrderStation;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.formatter.DecimalFormatterManager;
import com.webapp.a4_order_station_driver.utils.language.AppLanguageUtil;
import com.webapp.a4_order_station_driver.utils.view.NavigationView;

import java.util.ArrayList;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersHolder> {

    private ArrayList<OrderStation> orders;
    private FragmentActivity activity;
    private NavigationView view;

    public OrdersAdapter(ArrayList<OrderStation> list, FragmentActivity activity, NavigationView view) {
        this.orders = list;
        this.activity = activity;
        this.view = view;
    }

    @NonNull
    @Override
    public OrdersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrdersHolder(ItemOrderBinding.inflate(LayoutInflater
                .from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersHolder holder, int position) {
        holder.setData(orders.get(position), view);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void addItem(OrderStation order) {
        orders.add(order);
        notifyItemInserted(getItemCount() - 1);
    }

    public class OrdersHolder extends RecyclerView.ViewHolder {

        private ItemOrderBinding binding;

        private OrderStation order;
        private NavigationView view;

        public OrdersHolder(ItemOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            click();
        }

        private void click() {
            binding.rootLayout.setOnClickListener(view -> setId());
        }

        public void setId() {
            MainActivity.setId(order);
            view.navigate(7);
        }

        public void setData(OrderStation order, NavigationView navigationView) {
            this.view = navigationView;
            this.order = order;
            binding.tvTime.setText(ToolUtils.getTime(order.getOrder_created_timestamp()));
            binding.tvDate.setText(ToolUtils.getDate(order.getOrder_created_timestamp()));
            binding.tvNumItems.setText(order.getItem_count() + " " + activity.getString(R.string.items));
            binding.tvPaymentWay.setText(order.getPayment_type());
            binding.tvReceiverPoint.setText(order.getType_of_receive());
            if (order.getStatus().equals(AppContent.DELIVERY_STATUS)) {
                binding.tvOrderState.setBackgroundResource(R.drawable.green_button);
            } else if (order.getStatus().equals(AppContent.CANCEL_STATUS)) {
                binding.tvOrderState.setBackgroundResource(R.drawable.red_button);
            }
            binding.tvOrderState.setText(order.getStatus());
            binding.tvPrice.setText(DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(order.getTotal())) + " " + AppController
                    .getInstance().getAppSettingsPreferences().getCountry().getCurrency_code());
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

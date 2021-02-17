package com.webapp.a4_order_station_driver.feature.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.models.ExtraItems;
import com.webapp.a4_order_station_driver.models.OrderItem;
import com.webapp.a4_order_station_driver.models.OrderItemItem;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.formatter.DecimalFormatterManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.OrderItemHolder> {

    private ArrayList<OrderItem> orderItems;

    public OrderItemsAdapter(ArrayList<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public OrderItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderItemHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemHolder holder, int position) {
        holder.setData(orderItems.get(position));
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public void addItem(OrderItem item) {
        orderItems.add(item);
        notifyItemInserted(getItemCount() - 1);
    }

    public class OrderItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_name) TextView tvItemName;
        @BindView(R.id.tv_item_describe) TextView tvItemDescribe;
        @BindView(R.id.tv_item_qnt) TextView tvItemQnt;
        @BindView(R.id.tv_item_price) TextView tvItemPrice;

        public OrderItemHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(OrderItem orderItem) {
            OrderItemItem item = orderItem.getOrderItemItem();
            if (AppController.getInstance().getAppSettingsPreferences().getAppLanguage().equals("en")) {
                tvItemName.setText(item.getName_en());
                for (ExtraItems s : orderItem.getExtra_items()) {
                    tvItemDescribe.append(s.getName_en() + "\n");
                }
            } else {
                tvItemName.setText(item.getName_ar());
                for (ExtraItems s : orderItem.getExtra_items()) {
                    tvItemDescribe.append(s.getName_ar() + "\n");
                }
            }
            tvItemQnt.setText(orderItem.getQty());
            tvItemPrice.setText(DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(orderItem.getPrice())) + " " + AppController
            .getInstance().getAppSettingsPreferences().getCountry().getCurrency_code());
        }
    }
}

package com.webapp.mohammad_al_loh.feature.main.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.webapp.mohammad_al_loh.databinding.ItemOrderItemsBinding;
import com.webapp.mohammad_al_loh.models.ExtraItems;
import com.webapp.mohammad_al_loh.models.OrderItem;
import com.webapp.mohammad_al_loh.models.OrderItemItem;
import com.webapp.mohammad_al_loh.utils.AppController;
import com.webapp.mohammad_al_loh.utils.formatter.DecimalFormatterManager;

import java.util.ArrayList;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.OrderItemHolder> {

    private ArrayList<OrderItem> orderItems;

    public OrderItemsAdapter(ArrayList<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public OrderItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderItemHolder(ItemOrderItemsBinding.inflate(LayoutInflater
                .from(parent.getContext()), parent, false));
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

        private ItemOrderItemsBinding binding;

        public OrderItemHolder(ItemOrderItemsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(OrderItem orderItem) {
            OrderItemItem item = orderItem.getOrderItemItem();
            if (AppController.getInstance().getAppSettingsPreferences().getAppLanguage().equals("en")) {
                binding.tvItemName.setText(item.getName_en());
                for (ExtraItems s : orderItem.getExtra_items()) {
                    binding.tvItemDescribe.append(s.getName_en() + "\n");
                }
            } else {
                binding.tvItemName.setText(item.getName_ar());
                for (ExtraItems s : orderItem.getExtra_items()) {
                    binding.tvItemDescribe.append(s.getName_ar() + "\n");
                }
            }
            binding.tvItemQnt.setText(orderItem.getQty());
            binding.tvItemPrice.setText((DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(orderItem.getPrice())) + " " + AppController
                    .getInstance().getAppSettingsPreferences().getCountry().getCurrency_code()));
        }
    }
}

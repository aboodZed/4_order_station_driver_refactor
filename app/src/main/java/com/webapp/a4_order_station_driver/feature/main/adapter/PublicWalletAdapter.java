package com.webapp.a4_order_station_driver.feature.main.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.webapp.a4_order_station_driver.databinding.ItemPublicWalletBinding;
import com.webapp.a4_order_station_driver.models.PublicOrder;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.formatter.DecimalFormatterManager;

import java.util.ArrayList;

public class PublicWalletAdapter extends RecyclerView.Adapter<PublicWalletAdapter.PublicWalletHolder> {

    ArrayList<PublicOrder> publicOrders;

    public PublicWalletAdapter(ArrayList<PublicOrder> orders) {
        this.publicOrders = orders;
    }

    @NonNull
    @Override
    public PublicWalletHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PublicWalletHolder(ItemPublicWalletBinding.inflate(LayoutInflater
                .from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PublicWalletHolder holder, int position) {
        holder.setData(publicOrders.get(position));
    }

    @Override
    public int getItemCount() {
        return publicOrders.size();
    }

    class PublicWalletHolder extends RecyclerView.ViewHolder {

        private ItemPublicWalletBinding binding;

        public PublicWalletHolder(ItemPublicWalletBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(PublicOrder publicOrder) {
            String currency = AppController.getInstance().getAppSettingsPreferences().getCountry().getCurrency_code();

            binding.tvOrderId.setText(("#" + publicOrder.getInvoice_number()));
            binding.tvDeliveryPrice.setText((DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(publicOrder.getDelivery_cost())) + " " + currency));
            binding.tvTaxPrice.setText((DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(publicOrder.getTax())) + " " + currency));
            binding.tvPriceDeliveryTax.setText((DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(publicOrder.getTotal())) + " " + currency));
            if (publicOrder.getPurchase_invoice_value() != null) {
                binding.tvPriceBill.setText((DecimalFormatterManager.getFormatterInstance()
                        .format(Double.parseDouble(publicOrder.getPurchase_invoice_value())) + " " + currency));
            } else {
                binding.tvPriceBill.setText(("0.00 " + currency));
            }
            binding.tvDelegateDues.setText((DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(publicOrder.getDriver_revenue())) + " " + currency));
            binding.tvAppDues.setText((DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(publicOrder.getApp_revenue())) + " " + currency));
        }
    }
}

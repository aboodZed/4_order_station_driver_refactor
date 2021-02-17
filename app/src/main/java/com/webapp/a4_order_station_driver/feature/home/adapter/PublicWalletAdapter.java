package com.webapp.a4_order_station_driver.feature.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.models.PublicOrder;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.formatter.DecimalFormatterManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PublicWalletAdapter extends RecyclerView.Adapter<PublicWalletAdapter.PublicWalletHolder> {

    ArrayList<PublicOrder> publicOrders;

    public PublicWalletAdapter(ArrayList<PublicOrder> orders) {
        this.publicOrders = orders;
    }

    @NonNull
    @Override
    public PublicWalletHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PublicWalletHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_public_wallet, parent, false));
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

        @BindView(R.id.tv_order_id) TextView tvOrderId;
        @BindView(R.id.tv_delivery_price) TextView tvDeliveryPrice;
        @BindView(R.id.tv_tax_price) TextView tvTaxPrice;
        @BindView(R.id.tv_price_bill) TextView tvPriceBill;
        @BindView(R.id.tv_price_delivery_tax) TextView tvPriceDeliveryTax;
        @BindView(R.id.tv_delegate_dues) TextView tvDelegateDues;
        @BindView(R.id.tv_app_dues) TextView tvAppDues;

        public PublicWalletHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(PublicOrder publicOrder) {
            String currency = AppController.getInstance().getAppSettingsPreferences().getCountry().getCurrency_code();

            tvOrderId.setText("#" + publicOrder.getInvoice_number());
            tvDeliveryPrice.setText(DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(publicOrder.getDelivery_cost())) + " " + currency);
            tvTaxPrice.setText(DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(publicOrder.getTax())) + " " + currency);
            tvPriceDeliveryTax.setText(DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(publicOrder.getTotal())) + " " + currency);
            tvPriceBill.setText(DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(publicOrder.getPurchase_invoice_value())) + " " + currency);
            tvDelegateDues.setText(DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(publicOrder.getDriver_revenue())) + " " + currency);
            tvAppDues.setText(DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(publicOrder.getApp_revenue())) + " " + currency);
        }
    }
}

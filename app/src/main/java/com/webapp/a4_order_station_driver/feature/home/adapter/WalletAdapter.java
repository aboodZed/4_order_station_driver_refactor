package com.webapp.a4_order_station_driver.feature.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.models.Ongoing;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.formatter.DecimalFormatterManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.WalletHolder> {

    private ArrayList<Ongoing> ongoings;

    public WalletAdapter(ArrayList<Ongoing> ongoings) {
        this.ongoings = ongoings;
    }

    @NonNull
    @Override
    public WalletHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WalletHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_balance, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WalletHolder holder, int position) {
        holder.setData(ongoings.get(position));
    }

    @Override
    public int getItemCount() {
        return ongoings.size();
    }

    public void addItem(Ongoing ongoing) {
        ongoings.add(ongoing);
        notifyItemInserted(getItemCount() - 1);
    }

    public ArrayList<Ongoing> getOngoings() {
        return ongoings;
    }

    public class WalletHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_order_id) TextView tvOrderId;
        @BindView(R.id.tv_payment_way) TextView tvPaymentWay;
        @BindView(R.id.tv_price) TextView tvPrice;

        public WalletHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(Ongoing ongoing) {
            tvOrderId.setText("#" + ongoing.getInvoice_number());
            tvPaymentWay.setText(ongoing.getPayment_type());
            tvPrice.setText(DecimalFormatterManager.getFormatterInstance()
                    .format(ongoing.getTotal()) + " " + AppController.getInstance()
                    .getAppSettingsPreferences().getCountry().getCurrency_code());
            if (ongoing.getTotal() < 0) {
                tvPrice.setBackgroundResource(R.drawable.red_button);
            }
        }
    }
}

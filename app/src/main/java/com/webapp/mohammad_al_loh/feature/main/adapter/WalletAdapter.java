package com.webapp.mohammad_al_loh.feature.main.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.webapp.mohammad_al_loh.R;
import com.webapp.mohammad_al_loh.databinding.ItemBalanceBinding;
import com.webapp.mohammad_al_loh.models.Ongoing;
import com.webapp.mohammad_al_loh.utils.AppController;
import com.webapp.mohammad_al_loh.utils.formatter.DecimalFormatterManager;

import java.util.ArrayList;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.WalletHolder> {

    private ArrayList<Ongoing> ongoings;

    public WalletAdapter(ArrayList<Ongoing> ongoings) {
        this.ongoings = ongoings;
    }

    @NonNull
    @Override
    public WalletHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WalletHolder(ItemBalanceBinding.inflate(LayoutInflater
                .from(parent.getContext()), parent, false));
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

        private ItemBalanceBinding binding;

        public WalletHolder(ItemBalanceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(Ongoing ongoing) {
            binding.tvOrderId.setText(("#" + ongoing.getInvoice_number()));
            binding.tvPaymentWay.setText(ongoing.getPayment_type());
            binding.tvPrice.setText((DecimalFormatterManager.getFormatterInstance()
                    .format(ongoing.getTotal()) + " " + AppController.getInstance()
                    .getAppSettingsPreferences().getCountry().getCurrency_code()));
            if (ongoing.getTotal() < 0) {
                binding.tvPrice.setBackgroundResource(R.drawable.red_button);
            }
        }
    }
}

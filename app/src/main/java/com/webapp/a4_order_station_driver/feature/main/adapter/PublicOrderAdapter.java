package com.webapp.a4_order_station_driver.feature.main.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.webapp.a4_order_station_driver.databinding.ItemPublicOrderBinding;
import com.webapp.a4_order_station_driver.feature.order.publicOrderView.PublicOrderViewFragment;
import com.webapp.a4_order_station_driver.models.PublicOrder;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.NavigateUtil;
import com.webapp.a4_order_station_driver.utils.formatter.DecimalFormatterManager;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;

import java.util.ArrayList;

public class PublicOrderAdapter extends RecyclerView.Adapter<PublicOrderAdapter.PubicOrderHolder> {

    private ArrayList<PublicOrder> publicOrders = new ArrayList<>();
    private BaseActivity baseActivity;

    public PublicOrderAdapter(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    @NonNull
    @Override
    public PubicOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PubicOrderHolder(ItemPublicOrderBinding.inflate(LayoutInflater
                .from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PubicOrderHolder holder, int position) {
        holder.setData(publicOrders.get(position));
    }

    @Override
    public int getItemCount() {
        return publicOrders.size();
    }

    public void addAll(ArrayList<PublicOrder> arrayList) {
        if (arrayList != null) {
            publicOrders.addAll(arrayList);
            notifyDataSetChanged();
        }
    }

    class PubicOrderHolder extends RecyclerView.ViewHolder {

        private ItemPublicOrderBinding binding;

        private PublicOrder publicOrder;

        public PubicOrderHolder(ItemPublicOrderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            click();
        }

        private void click() {
            binding.orderItemLayout.setOnClickListener(view -> openChat());
        }

        public void openChat() {
            new NavigateUtil().openOrder(baseActivity, publicOrder
                    , PublicOrderViewFragment.page, true);
           /* PublicOrderViewFragment publicChatFragment = PublicOrderViewFragment.newInstance(publicOrder, baseActivity, tracking);
            publicChatFragment.show(activity.getSupportFragmentManager(), "");*/
        }

        public void setData(PublicOrder publicOrder) {
            this.publicOrder = publicOrder;
            String[] s = publicOrder.getCreated_at().split(" ");
            binding.tvDate.setText((s[0] + "\n" + s[1]));
            binding.tvRestaurantName.setText(publicOrder.getStore_name());
            binding.tvAddress.setText(publicOrder.getStore_address());
            //tvItemsNum.setText(publicOrder.get);
            binding.tvReceivePlace.setText(publicOrder.getDestination_address());
            binding.tvTotalPrice.setText((DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(publicOrder.getTotal())) + " " + AppController.
                    getInstance().getAppSettingsPreferences().getCountry().getCurrency_code()));
            binding.tvStatus.setText(publicOrder.getStatus_translation());
        }
    }
}

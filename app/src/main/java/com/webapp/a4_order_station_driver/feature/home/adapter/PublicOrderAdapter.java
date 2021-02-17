package com.webapp.a4_order_station_driver.feature.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.models.PublicOrder;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.dialogs.PublicChatFragment;
import com.webapp.a4_order_station_driver.utils.formatter.DecimalFormatterManager;
import com.webapp.a4_order_station_driver.utils.view.NavigationView;
import com.webapp.a4_order_station_driver.utils.view.Tracking;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PublicOrderAdapter extends RecyclerView.Adapter<PublicOrderAdapter.PubicOrderHolder> {

    private ArrayList<PublicOrder> publicOrders = new ArrayList<>();
    private FragmentActivity activity;
    private NavigationView view;
    private Tracking tracking;

    public PublicOrderAdapter(FragmentActivity activity
            , NavigationView view, Tracking tracking) {
        this.activity = activity;
        this.view = view;
        this.tracking = tracking;
    }

    @NonNull
    @Override
    public PubicOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PubicOrderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_public_order, parent, false));
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

        @BindView(R.id.order_item_layout) ConstraintLayout orderItemLayout;
        @BindView(R.id.tv_date) TextView tvDate;
        @BindView(R.id.tv_restaurant_name) TextView tvRestaurantName;
        @BindView(R.id.tv_address) TextView tvAddress;
        @BindView(R.id.tv_items_num) TextView tvItemsNum;
        @BindView(R.id.tv_receive_place) TextView tvReceivePlace;
        @BindView(R.id.tv_total_price) TextView tvTotalPrice;
        @BindView(R.id.tv_status) TextView tvStatus;

        private PublicOrder publicOrder;

        public PubicOrderHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.order_item_layout)
        public void openChat() {
            PublicChatFragment publicChatFragment = PublicChatFragment.newInstance(publicOrder, view, tracking);
            publicChatFragment.show(activity.getSupportFragmentManager(), "");
        }

        public void setData(PublicOrder publicOrder) {
            this.publicOrder = publicOrder;
            String s[] = publicOrder.getCreated_at().split(" ");
            tvDate.setText(s[0] + "\n" + s[1]);
            tvRestaurantName.setText(publicOrder.getStore_name());
            tvAddress.setText(publicOrder.getStore_address());
            //tvItemsNum.setText(publicOrder.get);
            tvReceivePlace.setText(publicOrder.getDestination_address());
            tvTotalPrice.setText(DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(publicOrder.getTotal()))+" " + AppController.
                    getInstance().getAppSettingsPreferences().getCountry().getCurrency_code());
            tvStatus.setText(publicOrder.getStatus_translation());
        }
    }
}

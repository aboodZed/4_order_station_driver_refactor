package com.webapp.a4_order_station_driver.feature.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.feature.home.MainActivity;
import com.webapp.a4_order_station_driver.models.OrderStation;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.formatter.DecimalFormatterManager;
import com.webapp.a4_order_station_driver.utils.view.NavigationView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrdersHolder(view);
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

        @BindView(R.id.root_layout) LinearLayout rootLayout;
        @BindView(R.id.tv_time) TextView tvTime;
        @BindView(R.id.tv_date) TextView tvDate;
        @BindView(R.id.tv_num_items) TextView tvNumItems;
        @BindView(R.id.tv_payment_way) TextView tvPaymentWay;
        @BindView(R.id.tv_receiver_point) TextView tvReceiverPoint;
        @BindView(R.id.tv_order_state) TextView tvOrderState;
        @BindView(R.id.tv_price) TextView tvPrice;
        @BindView(R.id.tv_co_name) TextView tvCoName;
        @BindView(R.id.tv_co_address) TextView tvCoAddress;
        @BindView(R.id.tv_receiver_name) TextView tvReceiverName;
        @BindView(R.id.tv_receiver_address) TextView tvReceiverAddress;

        private OrderStation order;
        private NavigationView view;

        public OrdersHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.root_layout)
        public void setId() {
            MainActivity.setId(order);
            view.navigate(7);
        }

        public void setData(OrderStation order, NavigationView navigationView) {
            this.view = navigationView;
            this.order = order;
            tvTime.setText(ToolUtils.getTime(order.getOrder_created_timestamp()));
            tvDate.setText(ToolUtils.getDate(order.getOrder_created_timestamp()));
            tvNumItems.setText(order.getItem_count() + " " + activity.getString(R.string.items));
            tvPaymentWay.setText(order.getPayment_type());
            tvReceiverPoint.setText(order.getType_of_receive());
            if (order.getStatus().equals("delivered")) {
                tvOrderState.setBackgroundResource(R.drawable.green_button);
            }else if (order.getStatus().equals("cancel")){
                tvOrderState.setBackgroundResource(R.drawable.red_button);
            }
            tvOrderState.setText(order.getStatus());
            tvPrice.setText(DecimalFormatterManager.getFormatterInstance()
                    .format(Double.parseDouble(order.getTotal()))+ " " + AppController
                    .getInstance().getAppSettingsPreferences().getCountry().getCurrency_code());
            tvReceiverName.setText(order.getUser().getName());
            tvReceiverAddress.setText(order.getUser().getAddress());

            if (AppController.getInstance().getAppSettingsPreferences()
                    .getAppLanguage().equals("en")) {
                tvCoName.setText(order.getShop().getName_en());
                tvCoAddress.setText(order.getShop().getAddress_en());
            } else {
                tvCoName.setText(order.getShop().getName_ar());
                tvCoAddress.setText(order.getShop().getAddress_ar());
            }
        }
    }
}

package com.webapp.a4_order_station_driver.feature.home.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.feature.home.MainActivity;
import com.webapp.a4_order_station_driver.models.Notification;
import com.webapp.a4_order_station_driver.models.OrderStation;
import com.webapp.a4_order_station_driver.models.PublicArrays;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.dialogs.PublicChatFragment;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.view.NavigationView;
import com.webapp.a4_order_station_driver.utils.view.Tracking;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationHolder> {

    private ArrayList<Notification> notifications;
    private NavigationView view;
    private Activity activity;
    private FragmentManager fragmentManager;
    private Tracking tracking;

    public NotificationsAdapter(ArrayList<Notification> notifications, NavigationView view,
                                Activity activity, FragmentManager fragmentManager, Tracking tracking) {
        this.notifications = notifications;
        this.fragmentManager = fragmentManager;
        this.view = view;
        this.activity = activity;
        this.tracking = tracking;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
        holder.setData(notifications.get(position), view, activity, fragmentManager, tracking);
    }

    public void addItem(Notification notification) {
        this.notifications.add(notification);
        notifyItemInserted(getItemCount() - 1);
    }

    public void deleteItem(Notification notification) {
        int p = this.notifications.indexOf(notification);
        this.notifications.remove(notification);
        notifyItemRemoved(p);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    static class NotificationHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ll_notification) LinearLayout llNotification;
        @BindView(R.id.tv_co_name) TextView tvCoName;
        @BindView(R.id.tv_datetime) TextView tvDatetime;
        @BindView(R.id.tv_message) TextView tvMessage;

        private int id;
        private String type = "";
        private NavigationView view;
        private Activity activity;
        private FragmentManager fragmentManager;
        private Tracking tracking;

        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.ll_notification)
        public void openOrder() {
            if (ToolUtils.checkTheInternet()) {
                if (id != 0) {
                    if (!type.equals("public")) {
                        WaitDialogFragment.newInstance().show(fragmentManager, "");
                        AppController.getInstance().getApi().getOrderById(id).enqueue(new Callback<OrderStation>() {
                            @Override
                            public void onResponse(Call<OrderStation> call, Response<OrderStation> response) {
                                if (response.isSuccessful()) {
                                    if (response.body() != null) {
                                        MainActivity.setId(response.body());
                                        if (response.body().getStatus().equals("ready")) {
                                            view.navigate(6);
                                        } else if (response.body().getDriver_id() != null) {
                                            if (response.body().getDriver_id().equals(AppController.getInstance()
                                                    .getAppSettingsPreferences().getLogin().getUser().getId() + "")) {
                                                view.navigate(7);
                                            }
                                        }
                                    }
                                    WaitDialogFragment.newInstance().dismiss();
                                } else {
                                    ToolUtils.showError(activity, response.errorBody());
                                    WaitDialogFragment.newInstance().dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<OrderStation> call, Throwable t) {
                                ToolUtils.showLongToast(t.getMessage(), activity);
                                WaitDialogFragment.newInstance().dismiss();
                            }
                        });
                    } else if (type.equals("public")) {
                        WaitDialogFragment.newInstance().show(fragmentManager, "");
                        AppController.getInstance().getApi().getPublicOrder(id).enqueue(new Callback<PublicArrays>() {
                            @Override
                            public void onResponse(Call<PublicArrays> call, Response<PublicArrays> response) {
                                if (response.isSuccessful()) {
                                    if (response.body().getPublicOrder().getStatus().equals("pending")
                                            || response.body().getPublicOrder().getDriver_id() == null) {

                                        MainActivity.setPublicOrder(response.body().getPublicOrder());
                                        view.navigate(10);

                                    } else if (response.body().getPublicOrder().getDriver_id().equals(AppController.getInstance()
                                            .getAppSettingsPreferences().getLogin().getUser().getId() + "")) {
                                        PublicChatFragment publicChatFragment = PublicChatFragment.newInstance(response.body().getPublicOrder(), view, tracking);
                                        publicChatFragment.show(fragmentManager, "");
                                    } else {
                                        ToolUtils.showLongToast(activity.getString(R.string.can_not_open), activity);
                                    }


                                    WaitDialogFragment.newInstance().dismiss();
                                } else {
                                    ToolUtils.showError(activity, response.errorBody());
                                    WaitDialogFragment.newInstance().dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<PublicArrays> call, Throwable t) {
                                ToolUtils.showLongToast(t.getMessage(), activity);
                                WaitDialogFragment.newInstance().dismiss();
                            }
                        });
                    } else {
                        ToolUtils.showLongToast(activity.getString(R.string.can_not_open), activity);
                    }
                }
            } else {
                ToolUtils.showLongToast(activity.getString(R.string.no_connection), activity);
            }
        }

        private void setData(Notification data, NavigationView view, Activity activity, FragmentManager fragmentManager, Tracking tracking) {
            this.tracking = tracking;
            this.view = view;
            this.fragmentManager = fragmentManager;
            this.activity = activity;
            if (data.getData().getType() != null) {
                this.type = data.getData().getType();
            }
            if (this.type.equals("public")) {
                this.id = data.getData().getPublic_order_id();
            } else {
                this.id = data.getData().getOrder_id();
            }
            if (id == 0){
                tvCoName.setText(activity.getString(R.string.admin_message));
            }else {
                tvCoName.setText(id + "");
            }
            tvDatetime.setText(ToolUtils.getTime(data.getCreated_at()) + " "
                    + ToolUtils.getDate(data.getCreated_at()));
            tvMessage.setText(data.getData().getMsg());
        }
    }
}

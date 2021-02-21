package com.webapp.a4_order_station_driver.feature.main.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.ItemNotificationBinding;
import com.webapp.a4_order_station_driver.feature.main.MainActivity;
import com.webapp.a4_order_station_driver.models.Notification;
import com.webapp.a4_order_station_driver.models.OrderStation;
import com.webapp.a4_order_station_driver.models.PublicArrays;
import com.webapp.a4_order_station_driver.utils.APIUtils;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.dialogs.PublicChatFragment;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;
import com.webapp.a4_order_station_driver.utils.view.Tracking;

import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationHolder> {

    private ArrayList<Notification> notifications;
    private BaseActivity baseActivity;
    private Activity activity;
    private FragmentManager fragmentManager;
    private Tracking tracking;

    public NotificationsAdapter(ArrayList<Notification> notifications, BaseActivity baseActivity,
                                Activity activity, FragmentManager fragmentManager, Tracking tracking) {
        this.notifications = notifications;
        this.fragmentManager = fragmentManager;
        this.baseActivity = baseActivity;
        this.activity = activity;
        this.tracking = tracking;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationHolder(ItemNotificationBinding.inflate(LayoutInflater
                .from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
        holder.setData(notifications.get(position), baseActivity, activity, fragmentManager, tracking);
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

        private ItemNotificationBinding binding;

        private int id;
        private String type = "";
        private BaseActivity baseActivity;
        private Activity activity;
        private FragmentManager fragmentManager;
        private Tracking tracking;

        public NotificationHolder(ItemNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            click();
        }

        private void click() {
            binding.llNotification.setOnClickListener(view -> openOrder());
        }

        private void setData(Notification data, BaseActivity baseActivity, Activity activity
                , FragmentManager fragmentManager, Tracking tracking) {
            this.tracking = tracking;
            this.baseActivity = baseActivity;
            this.fragmentManager = fragmentManager;
            this.activity = activity;

            if (data.getData().getType() != null) {
                this.type = data.getData().getType();
                if (this.type.equals(AppContent.TYPE_ORDER_PUBLIC)) {
                    this.id = data.getData().getPublic_order_id();
                } else {
                    this.id = data.getData().getOrder_id();
                }
            }

            if (id == 0) {
                binding.tvCoName.setText(activity.getString(R.string.admin_message));
            } else {
                binding.tvCoName.setText(id + "");
            }
            binding.tvDatetime.setText(ToolUtils.getTime(data.getCreated_at()) + " "
                    + ToolUtils.getDate(data.getCreated_at()));
            binding.tvMessage.setText(data.getData().getMsg());
        }

        public void openOrder() {
            if (id != 0) {
                if (!type.equals(AppContent.TYPE_ORDER_PUBLIC)) {
                    WaitDialogFragment.newInstance().show(fragmentManager, "");
                    new APIUtils<OrderStation>(activity).getData(AppController
                                    .getInstance().getApi().getOrderById(id)
                            , new RequestListener<OrderStation>() {
                                @Override
                                public void onSuccess(OrderStation orderStation, String msg) {
                                    WaitDialogFragment.newInstance().dismiss();
                                    MainActivity.setId(orderStation);
                                    if (orderStation.getStatus().equals(AppContent.READY_STATUS)) {
                                        baseActivity.navigate(6);
                                    } else if (orderStation.getDriver_id() != null) {
                                        if (orderStation.getDriver_id().equals(AppController.getInstance()
                                                .getAppSettingsPreferences().getLogin().getUser().getId() + "")) {
                                            baseActivity.navigate(7);
                                        } else {
                                            ToolUtils.showLongToast(activity.getString(R.string.can_not_open), activity);
                                        }
                                    } else {
                                        ToolUtils.showLongToast(activity.getString(R.string.can_not_open), activity);
                                    }
                                }

                                @Override
                                public void onError(String msg) {
                                    ToolUtils.showLongToast(msg, activity);
                                    WaitDialogFragment.newInstance().dismiss();
                                }

                                @Override
                                public void onFail(String msg) {
                                    ToolUtils.showLongToast(msg, activity);
                                    WaitDialogFragment.newInstance().dismiss();
                                }
                            });

                } else {
                    WaitDialogFragment.newInstance().show(fragmentManager, "");

                    new APIUtils<PublicArrays>(activity)
                            .getData(AppController.getInstance().getApi().getPublicOrder(id)
                                    , new RequestListener<PublicArrays>() {
                                        @Override
                                        public void onSuccess(PublicArrays publicArrays, String msg) {
                                            if (publicArrays.getPublicOrder().getStatus().equals(AppContent.PENDING_STATUS)
                                                    || publicArrays.getPublicOrder().getDriver_id() == null) {

                                                MainActivity.setPublicOrder(publicArrays.getPublicOrder());
                                                baseActivity.navigate(10);

                                            } else if (publicArrays.getPublicOrder().getDriver_id()
                                                    .equals(String.valueOf(AppController.getInstance()
                                                            .getAppSettingsPreferences().getLogin().getUser().getId()))) {

                                                PublicChatFragment publicChatFragment = PublicChatFragment
                                                        .newInstance(publicArrays.getPublicOrder(), baseActivity, tracking);
                                                publicChatFragment.show(fragmentManager, "");

                                            } else {
                                                ToolUtils.showLongToast(activity.getString(R.string.can_not_open), activity);
                                            }

                                            WaitDialogFragment.newInstance().dismiss();
                                        }

                                        @Override
                                        public void onError(String msg) {
                                            ToolUtils.showLongToast(msg, activity);
                                            WaitDialogFragment.newInstance().dismiss();
                                        }

                                        @Override
                                        public void onFail(String msg) {
                                            ToolUtils.showLongToast(msg, activity);
                                            WaitDialogFragment.newInstance().dismiss();
                                        }
                                    });
                }
            }
        }
    }
}

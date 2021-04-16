package com.webapp.a4_order_station_driver.feature.main.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.ItemNotificationBinding;
import com.webapp.a4_order_station_driver.feature.order.newOrderStation.NewOrderStationFragment;
import com.webapp.a4_order_station_driver.feature.order.newPublicOrder.NewPublicOrderFragment;
import com.webapp.a4_order_station_driver.feature.order.orderStationView.OrderStationViewFragment;
import com.webapp.a4_order_station_driver.feature.order.publicOrderView.PublicOrderViewFragment;
import com.webapp.a4_order_station_driver.models.Notification;
import com.webapp.a4_order_station_driver.models.NotificationList;
import com.webapp.a4_order_station_driver.models.OrderStation;
import com.webapp.a4_order_station_driver.models.PublicOrderObject;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.NavigateUtil;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationHolder> {

    private ArrayList<Notification> notifications;
    private BaseActivity baseActivity;
    private DialogView<NotificationList> dialogView;

    public NotificationsAdapter(ArrayList<Notification> notifications
            , BaseActivity baseActivity, DialogView<NotificationList> dialogView) {
        this.notifications = notifications;
        this.dialogView = dialogView;
        this.baseActivity = baseActivity;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationHolder(ItemNotificationBinding.inflate(LayoutInflater
                .from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
        holder.setData(notifications.get(position), baseActivity, dialogView);
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
        private DialogView<NotificationList> dialogView;

        public NotificationHolder(ItemNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            click();
        }

        private void click() {
            binding.llNotification.setOnClickListener(view -> openOrder());
        }

        private void setData(Notification data, BaseActivity baseActivity
                , DialogView<NotificationList> dialogView) {
            this.baseActivity = baseActivity;
            this.dialogView = dialogView;

            if (data.getData().getType() != null) {
                this.type = data.getData().getType();
                if (this.type.equals(AppContent.TYPE_ORDER_PUBLIC)) {
                    this.id = data.getData().getPublic_order_id();
                } else {
                    this.id = data.getData().getOrder_id();
                }
            }

            if (id == 0) {
                binding.tvCoName.setText(baseActivity.getString(R.string.admin_message));
            } else {
                binding.tvCoName.setText(String.valueOf(id));
            }
            binding.tvDatetime.setText((ToolUtil.getTime(data.getCreated_at())
                    + " " + ToolUtil.getDate(data.getCreated_at())));
            binding.tvMessage.setText(data.getData().getMsg());
        }

        public void openOrder() {
            if (id != 0) {
                if (!type.equals(AppContent.TYPE_ORDER_PUBLIC)) {
                    openOrderStation();
                } else {
                    openPublicOrder();
                }
            }
        }

        private void openOrderStation() {
            dialogView.showDialog("");
            new APIUtil<OrderStation>(baseActivity).getData(AppController
                            .getInstance().getApi().getOrderById(id)
                    , new RequestListener<OrderStation>() {
                        @Override
                        public void onSuccess(OrderStation orderStation, String msg) {
                            dialogView.hideDialog();
                            //MainActivity.setId(orderStation);
                            if (orderStation.getStatus().equals(AppContent.READY_STATUS)) {
                                new NavigateUtil().openOrder(baseActivity, orderStation, NewOrderStationFragment.page, true);
                                //baseActivity.navigate(6);
                            } else if (orderStation.getDriver_id() != null) {
                                if (orderStation.getDriver_id().equals(AppController.getInstance()
                                        .getAppSettingsPreferences().getLogin().getUser().getId() + "")) {
                                    new NavigateUtil().openOrder(baseActivity, orderStation, OrderStationViewFragment.page, true);
                                    //baseActivity.navigate(7);
                                } else {
                                    ToolUtil.showLongToast(baseActivity.getString(R.string.can_not_open), baseActivity);
                                }
                            } else {
                                ToolUtil.showLongToast(baseActivity.getString(R.string.can_not_open), baseActivity);
                            }
                        }

                        @Override
                        public void onError(String msg) {
                            ToolUtil.showLongToast(msg, baseActivity);
                            dialogView.hideDialog();
                        }

                        @Override
                        public void onFail(String msg) {
                            ToolUtil.showLongToast(msg, baseActivity);
                            dialogView.hideDialog();
                        }
                    });
        }

        private void openPublicOrder() {
            dialogView.showDialog("");

            new APIUtil<PublicOrderObject>(baseActivity)
                    .getData(AppController.getInstance().getApi().getPublicOrder(id)
                            , new RequestListener<PublicOrderObject>() {
                                @Override
                                public void onSuccess(PublicOrderObject publicOrderObject, String msg) {
                                    if (publicOrderObject.getPublicOrder().getStatus().equals(AppContent.PENDING_STATUS)
                                            || publicOrderObject.getPublicOrder().getDriver_id() == null) {

                                        new NavigateUtil().openOrder(baseActivity, publicOrderObject.getPublicOrder()
                                                , NewPublicOrderFragment.page, true);

                                    } else if (publicOrderObject.getPublicOrder().getDriver_id()
                                            .equals(String.valueOf(AppController.getInstance()
                                                    .getAppSettingsPreferences().getLogin().getUser().getId()))) {

                                        new NavigateUtil().openOrder(baseActivity, publicOrderObject.getPublicOrder()
                                                , PublicOrderViewFragment.page, true);
                                    } else {
                                        ToolUtil.showLongToast(baseActivity.getString(R.string.can_not_open), baseActivity);
                                    }
                                    dialogView.hideDialog();
                                }

                                @Override
                                public void onError(String msg) {
                                    ToolUtil.showLongToast(msg, baseActivity);
                                    dialogView.hideDialog();
                                }

                                @Override
                                public void onFail(String msg) {
                                    ToolUtil.showLongToast(msg, baseActivity);
                                    dialogView.hideDialog();
                                }
                            });
        }
    }
}

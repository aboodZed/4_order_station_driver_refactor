package com.webapp.a4_order_station_driver.feature.main.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.ItemNotificationBinding;
import com.webapp.a4_order_station_driver.feature.order.newOrderStation.NewOrderStationFragment;
import com.webapp.a4_order_station_driver.feature.order.newPublicOrder.NewPublicOrderFragment;
import com.webapp.a4_order_station_driver.feature.order.orderStationView.OrderStationViewFragment;
import com.webapp.a4_order_station_driver.feature.order.publicOrderView.PublicOrderViewFragment;
import com.webapp.a4_order_station_driver.models.Notification;
import com.webapp.a4_order_station_driver.models.OrderStation;
import com.webapp.a4_order_station_driver.models.PublicOrderObject;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.NavigateUtil;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationHolder> {

    private ArrayList<Notification> notifications;
    private BaseActivity baseActivity;
    private FragmentManager fragmentManager;

    public NotificationsAdapter(ArrayList<Notification> notifications
            , BaseActivity baseActivity, FragmentManager fragmentManager) {
        this.notifications = notifications;
        this.fragmentManager = fragmentManager;
        this.baseActivity = baseActivity;
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
        holder.setData(notifications.get(position), baseActivity, fragmentManager);
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
        private FragmentManager fragmentManager;

        public NotificationHolder(ItemNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            click();
        }

        private void click() {
            binding.llNotification.setOnClickListener(view -> openOrder());
        }

        private void setData(Notification data, BaseActivity baseActivity, FragmentManager fragmentManager) {
            this.baseActivity = baseActivity;
            this.fragmentManager = fragmentManager;

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
                binding.tvCoName.setText(id + "");
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
            WaitDialogFragment.newInstance().show(fragmentManager, "");
            new APIUtil<OrderStation>(baseActivity).getData(AppController
                            .getInstance().getApi().getOrderById(id)
                    , new RequestListener<OrderStation>() {
                        @Override
                        public void onSuccess(OrderStation orderStation, String msg) {
                            WaitDialogFragment.newInstance().dismiss();
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
                            WaitDialogFragment.newInstance().dismiss();
                        }

                        @Override
                        public void onFail(String msg) {
                            ToolUtil.showLongToast(msg, baseActivity);
                            WaitDialogFragment.newInstance().dismiss();
                        }
                    });
        }

        private void openPublicOrder() {
            WaitDialogFragment.newInstance().show(fragmentManager, "");

            new APIUtil<PublicOrderObject>(baseActivity)
                    .getData(AppController.getInstance().getApi().getPublicOrder(id)
                            , new RequestListener<PublicOrderObject>() {
                                @Override
                                public void onSuccess(PublicOrderObject publicOrderObject, String msg) {
                                    if (publicOrderObject.getPublicOrder().getStatus().equals(AppContent.PENDING_STATUS)
                                            || publicOrderObject.getPublicOrder().getDriver_id() == null) {

                                        new NavigateUtil().openOrder(baseActivity, publicOrderObject.getPublicOrder()
                                                , NewPublicOrderFragment.page, true);

                                                /*MainActivity.setPublicOrder(publicOrderObject.getPublicOrder());
                                                baseActivity.navigate(10);*/

                                    } else if (publicOrderObject.getPublicOrder().getDriver_id()
                                            .equals(String.valueOf(AppController.getInstance()
                                                    .getAppSettingsPreferences().getLogin().getUser().getId()))) {

                                        new NavigateUtil().openOrder(baseActivity, publicOrderObject.getPublicOrder()
                                                , PublicOrderViewFragment.page, true);
                                    } else {
                                        ToolUtil.showLongToast(baseActivity.getString(R.string.can_not_open), baseActivity);
                                    }

                                    WaitDialogFragment.newInstance().dismiss();
                                }

                                @Override
                                public void onError(String msg) {
                                    ToolUtil.showLongToast(msg, baseActivity);
                                    WaitDialogFragment.newInstance().dismiss();
                                }

                                @Override
                                public void onFail(String msg) {
                                    ToolUtil.showLongToast(msg, baseActivity);
                                    WaitDialogFragment.newInstance().dismiss();
                                }
                            });
        }
    }
}

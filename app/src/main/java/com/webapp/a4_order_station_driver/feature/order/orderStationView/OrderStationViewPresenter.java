package com.webapp.a4_order_station_driver.feature.order.orderStationView;

import com.webapp.a4_order_station_driver.feature.main.orders.OrdersFragment;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.models.Order;
import com.webapp.a4_order_station_driver.models.OrderStation;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;
import com.webapp.a4_order_station_driver.utils.location.tracking.OrderGPSTracking;

public class OrderStationViewPresenter {

    private BaseActivity baseActivity;
    private DialogView<OrderStation> dialogView;

    public OrderStationViewPresenter(BaseActivity baseActivity, DialogView<OrderStation> dialogView) {
        this.baseActivity = baseActivity;
        this.dialogView = dialogView;
    }

    public void getOrderData(Order order) {
        dialogView.showDialog("");
        new APIUtil<OrderStation>(baseActivity).getData(AppController.getInstance()
                .getApi().getOrderById(order.getId()), new RequestListener<OrderStation>() {
            @Override
            public void onSuccess(OrderStation orderStation, String msg) {
                dialogView.setData(orderStation);
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

    public void finishOrder(OrderStation orderStation) {
        dialogView.showDialog("");
        new APIUtil<Message>(baseActivity).getData(AppController.getInstance()
                .getApi().deliveryOrder(orderStation.getId()), new RequestListener<Message>() {
            @Override
            public void onSuccess(Message message, String msg) {
                OrderGPSTracking.newInstance(baseActivity).removeUpdates();
                dialogView.hideDialog();
                baseActivity.navigate(OrdersFragment.page);
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

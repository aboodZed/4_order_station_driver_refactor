package com.webapp.mohammad_al_loh.feature.order.orderStationView;

import com.webapp.mohammad_al_loh.feature.main.orders.OrdersFragment;
import com.webapp.mohammad_al_loh.models.Message;
import com.webapp.mohammad_al_loh.models.Order;
import com.webapp.mohammad_al_loh.models.OrderStation;
import com.webapp.mohammad_al_loh.utils.APIUtil;
import com.webapp.mohammad_al_loh.utils.AppController;
import com.webapp.mohammad_al_loh.utils.ToolUtil;
import com.webapp.mohammad_al_loh.utils.language.BaseActivity;
import com.webapp.mohammad_al_loh.utils.listeners.DialogView;
import com.webapp.mohammad_al_loh.utils.listeners.RequestListener;
import com.webapp.mohammad_al_loh.utils.location.tracking.OrderGPSTracking;

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

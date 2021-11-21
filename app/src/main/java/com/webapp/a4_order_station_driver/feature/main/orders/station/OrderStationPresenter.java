package com.webapp.a4_order_station_driver.feature.main.orders.station;

import android.app.Activity;

import com.webapp.a4_order_station_driver.models.OrderStationList;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

class OrderStationPresenter {

    private Activity baseActivity;
    private DialogView<OrderStationList> dialogView;

    public OrderStationPresenter(Activity baseActivity, DialogView<OrderStationList> dialogView) {
        this.baseActivity = baseActivity;
        this.dialogView = dialogView;
        getData();
    }


    private void getData() {
        dialogView.showDialog("");
        new APIUtil<OrderStationList>(baseActivity).getData(AppController.getInstance()
                .getApi().getOrdersStation(), new RequestListener<OrderStationList>() {
            @Override
            public void onSuccess(OrderStationList orderStationList, String msg) {
                dialogView.setData(orderStationList);
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

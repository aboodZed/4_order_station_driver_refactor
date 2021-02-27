package com.webapp.a4_order_station_driver.feature.main.orders.publicO;

import com.webapp.a4_order_station_driver.models.PublicOrderListObject;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

class OrderPublicPresenter {

    private BaseActivity baseActivity;
    private DialogView<PublicOrderListObject> dialogView;

    public OrderPublicPresenter(BaseActivity baseActivity, DialogView<PublicOrderListObject> dialogView) {
        this.baseActivity = baseActivity;
        this.dialogView = dialogView;
        getData("public/order/driver/ordersList");
    }

    protected void getData(String s) {
        dialogView.showDialog("");
        new APIUtil<PublicOrderListObject>(baseActivity).getData(AppController.getInstance()
                .getApi().getPublicOrders(s), new RequestListener<PublicOrderListObject>() {
            @Override
            public void onSuccess(PublicOrderListObject publicOrderListObject, String msg) {
                dialogView.setData(publicOrderListObject);
                dialogView.hideDialog();
            }

            @Override
            public void onError(String msg) {
                dialogView.hideDialog();
                ToolUtil.showLongToast(msg, baseActivity);
            }

            @Override
            public void onFail(String msg) {
                dialogView.hideDialog();
                ToolUtil.showLongToast(msg, baseActivity);
            }
        });
    }
}

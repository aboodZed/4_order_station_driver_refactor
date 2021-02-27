package com.webapp.a4_order_station_driver.feature.order.publicOrderView;

import com.webapp.a4_order_station_driver.models.Order;
import com.webapp.a4_order_station_driver.models.PublicOrder;
import com.webapp.a4_order_station_driver.models.PublicOrderObject;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

public class PublicOrderViewPresenter {

    private BaseActivity baseActivity;
    private DialogView<PublicOrderObject> dialogView;

    public PublicOrderViewPresenter(BaseActivity baseActivity, DialogView<PublicOrderObject> dialogView) {
        this.baseActivity = baseActivity;
        this.dialogView = dialogView;
    }

    public void getData(Order order){
        dialogView.showDialog("");
        new APIUtil<PublicOrderObject>(baseActivity).getData(AppController.getInstance()
                .getApi().getPublicOrder(order.getId()), new RequestListener<PublicOrderObject>() {
            @Override
            public void onSuccess(PublicOrderObject publicOrderObject, String msg) {
                dialogView.setData(publicOrderObject);
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

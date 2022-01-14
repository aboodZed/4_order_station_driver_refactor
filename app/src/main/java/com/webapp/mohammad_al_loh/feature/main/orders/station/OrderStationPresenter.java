package com.webapp.mohammad_al_loh.feature.main.orders.station;

import com.webapp.mohammad_al_loh.models.OrderStationList;
import com.webapp.mohammad_al_loh.utils.APIUtil;
import com.webapp.mohammad_al_loh.utils.AppController;
import com.webapp.mohammad_al_loh.utils.ToolUtil;
import com.webapp.mohammad_al_loh.utils.language.BaseActivity;
import com.webapp.mohammad_al_loh.utils.listeners.DialogView;
import com.webapp.mohammad_al_loh.utils.listeners.RequestListener;

class OrderStationPresenter {

    private BaseActivity baseActivity;
    private DialogView<OrderStationList> dialogView;

    public OrderStationPresenter(BaseActivity baseActivity, DialogView<OrderStationList> dialogView) {
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

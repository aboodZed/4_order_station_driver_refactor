package com.webapp.mohammad_al_loh.feature.main.orders.publicO;

import com.webapp.mohammad_al_loh.models.PublicOrderListObject;
import com.webapp.mohammad_al_loh.utils.APIUtil;
import com.webapp.mohammad_al_loh.utils.AppController;
import com.webapp.mohammad_al_loh.utils.ToolUtil;
import com.webapp.mohammad_al_loh.utils.language.BaseActivity;
import com.webapp.mohammad_al_loh.utils.listeners.DialogView;
import com.webapp.mohammad_al_loh.utils.listeners.RequestListener;

class OrderPublicPresenter {

    private BaseActivity baseActivity;
    private DialogView<PublicOrderListObject> dialogView;

    public OrderPublicPresenter(BaseActivity baseActivity, DialogView<PublicOrderListObject> dialogView) {
        this.baseActivity = baseActivity;
        this.dialogView = dialogView;
        getData("driver-orders-list");
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

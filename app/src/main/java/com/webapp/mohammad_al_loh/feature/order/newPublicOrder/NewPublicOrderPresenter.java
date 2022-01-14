package com.webapp.mohammad_al_loh.feature.order.newPublicOrder;

import com.webapp.mohammad_al_loh.R;
import com.webapp.mohammad_al_loh.feature.main.orders.OrdersFragment;
import com.webapp.mohammad_al_loh.feature.main.orders.publicO.OrderPublicFragment;
import com.webapp.mohammad_al_loh.feature.main.wallets.publicO.PublicWalletFragment;
import com.webapp.mohammad_al_loh.feature.main.wallets.WalletFragment;
import com.webapp.mohammad_al_loh.models.Message;
import com.webapp.mohammad_al_loh.models.PublicOrder;
import com.webapp.mohammad_al_loh.utils.APIUtil;
import com.webapp.mohammad_al_loh.utils.AppContent;
import com.webapp.mohammad_al_loh.utils.AppController;
import com.webapp.mohammad_al_loh.utils.ToolUtil;
import com.webapp.mohammad_al_loh.utils.language.BaseActivity;
import com.webapp.mohammad_al_loh.utils.listeners.DialogView;
import com.webapp.mohammad_al_loh.utils.listeners.RequestListener;
import com.webapp.mohammad_al_loh.utils.location.tracking.OrderGPSTracking;

class NewPublicOrderPresenter {

    private BaseActivity baseActivity;
    private DialogView<Message> dialogView;

    public NewPublicOrderPresenter(BaseActivity baseActivity, DialogView<Message> dialogView) {
        this.baseActivity = baseActivity;
        this.dialogView = dialogView;
    }

    public void accept(PublicOrder publicOrder) {
        dialogView.showDialog("");
        new APIUtil<Message>(baseActivity).getData(AppController.getInstance()
                .getApi().pickupPublicOrder(publicOrder.getId()), new RequestListener<Message>() {
            @Override
            public void onSuccess(Message message, String msg) {
                dialogView.hideDialog();
                publicOrder.setType(AppContent.TYPE_ORDER_PUBLIC);
                AppController.getInstance().getAppSettingsPreferences()
                        .setTrackingOrder(publicOrder, AppContent.TYPE_ORDER_PUBLIC);
                OrderGPSTracking.newInstance(baseActivity).startGPSTracking();

                OrdersFragment.viewPagerPage = OrderPublicFragment.viewPagerPage;
                WalletFragment.viewPagerPage = PublicWalletFragment.viewPagerPage;

                ToolUtil.showLongToast(baseActivity.getString(R.string.closeApp), baseActivity);
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

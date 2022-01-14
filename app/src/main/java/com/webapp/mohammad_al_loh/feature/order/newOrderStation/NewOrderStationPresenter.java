package com.webapp.mohammad_al_loh.feature.order.newOrderStation;

import com.webapp.mohammad_al_loh.R;
import com.webapp.mohammad_al_loh.feature.main.orders.OrdersFragment;
import com.webapp.mohammad_al_loh.feature.main.orders.station.OrderStationFragment;
import com.webapp.mohammad_al_loh.feature.main.wallets.station.OrderStationWalletFragment;
import com.webapp.mohammad_al_loh.feature.main.wallets.WalletFragment;
import com.webapp.mohammad_al_loh.models.Message;
import com.webapp.mohammad_al_loh.models.OrderStation;
import com.webapp.mohammad_al_loh.utils.APIUtil;
import com.webapp.mohammad_al_loh.utils.AppContent;
import com.webapp.mohammad_al_loh.utils.AppController;
import com.webapp.mohammad_al_loh.utils.ToolUtil;
import com.webapp.mohammad_al_loh.utils.language.BaseActivity;
import com.webapp.mohammad_al_loh.utils.listeners.DialogView;
import com.webapp.mohammad_al_loh.utils.listeners.RequestListener;
import com.webapp.mohammad_al_loh.utils.location.tracking.OrderGPSTracking;

public class NewOrderStationPresenter {

    private BaseActivity baseActivity;
    private DialogView<Message> dialogView;

    public NewOrderStationPresenter(BaseActivity baseActivity, DialogView<Message> dialogView) {
        this.baseActivity = baseActivity;
        this.dialogView = dialogView;
    }

    public void accept(OrderStation orderStation) {
        dialogView.showDialog("");
        new APIUtil<Message>(baseActivity).getData(AppController.getInstance()
                .getApi().pickupOrder(orderStation.getId()), new RequestListener<Message>() {
            @Override
            public void onSuccess(Message message, String msg) {
                dialogView.hideDialog();

                AppController.getInstance().getAppSettingsPreferences()
                        .setTrackingOrder(orderStation, AppContent.TYPE_ORDER_4STATION);
                OrderGPSTracking.newInstance(baseActivity).startGPSTracking();

                ToolUtil.showLongToast(baseActivity.getString(R.string.closeApp), baseActivity);
                OrdersFragment.viewPagerPage = OrderStationFragment.viewPagerPage;
                WalletFragment.viewPagerPage = OrderStationWalletFragment.viewPagerPage;
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

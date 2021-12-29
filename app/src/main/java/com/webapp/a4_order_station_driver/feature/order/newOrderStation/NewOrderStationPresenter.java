package com.webapp.a4_order_station_driver.feature.order.newOrderStation;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.feature.main.orders.OrdersFragment;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.models.OrderStation;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;
import com.webapp.a4_order_station_driver.utils.location.tracking.OrderGPSTracking;

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
                //OrdersFragment.viewPagerPage = OrderStationFragment.viewPagerPage;
                //WalletFragment.viewPagerPage = OrderStationWalletFragment.viewPagerPage;
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

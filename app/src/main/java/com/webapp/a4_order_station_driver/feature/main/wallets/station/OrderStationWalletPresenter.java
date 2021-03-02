package com.webapp.a4_order_station_driver.feature.main.wallets.station;

import android.app.Activity;

import com.webapp.a4_order_station_driver.models.StationWallet;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

public class OrderStationWalletPresenter {

    private Activity activity;
    private DialogView<StationWallet> dialogView;

    public OrderStationWalletPresenter(Activity activity, DialogView<StationWallet> dialogView) {
        this.activity = activity;
        this.dialogView = dialogView;
    }

    public void getData() {
        new APIUtil<StationWallet>(activity).getData(AppController.getInstance()
                .getApi().getWalletDetails(), new RequestListener<StationWallet>() {
            @Override
            public void onSuccess(StationWallet stationWallet, String msg) {
                dialogView.setData(stationWallet);
                dialogView.hideDialog();
            }

            @Override
            public void onError(String msg) {
                dialogView.hideDialog();
                ToolUtil.showLongToast(msg, activity);
            }

            @Override
            public void onFail(String msg) {
                dialogView.hideDialog();
                ToolUtil.showLongToast(msg, activity);
            }
        });
    }
}

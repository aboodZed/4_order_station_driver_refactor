package com.webapp.a4_order_station_driver.feature.main.wallets;

import com.webapp.a4_order_station_driver.models.StationWallet;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

public class WalletPresenter {

    private BaseActivity baseActivity;
    private DialogView<StationWallet> dialogView;

    public WalletPresenter(BaseActivity baseActivity, DialogView<StationWallet> dialogView) {
        this.baseActivity = baseActivity;
        this.dialogView = dialogView;
        getData();
    }

    private void getData() {
        dialogView.showDialog("");
        new APIUtil<StationWallet>(baseActivity).getData(AppController.getInstance()
                .getApi().getWalletDetails(), new RequestListener<StationWallet>() {
            @Override
            public void onSuccess(StationWallet stationWallet, String msg) {
                dialogView.hideDialog();
                dialogView.setData(stationWallet);
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

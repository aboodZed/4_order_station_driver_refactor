package com.webapp.mohammad_al_loh.feature.main.wallets.station;

import android.app.Activity;

import com.webapp.mohammad_al_loh.models.StationWallet;
import com.webapp.mohammad_al_loh.utils.APIUtil;
import com.webapp.mohammad_al_loh.utils.AppController;
import com.webapp.mohammad_al_loh.utils.ToolUtil;
import com.webapp.mohammad_al_loh.utils.listeners.DialogView;
import com.webapp.mohammad_al_loh.utils.listeners.RequestListener;

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

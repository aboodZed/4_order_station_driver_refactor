package com.webapp.a4_order_station_driver.feature.main.wallets.publicO;

import android.app.Activity;
import android.util.Log;

import com.webapp.a4_order_station_driver.models.PublicWallet;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

public class PublicWalletPresenter {

    private Activity activity;
    private DialogView<PublicWallet> dialogView;

    public PublicWalletPresenter(Activity activity, DialogView<PublicWallet> dialogView) {
        this.activity = activity;
        this.dialogView = dialogView;
    }

    public void getData() {
        dialogView.showDialog("");
        new APIUtil<PublicWallet>(activity).getData(AppController.getInstance()
                .getApi().getPublicWallet(), new RequestListener<PublicWallet>() {
            @Override
            public void onSuccess(PublicWallet publicWallet, String msg) {
                dialogView.hideDialog();
                dialogView.setData(publicWallet);
            }

            @Override
            public void onError(String msg) {
                Log.e(getClass().getName() + " : response Error", msg);
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

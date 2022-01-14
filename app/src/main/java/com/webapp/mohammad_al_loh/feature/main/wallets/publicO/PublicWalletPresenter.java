package com.webapp.mohammad_al_loh.feature.main.wallets.publicO;

import android.app.Activity;
import android.util.Log;

import com.webapp.mohammad_al_loh.models.PublicWallet;
import com.webapp.mohammad_al_loh.utils.APIUtil;
import com.webapp.mohammad_al_loh.utils.AppController;
import com.webapp.mohammad_al_loh.utils.ToolUtil;
import com.webapp.mohammad_al_loh.utils.listeners.DialogView;
import com.webapp.mohammad_al_loh.utils.listeners.RequestListener;

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

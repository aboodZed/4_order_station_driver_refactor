package com.webapp.mohammad_al_loh.feature.main.rate;

import android.app.Activity;

import com.webapp.mohammad_al_loh.models.RatingObject;
import com.webapp.mohammad_al_loh.utils.APIUtil;
import com.webapp.mohammad_al_loh.utils.AppController;
import com.webapp.mohammad_al_loh.utils.ToolUtil;
import com.webapp.mohammad_al_loh.utils.listeners.DialogView;
import com.webapp.mohammad_al_loh.utils.listeners.RequestListener;

class RatingPresenter {

    private Activity activity;
    private DialogView<RatingObject> dialogView;

    public RatingPresenter(Activity activity, DialogView<RatingObject> dialogView) {
        this.activity = activity;
        this.dialogView = dialogView;
    }

    public void getRatings(String url) {
        dialogView.showDialog("");
        new APIUtil<RatingObject>(activity).getData(AppController.getInstance()
                .getApi().getRating(url), new RequestListener<RatingObject>() {
            @Override
            public void onSuccess(RatingObject ratings, String msg) {
                dialogView.hideDialog();
                dialogView.setData(ratings);
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

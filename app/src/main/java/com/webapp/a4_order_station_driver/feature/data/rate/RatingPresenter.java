package com.webapp.a4_order_station_driver.feature.data.rate;

import android.app.Activity;

import com.webapp.a4_order_station_driver.models.Rating;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

class RatingPresenter {

    private Activity activity;
    private DialogView<Rating> dialogView;

    public RatingPresenter(Activity activity, DialogView<Rating> dialogView) {
        this.activity = activity;
        this.dialogView = dialogView;
    }

    public void getRatings(String url) {
        dialogView.showDialog("");
        new APIUtil<Rating>(activity).getData(AppController.getInstance()
                .getApi().getRating(url), new RequestListener<Rating>() {
            @Override
            public void onSuccess(Rating testRating, String msg) {
                dialogView.hideDialog();
                dialogView.setData(testRating);
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

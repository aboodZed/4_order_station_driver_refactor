package com.webapp.mohammad_al_loh.feature.splash;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.webapp.mohammad_al_loh.feature.login.LoginActivity;
import com.webapp.mohammad_al_loh.feature.main.MainActivity;
import com.webapp.mohammad_al_loh.models.Login;
import com.webapp.mohammad_al_loh.models.Order;
import com.webapp.mohammad_al_loh.models.OrderStation;
import com.webapp.mohammad_al_loh.models.PublicOrderObject;
import com.webapp.mohammad_al_loh.models.ResultUser;
import com.webapp.mohammad_al_loh.utils.APIUtil;
import com.webapp.mohammad_al_loh.utils.AppContent;
import com.webapp.mohammad_al_loh.utils.AppController;
import com.webapp.mohammad_al_loh.utils.ToolUtil;
import com.webapp.mohammad_al_loh.utils.language.AppLanguageUtil;
import com.webapp.mohammad_al_loh.utils.language.BaseActivity;
import com.webapp.mohammad_al_loh.utils.listeners.RequestListener;
import com.webapp.mohammad_al_loh.utils.location.tracking.OrderGPSTracking;

class SplashPresenter {

    private BaseActivity baseActivity;

    public SplashPresenter(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
        //functions
        setLanguage();
        checkOrderProcess();
        checkUserLogin();
    }

    private void setLanguage() {
        if (AppController.getInstance().getAppSettingsPreferences().isFirstRun()) {
            AppLanguageUtil.getInstance().setAppFirstRunLng();
        } else {
            AppLanguageUtil.getInstance().setAppLanguage(baseActivity
                    , AppController.getInstance().getAppSettingsPreferences().getAppLanguage());
        }
    }

    private void checkOrderProcess() {
        Order order = AppController.getInstance().getAppSettingsPreferences().getTrackingOrder();

        if (AppController.getInstance().getAppSettingsPreferences().getTrackingOrder() != null) {
            if (order.getType().equals(AppContent.TYPE_ORDER_4STATION)) {
                new APIUtil<OrderStation>(baseActivity).getData(AppController.getInstance().getApi()
                        .getOrderById(order.getId()), new RequestListener<OrderStation>() {
                    @Override
                    public void onSuccess(OrderStation orderStation, String msg) {
                        if (orderStation.getStatus().equals(AppContent.DELIVERED_STATUS)) {
                            OrderGPSTracking.newInstance(baseActivity).removeUpdates();
                        } else {
                            OrderGPSTracking.newInstance(baseActivity).startGPSTracking();
                        }
                    }

                    @Override
                    public void onError(String msg) {

                    }

                    @Override
                    public void onFail(String msg) {

                    }
                });
            } else {
                new APIUtil<PublicOrderObject>(baseActivity).getData(AppController.getInstance().getApi()
                        .getPublicOrder(order.getId()), new RequestListener<PublicOrderObject>() {
                    @Override
                    public void onSuccess(PublicOrderObject publicOrderObject, String msg) {
                        if (publicOrderObject.getPublicOrder().getStatus().equals(AppContent.DELIVERED_STATUS)) {
                            OrderGPSTracking.newInstance(baseActivity).removeUpdates();
                        } else {
                            OrderGPSTracking.newInstance(baseActivity).startGPSTracking();
                        }
                    }

                    @Override
                    public void onError(String msg) {

                    }

                    @Override
                    public void onFail(String msg) {

                    }
                });
            }
        }
    }

    private void checkUserLogin() {
        if (AppController.getInstance().getAppSettingsPreferences().getIsLogin()) {

            new APIUtil<ResultUser>(baseActivity).getData(AppController.getInstance().getApi().getUserData()
                    , new RequestListener<ResultUser>() {
                        @Override
                        public void onSuccess(ResultUser result, String msg) {
                            if (result.isSuccess()){
                                //update user
                                Login login = AppController.getInstance().getAppSettingsPreferences().getLogin();
                                login.setUser(result.getUser());
                                AppController.getInstance().getAppSettingsPreferences().setLogin(login);
                                //print user token
                                Log.e(getClass().getName() + " : userData", result.getUser().toString());

                                Log.e("usertoken", AppController.getInstance()
                                        .getAppSettingsPreferences().getLogin().getAccess_token());
                                //navigate
                                FirebaseFirestore.getInstance().collection(AppContent.FIREBASE_PUBLIC_TRACKING_INSTANCE)
                                        .document(AppContent.FIREBASE_DATA).addSnapshotListener((value, error) -> {
                                            String s = value.getString(AppContent.FIREBASE_STATUS);
                                        });
                                baseActivity.navigate(MainActivity.page);
                            }else {
                                ToolUtil.showLongToast(msg, baseActivity);
                                baseActivity.navigate(LoginActivity.page);
                            }
                        }

                        @Override
                        public void onError(String msg) {
                            ToolUtil.showLongToast(msg, baseActivity);
                            baseActivity.navigate(LoginActivity.page);
                        }

                        @Override
                        public void onFail(String msg) {
                            ToolUtil.showLongToast(msg, baseActivity);
                            baseActivity.navigate(LoginActivity.page);
                        }
                    });

            /*
            public void onSuccess(User user, String msg) {
                            //update user
                            Login login = AppController.getInstance().getAppSettingsPreferences().getLogin();
                            login.setUser(user);
                            AppController.getInstance().getAppSettingsPreferences().setLogin(login);
                            //print user token
                            Log.e(getClass().getName() + " : userData", user.toString());

                            Log.e("usertoken", AppController.getInstance()
                                    .getAppSettingsPreferences().getLogin().getAccess_token());
                            //navigate
                            FirebaseFirestore.getInstance().collection(AppContent.FIREBASE_PUBLIC_TRACKING_INSTANCE)
                                    .document(AppContent.FIREBASE_DATA).addSnapshotListener((value, error) -> {
                                        String s = value.getString(AppContent.FIREBASE_STATUS);
                                    });
                            baseActivity.navigate(MainActivity.page);
                        }

                        @Override
                        public void onError(String msg) {
                            ToolUtil.showLongToast(msg, baseActivity);
                            baseActivity.navigate(LoginActivity.page);
                        }

                        @Override
                        public void onFail(String msg) {
                            ToolUtil.showLongToast(msg, baseActivity);
                            baseActivity.navigate(LoginActivity.page);
                        }
             */

        } else {
            baseActivity.navigate(LoginActivity.page);
        }
    }

}

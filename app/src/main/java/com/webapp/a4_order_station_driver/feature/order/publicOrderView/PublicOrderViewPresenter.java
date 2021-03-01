package com.webapp.a4_order_station_driver.feature.order.publicOrderView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.models.Order;
import com.webapp.a4_order_station_driver.models.PublicOrder;
import com.webapp.a4_order_station_driver.models.PublicOrderObject;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.Photo.PhotoTakerManager;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

import static android.app.Activity.RESULT_OK;

public class PublicOrderViewPresenter {

    private BaseActivity baseActivity;
    private DialogView<PublicOrderObject> dialogView;
    private PhotoTakerManager photoTakerManager;

    public PublicOrderViewPresenter(BaseActivity baseActivity, DialogView<PublicOrderObject> dialogView
            , PhotoTakerManager photoTakerManager) {
        this.baseActivity = baseActivity;
        this.dialogView = dialogView;
        this.photoTakerManager = photoTakerManager;
    }

    public void getData(Order order) {
        dialogView.showDialog("");
        new APIUtil<PublicOrderObject>(baseActivity).getData(AppController.getInstance()
                .getApi().getPublicOrder(order.getId()), new RequestListener<PublicOrderObject>() {
            @Override
            public void onSuccess(PublicOrderObject publicOrderObject, String msg) {
                dialogView.setData(publicOrderObject);
                dialogView.hideDialog();
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AppContent.REQUEST_STUDIO:
                    photoTakerManager.processGalleryPhoto(baseActivity, data);
                    break;
                case AppContent.REQUEST_CAMERA:
                    photoTakerManager.processCameraPhoto(baseActivity);
                    break;
            }
            /*if (requestCode == REQUEST_STUDIO) {
                try {
                    //code
                    if (data != null) {
                        filePath = data.getData();
                        uploadImage();
                    }
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                    WaitDialogFragment.newInstance().dismiss();
                }
            } else if (requestCode == REQUEST_CAMERA) {
                try {
                    //code
                    filePath = photoTakerManager.getCurrentPhotoUri();
                    photoTakerManager.processTakenPhoto(getActivity());
                    //code
                } catch (OutOfMemoryError error) {
                    error.printStackTrace();
                    Toast.makeText(getActivity(), R.string.big_image, Toast.LENGTH_SHORT).show();
                    WaitDialogFragment.newInstance().dismiss();
                }
            }*/
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case AppContent.REQUEST_PERMISSIONS_R_W_STORAGE_CAMERA:
                    ToolUtil.showLongToast(baseActivity.getString(R.string.permission_garnted), baseActivity);
                    break;
            }
        } else {
            ToolUtil.showLongToast(baseActivity.getString(R.string.permission_denial), baseActivity);
        }
    }
}

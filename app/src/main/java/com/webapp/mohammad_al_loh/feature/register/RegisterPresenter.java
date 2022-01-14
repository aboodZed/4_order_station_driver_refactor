package com.webapp.mohammad_al_loh.feature.register;

import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.fragment.app.Fragment;

import com.webapp.mohammad_al_loh.R;
import com.webapp.mohammad_al_loh.feature.register.one.RegisterStepOneFragment;
import com.webapp.mohammad_al_loh.feature.register.two.RegisterStepTwoFragment;
import com.webapp.mohammad_al_loh.utils.AppContent;
import com.webapp.mohammad_al_loh.utils.ToolUtil;
import com.webapp.mohammad_al_loh.utils.language.BaseActivity;

class RegisterPresenter {

    private BaseActivity baseActivity;

    public RegisterPresenter(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final Fragment fragmentInFrame = baseActivity.getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        if (fragmentInFrame instanceof RegisterStepOneFragment) {
            fragmentInFrame.onActivityResult(requestCode, resultCode, data);
        }
        if (fragmentInFrame instanceof RegisterStepTwoFragment) {
            fragmentInFrame.onActivityResult(requestCode, resultCode, data);
        }
    }
}

package com.webapp.a4_order_station_driver.utils.language;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    public View rootView;

    protected void setRootView(View rootView) {
        this.rootView = rootView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(rootView);
        super.onCreate(savedInstanceState);
    }

    public abstract void navigate(int page);
}

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
        super.onCreate(savedInstanceState);
        setContentView(rootView);
    }

    protected void setOnClickListeners(View[] views, View.OnClickListener onClickListener) {
        for (View view : views) {
            view.setOnClickListener(onClickListener);
        }
    }

    public abstract void navigate(int page);
}

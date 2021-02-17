package com.webapp.a4_order_station_driver.feature.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.feature.home.adapter.NotificationsAdapter;
import com.webapp.a4_order_station_driver.models.Arrays;
import com.webapp.a4_order_station_driver.models.Notification;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.view.NavigationView;
import com.wang.avi.AVLoadingIndicatorView;
import com.webapp.a4_order_station_driver.utils.view.Tracking;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment {

    @BindView(R.id.rv_notification) RecyclerView rvNotification;
    @BindView(R.id.avi) AVLoadingIndicatorView avi;

    private NotificationsAdapter notificationsAdapter;
    private static NavigationView view;
    private static Tracking tracking;

    public static NotificationFragment newInstance(NavigationView navigationView, Tracking t) {
        NotificationFragment fragment = new NotificationFragment();
        view = navigationView;
        tracking = t;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    private void setData() {
        if (ToolUtils.checkTheInternet()) {
            AppController.getInstance().getApi().getNotifications().enqueue(new Callback<Arrays>() {
                @Override
                public void onResponse(Call<Arrays> call, Response<Arrays> response) {
                    if (response.isSuccessful()) {
                        initRecycleView(response.body().getNotifications());
                        avi.setVisibility(View.GONE);
                    } else {
                        ToolUtils.showError(getActivity(), response.errorBody());
                        avi.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<Arrays> call, Throwable t) {
                    t.printStackTrace();
                    avi.setVisibility(View.GONE);
                }
            });
        } else {
            ToolUtils.showLongToast(getString(R.string.no_connection), getActivity());
        }
    }

    public void initRecycleView(ArrayList<Notification> notifications) {
        notificationsAdapter = new NotificationsAdapter(notifications, view, getActivity(), getFragmentManager(), tracking);
        rvNotification.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNotification.setItemAnimator(new DefaultItemAnimator());
        rvNotification.setAdapter(notificationsAdapter);
    }
}

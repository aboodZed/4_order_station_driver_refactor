package com.webapp.a4_order_station_driver.feature.main.natification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.FragmentNotificationBinding;
import com.webapp.a4_order_station_driver.feature.main.adapter.NotificationsAdapter;
import com.webapp.a4_order_station_driver.models.Arrays;
import com.webapp.a4_order_station_driver.models.Notification;
import com.webapp.a4_order_station_driver.utils.APIUtils;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;
import com.webapp.a4_order_station_driver.utils.view.Tracking;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment {

    public static final int page = 208;

    private FragmentNotificationBinding binding;

    private NotificationsAdapter notificationsAdapter;
    private BaseActivity baseActivity;
    private Tracking tracking;

    public NotificationFragment(BaseActivity baseActivity, Tracking tracking) {
        this.baseActivity = baseActivity;
        this.tracking = tracking;
    }

    public static NotificationFragment newInstance(BaseActivity baseActivity, Tracking tracking) {
        NotificationFragment fragment = new NotificationFragment(baseActivity, tracking);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View v = inflater.inflate(R.layout.fragment_notification, container, false);
        binding = FragmentNotificationBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    private void setData() {
        new APIUtils<Arrays>(getActivity()).getData(AppController.getInstance()
                .getApi().getNotifications(), new RequestListener<Arrays>() {
            @Override
            public void onSuccess(Arrays arrays, String msg) {
                initRecycleView(arrays.getNotifications());
                binding.avi.setVisibility(View.GONE);
            }

            @Override
            public void onError(String msg) {
                ToolUtils.showLongToast(msg, getActivity());
                binding.avi.setVisibility(View.GONE);
            }

            @Override
            public void onFail(String msg) {
                ToolUtils.showLongToast(msg, getActivity());
                binding.avi.setVisibility(View.GONE);
            }
        });
    }

    public void initRecycleView(ArrayList<Notification> notifications) {
        notificationsAdapter = new NotificationsAdapter(notifications, baseActivity
                , getActivity(), getChildFragmentManager(), tracking);
        binding.rvNotification.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvNotification.setItemAnimator(new DefaultItemAnimator());
        binding.rvNotification.setAdapter(notificationsAdapter);
    }
}

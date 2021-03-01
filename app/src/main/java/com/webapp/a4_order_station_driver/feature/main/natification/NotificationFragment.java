package com.webapp.a4_order_station_driver.feature.main.natification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.webapp.a4_order_station_driver.databinding.FragmentNotificationBinding;
import com.webapp.a4_order_station_driver.feature.main.adapter.NotificationsAdapter;
import com.webapp.a4_order_station_driver.models.NotificationList;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.language.BaseActivity;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;

public class NotificationFragment extends Fragment implements DialogView<NotificationList> {

    public static final int page = 208;

    private FragmentNotificationBinding binding;

    private NotificationsAdapter notificationsAdapter;
    private BaseActivity baseActivity;

    public NotificationFragment(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    public static NotificationFragment newInstance(BaseActivity baseActivity) {
        NotificationFragment fragment = new NotificationFragment(baseActivity);
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
        new NotificationPresenter(baseActivity, this);
        return binding.getRoot();
    }

    @Override
    public void setData(NotificationList notifications) {
        notificationsAdapter = new NotificationsAdapter(notifications.getNotifications(), baseActivity,this);
        binding.rvNotification.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvNotification.setItemAnimator(new DefaultItemAnimator());
        binding.rvNotification.setAdapter(notificationsAdapter);
    }

    @Override
    public void showDialog(String s) {
        WaitDialogFragment.newInstance().show(getChildFragmentManager(),"");
    }

    @Override
    public void hideDialog() {
        WaitDialogFragment.newInstance().dismiss();
    }
}

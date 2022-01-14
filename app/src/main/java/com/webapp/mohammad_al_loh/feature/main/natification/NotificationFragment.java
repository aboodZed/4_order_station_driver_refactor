package com.webapp.mohammad_al_loh.feature.main.natification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.webapp.mohammad_al_loh.databinding.FragmentNotificationBinding;
import com.webapp.mohammad_al_loh.feature.main.adapter.NotificationsAdapter;
import com.webapp.mohammad_al_loh.models.NotificationList;
import com.webapp.mohammad_al_loh.utils.dialogs.WaitDialogFragment;
import com.webapp.mohammad_al_loh.utils.language.BaseActivity;
import com.webapp.mohammad_al_loh.utils.listeners.DialogView;

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

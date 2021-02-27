package com.webapp.a4_order_station_driver.utils.dialogs;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.FragmentSendMessageBinding;
import com.webapp.a4_order_station_driver.models.Message;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

import java.util.HashMap;

public class SendMessageDialog extends DialogFragment {

    private FragmentSendMessageBinding binding;

    public static SendMessageDialog newInstance() {
        SendMessageDialog fragment = new SendMessageDialog();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // View view = inflater.inflate(R.layout.fragment_send_message, container, false);
        binding = FragmentSendMessageBinding.inflate(getLayoutInflater());
        click();
        return binding.getRoot();
    }

    private void click() {
        binding.btnSend.setOnClickListener(view -> sendMessage());
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.transparent);
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        setCancelable(false);
        super.onResume();
        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                SendMessageDialog.this.dismiss();
                return true;
            } else return false;
        });
    }

    public void sendMessage() {
        String subject = binding.etEnterSubject.getText().toString().trim();
        String message = binding.etEnterMessage.getText().toString().trim();
        HashMap<String, String> map = new HashMap<>();
        map.put("name", AppController.getInstance().getAppSettingsPreferences().getLogin().getUser().getName());
        map.put("email", AppController.getInstance().getAppSettingsPreferences().getLogin().getUser().getEmail());
        map.put("mobile", AppController.getInstance().getAppSettingsPreferences().getLogin().getUser().getMobile());
        map.put("country_id", AppController.getInstance().getAppSettingsPreferences().getCountry().getId() + "");

        if (TextUtils.isEmpty(subject)) {
            binding.etEnterSubject.setError(getString(R.string.empty_error));
            return;
        } else {
            map.put("subject", subject);
        }
        if (TextUtils.isEmpty(message)) {
            binding.etEnterMessage.setError(getString(R.string.empty_error));
            return;
        } else {
            map.put("message", message);
        }
        send(map);
    }

    private void send(HashMap<String, String> map) {
        WaitDialogFragment.newInstance().show(getChildFragmentManager(), "");
        new APIUtil<Message>(getActivity()).getData(AppController.getInstance()
                .getApi().sendMessage(map), new RequestListener<Message>() {
            @Override
            public void onSuccess(Message message, String msg) {
                ToolUtil.showLongToast(message.getMassage(), getActivity());
                WaitDialogFragment.newInstance().dismiss();
                dismiss();
            }

            @Override
            public void onError(String msg) {
                ToolUtil.showLongToast(msg, getActivity());
                WaitDialogFragment.newInstance().dismiss();
                dismiss();
            }

            @Override
            public void onFail(String msg) {
                ToolUtil.showLongToast(msg, getActivity());
                WaitDialogFragment.newInstance().dismiss();
                dismiss();
            }
        });
    }
}

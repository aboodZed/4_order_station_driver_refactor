package com.webapp.a4_order_station_driver.feature.order.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.FragmentChatBinding;
import com.webapp.a4_order_station_driver.models.ChatMessage;
import com.webapp.a4_order_station_driver.models.Order;
import com.webapp.a4_order_station_driver.models.OrderStation;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppContent;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.NotificationUtil;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.feature.order.adapter.ChatAdapter;
import com.webapp.a4_order_station_driver.utils.dialogs.WaitDialogFragment;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

import java.util.ArrayList;

public class ChatFragment extends Fragment {

    public final static int page = 505;

    public static boolean isOpenChat;

    private FragmentChatBinding binding;

    private DatabaseReference db;
    private ArrayList<ChatMessage> messageArrayList;
    private OrderStation orderStation;
    private ChatAdapter chatAdapter;

    public static ChatFragment newInstance(Order order) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putSerializable(AppContent.ORDER_OBJECT, order);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View v = inflater.inflate(R.layout.fragment_chat, container, false);
        binding = FragmentChatBinding.inflate(getLayoutInflater());
        getOrderData();
        click();
        return binding.getRoot();
    }

   /* @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        setCancelable(false);
        super.onResume();
        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                ChatFragment.this.dismiss();
                return true;
            } else return false;
        });
    }*/

    @Override
    public void onStart() {
        super.onStart();
        isOpenChat = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isOpenChat = false;
    }

    //functions

    private void click() {
        // binding.ivBack.setOnClickListener(view -> dismiss());
        binding.ivUploadMessage.setOnClickListener(view -> sendMessage());
    }

    private void getOrderData() {
        if (getArguments() != null) {
            WaitDialogFragment.newInstance().show(getChildFragmentManager(), "");

            Order order = (Order) getArguments().get(AppContent.ORDER_OBJECT);
            new APIUtil<OrderStation>(requireActivity()).getData(AppController
                            .getInstance().getApi().getOrderById(order.getId())
                    , new RequestListener<OrderStation>() {
                        @Override
                        public void onSuccess(OrderStation orderStation, String msg) {
                            WaitDialogFragment.newInstance().dismiss();
                            ChatFragment.this.orderStation = orderStation;
                            db = FirebaseDatabase.getInstance().getReference(AppContent.FIREBASE_CHAT_INSTANCE);
                            getMessages();
                            initRecycleView();
                        }

                        @Override
                        public void onError(String msg) {
                            WaitDialogFragment.newInstance().dismiss();
                            ToolUtil.showLongToast(msg, requireActivity());
                        }

                        @Override
                        public void onFail(String msg) {
                            WaitDialogFragment.newInstance().dismiss();
                            ToolUtil.showLongToast(msg, requireActivity());
                        }
                    });
        }
    }

    private void initRecycleView() {
        messageArrayList = new ArrayList<>();
        chatAdapter = new ChatAdapter(getActivity(), messageArrayList);
        binding.rvChat.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvChat.setItemAnimator(new DefaultItemAnimator());
        binding.rvChat.setAdapter(chatAdapter);
    }

    //get message
    private void getMessages() {
        db.child(orderStation.getId() + "").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                messageArrayList.add(dataSnapshot.getValue(ChatMessage.class));
                chatAdapter.notifyDataSetChanged();
                binding.rvChat.scrollToPosition(messageArrayList.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //send message
    private void sendMessage() {
        if (ToolUtil.checkTheInternet()) {
            if (!binding.etMessage.getText().toString().equals("")) {
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setText(binding.etMessage.getText().toString());
                chatMessage.setSender_id(AppController.getInstance().getAppSettingsPreferences().getLogin().getUser().getId());
                chatMessage.setSender_name(AppController.getInstance().getAppSettingsPreferences().getLogin().getUser().getName());
                chatMessage.setSender_avatar_url(AppController.getInstance().getAppSettingsPreferences().getLogin().getUser().getAvatar_url());
                chatMessage.setTime(System.currentTimeMillis() / 1000);
                String key = db.push().getKey();
                db.child(orderStation.getId() + "").child(key).setValue(chatMessage);
                ToolUtil.hideSoftKeyboard(getActivity(), binding.etMessage);
                binding.etMessage.setText("");
                NotificationUtil.sendMessageNotification(getActivity(), orderStation.getInvoice_number()
                        , orderStation.getId() + "", orderStation.getUser().getId() + ""
                        , AppContent.TYPE_ORDER_4STATION);
            }
        } else {
            ToolUtil.showLongToast(getString(R.string.no_connection), getActivity());
        }
    }
}

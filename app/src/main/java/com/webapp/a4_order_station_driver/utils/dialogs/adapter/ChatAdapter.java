package com.webapp.a4_order_station_driver.utils.dialogs.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.webapp.a4_order_station_driver.databinding.ItemChatLeftBinding;
import com.webapp.a4_order_station_driver.databinding.ItemChatRightBinding;
import com.webapp.a4_order_station_driver.models.ChatMessage;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ChatMessage> chatMessages;
    private DateFormat dateFormat;
    private Activity activity;

    public ChatAdapter(Activity activity, ArrayList<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
        this.activity = activity;
        dateFormat = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT);
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).getSender_id() == AppController.getInstance()
                .getAppSettingsPreferences().getLogin().getUser().getId()) {
            return 1;
        } else {
            return 2;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new DriverHolder(ItemChatRightBinding.inflate(LayoutInflater
                    .from(parent.getContext()), parent, false));
        } else {
            return new ReceiverHolder(ItemChatLeftBinding.inflate(LayoutInflater
                    .from(parent.getContext()), parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DriverHolder) {
            ((DriverHolder) holder).setData(chatMessages.get(position));
        } else if (holder instanceof ReceiverHolder) {
            ((ReceiverHolder) holder).setData(chatMessages.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public void addItem(ChatMessage chatMessage) {
        chatMessages.add(chatMessage);
        notifyItemInserted(getItemCount() - 1);
    }

    public class DriverHolder extends RecyclerView.ViewHolder {

        private ItemChatRightBinding binding;

        public DriverHolder(ItemChatRightBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void setData(ChatMessage data) {
            binding.tvMessageText.setText(data.getText());
            try {
                binding.tvMessageTime.setText(dateFormat.format(data.getTime()));
            } catch (Exception e) {
                Log.e("error", "" + e.getMessage());
            }
            ToolUtils.loadImage(activity, binding.pbWaitAvater
                    , data.getSender_avatar_url(), binding.ivUserImage);
        }
    }

    public class ReceiverHolder extends RecyclerView.ViewHolder {

        private ItemChatLeftBinding binding;

        public ReceiverHolder(ItemChatLeftBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void setData(ChatMessage data) {
            binding.tvMessageText.setText(data.getText());
            try {
                binding.tvMessageTime.setText(dateFormat.format(data.getTime()));
            } catch (Exception e) {
                Log.e("error", "" + e.getMessage());
            }
            ToolUtils.loadImage(activity, binding.pbWaitAvater
                    , data.getSender_avatar_url(), binding.ivUserImage);
        }
    }
}

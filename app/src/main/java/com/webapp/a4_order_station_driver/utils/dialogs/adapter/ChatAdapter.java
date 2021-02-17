package com.webapp.a4_order_station_driver.utils.dialogs.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.models.ChatMessage;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

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
            return new DriverHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_right, parent, false));
        } else {
            return new ReceiverHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_left, parent, false));
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

        @BindView(R.id.tv_message_text)
        TextView tvMessageText;
        @BindView(R.id.tv_message_time)
        TextView tvMessageTime;
        @BindView(R.id.pb_wait_avater)
        ProgressBar pbWaitAvater;
        @BindView(R.id.iv_user_image)
        ImageView ivUserImage;

        public DriverHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setData(ChatMessage data) {
            tvMessageText.setText(data.getText());
            try {
                tvMessageTime.setText(dateFormat.format(data.getTime()));
            } catch (Exception e) {
                Log.e("error", "" + e.getMessage());
            }
            ToolUtils.loadImage(activity, pbWaitAvater, data.getSender_avatar_url(), ivUserImage);
        }
    }

    public class ReceiverHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.pb_wait_avater)
        ProgressBar pbWaitAvater;
        @BindView(R.id.iv_user_image)
        ImageView ivUserImage;
        @BindView(R.id.tv_message_text)
        TextView tvMessageText;
        @BindView(R.id.tv_message_time)
        TextView tvMessageTime;

        public ReceiverHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setData(ChatMessage data) {
            tvMessageText.setText(data.getText());
            try {
                tvMessageTime.setText(dateFormat.format(data.getTime()));
            } catch (Exception e) {
                Log.e("error", "" + e.getMessage());
            }
            ToolUtils.loadImage(activity, pbWaitAvater, data.getSender_avatar_url(), ivUserImage);
        }
    }
}

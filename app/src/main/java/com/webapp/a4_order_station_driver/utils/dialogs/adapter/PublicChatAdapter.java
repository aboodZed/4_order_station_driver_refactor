package com.webapp.a4_order_station_driver.utils.dialogs.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.models.PublicChatMessage;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.dialogs.ImageFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PublicChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<PublicChatMessage> publicChatMessages;
    private Activity activity;
    private FragmentManager fragmentManager;

    public PublicChatAdapter(ArrayList<PublicChatMessage> publicChatMessages, Activity activity, FragmentManager fragmentManager) {
        this.publicChatMessages = publicChatMessages;
        this.activity = activity;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getItemViewType(int position) {
        if (publicChatMessages.get(position).getSender_id() == AppController.getInstance()
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
                    .inflate(R.layout.item_right_public_chat, parent, false));
        } else {
            return new ReceiverHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_left_public_chat, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DriverHolder) {
            ((DriverHolder) holder).setData(publicChatMessages.get(position));
        } else if (holder instanceof ReceiverHolder) {
            ((ReceiverHolder) holder).setData(publicChatMessages.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return publicChatMessages.size();
    }

    public class DriverHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.message)
        TextView message;
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.progress_bar)
        ProgressBar progressBar;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.sender_image)
        CircleImageView senderImage;

        public DriverHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setData(PublicChatMessage data) {
            message.setText(data.getText());
            ToolUtils.loadImage(activity, progressBar, data.getSender_avatar_url(), senderImage);
            if (!data.getImageUrl().isEmpty()) {
                progressBar.setVisibility(View.VISIBLE);
                Glide.with(activity).load(data.getImageUrl()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        image.setVisibility(View.VISIBLE);

                        return false;
                    }
                }).into(image);
            } else {
                progressBar.setVisibility(View.GONE);
                image.setVisibility(View.GONE);
                message.setVisibility(View.VISIBLE);
                message.setText(message.getText());
            }
            time.setText(ToolUtils.getDate(data.getTime()));
        }

        @OnClick(R.id.image)
        public void open(){
            ImageFragment.newInstance(ToolUtils.getBitmapFromImageView(image)).show(fragmentManager, "");
        }
    }

    public class ReceiverHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sender_image)
        CircleImageView senderImage;
        @BindView(R.id.message)
        TextView message;
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.progress_bar)
        ProgressBar progressBar;
        @BindView(R.id.time)
        TextView time;

        private PublicChatMessage publicChatMessage;

        public ReceiverHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setData(PublicChatMessage data) {
            publicChatMessage = data;
            message.setText(data.getText());
            ToolUtils.loadImage(activity, progressBar, data.getSender_avatar_url(), senderImage);
            if (!data.getImageUrl().isEmpty()) {
                progressBar.setVisibility(View.VISIBLE);
                Glide.with(activity).load(data.getImageUrl()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        image.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).into(image);
            } else {
                progressBar.setVisibility(View.GONE);
                image.setVisibility(View.GONE);
                message.setVisibility(View.VISIBLE);
                message.setText(message.getText());
            }
            time.setText(ToolUtils.getDate(data.getTime()));
        }

        @OnClick(R.id.image)
        public void open(){
            ImageFragment.newInstance(ToolUtils.getBitmapFromImageView(image)).show(fragmentManager, "");
        }
    }
}

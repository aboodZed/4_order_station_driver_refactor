package com.webapp.a4_order_station_driver.feature.order.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.webapp.a4_order_station_driver.databinding.ItemLeftPublicChatBinding;
import com.webapp.a4_order_station_driver.databinding.ItemRightPublicChatBinding;
import com.webapp.a4_order_station_driver.models.ChatMessage;
import com.webapp.a4_order_station_driver.models.PublicChatMessage;
import com.webapp.a4_order_station_driver.utils.APIImageUtil;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.dialogs.ImageFragment;

import java.util.ArrayList;

public class PublicChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<PublicChatMessage> publicChatMessages;
    private Activity activity;
    private FragmentManager fragmentManager;

    public PublicChatAdapter(Activity activity, FragmentManager fragmentManager) {
        this.publicChatMessages = new ArrayList<>();
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
            return new DriverHolder(ItemRightPublicChatBinding.inflate(LayoutInflater
                    .from(parent.getContext()), parent, false));
        } else {
            return new ReceiverHolder(ItemLeftPublicChatBinding.inflate(LayoutInflater
                    .from(parent.getContext()), parent, false));
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

    public void addItem(PublicChatMessage publicChatMessage) {
        publicChatMessages.add(publicChatMessage);
        notifyItemInserted(getItemCount() - 1);
    }

    public class DriverHolder extends RecyclerView.ViewHolder {

        private ItemRightPublicChatBinding binding;

        public DriverHolder(ItemRightPublicChatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            click();
        }

        private void click() {
            binding.image.setOnClickListener(view -> open());
        }

        private void setData(PublicChatMessage data) {
            binding.message.setText(data.getText());
            ToolUtil.loadImage(activity, binding.progressBar, data.getSender_avatar_url(), binding.senderImage);
            if (!data.getImageUrl().isEmpty()) {
                binding.progressBar.setVisibility(View.VISIBLE);
                Glide.with(activity).load(data.getImageUrl()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        binding.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.image.setVisibility(View.VISIBLE);

                        return false;
                    }
                }).into(binding.image);
            } else {
                binding.progressBar.setVisibility(View.GONE);
                binding.image.setVisibility(View.GONE);
                binding.message.setVisibility(View.VISIBLE);
                binding.message.setText(binding.message.getText());
            }
            binding.time.setText(ToolUtil.getDate(data.getTime()));
        }

        public void open() {
            ImageFragment.newInstance(APIImageUtil.getBitmapFromImageView(binding.image)).show(fragmentManager, "");
        }
    }

    public class ReceiverHolder extends RecyclerView.ViewHolder {

        private ItemLeftPublicChatBinding binding;

        private PublicChatMessage publicChatMessage;

        public ReceiverHolder(ItemLeftPublicChatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            click();
        }

        private void click() {
            binding.image.setOnClickListener(view -> open());
        }

        private void setData(PublicChatMessage data) {
            publicChatMessage = data;
            binding.message.setText(data.getText());
            ToolUtil.loadImage(activity, binding.progressBar, data.getSender_avatar_url(), binding.senderImage);
            if (!data.getImageUrl().isEmpty()) {
                binding.progressBar.setVisibility(View.VISIBLE);
                Glide.with(activity).load(data.getImageUrl()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        binding.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.image.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).into(binding.image);
            } else {
                binding.progressBar.setVisibility(View.GONE);
                binding.image.setVisibility(View.GONE);
                binding.message.setVisibility(View.VISIBLE);
                binding.message.setText(binding.message.getText());
            }
            binding.time.setText(ToolUtil.getDate(data.getTime()));
        }

        public void open() {
            ImageFragment.newInstance(APIImageUtil.getBitmapFromImageView(binding.image)).show(fragmentManager, "");
        }
    }
}

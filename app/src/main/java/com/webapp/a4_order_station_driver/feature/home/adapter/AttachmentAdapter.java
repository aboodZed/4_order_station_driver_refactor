package com.webapp.a4_order_station_driver.feature.home.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.models.Attachment;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.dialogs.ImageFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.AttachmentHolder> {

    private Activity activity;
    private ArrayList<Attachment> attachments;
    private FragmentManager fragmentManager;

    public AttachmentAdapter(Activity activity, ArrayList<Attachment> attachments, FragmentManager fragmentManager) {
        this.activity = activity;
        this.attachments = attachments;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public AttachmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AttachmentHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attachment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AttachmentHolder holder, int position) {
        holder.setData(attachments.get(position));
    }

    @Override
    public int getItemCount() {
        return attachments.size();
    }

    class AttachmentHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_image)
        ImageView ivImage;
        @BindView(R.id.pb_wait_image)
        ProgressBar pbWaitImage;

        public AttachmentHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void setData(Attachment attachment) {
            ToolUtils.loadImage(activity, pbWaitImage, attachment.getImage_url(), ivImage);
        }

        @OnClick(R.id.iv_image)
        public void openImage() {
            ImageFragment.newInstance(ToolUtils.getBitmapFromImageView(ivImage)).show(fragmentManager, "");
        }
    }

}

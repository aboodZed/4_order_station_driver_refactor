package com.webapp.a4_order_station_driver.feature.home.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.models.RatingData;
import com.webapp.a4_order_station_driver.utils.ToolUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewHolder> {

    private ArrayList<RatingData> reviews = new ArrayList<>();
    private Activity activity;

    public ReviewsAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rating, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {
        holder.setData(reviews.get(position));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void addItem(RatingData review) {
        reviews.add(review);
        notifyItemInserted(getItemCount() - 1);
    }

    public void addAll(ArrayList<RatingData> arrayList) {
        if (arrayList != null) {
            reviews.addAll(arrayList);
            notifyDataSetChanged();
        }
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_user_image) ImageView ivUserImage;
        @BindView(R.id.pb_wait_avater) ProgressBar pbWaitAvater;
        @BindView(R.id.tv_user_name) TextView tvUserName;
        @BindView(R.id.rb_review) RatingBar rbReview;
        @BindView(R.id.tv_datetime) TextView tvDatetime;
        @BindView(R.id.tv_review_text) TextView tvReviewText;

        public ReviewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(RatingData review) {
            ToolUtils.loadImage(activity, pbWaitAvater, review.getUser().getAvatar_url(), ivUserImage);
            tvUserName.setText(review.getUser().getName());
            tvReviewText.setText(review.getReview());
            float rate = Float.parseFloat(review.getRate());
            rbReview.setRating(rate);
            tvDatetime.setText(ToolUtils.getTime(review.getCreated_timestamp()) + " "
                    + ToolUtils.getDate(review.getCreated_timestamp()));
        }
    }
}

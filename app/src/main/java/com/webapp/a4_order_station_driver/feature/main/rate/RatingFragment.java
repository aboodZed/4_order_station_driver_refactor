package com.webapp.a4_order_station_driver.feature.main.rate;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.webapp.a4_order_station_driver.databinding.FragmentRatingBinding;
import com.webapp.a4_order_station_driver.feature.main.adapter.ReviewsAdapter;
import com.webapp.a4_order_station_driver.models.Arrays;
import com.webapp.a4_order_station_driver.utils.APIUtils;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

public class RatingFragment extends Fragment {

    public static final int page = 205;

    private FragmentRatingBinding binding;

    private ReviewsAdapter reviewsAdapter;
    private String next_page_url;

    private boolean loadingMoreItems;

    public static RatingFragment newInstance() {
        RatingFragment fragment = new RatingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View v = inflater.inflate(R.layout.fragment_rating, container, false);
        binding = FragmentRatingBinding.inflate(getLayoutInflater());
        initRecycleView();
        getRatings("/api/v1/delivery/ratings");
        return binding.getRoot();
    }

    //functions
    private void getRatings(String url) {
        binding.avi.setVisibility(View.VISIBLE);
        new APIUtils<Arrays>(getActivity()).getData(AppController.getInstance()
                .getApi().getRating(url), new RequestListener<Arrays>() {
            @Override
            public void onSuccess(Arrays arrays, String msg) {
                loadingMoreItems = false;
                binding.avi.setVisibility(View.GONE);
                next_page_url = arrays.getRatings().getNext_page_url();
                reviewsAdapter.addAll(arrays.getRatings().getData());
            }

            @Override
            public void onError(String msg) {
                loadingMoreItems = false;
                binding.avi.setVisibility(View.GONE);
                ToolUtils.showLongToast(msg, getActivity());
            }

            @Override
            public void onFail(String msg) {
                loadingMoreItems = false;
                binding.avi.setVisibility(View.GONE);
                ToolUtils.showLongToast(msg, getActivity());
            }
        });
    }

    private void setRecyclerViewScrollListener() {
        binding.rvRating.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)
                        && !TextUtils.isEmpty(next_page_url) && !loadingMoreItems) {
                    binding.avi.setVisibility(View.VISIBLE);
                    loadingMoreItems = true;
                    getRatings(next_page_url);
                }
            }
        });
    }

    private void initRecycleView() {
        reviewsAdapter = new ReviewsAdapter(getActivity());
        binding.rvRating.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvRating.setItemAnimator(new DefaultItemAnimator());
        binding.rvRating.setAdapter(reviewsAdapter);
        setRecyclerViewScrollListener();
    }
}

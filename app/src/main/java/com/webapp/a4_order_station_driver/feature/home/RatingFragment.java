package com.webapp.a4_order_station_driver.feature.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.feature.home.adapter.ReviewsAdapter;
import com.webapp.a4_order_station_driver.models.Arrays;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;
import com.webapp.a4_order_station_driver.utils.view.NavigationView;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingFragment extends Fragment {

    @BindView(R.id.rv_rating) RecyclerView rvRating;
    @BindView(R.id.avi) AVLoadingIndicatorView avi;

    private static NavigationView view;
    private ReviewsAdapter reviewsAdapter;
    private String next_page_url;

    private boolean loadingMoreItems;

    public static RatingFragment newInstance(NavigationView navigationView) {
        RatingFragment fragment = new RatingFragment();
        view = navigationView;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rating, container, false);
        ButterKnife.bind(this, v);
        initRecycleView();
        getRatings("/api/v1/delivery/ratings");
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //functions

    private void getRatings(String url) {
        if (ToolUtils.checkTheInternet()) {
            avi.setVisibility(View.VISIBLE);
            AppController.getInstance().getApi().getRating(url).enqueue(new Callback<Arrays>() {
                @Override
                public void onResponse(@NonNull Call<Arrays> call, @NonNull Response<Arrays> response) {
                    if (getContext() != null) {
                        loadingMoreItems = false;
                        avi.setVisibility(View.GONE);
                        if (response.isSuccessful()) {
                            next_page_url = response.body().getRatings().getNext_page_url();
                            reviewsAdapter.addAll(response.body().getRatings().getData());
                        } else {
                            ToolUtils.showError(getActivity(), response.errorBody());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Arrays> call, @NonNull Throwable t) {
                    if (getContext() != null) {
                        loadingMoreItems = false;
                        avi.setVisibility(View.GONE);
                        ToolUtils.showLongToast(t.getMessage(), getActivity());
                    }
                }
            });
        } else {
            ToolUtils.showLongToast(getString(R.string.no_connection), getActivity());
        }
    }

    private void setRecyclerViewScrollListener() {
        rvRating.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1) && !TextUtils.isEmpty(next_page_url) && !loadingMoreItems) {
                    avi.setVisibility(View.VISIBLE);
                    loadingMoreItems = true;
                    getRatings(next_page_url);
                }
            }
        });
    }

    private void initRecycleView() {
        reviewsAdapter = new ReviewsAdapter(getActivity());
        rvRating.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvRating.setItemAnimator(new DefaultItemAnimator());
        rvRating.setAdapter(reviewsAdapter);
        setRecyclerViewScrollListener();
    }
}

package com.webapp.mohammad_al_loh.feature.main.rate;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.webapp.mohammad_al_loh.databinding.FragmentRatingBinding;
import com.webapp.mohammad_al_loh.feature.main.adapter.ReviewsAdapter;
import com.webapp.mohammad_al_loh.models.RatingObject;
import com.webapp.mohammad_al_loh.utils.listeners.DialogView;

public class RatingFragment extends Fragment implements DialogView<RatingObject> {

    public static final int page = 205;

    private FragmentRatingBinding binding;

    private ReviewsAdapter reviewsAdapter;
    private String next_page_url;

    private boolean loadingMoreItems;
    private RatingPresenter presenter;

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
        binding = FragmentRatingBinding.inflate(getLayoutInflater());
        initRecycleView();
        presenter = new RatingPresenter(requireActivity(), this);
        presenter.getRatings("/api/v1/delivery/ratings");
        return binding.getRoot();
    }

    //functions


    private void setRecyclerViewScrollListener() {
        binding.rvRating.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)
                        && !TextUtils.isEmpty(next_page_url) && !loadingMoreItems) {
                    presenter.getRatings(next_page_url);
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

    @Override
    public void setData(RatingObject ratingObject) {
        next_page_url = ratingObject.getRatings().getNext_page_url();
        reviewsAdapter.addAll(ratingObject.getRatings().getData());
    }

    @Override
    public void showDialog(String s) {
        loadingMoreItems = true;
        binding.avi.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDialog() {
        loadingMoreItems = false;
        binding.avi.setVisibility(View.GONE);
    }
}

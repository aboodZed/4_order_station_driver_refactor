package com.webapp.a4_order_station_driver.feature.data.rate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.webapp.a4_order_station_driver.databinding.FragmentRatingBinding;
import com.webapp.a4_order_station_driver.feature.main.adapter.ReviewsAdapter;
import com.webapp.a4_order_station_driver.models.Rating;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.listeners.DialogView;

public class RatingFragment extends Fragment implements DialogView<Rating> {

    public static final int page = 601;

    private FragmentRatingBinding binding;

    private ReviewsAdapter reviewsAdapter;
    //private String next_page_url;

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
        setUserRating();
        initRecycleView();
        presenter = new RatingPresenter(requireActivity(), this);
        presenter.getRatings("/api/v3/driver/ratings");
        return binding.getRoot();
    }

    private void setUserRating() {
        binding.rbReview.setRating(AppController.getInstance().getAppSettingsPreferences()
                .getUser().getRate());
    }

    //functions


    private void setRecyclerViewScrollListener() {
        binding.rvRating.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
               /* if (!recyclerView.canScrollVertically(1)
                        && !TextUtils.isEmpty(next_page_url) && !loadingMoreItems) {
                    presenter.getRatings(next_page_url);
                }*/
            }
        });
    }

    private void initRecycleView() {
        reviewsAdapter = new ReviewsAdapter(getActivity());
        binding.rvRating.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvRating.setItemAnimator(new DefaultItemAnimator());
        binding.rvRating.setAdapter(reviewsAdapter);
        //reviewsAdapter.addItem(new RatingData());

        setRecyclerViewScrollListener();
    }

    @Override
    public void setData(Rating ratingObject) {
        //next_page_url = ratingObject.getRatings().getNext_page_url();
        reviewsAdapter.addAll(ratingObject.getData());
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

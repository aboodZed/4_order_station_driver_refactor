package com.webapp.a4_order_station_driver.utils.dialogs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.FragmentCountryBinding;
import com.webapp.a4_order_station_driver.models.CountryList;
import com.webapp.a4_order_station_driver.models.Country;
import com.webapp.a4_order_station_driver.utils.APIUtil;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtil;
import com.webapp.a4_order_station_driver.utils.dialogs.adapter.CountryAdapter;
import com.webapp.a4_order_station_driver.utils.listeners.RequestListener;

import java.util.ArrayList;

public class CountryFragment extends DialogFragment {

    private FragmentCountryBinding binding;

    private CountryListener countryListener;
    private ArrayList<Country> countries;

    public static CountryFragment newInstance() {
        return new CountryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCountryBinding.inflate(getLayoutInflater());
        data();
        click();
        return binding.getRoot();
    }

    private void click() {
        binding.btnRefresh.setOnClickListener(view -> data());
    }


    private void data() {
        WaitDialogFragment.newInstance().show(getChildFragmentManager(), "");
        new APIUtil<CountryList>(getActivity()).getData(AppController.getInstance()
                .getApi().getCountries(), new RequestListener<CountryList>() {
            @Override
            public void onSuccess(CountryList countries, String msg) {
                WaitDialogFragment.newInstance().dismiss();
                binding.btnRefresh.setVisibility(View.GONE);
                CountryFragment.this.countries = countries.getCountries();
                Log.e(getClass().getName() + " : countries", countries.toString());
                setData();
            }

            @Override
            public void onError(String msg) {
                binding.btnRefresh.setVisibility(View.VISIBLE);
                WaitDialogFragment.newInstance().dismiss();
                ToolUtil.showLongToast(msg, getActivity());
            }

            @Override
            public void onFail(String msg) {
                binding.btnRefresh.setVisibility(View.VISIBLE);
                WaitDialogFragment.newInstance().dismiss();
                ToolUtil.showLongToast(msg, getActivity());
            }
        });
    }

    private void setData() {
        CountryAdapter countryAdapter = new CountryAdapter(this, countryListener, countries);
        binding.rvCountries.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvCountries.setItemAnimator(new DefaultItemAnimator());
        binding.rvCountries.setAdapter(countryAdapter);
    }

    public void setCountryListener(CountryListener countryListener) {
        this.countryListener = countryListener;
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        setCancelable(false);
        super.onResume();
    }

    public interface CountryListener {
        void selectedCountry();
    }
}
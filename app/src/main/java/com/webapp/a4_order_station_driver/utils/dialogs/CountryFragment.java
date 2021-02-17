package com.webapp.a4_order_station_driver.utils.dialogs;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.webapp.a4_order_station_driver.R;
import com.webapp.a4_order_station_driver.databinding.FragmentCountryBinding;
import com.webapp.a4_order_station_driver.models.Arrays;
import com.webapp.a4_order_station_driver.models.Country;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.ToolUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        //View view = inflater.inflate(R.layout.fragment_country, container, false);
        binding = FragmentCountryBinding.inflate(getLayoutInflater());
        data();
        click();
        return binding.getRoot();
    }

    private void click() {
        binding.btnFirst.setOnClickListener(view -> saudiArabia());
        binding.btnSecond.setOnClickListener(view -> egypt());
        binding.btnThird.setOnClickListener(view -> morocco());
        binding.btnFourth.setOnClickListener(view -> tunisia());
    }

    private void data() {
        WaitDialogFragment.newInstance().show(getFragmentManager(), "");
        AppController.getInstance().getApi().getCountries()
                .enqueue(new Callback<Arrays>() {
                    @Override
                    public void onResponse(Call<Arrays> call, Response<Arrays> response) {
                        if (response.isSuccessful()) {
                            countries = response.body().getCountries();
                            setData();
                        } else {
                            ToolUtils.showError(getActivity(), response.errorBody());
                        }
                        WaitDialogFragment.newInstance().dismiss();
                    }

                    @Override
                    public void onFailure(Call<Arrays> call, Throwable t) {
                        ToolUtils.showLongToast(t.getLocalizedMessage(), getActivity());
                        WaitDialogFragment.newInstance().dismiss();
                    }
                });
    }

    private void setData() {
        boolean language = AppController.getInstance().getAppSettingsPreferences().getAppLanguage().equals("en");
        //first
        if (countries.size() > 0) {
            binding.btnFirst.setVisibility(View.VISIBLE);
            if (language) {
                binding.btnFirst.setText(countries.get(0).getName_en());
            } else {
                binding.btnFirst.setText(countries.get(0).getName_ar());
            }
        }
        //second
        if (countries.size() > 1) {
            binding.btnSecond.setVisibility(View.VISIBLE);
            if (language) {
                binding.btnSecond.setText(countries.get(1).getName_en());
            } else {
                binding.btnSecond.setText(countries.get(1).getName_ar());
            }
        }
        //third
        if (countries.size() > 2) {
            binding.btnThird.setVisibility(View.VISIBLE);
            if (language) {
                binding.btnThird.setText(countries.get(2).getName_en());
            } else {
                binding.btnThird.setText(countries.get(2).getName_ar());
            }
        }
        //fourth
        if (countries.size() > 3) {
            binding.btnFourth.setVisibility(View.VISIBLE);
            if (language) {
                binding.btnFourth.setText(countries.get(3).getName_en());
            } else {
                binding.btnFourth.setText(countries.get(3).getName_ar());
            }
        }
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
        getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                CountryFragment.this.dismiss();
                return true;
            } else return false;
        });
    }

    public void saudiArabia() {
        AppController.getInstance().getAppSettingsPreferences().setCountry(countries.get(0));
        countryListener.selectedCountry();
        dismiss();
    }

    public void egypt() {
        AppController.getInstance().getAppSettingsPreferences().setCountry(countries.get(1));
        countryListener.selectedCountry();
        dismiss();
    }

    public void morocco() {
        AppController.getInstance().getAppSettingsPreferences().setCountry(countries.get(2));
        countryListener.selectedCountry();
        dismiss();
    }

    public void tunisia() {
        AppController.getInstance().getAppSettingsPreferences().setCountry(countries.get(3));
        countryListener.selectedCountry();
        dismiss();
    }

    public interface CountryListener {
        void selectedCountry();
    }
}
package com.webapp.a4_order_station_driver.utils.dialogs.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.webapp.a4_order_station_driver.databinding.ItemCountryBinding;
import com.webapp.a4_order_station_driver.models.Country;
import com.webapp.a4_order_station_driver.utils.AppController;
import com.webapp.a4_order_station_driver.utils.dialogs.CountryFragment;

import java.util.ArrayList;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryHolder> {

    private ArrayList<Country> items;
    private CountryFragment countryFragment;
    private CountryFragment.CountryListener countryListener;

    public CountryAdapter(CountryFragment countryFragment, CountryFragment.CountryListener countryListener
            , ArrayList<Country> items) {
        this.countryFragment = countryFragment;
        this.countryListener = countryListener;
        this.items = items;
    }

    @NonNull
    @Override
    public CountryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CountryHolder(ItemCountryBinding.inflate(LayoutInflater
                .from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CountryHolder holder, int position) {
        holder.setData(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Country item) {
        this.items.add(item);
        notifyItemInserted(getItemCount() - 1);
    }

    public void addAll(ArrayList<Country> items) {
        if (items != null) {
            this.items.addAll(items);
            notifyDataSetChanged();
        }
    }

    public class CountryHolder extends RecyclerView.ViewHolder {

        private ItemCountryBinding binding;

        public CountryHolder(ItemCountryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(Country item) {
            boolean language = AppController.getInstance().getAppSettingsPreferences().getAppLanguage().equals("en");
            if (language){
                binding.btnCountry.setText(item.getName_en());
            }else {
                binding.btnCountry.setText(item.getName_ar());
            }

            onClick(item);
        }

        private void onClick(Country country) {
            binding.btnCountry.setOnClickListener(view -> {
                AppController.getInstance().getAppSettingsPreferences().setCountry(country);
                countryListener.selectedCountry();
                countryFragment.dismiss();
            });
        }


    }
}

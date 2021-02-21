package com.webapp.a4_order_station_driver.feature.main.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SectionPageAdapter extends FragmentPagerAdapter  {

    private List<Fragment> mFragmentList;
    private List<String> mStringList;

    public SectionPageAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mFragmentList = new ArrayList<>();
        mStringList = new ArrayList<>();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mStringList.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mStringList.get(position);
    }

    @Override
    public Fragment getItem(int i) {
        return mFragmentList.get(i);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}

package com.wearetogether.v2.ui.components;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class AccessSlidePagerAdapter extends FragmentStatePagerAdapter {
    private List<AccessFragment> fragmentList;

    public AccessSlidePagerAdapter(FragmentManager fm, List<AccessFragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}

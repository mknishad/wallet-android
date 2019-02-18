package com.wallet.ui.adapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FragmentCategoryAdapter extends FragmentPagerAdapter {

    private List<FragmentHolder> fragmentHolderList;

    public FragmentCategoryAdapter(FragmentManager fm, List<FragmentHolder> fragmentHolderList) {
        super(fm);
        this.fragmentHolderList = fragmentHolderList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentHolderList.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return fragmentHolderList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentHolderList.get(position).getTitle();
    }
}

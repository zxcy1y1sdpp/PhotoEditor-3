package com.example.photoeditor.manager;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdapter extends FragmentPagerAdapter {
    
    private List<Fragment> mFragmentList;
    private List<String>   mFragmentTitleList;
    
    public FragmentAdapter(FragmentManager fm) {
        super(fm);
        mFragmentList = new ArrayList<>();
        mFragmentTitleList = new ArrayList<>();
    }
    
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }
    
    @Override
    public int getCount() {
        return mFragmentList.size();
    }
    
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
    
    public void addFragment(Fragment fragment, String title){
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }
    
}

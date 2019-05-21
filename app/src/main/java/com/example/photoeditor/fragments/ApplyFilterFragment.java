package com.example.photoeditor.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.photoeditor.R;
import com.example.photoeditor.listeners.FilterFragmentListener;
import com.example.photoeditor.manager.FragmentAdapter;
import com.zomato.photofilters.imageprocessors.Filter;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */

public class ApplyFilterFragment extends Fragment
        implements EditFilterEffectFragment.EditFilterListener,
                   FilterEffectFragment.FilterEffectListener {
    
    // Widgets
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    
    // Custom References
    private FilterEffectFragment mFilterEffectFragment;
    private EditFilterEffectFragment mEditFilterEffectFragment;
    private FragmentAdapter mFragmentAdapter;
    
    // Listener Ref
    private FilterFragmentListener mFilterFragmentListener;
    
    public void setFilterFragmentListener(FilterFragmentListener listener){
        mFilterFragmentListener = listener;
    }
    
    public ApplyFilterFragment() {
        // Required empty public constructor
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        initRef();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        
        setUpViewPager();
    }
    
    private void setUpViewPager() {
        if(mFragmentAdapter == null){
            mFragmentAdapter = new FragmentAdapter(Objects.requireNonNull(getActivity()).getSupportFragmentManager());
    
            mFragmentAdapter.addFragment(mFilterEffectFragment,"Filters");
            mFragmentAdapter.addFragment(mEditFilterEffectFragment,"Edit Filters");
    
            mViewPager.setAdapter(mFragmentAdapter);
            mTabLayout.setupWithViewPager(mViewPager);
        }
    }
    
    private void initRef() {
        mFilterEffectFragment = new FilterEffectFragment();
        mFilterEffectFragment.setmFilterEffectListener(this);
        mEditFilterEffectFragment = new EditFilterEffectFragment();
        mEditFilterEffectFragment.setEditFilterListener(this);
    }
    
    private void initViews(View view) {
        mTabLayout = view.findViewById(R.id.fragment_filter_tablayout);
        mViewPager = view.findViewById(R.id.fragment_filter_view_pager);
    }
    
    @Override
    public void onBrightnessChanged(float brightness) {
        mFilterFragmentListener.onFilterBrightnessChanged(brightness);
    }
    
    @Override
    public void onContrastChanged(float contrast) {
        mFilterFragmentListener.onFilterContrastChanged(contrast);
    }
    
    @Override
    public void onSaturationChanged(float saturation) {
        mFilterFragmentListener.onFilterSaturationChanged(saturation);
    }
    
    @Override
    public void onFilterEffectSelected(Filter filter) {
        if (mFilterFragmentListener != null)
            mFilterFragmentListener.onFilterSelected(filter);
    }
    
    public void resetControls() {
        if(mEditFilterEffectFragment != null){
            mEditFilterEffectFragment.resetControls();
        }
    }
    
}

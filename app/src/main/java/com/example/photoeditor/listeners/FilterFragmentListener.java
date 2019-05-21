package com.example.photoeditor.listeners;

import com.zomato.photofilters.imageprocessors.Filter;

public interface FilterFragmentListener {
    void onFilterSelected(Filter filter);
    void onFilterBrightnessChanged(float brightness);
    void onFilterContrastChanged(float contrast);
    void onFilterSaturationChanged(float saturation);
    void onEditStarted();
    void onEditCompleted();
}

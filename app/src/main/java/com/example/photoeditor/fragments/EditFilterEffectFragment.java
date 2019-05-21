package com.example.photoeditor.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.photoeditor.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditFilterEffectFragment extends Fragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    
    // Widgets
    private TextView mBrightness_TV, mContrast_TV, mSaturation_TV;
    private SeekBar mBrightness_Seekbar, mContrast_Seekbar, mSaturation_Seekbar;
    
    private ImageButton mBrightnessButton, mContrastButton, mSaturationButton;
    
    // Edit Filter Listener
    public interface EditFilterListener {
        void onBrightnessChanged(float brightness);
        void onContrastChanged(float contrast);
        void onSaturationChanged(float saturation);
    }
    
    // Edit Filter Listener Ref
    private EditFilterListener mEditFilterListener;
    
    public void setEditFilterListener(EditFilterListener listener){
        mEditFilterListener = listener;
    }
    
    public EditFilterEffectFragment() {
        // Required empty public constructor
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_filter_effect, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        initRef();
        clickOnViews();
    }
    
    private void initViews(View view) {
        mContrast_TV = view.findViewById(R.id.contrast_tv);
        mSaturation_TV = view.findViewById(R.id.saturation_tv);
        mBrightness_TV = view.findViewById(R.id.brightness_tv);
    
        mContrast_Seekbar = view.findViewById(R.id.contrast_seekbar);
        mSaturation_Seekbar = view.findViewById(R.id.saturation_seekbar);
        mBrightness_Seekbar = view.findViewById(R.id.brightness_seekbar);
        
        mContrastButton = view.findViewById(R.id.contrast_btn);
        mSaturationButton = view.findViewById(R.id.saturation_btn);
        mBrightnessButton = view.findViewById(R.id.brightness_btn);
    }
    
    private void initRef() {
        mBrightness_Seekbar.setMax(200);
        mContrast_Seekbar.setMax(20);
        mSaturation_Seekbar.setMax(30);
        
        resetControls();
    }
    
    private void clickOnViews() {
        mContrastButton.setOnClickListener(this);
        mSaturationButton.setOnClickListener(this);
        mBrightnessButton.setOnClickListener(this);
        
        mBrightness_Seekbar.setOnSeekBarChangeListener(this);
        mContrast_Seekbar.setOnSeekBarChangeListener(this);
        mSaturation_Seekbar.setOnSeekBarChangeListener(this);
    }
    
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        
        switch (seekBar.getId()){
            case R.id.brightness_seekbar:{
                mEditFilterListener.onBrightnessChanged(progress-100);
                break;
            }
            case R.id.contrast_seekbar:{
                progress += 10;
                float value = 0.10f * progress;
                mEditFilterListener.onContrastChanged(value);
                break;
            }
            case R.id.saturation_seekbar:{
                float value = 0.10f * progress;
                mEditFilterListener.onSaturationChanged(value);
                break;
            }
            default:
                break;
        }
    }
    
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    
        switch (seekBar.getId()){
            case R.id.brightness_seekbar:{
                mBrightness_TV.setTextColor(getResources().getColor(R.color.colorPrimary));
                mContrast_TV.setTextColor(getResources().getColor(R.color.colorGray));
                mSaturation_TV.setTextColor(getResources().getColor(R.color.colorGray));
                
                break;
            }
            case R.id.contrast_seekbar:{
                mContrast_TV.setTextColor(getResources().getColor(R.color.colorPrimary));
                mBrightness_TV.setTextColor(getResources().getColor(R.color.colorGray));
                mSaturation_TV.setTextColor(getResources().getColor(R.color.colorGray));
                
                break;
            }
            case R.id.saturation_seekbar:{
                mSaturation_TV.setTextColor(getResources().getColor(R.color.colorPrimary));
                mContrast_TV.setTextColor(getResources().getColor(R.color.colorGray));
                mBrightness_TV.setTextColor(getResources().getColor(R.color.colorGray));

                break;
            }
            default:
                break;
        }
    }
    
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    
        switch (seekBar.getId()){
            case R.id.brightness_seekbar:{
                mBrightness_TV.setTextColor(getResources().getColor(R.color.colorGray));
                mContrast_TV.setTextColor(getResources().getColor(R.color.colorGray));
                mSaturation_TV.setTextColor(getResources().getColor(R.color.colorGray));
                
                break;
            }
            case R.id.contrast_seekbar:{
                mBrightness_TV.setTextColor(getResources().getColor(R.color.colorGray));
                mContrast_TV.setTextColor(getResources().getColor(R.color.colorGray));
                mSaturation_TV.setTextColor(getResources().getColor(R.color.colorGray));
                
                break;
            }
            case R.id.saturation_seekbar:{
                mBrightness_TV.setTextColor(getResources().getColor(R.color.colorGray));
                mContrast_TV.setTextColor(getResources().getColor(R.color.colorGray));
                mSaturation_TV.setTextColor(getResources().getColor(R.color.colorGray));

                break;
            }
            default:
                break;
        }
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.brightness_btn:{
                
                mBrightness_TV.setVisibility(View.VISIBLE);
                mBrightness_Seekbar.setVisibility(View.VISIBLE);
                
                mContrast_TV.setVisibility(View.GONE);
                mContrast_Seekbar.setVisibility(View.GONE);
    
                mSaturation_TV.setVisibility(View.GONE);
                mSaturation_Seekbar.setVisibility(View.GONE);
    
                break;
            }
            case R.id.contrast_btn:{
    
                mContrast_TV.setVisibility(View.VISIBLE);
                mContrast_Seekbar.setVisibility(View.VISIBLE);
    
                mBrightness_TV.setVisibility(View.GONE);
                mBrightness_Seekbar.setVisibility(View.GONE);
    
                mSaturation_TV.setVisibility(View.GONE);
                mSaturation_Seekbar.setVisibility(View.GONE);
    
                break;
            }
            case R.id.saturation_btn:{
    
                mSaturation_TV.setVisibility(View.VISIBLE);
                mSaturation_Seekbar.setVisibility(View.VISIBLE);
    
                mBrightness_TV.setVisibility(View.GONE);
                mBrightness_Seekbar.setVisibility(View.GONE);
    
                mContrast_TV.setVisibility(View.GONE);
                mContrast_Seekbar.setVisibility(View.GONE);
                
                break;
            }
            default:
                break;
        }
    }
    
    public void resetControls(){
        mContrast_Seekbar.setProgress(0);
        mSaturation_Seekbar.setProgress(10);
        mBrightness_Seekbar.setProgress(100);
    }
    
}

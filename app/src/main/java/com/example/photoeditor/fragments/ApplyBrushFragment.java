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
import com.example.photoeditor.listeners.BrushFragmentListener;

import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApplyBrushFragment extends Fragment {
    
    // Widgets
    private SeekBar mBrushSizeSeekbar,mBrushOpacitySeekbar;
    private TextView mBrushSizeTextView, mBrushOpacityTextView;
    private ImageButton mBrushButton, mEraserButton, mColorPickerButton;
    
    // Local Variables
    private boolean mIsEraser = false;
    private int mSelectedColor = Color.parseColor("#000000");
    
    // Brush Listener Ref
    private BrushFragmentListener mBrushListener;
    
    public void setBrushFragmentListener(BrushFragmentListener listener){
        mBrushListener = listener;
    }
    
    public ApplyBrushFragment() {
        // Required empty public constructor
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_apply_brush, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        clickOnViews();
    }
    
    private void initViews(View view) {
        mBrushButton = view.findViewById(R.id.brush_btn);
        mEraserButton = view.findViewById(R.id.eraser_btn);
        mColorPickerButton = view.findViewById(R.id.color_picker_btn);
        mBrushSizeTextView = view.findViewById(R.id.brush_size_tv);
        mBrushSizeSeekbar = view.findViewById(R.id.brush_size_seekbar);
        mBrushOpacityTextView = view.findViewById(R.id.brush_opacity_tv);
        mBrushOpacitySeekbar = view.findViewById(R.id.brush_opacity_seekbar);
    }
    
    private void clickOnViews() {
    
        // When Brush Size Changed
        mBrushSizeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(!mIsEraser)
                    mBrushListener.onBrushSizeChanged(progress);
            }
        
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mBrushSizeTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                mBrushOpacityTextView.setTextColor(getResources().getColor(R.color.colorGray));
            }
        
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mBrushOpacityTextView.setTextColor(getResources().getColor(R.color.colorGray));
                mBrushSizeTextView.setTextColor(getResources().getColor(R.color.colorGray));
            }
        });
    
        // When Brush Opacity Changed
        mBrushOpacitySeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBrushListener.onBrushOpacityChanged(progress);
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mBrushOpacityTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                mBrushSizeTextView.setTextColor(getResources().getColor(R.color.colorGray));
            }
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mBrushOpacityTextView.setTextColor(getResources().getColor(R.color.colorGray));
                mBrushSizeTextView.setTextColor(getResources().getColor(R.color.colorGray));
            }
        });
    
        // When Brush Button Clicked
        mBrushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsEraser = false;
                mBrushButton.setVisibility(View.GONE);
                mEraserButton.setVisibility(View.VISIBLE);
    
                mBrushOpacityTextView.setVisibility(View.VISIBLE);
                mBrushOpacitySeekbar.setVisibility(View.VISIBLE);
                mColorPickerButton.setVisibility(View.VISIBLE);
                
                mBrushListener.onBrushStateChanged(mIsEraser);
            }
        });
    
        // When Eraser Button Clicked
        mEraserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsEraser = true;
                mEraserButton.setVisibility(View.GONE);
                mBrushButton.setVisibility(View.VISIBLE);
    
                mBrushOpacityTextView.setVisibility(View.GONE);
                mBrushOpacitySeekbar.setVisibility(View.GONE);
                mColorPickerButton.setVisibility(View.GONE);
                
                mBrushListener.onBrushStateChanged(mIsEraser);
            }
        });
        
        // When Color Picker Button Clicked
        mColorPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerDialog();
                mBrushListener.onBrushColorChanged(mSelectedColor);
            }
        });
        
    }
    
    private void showColorPickerDialog() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(getActivity(), mSelectedColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                dialog.getDialog().dismiss();
            }
            
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mSelectedColor = color;
                dialog.getDialog().dismiss();
            }
        });
        
        colorPicker.show();
    }
    
}

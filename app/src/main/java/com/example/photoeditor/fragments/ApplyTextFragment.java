package com.example.photoeditor.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.photoeditor.R;
import com.example.photoeditor.fragments.dialogs.FontsListDialogFragment;
import com.example.photoeditor.listeners.TextFragmentListener;

import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApplyTextFragment extends Fragment {
    
    // Widgets
    private TextInputLayout mTypedText_TIL;
    private EditText mTypedText_ET;
    private ImageButton mColorPickerButton, mTextFontPickerButton, mOKButton;
    private Typeface mFontFamily;
    
    // Local Variables
    private int mSelectedColor = Color.parseColor("#000000"); // Default Color is Black
    
    // TextFragment Listener Ref
    private TextFragmentListener mTextFragmentListener;
    
    public void setTextFragmentListener(TextFragmentListener listener){
        mTextFragmentListener = listener;
    }
    
    public ApplyTextFragment() {
        // Required empty public constructor
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_apply_text, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        clickOnViews();
    }
    
    private void clickOnViews() {
        // When Color Button is clicked
        mColorPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerDialog();
            }
        });
    
        // When Text Font Button is clicked
        mTextFontPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FontsListDialogFragment dialogFragment = new FontsListDialogFragment();
                if (getFragmentManager() != null) {
                    dialogFragment.show(getFragmentManager(),"TextFragment");
                }
                
                dialogFragment.setFontFamilyListener(new FontsListDialogFragment.FontFamilyListener() {
                    @Override
                    public void onFontFamilySelected(Typeface fontStyle) {
                        mFontFamily = fontStyle;
                    }
                });
            }
        });
    
        // When Done Button is clicked
        mOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String typedText = mTypedText_ET.getText().toString().trim();
                
                if(!typedText.isEmpty()){
                    mTypedText_ET.setText("");
                    mTextFragmentListener.onButtonClicked(typedText,mSelectedColor,mFontFamily);
                }else {
                    mTypedText_ET.setError("Type something cool");
                    mTypedText_ET.setText("");
                }
            }
        });
    }
    
    private void initViews(View view) {
        mTypedText_TIL = view.findViewById(R.id.type_til);
        mTypedText_ET = mTypedText_TIL.getEditText();
        
        mColorPickerButton = view.findViewById(R.id.color_picker_btn);
        mTextFontPickerButton = view.findViewById(R.id.font_style_picker_btn);
        mOKButton = view.findViewById(R.id.ok_btn);
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

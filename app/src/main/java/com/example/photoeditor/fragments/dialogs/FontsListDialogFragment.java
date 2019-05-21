package com.example.photoeditor.fragments.dialogs;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.provider.FontRequest;
import android.support.v4.provider.FontsContractCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.photoeditor.MainActivity;
import com.example.photoeditor.R;
import com.example.photoeditor.adapter.FontListAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FontsListDialogFragment extends BottomSheetDialogFragment
        implements FontListAdapter.FontListener {
    
    // Widgets
    private RecyclerView mFontListRecyclerView;
    private ImageButton mCloseDialogButton;
    
    // Local References
    private FontListAdapter mFontsAdapter;
    
    // Listener Reference
    private FontFamilyListener mFontFamilyListener;
    
    public void setFontFamilyListener(FontFamilyListener listener){
        mFontFamilyListener = listener;
    }
    
    public interface FontFamilyListener {
        void onFontFamilySelected(Typeface fontStyle);
    }
    
    public FontsListDialogFragment() {
        // Required empty public constructor
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fonts_list_dialog, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        clickOnButton();
        populateRecyclerView();
        setCancelable(false);
    }
    
    private void clickOnButton() {
        mCloseDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }
    
    private void initViews(View view) {
        mFontListRecyclerView = view.findViewById(R.id.font_list_recycler_view);
        mCloseDialogButton = view.findViewById(R.id.close_dialog_btn);
    }
    private void populateRecyclerView() {
        if(mFontsAdapter == null){
            mFontsAdapter = new FontListAdapter(getContext(),this);
        }
        
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        mFontListRecyclerView.setLayoutManager(llm);
        mFontListRecyclerView.setHasFixedSize(true);
        mFontListRecyclerView.setAdapter(mFontsAdapter);
    }
    
    @Override
    public void onFontSelected(Typeface font) {
        mFontFamilyListener.onFontFamilySelected(font);
        getDialog().dismiss();
    }
    
}

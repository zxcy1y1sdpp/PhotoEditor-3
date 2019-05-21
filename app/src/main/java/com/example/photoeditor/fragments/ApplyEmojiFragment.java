package com.example.photoeditor.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.photoeditor.R;
import com.example.photoeditor.adapter.EmojiFragmentAdapter;
import com.example.photoeditor.listeners.EmojiFragmentListener;

import java.util.Objects;

import ja.burhanrashid52.photoeditor.PhotoEditor;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApplyEmojiFragment extends Fragment
        implements EmojiFragmentAdapter.EmojiSelectionListener {
    
    private EmojiFragmentAdapter mEmojiAdapter;
    
    // Widgets
    private RecyclerView mEmojiRecyclerView;
    
    // Listener Ref
    private EmojiFragmentListener mEmojiListener;
    
    public void setEmojiListener(EmojiFragmentListener listener){
        mEmojiListener = listener;
    }
    
    public ApplyEmojiFragment() {
        // Required empty public constructor
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_apply_emoji, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initView(view);
        populateRecyclerView();
    }
    
    private void populateRecyclerView() {
        if(mEmojiAdapter == null){
            mEmojiAdapter = new EmojiFragmentAdapter(getContext(), PhotoEditor.getEmojis(Objects.requireNonNull(getContext())),this);
        }
    
        GridLayoutManager glm = new GridLayoutManager(getActivity(),5);
        
        mEmojiRecyclerView.setHasFixedSize(true);
        mEmojiRecyclerView.setLayoutManager(glm);
        mEmojiRecyclerView.setAdapter(mEmojiAdapter);
    }
    
    private void initView(View view) {
        mEmojiRecyclerView = view.findViewById(R.id.emoji_recycler_view);
    }
    
    @Override
    public void onEmojiSelected(String emoji) {
        mEmojiListener.onEmojiSelected(emoji);
    }
    
}

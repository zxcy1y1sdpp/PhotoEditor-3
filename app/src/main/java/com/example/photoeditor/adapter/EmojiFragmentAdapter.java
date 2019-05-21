package com.example.photoeditor.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.photoeditor.R;

import java.util.List;

import io.github.rockerhieu.emojicon.EmojiconTextView;

public class EmojiFragmentAdapter extends RecyclerView.Adapter<EmojiFragmentAdapter.EmojiFragmentViewHolder> {
    
    private Context mContext;
    private List<String> mEmojiList;
    
    // interface
    public interface EmojiSelectionListener {
        void onEmojiSelected(String emoji);
    }
    
    private EmojiSelectionListener mListener;
    
    public void setListener(EmojiSelectionListener listener){
        mListener = listener;
        notifyDataSetChanged();
    }
    
    public EmojiFragmentAdapter(Context mContext, List<String> mEmojiList,
                                EmojiSelectionListener listener) {
        
        this.mContext = mContext;
        this.mEmojiList = mEmojiList;
        setListener(listener);
    }
    
    @NonNull
    @Override
    public EmojiFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        
        View view = LayoutInflater.from(mContext).inflate(R.layout.emoji_items,viewGroup,false);
        
        EmojiFragmentViewHolder viewHolder = new EmojiFragmentViewHolder(view);
        
        return viewHolder;
    }
    
    @Override
    public void onBindViewHolder(@NonNull EmojiFragmentViewHolder emojiFragmentViewHolder, final int position) {
        
        emojiFragmentViewHolder.mEmojIcon_TV.setText(mEmojiList.get(position));
        
        // When Emoji Item clicked
        emojiFragmentViewHolder
                .itemView
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onEmojiSelected(mEmojiList.get(position));
                    }
                });
    }
    
    @Override
    public int getItemCount() {
        return mEmojiList.size();
    }
    
    public static class EmojiFragmentViewHolder extends RecyclerView.ViewHolder {

        private EmojiconTextView mEmojIcon_TV;
        
        public EmojiFragmentViewHolder(@NonNull View itemView) {
            super(itemView);
            mEmojIcon_TV = itemView.findViewById(R.id.emojicons_tv);
        }
    }
}

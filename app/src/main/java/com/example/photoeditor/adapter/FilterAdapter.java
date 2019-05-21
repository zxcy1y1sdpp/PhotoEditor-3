package com.example.photoeditor.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.photoeditor.R;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;

import java.util.List;

import ja.burhanrashid52.photoeditor.PhotoFilter;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {
    
    private Context mContext;
    private List<ThumbnailItem> mThumbnailList;
    private int mSelectedIndex = 0;
    
    // Listener Ref
    private FilterSelectedListener mFilterSelectedListener;
    
    public void setFilterSelectedListener(FilterSelectedListener listener){
        mFilterSelectedListener = listener;
    }
    
//    private final String[] mFilterNames = new String[] {
//            "Auto Fix", "Brightness", "Black & White", "Contrast",
//            "Cross Process", "Documentary", "Due Tone", "Fill Light",
//            "Fish Eye", "Flip Horizontal", "Flip Vertical", "Grain",
//            "Gray Scale", "Lomish", "Negative", "None",
//            "Posterize", "Rotate", "Saturate", "Sepia",
//            "Sharpen", "Temperature", "Tint", "Vignette",
//    };
    
    
    public FilterAdapter(Context mContext, List<ThumbnailItem> mThumbnailList) {
        this.mContext = mContext;
        this.mThumbnailList = mThumbnailList;
    }
    
    public FilterAdapter(Context mContext, List<ThumbnailItem> mThumbnailList,
                         FilterSelectedListener listener) {
        
        this.mContext = mContext;
        this.mThumbnailList = mThumbnailList;
        setFilterSelectedListener(listener);
    }
    
    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.filter_items,viewGroup,false);
        
        FilterViewHolder viewHolder = new FilterViewHolder(itemView);
        
        return viewHolder;
    }
    
    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder holder, final int position) {
        final ThumbnailItem item = mThumbnailList.get(position);

        // set Filter Name
        holder.mFilterNameHolder.setText(item.filterName);
        
        // Loading image into holder
        Glide
                .with(mContext)
                .asBitmap()
                .load(item.image)
                .into(holder.mFilterHolder);
        
        
        // Trigger Filter selection
        holder
                .itemView
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mFilterSelectedListener.onFilterSelected(item.filter);
                        mSelectedIndex = position;
                        notifyDataSetChanged();
                    }
                });
        
        if(mSelectedIndex == position){
            holder.mFilterNameHolder.setTextColor(ContextCompat.getColor(mContext,R.color.selected_filter));
        }else {
            holder.mFilterNameHolder.setTextColor(ContextCompat.getColor(mContext,R.color.normal_filter));
        }
        
    }
    
    @Override
    public int getItemCount() {
        return mThumbnailList.size();
    }
    
    public static class FilterViewHolder extends RecyclerView.ViewHolder {
    
        private TextView mFilterNameHolder;
        private ImageView mFilterHolder;
        
        public FilterViewHolder(@NonNull View itemView) {
            super(itemView);
            
            mFilterNameHolder = itemView.findViewById(R.id.filter_name_holder);
            mFilterHolder = itemView.findViewById(R.id.filter_holder);
        }
    }
    
    // Filter Selected Interface
    public interface FilterSelectedListener {
        void onFilterSelected(Filter filter);
    }
    
}

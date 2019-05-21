package com.example.photoeditor.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.provider.FontRequest;
import android.support.v4.provider.FontsContractCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.photoeditor.R;

import java.util.ArrayList;
import java.util.List;

public class FontListAdapter extends RecyclerView.Adapter<FontListAdapter.FontListViewHolder> {

    private Context mContext;
    private List<Typeface> mFontList = new ArrayList<>();
    private FontListener mFontListener;
    
    public void setFontListener(FontListener listener){
        mFontListener = listener;
    }
    
    // interface
    public interface FontListener{
        void onFontSelected(Typeface font);
    }
    
    public FontListAdapter(Context mContext, FontListener listener) {
        this.mContext = mContext;
        getFontList();
        setFontListener(listener);
    }
    
    @NonNull
    @Override
    public FontListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        
        View view = LayoutInflater.from(mContext).inflate(R.layout.font_items,viewGroup,false);
        
        FontListViewHolder viewHolder = new FontListViewHolder(view);
        
        return viewHolder;
    }
    
    @Override
    public void onBindViewHolder(@NonNull FontListViewHolder fontListViewHolder, final int position) {
        
        fontListViewHolder.mFontStyleHolder.setTypeface(mFontList.get(position));
        
        // Apply Typeface selection Listener
        fontListViewHolder
                .itemView
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mFontListener.onFontSelected(mFontList.get(position));
                    }
                });
        
    }
    
    @Override
    public int getItemCount() {
        return mFontList.size();
    }
    
    private void getFontList() {

//        DownloadableFontList.FontListCallback fontListCallback = new DownloadableFontList.FontListCallback(){
//            @Override
//            public void onFontListRetrieved(FontList fontList) {
//                // Do your work here
//            }
//
//            @Override
//            public void onTypefaceRequestFailed(int reason) {
//
//            }
//        };
//
//
//        DownloadableFontList.requestDownloadableFontList(fontListCallback,R.string.font_api_key);
        
        FontRequest fontRequest = new FontRequest("com.google.android.gms.fonts",
                "com.google.android.gms", "Finger Paint", R.array.google_fonts_certs);

        FontsContractCompat.FontRequestCallback fontRequestCallback =

                new FontsContractCompat.FontRequestCallback() {
                    @Override
                    public void onTypefaceRetrieved(Typeface typeface) {
                        mFontList.add(typeface);
                    }
                    @Override
                    public void onTypefaceRequestFailed(int reason) {
                        Toast.makeText(mContext,
                                "Failed download font", Toast.LENGTH_LONG)
                                .show();
                    }
                };

        FontsContractCompat.requestFont(mContext, fontRequest, fontRequestCallback , getThreadHandler());
    }
    
    private Handler getThreadHandler() {
        Handler zHandler = null;
        
        if (zHandler == null) {
            HandlerThread handlerThread = new HandlerThread("fonts");
            handlerThread.start();
            zHandler = new Handler(handlerThread.getLooper());
        }
        return zHandler;
    }
    
    public static class FontListViewHolder extends RecyclerView.ViewHolder {
    
        private TextView mFontStyleHolder;
        
        public FontListViewHolder(@NonNull View itemView) {
            super(itemView);
            mFontStyleHolder = itemView.findViewById(R.id.font_style_holder_tv);
        }
    }
}

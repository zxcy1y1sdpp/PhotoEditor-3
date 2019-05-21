package com.example.photoeditor.fragments;

import android.graphics.Bitmap;
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
import com.example.photoeditor.abstracts.Constants;
import com.example.photoeditor.adapter.FilterAdapter;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class FilterEffectFragment extends Fragment implements FilterAdapter.FilterSelectedListener {
    
    // Widgets
    private RecyclerView mFilterRecyclerView;
    
    private List<ThumbnailItem> mFilterThumbnailItemsList;
    FilterAdapter mFilterAdapter;
    
    // Filter Effect Listener Ref
    private FilterEffectListener mFilterEffectListener;
    
    public void setmFilterEffectListener(FilterEffectListener listener){
        mFilterEffectListener = listener;
    }
    
    // Interface
    public interface FilterEffectListener {
        void onFilterEffectSelected(Filter filter);
    }
    
    public FilterEffectFragment() {
        // Required empty public constructor
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter_effect, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        initRef();
        populateRecyclerView();
    }
    
    private void initViews(View view) {
        mFilterRecyclerView = view.findViewById(R.id.filter_effect_list_recycler_view);
    }
    
    private void initRef() {
        mFilterThumbnailItemsList = new ArrayList<>();
    }
    
    private void populateRecyclerView() {
        if(mFilterAdapter == null){
            mFilterAdapter = new FilterAdapter(getActivity(),mFilterThumbnailItemsList,this);
        }
        
        GridLayoutManager glm = new GridLayoutManager(getContext(),2);
        mFilterRecyclerView.setLayoutManager(glm);
        mFilterRecyclerView.setHasFixedSize(true);
        mFilterRecyclerView.setAdapter(mFilterAdapter);
        
        displayThumbnail(null);
    }
    
    private void displayThumbnail(final Bitmap bitmap) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Bitmap thumbnailBitmap;
                
                if(bitmap == null){
                    thumbnailBitmap = Constants.mBitmapToBeEdited;
                }else {
                    thumbnailBitmap = bitmap;
                    Constants.mBitmapToBeEdited = bitmap;
                }
                
                if(thumbnailBitmap == null)
                    return;
    
                ThumbnailsManager.clearThumbs();
                mFilterThumbnailItemsList.clear();
                
                // Adding Normal Bitmap First
                ThumbnailItem thumb = new ThumbnailItem();
                
                thumb.image = thumbnailBitmap;
                thumb.filterName = "Normal";
                
                ThumbnailsManager.addThumb(thumb);
                
                // Filters List
                List<Filter> filters = FilterPack.getFilterPack(Objects.requireNonNull(getActivity()));
                
                for(Filter filter : filters){
                    ThumbnailItem ti = new ThumbnailItem();
                    ti.filter = filter;
                    ti.filterName = filter.getName();
                    ti.image = thumbnailBitmap;
                    
                    ThumbnailsManager.addThumb(ti);
                }
                
                // Adding All Thumbnails To List
                mFilterThumbnailItemsList.addAll(ThumbnailsManager.processThumbs(getActivity()));

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mFilterAdapter.notifyDataSetChanged();
                    }
                });
                
            }
            
        };
        
        new Thread(runnable).start();
    }
    
    @Override
    public void onFilterSelected(Filter filter) {
        if(mFilterEffectListener != null)
            mFilterEffectListener.onFilterEffectSelected(filter);
    }
    
}

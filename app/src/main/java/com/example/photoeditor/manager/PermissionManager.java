package com.example.photoeditor.manager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class PermissionManager implements MultiplePermissionsListener {
    
    private static PermissionManager instance;
    private Context mContext;
    
    final String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    
    public interface AllPermissionsGrantedListener {
        void allGranted(boolean yesAllGranted);
    }
    
    private AllPermissionsGrantedListener mListener;
    
    public void setListener(AllPermissionsGrantedListener listener){
        mListener = listener;
    }
    
    public AllPermissionsGrantedListener getListener() {
        return mListener;
    }
    
    public static PermissionManager getInstance(Context context){
        if(instance == null)
            instance = new PermissionManager(context);
        return instance;
    }
    
    private PermissionManager(Context context){
        mContext = context;
    }
    
    public void askPermissions(){
    
        Dexter
                .withActivity((Activity) mContext)
                .withPermissions(PERMISSIONS)
                .withListener(this).check();
        
        }
        
    public boolean isPermissionRequired(){
            boolean yesFlag = false;
            
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                yesFlag = true;
            }
            
            return yesFlag;
        }
    
    @Override
    public void onPermissionsChecked(MultiplePermissionsReport report) {
        if(report.areAllPermissionsGranted()){
            mListener.allGranted(true);
        }else{
            mListener.allGranted(false);
        }
    }
    
    @Override
    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                   PermissionToken token) {
        token.continuePermissionRequest();
    }
    
    public boolean isPermissionsEnabled(){
        boolean flag = false;
        
        for(int i = 0; i < PERMISSIONS.length; i++){
            if(ContextCompat.checkSelfPermission(mContext,PERMISSIONS[i]) == PackageManager.PERMISSION_GRANTED){
                flag = true;
            }else {
                flag = false;
                break;
            }
        }
        
        return flag;
    }
    
}

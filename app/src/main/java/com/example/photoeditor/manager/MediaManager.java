package com.example.photoeditor.manager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.photoeditor.abstracts.Constants;
import com.iceteck.silicompressorr.SiliCompressor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;


public class MediaManager {
    
    private static MediaManager mInstance;
    
    public static MediaManager getInstance(){
        if(mInstance == null)
            mInstance = new MediaManager();
        
        return mInstance;
    }
    
    private MediaManager(){
        checkDir();
    }
    
    private void checkDir() {
        if(!Constants.PHOTO_EDITOR_FOLDER.exists()){
            Constants.PHOTO_EDITOR_FOLDER.mkdir();
        }
    }
    
    public void saveImageToMobile(Bitmap bitmap, String bitmapName){
        
        checkDir();
        
        File finalBitmapFile = new File(Constants.PHOTO_EDITOR_FOLDER, bitmapName);
        
        try {
            
            FileOutputStream fos = new FileOutputStream(finalBitmapFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100, fos);
            
            fos.flush();
            fos.close();
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public Bitmap convertToBitmap(Context context, Uri bitmapUri){
        Bitmap bitmap = null;
        
        if(bitmapUri != null){
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),bitmapUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return bitmap;
    }
    
    // Compress Bitmap & Returns compressed Bitmap path
    public String compressBitmapAndGetPath(Context context, String imagePath, boolean deleteSource){
        
        String filePath = SiliCompressor
                            .with(context)
                            .compress(imagePath, Constants.PHOTO_EDITOR_FOLDER, deleteSource);

        return filePath;
    }
    
    // Compress Bitmap & Returns compressed Bitmap
    public Bitmap compressBitmapAndGetBitmap(Context context, String imagePath, boolean deleteSource){
        
        Bitmap compressedBitmap = null;
        
        try {
            
            compressedBitmap = SiliCompressor.with(context).getCompressBitmap(imagePath,deleteSource);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return compressedBitmap;
    }
    
    public double calculateBitmapSize(File file) {
        
        double sizeInBytes = Double.parseDouble(String.valueOf(file.length()/1024));

        double sizeInKB = sizeInBytes/1024;
    
        double sizeInMB = Double.parseDouble(String.format("%.3f", sizeInKB/1024)) ;
        
        return sizeInMB;
    }
    
    public String saveImage(ContentResolver cr, Bitmap source, String name, String description) {
        
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE,name);
        cv.put(MediaStore.Images.Media.DISPLAY_NAME,name);
        cv.put(MediaStore.Images.Media.DESCRIPTION,description);
        cv.put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg");
        cv.put(MediaStore.Images.Media.DATE_ADDED,System.currentTimeMillis());
        cv.put(MediaStore.Images.Media.DATE_TAKEN,System.currentTimeMillis());
        
        Uri uri = null;
        String stringUri = null;
        
        uri = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,cv);
        
        if(source != null){
            OutputStream os = null;
            
            try {
                os = cr.openOutputStream(uri);
                source.compress(Bitmap.CompressFormat.JPEG,80,os);
                
            } catch (FileNotFoundException e) {
                
                if(uri != null){
                    cr.delete(uri,null, null);
                    uri = null;
                }
                
                e.printStackTrace();
            }finally {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            long id = ContentUris.parseId(uri);
            Bitmap miniThumb = MediaStore.Images.Thumbnails.getThumbnail(cr,id,MediaStore.Images.Thumbnails.MINI_KIND,null);
            storeThumbnail(cr,miniThumb,id,50f,50f,MediaStore.Images.Thumbnails.MICRO_KIND);
        
        }else {
            cr.delete(uri,null, null);
            uri = null;
        }
        
        if(uri != null)
            stringUri = uri.toString();
        
        return stringUri;
    }
    
    private Bitmap storeThumbnail(ContentResolver cr, Bitmap miniThumb, long id, float width, float height, int kind) {
        Matrix matrix = new Matrix();
        
        float scaleX = width/miniThumb.getWidth();
        float scaleY = width/miniThumb.getHeight();
        
        matrix.setScale(scaleX,scaleY);
        
        Bitmap thumb = Bitmap.createBitmap(miniThumb,0,0,miniThumb.getWidth(),miniThumb.getHeight(),matrix,true);
        
        ContentValues values = new ContentValues(4);
        values.put(MediaStore.Images.Thumbnails.KIND,kind);
        values.put(MediaStore.Images.Thumbnails.IMAGE_ID,id);
        values.put(MediaStore.Images.Thumbnails.HEIGHT,height);
        values.put(MediaStore.Images.Thumbnails.WIDTH,width);

        Uri uri = cr.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,values);
        
        try{
            OutputStream os = cr.openOutputStream(uri);
            thumb.compress(Bitmap.CompressFormat.JPEG,100,os);
            
            if (os != null) {
                os.close();
            }
    
            return thumb;
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
}

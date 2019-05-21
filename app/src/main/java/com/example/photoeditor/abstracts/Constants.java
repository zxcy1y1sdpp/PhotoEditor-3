package com.example.photoeditor.abstracts;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;

public abstract class Constants {
    
    private Constants() throws IllegalAccessException {
        throw new IllegalAccessException("Can't Instantiate Abstract Class");
    }
    
    // Constant Variables
    public static final int POSITIVE_BUTTON_IDENTIFIER = -1;
    public static final int NEGATIVE_BUTTON_IDENTIFIER = -2;
    public static final int NEUTRAL_BUTTON_IDENTIFIER = -3;
    public static final String ONE_BUTTON_DIALOG = "One Button Dialog";
    public static final String TWO_BUTTON_DIALOG = "Two Buttons Dialog";
    
    private static final String ROOT_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
    public static final File PHOTO_EDITOR_FOLDER = new File(ROOT_DIR,"Photo Editor");
    
    public static Bitmap mBitmapToBeEdited;
    
}

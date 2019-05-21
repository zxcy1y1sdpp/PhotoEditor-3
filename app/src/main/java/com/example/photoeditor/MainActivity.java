package com.example.photoeditor;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.photoeditor.abstracts.Constants;
import com.example.photoeditor.engine.Glide4Engine;
import com.example.photoeditor.fragments.ApplyBrushFragment;
import com.example.photoeditor.fragments.ApplyEmojiFragment;
import com.example.photoeditor.fragments.ApplyFilterFragment;
import com.example.photoeditor.fragments.ApplyTextFragment;
import com.example.photoeditor.listeners.BrushFragmentListener;
import com.example.photoeditor.listeners.EmojiFragmentListener;
import com.example.photoeditor.listeners.FilterFragmentListener;
import com.example.photoeditor.listeners.TextFragmentListener;
import com.example.photoeditor.manager.DialogManager;
import com.example.photoeditor.manager.FragmentAdapter;
import com.example.photoeditor.manager.MediaManager;
import com.example.photoeditor.manager.PermissionManager;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import java.io.File;
import java.util.List;

import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity
        implements DialogManager.DialogClickListener,
                   PermissionManager.AllPermissionsGrantedListener,
                   EmojiFragmentListener, BrushFragmentListener,
                   TextFragmentListener, FilterFragmentListener {
    
    private Uri mSelectedImageUri;
    private Bitmap mSelectedImage, mFilteredImage, mFinalImage;
    private DialogManager mDialogManager;

    // Local Variables
    private float mBrightness = 0f;
    private float mContrast = 1.0f;
    private float mSaturation = 1.0f;
    private String mSelectedImageName;
    private String mImageSavePath;
    
    // Widgets
    private FloatingActionButton mNewFAB;
    private Toolbar mToolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private PhotoEditorView mPhotoEditorView;
    private PhotoEditor mPhotoEditor;
    private Typeface mEmojiTypeface;
    private ConstraintLayout mMain_layout;
    
    // Custom References
    private FragmentAdapter mFragmentAdapter;
    private ApplyFilterFragment mFilterFragment;
    private ApplyBrushFragment mBrushFragment;
    private ApplyTextFragment mTextFragment;
    private ApplyEmojiFragment mEmojiFragment;
    private PermissionManager mPermissionManager;
    
    // Constant Variables
    private static final int REQUEST_CODE_CHOOSE = 3336;
    private static final String HOME = "Photo Editor";
    private final String mFilterStr = "";
    private final String mDrawStr = "";
    private final String mAddTextStr = "";
    private final String mAddEmojiStr = "";
    private final Integer[] mTabIcons = new Integer[]{
                                            R.drawable.ic_filter_24dp,
                                            R.drawable.ic_drawing_24dp,
                                            R.drawable.ic_text_24dp,
                                            R.drawable.ic_emoji_24dp
                                        };
    
    // Native Image Processing Library
    static{
        System.loadLibrary("NativeImageProcessor");
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    
        clearMemory();
        
        initViews();
        initRef();
        clickOnButton();
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        
        setUpToolbar();
        setUpViewPager();
        setUpTabIcons();
    }
    
    private void setUpTabIcons() {
        for(int i = 0; i < mTabLayout.getTabCount(); i++){
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if(tab != null){
                tab.setIcon(mTabIcons[i]);
            }
        }
    }
    
    private void setUpToolbar() {
        if(getSupportActionBar() != null){
            getSupportActionBar().setTitle(HOME);
            getSupportActionBar().setElevation(4.0f);
            getSupportActionBar().setLogo(getDrawable(R.drawable.logo));
            getSupportActionBar().setDisplayUseLogoEnabled(true);
        }
    }
    
    private void setUpViewPager() {
        if(mFragmentAdapter == null){
            mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
            mFragmentAdapter.addFragment(mFilterFragment,mFilterStr);
            mFragmentAdapter.addFragment(mBrushFragment, mDrawStr);
            mFragmentAdapter.addFragment(mTextFragment, mAddTextStr);
            mFragmentAdapter.addFragment(mEmojiFragment, mAddEmojiStr);
            
            mViewPager.setAdapter(mFragmentAdapter);
            mTabLayout.setupWithViewPager(mViewPager);
        }
    }
    
    private void initViews() {
        mToolbar = findViewById(R.id.mytoolbar);
        setSupportActionBar(mToolbar);
        
        mTabLayout = findViewById(R.id.tools_tablayout);
        mViewPager = findViewById(R.id.view_pager);
        mNewFAB = findViewById(R.id.fab);
        mPhotoEditorView = findViewById(R.id.photoEditorView);
        mMain_layout = findViewById(R.id.main_layout);
    }
    
    // Initialize References
    private void initRef() {
        mDialogManager = DialogManager.getInstance();
        mDialogManager.setListener(this);
        
        mPermissionManager = PermissionManager.getInstance(MainActivity.this);
        mPermissionManager.setListener(this);
        
        mEmojiTypeface = Typeface.createFromAsset(getAssets(),"emojione-android.ttf");
    
        mPhotoEditor = new PhotoEditor
                .Builder(this,mPhotoEditorView)
                .setDefaultEmojiTypeface(mEmojiTypeface)
                .setPinchTextScalable(true)
                .build();
        
        mFilterFragment = new ApplyFilterFragment();
        mFilterFragment.setFilterFragmentListener(this);
        
        mBrushFragment = new ApplyBrushFragment();
        mBrushFragment.setBrushFragmentListener(this);
        
        mTextFragment = new ApplyTextFragment();
        mTextFragment.setTextFragmentListener(this);
        
        mEmojiFragment = new ApplyEmojiFragment();
        mEmojiFragment.setEmojiListener(this);
    }
    
    private void clickOnButton() {
        mNewFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRuntimePermissions();
            }
        });
    }
    
    private void checkRuntimePermissions() {
        if(!mPermissionManager.isPermissionsEnabled()){
            if(mPermissionManager.isPermissionRequired()){
                mPermissionManager.askPermissions();
            }
        }else {
            pickImage(1);
        }
    }
    
    private void pickImage(final int maxSelectableImages){
        Matisse.from(this)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(maxSelectableImages)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new Glide4Engine())
                .forResult(REQUEST_CODE_CHOOSE);
    }
    
    private void loadImage(Uri uri, ImageView target) {
        if(uri != null){
            Glide
                    .with(this)
                    .load(uri)
                    .centerCrop()
                    .into(target);
        }else {
            mDialogManager.showAlertDialog(this,"ERROR","Can't load Image", "OK");
        }
    }
    
    private void loadImage(Bitmap bitmap, ImageView target) {
        if(bitmap != null){
            Glide
                    .with(this)
                    .asBitmap()
                    .load(bitmap)
                    .centerCrop()
                    .into(target);
        }else {
            mDialogManager.showAlertDialog(this,"ERROR","Can't load Image", "OK");
        }
    }
    
    // Get Result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if(resultCode == RESULT_OK && data != null){
            if (requestCode == REQUEST_CODE_CHOOSE) {
                List<String> mSelectedImagesPath = Matisse.obtainPathResult(data); // returns selected images path
    
                File file = new File(mSelectedImagesPath.get(0));
                mSelectedImageUri = Uri.fromFile(file);
                
                createBitmap();
                
                loadImage(mSelectedImageUri,mPhotoEditorView.getSource());
            }
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        getMenuInflater().inflate(R.menu.main_menu,menu);
        
        return true;
    }
    
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean flag = false;
        
        switch (item.getItemId()){
            case R.id.action_edit:{
                flag = true;
                
                if(mSelectedImageUri != null){
                    setUpVisibility(VISIBLE);
                    mNewFAB.setVisibility(GONE);
                    item.setVisible(false);
                }else {
                    mDialogManager.showAlertDialog(MainActivity.this,"No Image", "No Image Found", "OK");
                    setUpVisibility(GONE);
                }
                
                break;
            }
            case R.id.action_save:{
                showDialogBox();
                flag = true;
                break;
            }
            default:
                break;
        }
        
        return flag;
    }
    
    private void showDialogBox() {
        
        final MediaManager mediaManager = MediaManager.getInstance();
        
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    
        builder.setTitle("Save your Image");
    
        // Add Edit Text in dialog
        final EditText inputField = new EditText(this);
    
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        inputField.setLayoutParams(lp);
    
        builder.setView(inputField);
    
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            
                String text = inputField.getText().toString().trim();
            
                if(!text.isEmpty()) {
                    mSelectedImageName = text;
                    if(mFinalImage != null) {
                        final String path = mediaManager.saveImage(getContentResolver(), mFinalImage, mSelectedImageName, null);
                        
                        // Showing message
    
                        if(!TextUtils.isEmpty(path)){
        
                            Snackbar snackbar = Snackbar.make(mMain_layout, "Image Saved Successfully!", Snackbar.LENGTH_LONG);
                            snackbar.setAction("Open", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    openImage(path);
                                }
                            });
        
                            snackbar.show();
        
                        }else {
                            Snackbar snackbar = Snackbar.make(mMain_layout, "Unable Save Image", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
    
    
                    }
                }
                else
                    inputField.setError("This Field is required.");
            }
        });
    
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    
        AlertDialog dialog = builder.create();
        dialog.show();
        
    }
    
    private void showSnackbarAndOpenImage(String msg, String path) {
    
    }
    
    private void openImage(String imagePath) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(imagePath),"image/*");
        startActivity(intent);
    }
    
    // Create Bitmap from Uri
    private void createBitmap() {
        if(mSelectedImageUri != null){
            MediaManager mediaManager = MediaManager.getInstance();
            Bitmap bitmap = mediaManager.convertToBitmap(this,mSelectedImageUri);
    
            clearMemory();
            
            if(bitmap != null){
                mSelectedImage = bitmap.copy(Bitmap.Config.ARGB_8888,true);
                Constants.mBitmapToBeEdited = mSelectedImage.copy(Bitmap.Config.ARGB_8888,true);
                
                bitmap.recycle();
            }
            
            mFilteredImage = mSelectedImage.copy(Bitmap.Config.ARGB_8888,true);
            mFinalImage = mSelectedImage.copy(Bitmap.Config.ARGB_8888,true);
            
            loadImage(mSelectedImage,mPhotoEditorView.getSource());
        }
    }
    
    // Clear Memory
    private void clearMemory() {
        if(mSelectedImage != null)
            mSelectedImage.recycle();
        
        if(mFilteredImage != null)
            mFilteredImage.recycle();
        
        if(mFinalImage != null)
            mFinalImage.recycle();
    }
    
    private void setUpVisibility(int status) {
        mTabLayout.setVisibility(status);
        mViewPager.setVisibility(status);
    }
    
    @Override
    public void onDialogButtonClicked(DialogInterface dialog, int whichButton, String whichDialog, String input) {
        if(whichDialog.equals(Constants.ONE_BUTTON_DIALOG)){
            if(whichButton == Constants.POSITIVE_BUTTON_IDENTIFIER){
                dialog.dismiss();
            }
        }else if(whichDialog.equals(Constants.TWO_BUTTON_DIALOG)){
            if(whichButton == Constants.POSITIVE_BUTTON_IDENTIFIER){
                mSelectedImageName = input+".jpg";
                dialog.dismiss();
            }
            if(whichButton == Constants.NEGATIVE_BUTTON_IDENTIFIER){
                dialog.dismiss();
            }
        }
        
    }
    
    @Override
    public void allGranted(boolean yesAllGranted) {
        if(!yesAllGranted){
            mPermissionManager.askPermissions();
        }
        
        pickImage(1);
    }
    
    @Override
    public void onEmojiSelected(String emoji) {
        mPhotoEditor.addEmoji(emoji);
    }
    
    @Override
    public void onBrushSizeChanged(int size) {
        mPhotoEditor.setBrushSize(size);
    }
    
    @Override
    public void onBrushOpacityChanged(int opacity) {
        mPhotoEditor.setOpacity(opacity);
    }
    
    @Override
    public void onBrushColorChanged(int color) {
        mPhotoEditor.setBrushColor(color);
    }
    
    @Override
    public void onBrushStateChanged(boolean isEraser) {
        if(isEraser){
            mPhotoEditor.setBrushDrawingMode(false);
            mPhotoEditor.brushEraser();
        }else {
            mPhotoEditor.setBrushDrawingMode(true);
        }
    }
    
    @Override
    public void onButtonClicked(String typedText, int textColor, Typeface fontFamily) {
        if(!typedText.isEmpty()){
            mPhotoEditor.addText(fontFamily,typedText,textColor);
        }
    }
    
    @Override
    public void onBackPressed() {
        if(mViewPager.getVisibility() == VISIBLE || mTabLayout.getVisibility() == VISIBLE){
            setUpVisibility(GONE);
        }else {
            super.onBackPressed();
        }
        
    }
    
    @Override
    public void onFilterSelected(Filter filter) {
        resetControls();
        
        mFilteredImage = mSelectedImage.copy(Bitmap.Config.ARGB_8888,true);
        loadImage(filter.processFilter(mFilteredImage), mPhotoEditorView.getSource());
        mFinalImage = mFilteredImage.copy(Bitmap.Config.ARGB_8888,true);
    }
    
    private void resetControls() {
        mFilterFragment.resetControls();
        
        mBrightness = 0f;
        mContrast = 1.0f;
        mSaturation = 1.0f;
    }
    
    @Override
    public void onFilterBrightnessChanged(float brightness) {
        mBrightness = brightness;
        
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter((int) brightness));
        
        Bitmap inputImage = mFinalImage.copy(Bitmap.Config.ARGB_8888,true);
        Bitmap filteredBitmap = myFilter.processFilter(inputImage);
        
        loadImage(filteredBitmap,mPhotoEditorView.getSource());
    }
    
    @Override
    public void onFilterContrastChanged(float contrast) {
    
        mContrast = contrast;
        
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new ContrastSubFilter(contrast));
    
        Bitmap inputImage = mFinalImage.copy(Bitmap.Config.ARGB_8888,true);
        Bitmap filteredBitmap = myFilter.processFilter(inputImage);
    
        loadImage(filteredBitmap,mPhotoEditorView.getSource());
    }
    
    @Override
    public void onFilterSaturationChanged(float saturation) {
    
        mSaturation = saturation;
        
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new SaturationSubfilter(saturation));
    
        Bitmap inputImage = mFinalImage.copy(Bitmap.Config.ARGB_8888,true);
        Bitmap filteredBitmap = myFilter.processFilter(inputImage);
    
        loadImage(filteredBitmap,mPhotoEditorView.getSource());
    }
    
    @Override
    public void onEditStarted() {
    
    }
    
    @Override
    public void onEditCompleted() {
        Bitmap bitmap = mFilteredImage.copy(Bitmap.Config.ARGB_8888,true);
        
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter((int) mBrightness));
        myFilter.addSubFilter(new ContrastSubFilter(mContrast));
        myFilter.addSubFilter(new SaturationSubfilter(mSaturation));
        
        mFinalImage = myFilter.processFilter(bitmap);
        
        bitmap.recycle();
    }
    
}

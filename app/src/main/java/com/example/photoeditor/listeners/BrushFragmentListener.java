package com.example.photoeditor.listeners;

public interface BrushFragmentListener {
    void onBrushSizeChanged(int size);
    void onBrushOpacityChanged(int opacity);
    void onBrushColorChanged(int color);
    void onBrushStateChanged(boolean isEraser);
}

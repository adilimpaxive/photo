package com.photo.viedo.maker.photomaker.selffie.view;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;

public class HeadInfo {

    /* renamed from: bm */
    public Bitmap f73bm;
    public Matrix mMatrix = new Matrix();
    public Rect mRoi;
    public int viewHeight;
    public int viewWidth;

    public int getPreviewPaddingX() {
        return 0;
    }

    public int getPreviewPaddingY() {
        return 0;
    }

    public HeadInfo(Bitmap bitmap, int i, int i2) {
        this.f73bm = bitmap;
        this.viewWidth = i;
        this.viewHeight = i2;
        this.mRoi = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
    }

    public Bitmap getObjectBitmap() {
        return this.f73bm;
    }

    public int getPreviewWidth() {
        return this.viewWidth;
    }

    public int getPreviewHeight() {
        return this.viewHeight;
    }

    public Rect getDrawCanvasRoi() {
        return this.mRoi;
    }

    public Matrix getSupMatrix() {
        return this.mMatrix;
    }

    public Matrix getViewTransformMatrix() {
        return this.mMatrix;
    }
}

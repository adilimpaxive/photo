package com.photo.viedo.maker.photomaker.selffie.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import androidx.core.view.ViewCompat;

public class HeadController {
    public static final int AVAILABLE_TOUCH_AREA_OFFSET = 40;
    public static final int BOTTOM = 5;
    public static final int LEFT = 2;
    public static final int LEFT_BOTTOM = 8;
    public static final int LEFT_TOP = 6;
    public static final int MOVE = 1;
    public static final int NONE_TOUCH_TYPE = 0;
    public static final int RECT_CENTER_BOUNDARY = 2;
    public static final int RIGHT = 4;
    public static final int RIGHT_BOTTOM = 7;
    public static final int RIGHT_TOP = 9;
    public static final int TOP = 3;
    private Bitmap OrgObjectBmp;
    protected int limit = 150;
    protected int mAngle;
    private Bitmap mBitmapResource = null;
    private Bitmap mBitmapRotateResource = null;
    private int mBoundaryType = 0;
    private PointF mCenterPT = null;
    protected PointF mDestPt1 = null;
    protected PointF mDestPt2 = null;
    protected PointF mDestPt3 = null;
    protected PointF mDestPt4 = null;
    private Paint mDrawLinePaint = null;
    private Paint mDrawLinePaint1 = null;
    private RectF mDrawROI = null;
    private PointF mEndPT = null;
    private int mFixedHeight = 0;
    private int mFixedWidth = 0;
    private boolean mFreeRect = false;
    private Paint mGreyPaint = null;
    protected  com.photo.viedo.maker.photomaker.selffie.view.HeadInfo mImageData = null;
    private RectF mInitROI = null;
    private PointF mOriginalCenterPT = null;
    private PointF mOriginalDestPt1 = null;
    private PointF mOriginalDestPt2 = null;
    private PointF mOriginalDestPt3 = null;
    private PointF mOriginalDestPt4 = null;
    protected RectF mOriginalROI = null;
    private float mPrevX;
    private float mPrevY;
    protected int mPreviewHeight = 0;
    protected int mPreviewWidth = 0;
    protected RectF mROI = null;
    private Paint mRectPaint = null;
    private boolean mRightTopRotate = true;
    private PointF mStartPT = null;
    protected int mTouchType;
    private boolean mbApply;
    protected int minValueForClipboard = 100;

    public HeadController(Context context, Rect rect, HeadInfo headInfo, int i, boolean z) {
        initResource(context, headInfo);
        this.mFreeRect = z;
        this.mBoundaryType = i;
        this.OrgObjectBmp = headInfo.getObjectBitmap();
        if (rect.left < 0) {
            rect.left = 0;
        }
        if (rect.top < 0) {
            rect.top = 0;
        }
        if (rect.right > headInfo.getPreviewWidth() - 1) {
            rect.right = headInfo.getPreviewWidth() - 1;
        }
        if (rect.bottom > headInfo.getPreviewHeight() - 1) {
            rect.bottom = headInfo.getPreviewHeight() - 1;
        }
        if (rect.left > rect.right || rect.top > rect.bottom) {
            this.mbApply = false;
        } else {
            this.mbApply = true;
        }
        if (this.mbApply) {
            this.mROI = new RectF();
            Log.d("tri.dung", "mbApply == true ");
            int previewWidth = headInfo.getPreviewWidth() / 2;
            int previewHeight = headInfo.getPreviewHeight() / 3;
            int width = rect.width();
            int height = rect.height();
            Rect rect2 = new Rect();
            rect2.left = previewWidth - (width / 2);
            rect2.right = (rect2.left + width) - 1;
            rect2.top = previewHeight - (height / 2);
            rect2.bottom = (rect2.top + height) - 1;
            setRectRoi(rect2);
        }
    }

    public RectF getDrawBdry() {
        return getDrawROI();
    }

    public PointF getDrawCenterPt() {
        PointF pointF = new PointF();
        pointF.x = getCenterPT().x + ((float) this.mImageData.getPreviewPaddingX());
        pointF.y = getCenterPT().y + ((float) this.mImageData.getPreviewPaddingY());
        return pointF;
    }

    public PointF getCenterPt() {
        return getCenterPT();
    }

    public Bitmap GetObjectBitmap() {
        return this.OrgObjectBmp;
    }

    /* access modifiers changed from: protected */
    public void setFreeRect(boolean z) {
        this.mFreeRect = z;
    }

    /* access modifiers changed from: protected */
    public void setRectRoi(Rect rect) {
        this.mFixedWidth = rect.width();
        this.mFixedHeight = rect.height();
        this.mROI.set(rect);
        int width = (int) this.mROI.width();
        int height = (int) this.mROI.height();
        if (width > height) {
            int i = (int) (((float) height) * (((float) this.mFixedWidth) / ((float) this.mFixedHeight)));
            if (i > width) {
                int i2 = i - width;
                this.mROI.right += (float) i2;
            } else {
                int i3 = width - i;
                this.mROI.right -= (float) i3;
            }
            int width2 = (int) ((((float) this.mImageData.getDrawCanvasRoi().width()) - this.mROI.width()) / 2.0f);
            int width3 = (int) this.mROI.width();
            RectF rectF = this.mROI;
            rectF.left = (float) width2;
            rectF.right = rectF.left + ((float) width3);
        } else {
            int i4 = (int) (((float) width) * (((float) this.mFixedHeight) / ((float) this.mFixedWidth)));
            if (i4 > height) {
                int i5 = i4 - height;
                this.mROI.bottom += (float) i5;
            } else {
                int i6 = height - i4;
                this.mROI.bottom -= (float) i6;
            }
            int previewHeight = (int) ((((float) this.mImageData.getPreviewHeight()) - this.mROI.height()) / 3.0f);
            int height2 = (int) this.mROI.height();
            RectF rectF2 = this.mROI;
            rectF2.top = (float) previewHeight;
            rectF2.bottom = rectF2.top + ((float) height2);
        }
        this.mDrawROI.left = this.mROI.left + ((float) this.mImageData.getDrawCanvasRoi().left);
        this.mDrawROI.right = this.mROI.right + ((float) this.mImageData.getDrawCanvasRoi().left);
        this.mDrawROI.top = this.mROI.top + ((float) this.mImageData.getDrawCanvasRoi().top);
        this.mDrawROI.bottom = this.mROI.bottom + ((float) this.mImageData.getDrawCanvasRoi().top);
        this.mDestPt1.set(this.mROI.left, this.mROI.top);
        this.mDestPt2.set(this.mROI.right, this.mROI.top);
        this.mDestPt3.set(this.mROI.right, this.mROI.bottom);
        this.mDestPt4.set(this.mROI.left, this.mROI.bottom);
        this.mCenterPT.x = (this.mROI.left + this.mROI.right) / 2.0f;
        this.mCenterPT.y = (this.mROI.top + this.mROI.bottom) / 2.0f;
        this.mPreviewWidth = this.mImageData.getDrawCanvasRoi().width();
        this.mPreviewHeight = this.mImageData.getDrawCanvasRoi().height();
        calculateOriginal();
        this.mInitROI.set(this.mROI);
    }

    public void setAngle(int i) {
        this.mAngle = i;
    }

    public void destroy() {
        this.mROI = null;
        this.mDrawROI = null;
        this.mInitROI = null;
        this.mStartPT = null;
        this.mCenterPT = null;
        this.mEndPT = null;
        this.mDestPt1 = null;
        this.mDestPt2 = null;
        this.mDestPt3 = null;
        this.mDestPt4 = null;
        Bitmap bitmap = this.mBitmapResource;
        if (bitmap != null) {
            bitmap.recycle();
        }
        this.mBitmapResource = null;
        Bitmap bitmap2 = this.mBitmapRotateResource;
        if (bitmap2 != null) {
            bitmap2.recycle();
        }
        this.mBitmapRotateResource = null;
        this.mDrawLinePaint = null;
        this.mDrawLinePaint1 = null;
        this.mRectPaint = null;
        this.mGreyPaint = null;
    }

    public int getAngle() {
        return this.mAngle;
    }

    public boolean isFreeRect() {
        return this.mFreeRect;
    }

    /* access modifiers changed from: protected */
    public RectF getDrawROI() {
        if (this.mDrawROI == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postTranslate((float) this.mImageData.getDrawCanvasRoi().left, (float) this.mImageData.getDrawCanvasRoi().top);
        RectF rectF = new RectF();
        rectF.set(this.mROI);
        RectF rectF2 = new RectF();
        matrix.mapRect(rectF2, rectF);
        this.mDrawROI.set(rectF2.left, rectF2.top, rectF2.right, rectF2.bottom);
        return this.mDrawROI;
    }

    public RectF getRoi() {
        return new RectF(this.mROI);
    }

    public void setOrgDestROI(RectF rectF, PointF pointF, PointF pointF2, PointF pointF3, PointF pointF4, PointF pointF5, int i) {
        float f;
        this.mAngle = i;
        this.mOriginalROI = rectF;
        HeadInfo headInfo = this.mImageData;
        if (headInfo != null) {
            Matrix supMatrix = headInfo.getSupMatrix();
            float[] fArr = new float[9];
            Matrix matrix = new Matrix();
            supMatrix.getValues(fArr);
            float f2 = 0.0f;
            if (fArr[2] > 0.0f) {
                f = 0.0f;
            } else {
                f = fArr[2];
            }
            if (fArr[5] <= 0.0f) {
                f2 = fArr[5];
            }
            matrix.postScale(fArr[0], fArr[4]);
            matrix.postTranslate(f, f2);
            RectF rectF2 = new RectF();
            rectF2.set(rectF);
            RectF rectF3 = new RectF();
            matrix.mapRect(rectF3, rectF2);
            this.mROI.set((float) ((int) rectF3.left), (float) ((int) rectF3.top), (float) ((int) rectF3.right), (float) ((int) rectF3.bottom));
            float[] fArr2 = new float[8];
            fArr2[0] = pointF.x;
            fArr2[1] = pointF.y;
            matrix.mapPoints(fArr2);
            PointF pointF6 = this.mCenterPT;
            pointF6.x = (float) ((int) fArr2[0]);
            pointF6.y = (float) ((int) fArr2[1]);
            fArr2[0] = pointF2.x;
            fArr2[1] = pointF2.y;
            fArr2[2] = pointF3.x;
            fArr2[3] = pointF3.y;
            fArr2[4] = pointF4.x;
            fArr2[5] = pointF4.y;
            fArr2[6] = pointF5.x;
            fArr2[7] = pointF5.y;
            matrix.mapPoints(fArr2);
            PointF pointF7 = this.mDestPt1;
            pointF7.x = (float) ((int) fArr2[0]);
            pointF7.y = (float) ((int) fArr2[1]);
            PointF pointF8 = this.mDestPt2;
            pointF8.x = (float) ((int) fArr2[2]);
            pointF8.y = (float) ((int) fArr2[3]);
            PointF pointF9 = this.mDestPt3;
            pointF9.x = (float) ((int) fArr2[4]);
            pointF9.y = (float) ((int) fArr2[5]);
            PointF pointF10 = this.mDestPt4;
            pointF10.x = (float) ((int) fArr2[6]);
            pointF10.y = (float) ((int) fArr2[7]);
        }
    }

    /* access modifiers changed from: protected */
    public int InitMoveObject(float f, float f2) {
        int i;
        float f3;
        this.mTouchType = 0;
        if (this.mROI.width() < 160.0f || this.mROI.height() < 160.0f) {
            if (this.mROI.width() > this.mROI.height()) {
                f3 = this.mROI.height();
            } else {
                f3 = this.mROI.width();
            }
            i = (int) (f3 / 4.0f);
            if (i < 1) {
                i = 1;
            }
        } else {
            i = 40;
        }
        float f4 = (float) i;
        if (Math.abs(f - this.mDestPt2.x) < f4 && Math.abs(f2 - this.mDestPt2.y) < f4) {
            this.mCenterPT.set((this.mROI.left + this.mROI.right) / 2.0f, (this.mROI.top + this.mROI.bottom) / 2.0f);
            this.mStartPT.set(this.mROI.right, this.mROI.top);
            this.mTouchType = 9;
        } else if (Math.abs(f - ((this.mDestPt1.x + this.mDestPt4.x) / 2.0f)) < f4 && Math.abs(f2 - ((this.mDestPt1.y + this.mDestPt4.y) / 2.0f)) < f4 && this.mFreeRect) {
            this.mTouchType = 2;
        } else if (Math.abs(f - ((this.mDestPt1.x + this.mDestPt2.x) / 2.0f)) < f4 && Math.abs(f2 - ((this.mDestPt1.y + this.mDestPt2.y) / 2.0f)) < f4 && this.mFreeRect) {
            this.mTouchType = 3;
        } else if (Math.abs(f - ((this.mDestPt2.x + this.mDestPt3.x) / 2.0f)) < f4 && Math.abs(f2 - ((this.mDestPt2.y + this.mDestPt3.y) / 2.0f)) < f4 && this.mFreeRect) {
            this.mTouchType = 4;
        } else if (Math.abs(f - ((this.mDestPt3.x + this.mDestPt4.x) / 2.0f)) < f4 && Math.abs(f2 - ((this.mDestPt3.y + this.mDestPt4.y) / 2.0f)) < f4 && this.mFreeRect) {
            this.mTouchType = 5;
        } else if (Math.abs(f - this.mDestPt1.x) < f4 && Math.abs(f2 - this.mDestPt1.y) < f4) {
            this.mTouchType = 6;
        } else if (Math.abs(f - this.mDestPt3.x) < f4 && Math.abs(f2 - this.mDestPt3.y) < f4) {
            this.mTouchType = 7;
        } else if (Math.abs(f - this.mDestPt4.x) >= f4 || Math.abs(f2 - this.mDestPt4.y) >= f4) {
            if (checkPointInRect(f, f2, this.mDestPt1, this.mDestPt2, this.mDestPt3, this.mDestPt4)) {
                this.mTouchType = 1;
            }
        } else {
            this.mTouchType = 8;
        }
        this.mPrevX = f;
        this.mPrevY = f2;
        return this.mTouchType;
    }

    /* access modifiers changed from: protected */
    public void StartMoveObject(float f, float f2) {
        int i;
        int i2;
        float f3 = f;
        float f4 = f2;
        RectF rectF = new RectF();
        HeadInfo headInfo = this.mImageData;
        if (headInfo != null) {
            i2 = headInfo.getPreviewWidth();
            i = this.mImageData.getPreviewHeight();
        } else {
            i2 = 0;
            i = 0;
        }
        int i3 = this.mTouchType;
        if (i3 == 9) {
            if (this.mRightTopRotate) {
                this.mEndPT.set(f3, f4);
                int angle = getAngle(this.mCenterPT, this.mStartPT, this.mEndPT);
                float f5 = (float) angle;
                PointF rotationPoint = getRotationPoint(this.mROI.left, this.mROI.top, f5, this.mCenterPT.x, this.mCenterPT.y);
                this.mDestPt1.set(rotationPoint.x, rotationPoint.y);
                PointF rotationPoint2 = getRotationPoint(this.mROI.right, this.mROI.top, f5, this.mCenterPT.x, this.mCenterPT.y);
                this.mDestPt2.set(rotationPoint2.x, rotationPoint2.y);
                PointF rotationPoint3 = getRotationPoint(this.mROI.right, this.mROI.bottom, f5, this.mCenterPT.x, this.mCenterPT.y);
                this.mDestPt3.set(rotationPoint3.x, rotationPoint3.y);
                PointF rotationPoint4 = getRotationPoint(this.mROI.left, this.mROI.bottom, f5, this.mCenterPT.x, this.mCenterPT.y);
                this.mDestPt4.set(rotationPoint4.x, rotationPoint4.y);
                if (this.mBoundaryType == 2) {
                    this.mAngle = angle;
                }
            } else {
                resizeRectWithRotationAngle(f, f2, i2, i, i3);
            }
        } else if (i3 > 0) {
            if (i3 == 1) {
                Log.d("tri.dung", "move");
                float f6 = f3 - this.mPrevX;
                float f7 = f4 - this.mPrevY;
                float f8 = this.mDestPt1.x + f6;
                float f9 = this.mDestPt1.y + f7;
                float f10 = this.mDestPt2.x + f6;
                float f11 = this.mDestPt2.y + f7;
                float f12 = this.mDestPt3.x + f6;
                float f13 = this.mDestPt3.y + f7;
                float f14 = this.mDestPt4.x + f6;
                float f15 = this.mDestPt4.y + f7;
                if (this.mBoundaryType == 2) {
                    float f16 = (((f8 + f10) + f12) + f14) / 4.0f;
                    float f17 = (((f9 + f11) + f13) + f15) / 4.0f;
                    if (f16 >= 0.0f && f16 <= ((float) (i2 - 1))) {
                        this.mDestPt1.x = f8;
                        this.mDestPt2.x = f10;
                        this.mDestPt3.x = f12;
                        this.mDestPt4.x = f14;
                    }
                    if (f17 >= 0.0f && f17 <= ((float) (i - 1))) {
                        this.mDestPt1.y = f9;
                        this.mDestPt2.y = f11;
                        this.mDestPt3.y = f13;
                        this.mDestPt4.y = f15;
                    }
                }
                float f18 = (this.mROI.right - this.mROI.left) + 1.0f;
                float f19 = (this.mROI.bottom - this.mROI.top) + 1.0f;
                rectF.left = Math.min(this.mDestPt4.x, Math.min(this.mDestPt3.x, Math.min(this.mDestPt1.x, this.mDestPt2.x)));
                rectF.right = Math.max(this.mDestPt4.x, Math.max(this.mDestPt3.x, Math.max(this.mDestPt1.x, this.mDestPt2.x)));
                rectF.top = Math.min(this.mDestPt4.y, Math.min(this.mDestPt3.y, Math.min(this.mDestPt1.y, this.mDestPt2.y)));
                rectF.bottom = Math.max(this.mDestPt4.y, Math.max(this.mDestPt3.y, Math.max(this.mDestPt1.y, this.mDestPt2.y)));
                this.mCenterPT.x = (rectF.left + rectF.right) / 2.0f;
                this.mCenterPT.y = (rectF.top + rectF.bottom) / 2.0f;
                PointF rotationPoint5 = getRotationPoint(this.mDestPt1, -this.mAngle, this.mCenterPT.x, this.mCenterPT.y);
                this.mROI.left = rotationPoint5.x;
                this.mROI.top = rotationPoint5.y;
                RectF rectF2 = this.mROI;
                rectF2.right = (rectF2.left + f18) - 1.0f;
                RectF rectF3 = this.mROI;
                rectF3.bottom = (rectF3.top + f19) - 1.0f;
            } else if (i3 >= 2 && i3 <= 8) {
                resizeRectWithRotationAngle(f, f2, i2, i, i3);
            }
        }
        this.mPrevX = f3;
        this.mPrevY = f4;
    }

    private void resizeRectWithRotationAngle(float f, float f2, int i, int i2, int i3) {
        RectF rectF;
        int i4 = i3;
        float f3 = f;
        float f4 = f2;
        PointF rotationPoint = getRotationPoint(f, f2, (float) (-this.mAngle), this.mCenterPT.x, this.mCenterPT.y);
        PointF rotationPoint2 = getRotationPoint(this.mPrevX, this.mPrevY, (float) (-this.mAngle), this.mCenterPT.x, this.mCenterPT.y);
        float f5 = rotationPoint.x - rotationPoint2.x;
        float f6 = rotationPoint.y - rotationPoint2.y;
        if (Math.abs(f5) <= ((float) this.limit) && Math.abs(f6) <= ((float) this.limit)) {
            if (i4 == 2 || i4 == 4) {
                rectF = getClipROI(this.mROI, i3, f5, 0.0f, 0, 0, i, i2);
            } else if (i4 == 3 || i4 == 5) {
                rectF = getClipROI(this.mROI, i3, 0.0f, f6, 0, 0, i, i2);
            } else if (i4 == 6 || i4 == 9 || i4 == 8 || i4 == 7) {
                float[] diffFromCropRatio = getDiffFromCropRatio(f5, f6, i4);
                rectF = getClipROI(this.mROI, i3, diffFromCropRatio[0], diffFromCropRatio[1], 0, 0, i, i2);
            } else {
                rectF = null;
            }
            if (this.mBoundaryType == 2 && rectF.width() > this.mInitROI.width() / 4.0f && rectF.width() < this.mInitROI.width() * 2.0f && rectF.height() > this.mInitROI.height() / 4.0f && rectF.height() < this.mInitROI.height() * 2.0f) {
                this.mROI.set(rectF);
            }
        }
    }

    public void EndMoveObject() {
        int i = this.mTouchType;
        if (i == 9 || i == 1) {
            if (this.mBoundaryType == 2) {
                PointF rotationPoint = getRotationPoint(this.mROI.left, this.mROI.top, (float) this.mAngle, this.mCenterPT.x, this.mCenterPT.y);
                this.mDestPt1.set(rotationPoint.x, rotationPoint.y);
                PointF rotationPoint2 = getRotationPoint(this.mROI.right, this.mROI.top, (float) this.mAngle, this.mCenterPT.x, this.mCenterPT.y);
                this.mDestPt2.set(rotationPoint2.x, rotationPoint2.y);
                PointF rotationPoint3 = getRotationPoint(this.mROI.right, this.mROI.bottom, (float) this.mAngle, this.mCenterPT.x, this.mCenterPT.y);
                this.mDestPt3.set(rotationPoint3.x, rotationPoint3.y);
                PointF rotationPoint4 = getRotationPoint(this.mROI.left, this.mROI.bottom, (float) this.mAngle, this.mCenterPT.x, this.mCenterPT.y);
                this.mDestPt4.set(rotationPoint4.x, rotationPoint4.y);
            }
        } else if (i > 0 && this.mBoundaryType == 2) {
            PointF rotationPoint5 = getRotationPoint(this.mROI.left, this.mROI.top, (float) this.mAngle, this.mCenterPT.x, this.mCenterPT.y);
            this.mDestPt1.set(rotationPoint5.x, rotationPoint5.y);
            PointF rotationPoint6 = getRotationPoint(this.mROI.right, this.mROI.top, (float) this.mAngle, this.mCenterPT.x, this.mCenterPT.y);
            this.mDestPt2.set(rotationPoint6.x, rotationPoint6.y);
            PointF rotationPoint7 = getRotationPoint(this.mROI.right, this.mROI.bottom, (float) this.mAngle, this.mCenterPT.x, this.mCenterPT.y);
            this.mDestPt3.set(rotationPoint7.x, rotationPoint7.y);
            PointF rotationPoint8 = getRotationPoint(this.mROI.left, this.mROI.bottom, (float) this.mAngle, this.mCenterPT.x, this.mCenterPT.y);
            this.mDestPt4.set(rotationPoint8.x, rotationPoint8.y);
            RectF rectF = new RectF();
            rectF.left = Math.min(this.mDestPt4.x, Math.min(this.mDestPt3.x, Math.min(this.mDestPt1.x, this.mDestPt2.x)));
            rectF.right = Math.max(this.mDestPt4.x, Math.max(this.mDestPt3.x, Math.max(this.mDestPt1.x, this.mDestPt2.x)));
            rectF.top = Math.min(this.mDestPt4.y, Math.min(this.mDestPt3.y, Math.min(this.mDestPt1.y, this.mDestPt2.y)));
            rectF.bottom = Math.max(this.mDestPt4.y, Math.max(this.mDestPt3.y, Math.max(this.mDestPt1.y, this.mDestPt2.y)));
            this.mCenterPT.x = (rectF.left + rectF.right) / 2.0f;
            this.mCenterPT.y = (rectF.top + rectF.bottom) / 2.0f;
        }
        this.mTouchType = 0;
    }

    /* access modifiers changed from: protected */
    public void calculateOriginal() {
        float f;
        if (this.mImageData != null) {
            float[] fArr = new float[9];
            Matrix matrix = new Matrix();
            Matrix supMatrix = this.mImageData.getSupMatrix();
            Matrix matrix2 = new Matrix();
            supMatrix.getValues(fArr);
            float f2 = 0.0f;
            if (fArr[2] > 0.0f) {
                f = 0.0f;
            } else {
                f = fArr[2];
            }
            if (fArr[5] <= 0.0f) {
                f2 = fArr[5];
            }
            matrix.postScale(fArr[0], fArr[4]);
            matrix.postTranslate(f, f2);
            matrix.invert(matrix2);
            RectF rectF = new RectF();
            rectF.set(this.mROI.left, this.mROI.top, this.mROI.right, this.mROI.bottom);
            RectF rectF2 = new RectF();
            matrix2.mapRect(rectF2, rectF);
            this.mOriginalROI.set(rectF2.left, rectF2.top, rectF2.right, rectF2.bottom);
            float[] fArr2 = new float[8];
            fArr2[0] = this.mCenterPT.x;
            fArr2[1] = this.mCenterPT.y;
            matrix2.mapPoints(fArr2);
            PointF pointF = this.mOriginalCenterPT;
            pointF.x = (float) ((int) fArr2[0]);
            pointF.y = (float) ((int) fArr2[1]);
            fArr2[0] = this.mDestPt1.x;
            fArr2[1] = this.mDestPt1.y;
            fArr2[2] = this.mDestPt2.x;
            fArr2[3] = this.mDestPt2.y;
            fArr2[4] = this.mDestPt3.x;
            fArr2[5] = this.mDestPt3.y;
            fArr2[6] = this.mDestPt4.x;
            fArr2[7] = this.mDestPt4.y;
            matrix2.mapPoints(fArr2);
            PointF pointF2 = this.mOriginalDestPt1;
            pointF2.x = (float) ((int) fArr2[0]);
            pointF2.y = (float) ((int) fArr2[1]);
            PointF pointF3 = this.mOriginalDestPt2;
            pointF3.x = (float) ((int) fArr2[2]);
            pointF3.y = (float) ((int) fArr2[3]);
            PointF pointF4 = this.mOriginalDestPt3;
            pointF4.x = (float) ((int) fArr2[4]);
            pointF4.y = (float) ((int) fArr2[5]);
            PointF pointF5 = this.mOriginalDestPt4;
            pointF5.x = (float) ((int) fArr2[6]);
            pointF5.y = (float) ((int) fArr2[7]);
        }
    }

    public PointF getPrevPT() {
        return new PointF(this.mPrevX, this.mPrevY);
    }

    public PointF getCenterPT() {
        PointF pointF = new PointF();
        pointF.set(this.mCenterPT.x, this.mCenterPT.y);
        return pointF;
    }

    public PointF getOriginalDestPt1() {
        PointF pointF = new PointF();
        pointF.set(this.mOriginalDestPt1.x, this.mOriginalDestPt1.y);
        return pointF;
    }

    public PointF getOriginalDestPt2() {
        PointF pointF = new PointF();
        pointF.set(this.mOriginalDestPt2.x, this.mOriginalDestPt2.y);
        return pointF;
    }

    public PointF getOriginalDestPt3() {
        PointF pointF = new PointF();
        pointF.set(this.mOriginalDestPt3.x, this.mOriginalDestPt3.y);
        return pointF;
    }

    public PointF getOriginalDestPt4() {
        PointF pointF = new PointF();
        pointF.set(this.mOriginalDestPt4.x, this.mOriginalDestPt4.y);
        return pointF;
    }

    public void setDestROI(float f, float f2, float f3, float f4) {
        this.mROI.set(f, f2, f3, f4);
        float f5 = (float) ((int) ((this.mROI.left + this.mROI.right) / 2.0f));
        float f6 = (float) ((int) ((this.mROI.top + this.mROI.bottom) / 2.0f));
        PointF rotationPoint = getRotationPoint(this.mROI.left, this.mROI.top, (float) this.mAngle, f5, f6);
        this.mDestPt1.set(rotationPoint.x, rotationPoint.y);
        PointF rotationPoint2 = getRotationPoint(this.mROI.right, this.mROI.top, (float) this.mAngle, f5, f6);
        this.mDestPt2.set(rotationPoint2.x, rotationPoint2.y);
        PointF rotationPoint3 = getRotationPoint(this.mROI.right, this.mROI.bottom, (float) this.mAngle, f5, f6);
        this.mDestPt3.set(rotationPoint3.x, rotationPoint3.y);
        PointF rotationPoint4 = getRotationPoint(this.mROI.left, this.mROI.bottom, (float) this.mAngle, f5, f6);
        this.mDestPt4.set(rotationPoint4.x, rotationPoint4.y);
        this.mCenterPT.x = (this.mROI.left + this.mROI.right) / 2.0f;
        this.mCenterPT.y = (this.mROI.top + this.mROI.bottom) / 2.0f;
    }

    private void initResource(Context context, HeadInfo headInfo) {
        this.mImageData = headInfo;
        this.mOriginalROI = null;
        this.mStartPT = null;
        this.mCenterPT = null;
        this.mOriginalCenterPT = null;
        this.mEndPT = null;
        this.mAngle = 0;
        this.mStartPT = new PointF();
        this.mCenterPT = new PointF();
        this.mOriginalCenterPT = new PointF();
        this.mEndPT = new PointF();
        this.mDestPt1 = new PointF();
        this.mDestPt2 = new PointF();
        this.mDestPt3 = new PointF();
        this.mDestPt4 = new PointF();
        this.mOriginalDestPt1 = new PointF();
        this.mOriginalDestPt2 = new PointF();
        this.mOriginalDestPt3 = new PointF();
        this.mOriginalDestPt4 = new PointF();
        this.mROI = new RectF();
        this.mInitROI = new RectF();
        this.mOriginalROI = new RectF();
        this.mDrawROI = new RectF();
        this.mDrawLinePaint = new Paint();
        this.mDrawLinePaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mDrawLinePaint.setPathEffect(new DashPathEffect(new float[]{5.0f, 5.0f}, 3.0f));
        this.mDrawLinePaint.setStyle(Style.STROKE);
        this.mDrawLinePaint.setStrokeWidth(1.0f);
        this.mDrawLinePaint1 = new Paint();
        this.mDrawLinePaint1.setColor(-1);
        this.mDrawLinePaint1.setStyle(Style.STROKE);
        this.mDrawLinePaint1.setStrokeWidth(1.0f);
        this.mRectPaint = new Paint();
        this.mRectPaint.setStrokeWidth(4.0f);
        this.mRectPaint.setStyle(Style.STROKE);
        this.mRectPaint.setColor(-15638656);
        this.mGreyPaint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0f);
        this.mGreyPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        HeadInfo headInfo2 = this.mImageData;
        if (headInfo2 != null) {
            this.mPreviewWidth = headInfo2.getDrawCanvasRoi().width();
            this.mPreviewHeight = this.mImageData.getDrawCanvasRoi().height();
        }
    }

    private float[] getDiffFromCropRatio(float f, float f2, int i) {
        float[] fArr = new float[2];
        float abs = Math.abs(f);
        float abs2 = Math.abs(f2);
        if (abs <= abs2) {
            float width = (abs2 * this.mROI.width()) / this.mROI.height();
            switch (i) {
                case 6:
                case 7:
                    if (f2 >= 0.0f) {
                        fArr[0] = width;
                        fArr[1] = f2;
                        break;
                    } else {
                        fArr[0] = -width;
                        fArr[1] = f2;
                        break;
                    }
                case 8:
                case 9:
                    if (f2 >= 0.0f) {
                        fArr[0] = -width;
                        fArr[1] = f2;
                        break;
                    } else {
                        fArr[0] = width;
                        fArr[1] = f2;
                        break;
                    }
            }
        } else {
            float height = (abs * this.mROI.height()) / this.mROI.width();
            switch (i) {
                case 6:
                case 7:
                    if (f >= 0.0f) {
                        fArr[0] = f;
                        fArr[1] = height;
                        break;
                    } else {
                        fArr[0] = f;
                        fArr[1] = -height;
                        break;
                    }
                case 8:
                case 9:
                    if (f >= 0.0f) {
                        fArr[0] = f;
                        fArr[1] = -height;
                        break;
                    } else {
                        fArr[0] = f;
                        fArr[1] = height;
                        break;
                    }
            }
        }
        return fArr;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x006a, code lost:
        r0 = r4 - r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0058, code lost:
        r0 = r4 + r7;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private RectF getClipROI(RectF r4, int r5, float r6, float r7, int r8, int r9, int r10, int r11) {
        /*
            r3 = this;
            android.graphics.RectF r8 = new android.graphics.RectF
            r8.<init>()
            float r9 = r4.left
            float r10 = r4.top
            float r11 = r4.right
            float r0 = r4.bottom
            int r1 = r3.mBoundaryType
            r2 = 2
            if (r1 != r2) goto L_0x007e
            switch(r5) {
                case 1: goto L_0x0076;
                case 2: goto L_0x006d;
                case 3: goto L_0x0064;
                case 4: goto L_0x005b;
                case 5: goto L_0x0052;
                case 6: goto L_0x0043;
                case 7: goto L_0x0034;
                case 8: goto L_0x0025;
                case 9: goto L_0x0016;
                default: goto L_0x0015;
            }
        L_0x0015:
            goto L_0x007e
        L_0x0016:
            float r5 = r4.left
            float r9 = r5 - r6
            float r5 = r4.right
            float r11 = r5 + r6
            float r5 = r4.top
            float r10 = r5 + r7
            float r4 = r4.bottom
            goto L_0x006a
        L_0x0025:
            float r5 = r4.left
            float r9 = r5 + r6
            float r5 = r4.right
            float r11 = r5 - r6
            float r5 = r4.top
            float r10 = r5 - r7
            float r4 = r4.bottom
            goto L_0x0058
        L_0x0034:
            float r5 = r4.left
            float r9 = r5 - r6
            float r5 = r4.right
            float r11 = r5 + r6
            float r5 = r4.top
            float r10 = r5 - r7
            float r4 = r4.bottom
            goto L_0x0058
        L_0x0043:
            float r5 = r4.left
            float r9 = r5 + r6
            float r5 = r4.right
            float r11 = r5 - r6
            float r5 = r4.top
            float r10 = r5 + r7
            float r4 = r4.bottom
            goto L_0x006a
        L_0x0052:
            float r5 = r4.top
            float r10 = r5 - r7
            float r4 = r4.bottom
        L_0x0058:
            float r0 = r4 + r7
            goto L_0x007e
        L_0x005b:
            float r5 = r4.left
            float r9 = r5 - r6
            float r4 = r4.right
            float r11 = r4 + r6
            goto L_0x007e
        L_0x0064:
            float r5 = r4.top
            float r10 = r5 + r7
            float r4 = r4.bottom
        L_0x006a:
            float r0 = r4 - r7
            goto L_0x007e
        L_0x006d:
            float r5 = r4.left
            float r9 = r5 + r6
            float r4 = r4.right
            float r11 = r4 - r6
            goto L_0x007e
        L_0x0076:
            float r9 = r4.left
            float r11 = r4.right
            float r10 = r4.top
            float r0 = r4.bottom
        L_0x007e:
            r8.set(r9, r10, r11, r0)
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.softteam.background.selffie.view.HeadController.getClipROI(android.graphics.RectF, int, float, float, int, int, int, int):android.graphics.RectF");
    }

    public PointF getDestPt1() {
        return this.mDestPt1;
    }

    public PointF getDestPt2() {
        return this.mDestPt2;
    }

    public PointF getDestPt3() {
        return this.mDestPt3;
    }

    public PointF getDestPt4() {
        return this.mDestPt4;
    }

    public static PointF getRotationPoint(PointF pointF, int i, float f, float f2) {
        PointF pointF2 = new PointF();
        float f3 = pointF.x;
        float f4 = pointF.y;
        double radians = (double) ((float) Math.toRadians((double) i));
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        double d = (double) (f3 - f);
        double d2 = (double) (f4 - f2);
        pointF2.x = (float) ((int) Math.round((d * cos) - (d2 * sin)));
        pointF2.y = (float) ((int) Math.round((d * sin) + (d2 * cos)));
        pointF2.x += f;
        pointF2.y += f2;
        return pointF2;
    }

    public static PointF getRotationPoint(float f, float f2, float f3, float f4, float f5) {
        PointF pointF = new PointF();
        double radians = (double) ((float) Math.toRadians((double) f3));
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        double d = (double) (f - f4);
        double d2 = (double) (f2 - f5);
        pointF.x = (float) ((int) Math.round((d * cos) - (d2 * sin)));
        pointF.y = (float) ((int) Math.round((d * sin) + (d2 * cos)));
        pointF.x = (float) ((int) (pointF.x + f4));
        pointF.y = (float) ((int) (pointF.y + f5));
        return pointF;
    }

    public static boolean checkPointInRect(float f, float f2, PointF pointF, PointF pointF2, PointF pointF3, PointF pointF4) {
        PointF pointF5 = new PointF();
        pointF5.set(f, f2);
        return ccw(pointF, pointF2, pointF5) > 0.0f && ccw(pointF2, pointF3, pointF5) > 0.0f && ccw(pointF3, pointF4, pointF5) > 0.0f && ccw(pointF4, pointF, pointF5) > 0.0f;
    }

    public static float ccw(PointF pointF, PointF pointF2, PointF pointF3) {
        return ((pointF2.x - pointF.x) * (pointF3.y - pointF.y)) - ((pointF2.y - pointF.y) * (pointF3.x - pointF.x));
    }

    public static int getDpToPixel(Context context, int i) {
        return (int) (((double) (((float) i) * context.getResources().getDisplayMetrics().density)) + 0.5d);
    }

    public static int getAngle(PointF pointF, PointF pointF2, PointF pointF3) {
        PointF pointF4 = new PointF();
        PointF pointF5 = new PointF();
        pointF4.x = pointF2.x - pointF.x;
        pointF4.y = pointF2.y - pointF.y;
        pointF5.x = pointF3.x - pointF.x;
        pointF5.y = pointF3.y - pointF.y;
        int degrees = (int) Math.toDegrees(Math.acos(((double) ((pointF4.x * pointF5.x) + (pointF4.y * pointF5.y))) / (Math.sqrt((double) ((pointF4.x * pointF4.x) + (pointF4.y * pointF4.y))) * Math.sqrt((double) ((pointF5.x * pointF5.x) + (pointF5.y * pointF5.y))))));
        if ((pointF4.x * pointF5.y) - (pointF4.y * pointF5.x) < 0.0f) {
            degrees = 360 - degrees;
        }
        return degrees > 360 ? degrees - 360 : degrees;
    }
}

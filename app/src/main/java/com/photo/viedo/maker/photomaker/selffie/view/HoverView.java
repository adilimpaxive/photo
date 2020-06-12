package com.photo.viedo.maker.photomaker.selffie.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import com.photo.viedo.maker.photomaker.EraserActivity;
import com.photo.viedo.maker.photomaker.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class HoverView extends View {
    static final int DRAG = 1;
    public static int ERASE_MODE = 0;
    public static int MAGIC_MODE = 2;
    public static int MAGIC_MODE_RESTORE = 3;
    public static int MIRROR_MODE = 5;
    public static int MOVING_MODE = 4;
    static final int NONE = 0;
    public static int POINTER_DISTANCE = 0;
    private static final float TOUCH_TOLERANCE = 4.0f;
    public static int UNERASE_MODE = 1;
    static final int ZOOM = 2;
    static Paint eraser;
    private static Path mPath;
    private static Path mPathErase;
    public static int mode;
    static Canvas newCanvas;
    static Paint uneraser;
    PointF DownPT = new PointF();
    final int STACKSIZE = 10;
    String TAG = "tri.dung";

    /* renamed from: bm */
    Bitmap f74bm;
    int bmHeight;
    int bmWidth;
    ArrayList<Boolean> checkMirrorStep;
    Bitmap clippedBitmap;
    int currentIndex = -1;
    public PointF drawingPoint;
    private String filename;
    int[] lastBitmapData;
    private Paint mBitmapPaint;
    public Context mContext;
    private Paint mMaskPaint;

    /* renamed from: mX */
    private float f75mX;

    /* renamed from: mY */
    private float f76mY;
    Bitmap magicPointer;
    public int magicThreshold = 15;
    public int magicTouchRange =  200;
    Matrix matrix = new Matrix();
    PointF mid = new PointF();
    float oldDist = 1.0f;
    int[] saveBitmapData;
    Matrix savedMatrix = new Matrix();
    float scale = 1.0f;
    ArrayList<int[]> stackChange;
    PointF start = new PointF();
    private int strokeWidth = 40;
    int touchMode = 0;
    public PointF touchPoint;
    int viewHeight;
    int viewWidth;

    public HoverView(Context context, Bitmap bitmap, int i, int i2, int i3, int i4) {
        super(context);
        this.mContext = context;
        this.viewWidth = i3;
        this.viewHeight = i4;
        this.bmWidth = i;
        this.bmHeight = i2;
        setLayerType(1, null);
        init(bitmap, i, i2);
    }

    public void switchMode(int i) {
        mode = i;
        resetPath();
        saveLastMaskData();
        int i2 = mode;
        if (i2 == MAGIC_MODE || i2 == MAGIC_MODE_RESTORE) {
            this.magicPointer = BitmapFactory.decodeResource(getResources(), R.drawable.color_select);
        } else if (i2 == ERASE_MODE || i2 == UNERASE_MODE) {
            Bitmap decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.color_select);
            int i3 = this.strokeWidth;
            this.magicPointer = Bitmap.createScaledBitmap(decodeResource, i3 + 5, i3 + 5, false);
        }
        invalidate();
    }

    public int getMode() {
        return mode;
    }

    public void setMagicThreshold(int i) {
        this.magicThreshold = i;
    }

    public void mirrorImage() {
        Matrix matrix2 = new Matrix();
        matrix2.preScale(-1.0f, 1.0f);
        Bitmap bitmap = this.f74bm;
        Matrix matrix3 = matrix2;
        this.f74bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), this.f74bm.getHeight(), matrix3, true);
        bitmap.recycle();
        Bitmap bitmap2 = this.clippedBitmap;
        this.clippedBitmap = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), this.clippedBitmap.getHeight(), matrix3, true);
        bitmap2.recycle();
        Bitmap bitmap3 = this.f74bm;
        bitmap3.getPixels(this.saveBitmapData, 0, bitmap3.getWidth(), 0, 0, this.f74bm.getWidth(), this.f74bm.getHeight());
        saveLastMaskData();
        newCanvas = new Canvas(this.clippedBitmap);
        newCanvas.save();
        ((EraserActivity) this.mContext).updateUndoButton();
        invalidate();
        addToStack(true);
    }

    @SuppressLint("WrongThread")
    public String  save() {
        Bitmap saveDrawnBitmap = saveDrawnBitmap();
        File  externalStorageDirectory = Environment.getExternalStorageDirectory();
        StringBuilder sb = new StringBuilder();
        sb.append(externalStorageDirectory.getAbsolutePath());
        sb.append("/BackgroundRemover");

        File file = new File(sb.toString());
        file.mkdirs();
        File file2 = new File(file, this.filename);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            saveDrawnBitmap.compress(CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        refreshGallery(file2);
        StringBuilder sb2 = new StringBuilder();
        sb2.append(externalStorageDirectory);
        sb2.append("/BackgroundRemover/");
        sb2.append(this.filename);
        return sb2.toString();
    }

    private void refreshGallery(File file) {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(file));
        this.mContext.sendBroadcast(intent);
    }

    public void setEraseOffset(int i) {
        this.strokeWidth = i;
        float f = (float) i;
        eraser.setStrokeWidth(f);
        uneraser.setStrokeWidth(f);
        int i2 = i + 5;
        this.magicPointer = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.color_select), i2, i2, false);
        mPath.reset();
        resetPath();
        invalidate();
    }

    /* access modifiers changed from: 0000 */
    public void init(Bitmap bitmap, int i, int i2) {
        mPath = new Path();
        mPathErase = new Path();
        eraser = new Paint();
        eraser.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        eraser.setAntiAlias(true);
        eraser.setStyle(Style.STROKE);
        eraser.setStrokeJoin(Join.ROUND);
        eraser.setStrokeCap(Cap.ROUND);
        eraser.setStrokeWidth((float) this.strokeWidth);
        uneraser = new Paint();
        uneraser.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
        uneraser.setAntiAlias(true);
        uneraser.setStyle(Style.STROKE);
        uneraser.setStrokeJoin(Join.ROUND);
        uneraser.setStrokeCap(Cap.ROUND);
        uneraser.setStrokeWidth((float) this.strokeWidth);
        this.matrix.postTranslate((float) ((this.viewWidth - i) / 2), (float) ((this.viewHeight - i2) / 2));
        this.mBitmapPaint = new Paint();
        this.mBitmapPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        this.mBitmapPaint.setAntiAlias(true);
        this.mMaskPaint = new Paint();
        this.mMaskPaint.setAntiAlias(true);
        this.f74bm = bitmap;
        this.f74bm = this.f74bm.copy(Config.ARGB_8888, true);
        this.clippedBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
        newCanvas = new Canvas(this.clippedBitmap);
        newCanvas.save();
        newCanvas.drawARGB(255, 255, 255, 255);
        this.magicTouchRange = i > i2 ? i2 / 2 : i / 2;
        int i3 = i * i2;
        this.saveBitmapData = new int[i3];
        Bitmap bitmap2 = this.f74bm;
        bitmap2.getPixels(this.saveBitmapData, 0, bitmap2.getWidth(), 0, 0, this.f74bm.getWidth(), this.f74bm.getHeight());
        this.lastBitmapData = new int[i3];
        this.magicPointer = BitmapFactory.decodeResource(getResources(), R.drawable.color_select);
        float f = (float) (i / 2);
        float f2 = (float) (i2 / 2);
        this.touchPoint = new PointF(f, f2);
        this.drawingPoint = new PointF(f, f2);
        saveLastMaskData();
        this.stackChange = new ArrayList<>();
        this.checkMirrorStep = new ArrayList<>();
        addToStack(false);
        StringBuilder sb = new StringBuilder();
        sb.append("img_");
        sb.append(String.format("%d.jpg", new Object[]{Long.valueOf(System.currentTimeMillis())}));
        this.filename = sb.toString();
        POINTER_DISTANCE = (int) (this.mContext.getResources().getDisplayMetrics().density * 50.0f);
    }

    /* access modifiers changed from: 0000 */
    public void addToStack(boolean z) {
        if (this.stackChange.size() >= 10) {
            this.stackChange.remove(0);
            int i = this.currentIndex;
            if (i > 0) {
                this.currentIndex = i - 1;
            }
        }
        ArrayList<int[]> arrayList = this.stackChange;
        if (arrayList != null) {
            if (this.currentIndex == 0) {
                for (int size = arrayList.size() - 1; size > 0; size--) {
                    this.stackChange.remove(size);
                    this.checkMirrorStep.remove(size);
                }
            }
            int[] iArr = new int[(this.clippedBitmap.getWidth() * this.clippedBitmap.getHeight())];
            Bitmap bitmap = this.clippedBitmap;
            bitmap.getPixels(iArr, 0, bitmap.getWidth(), 0, 0, this.clippedBitmap.getWidth(), this.clippedBitmap.getHeight());
            this.stackChange.add(iArr);
            this.checkMirrorStep.add(Boolean.valueOf(z));
            this.currentIndex = this.stackChange.size() - 1;
        }
    }

    public void redo() {
        Log.d(this.TAG, "Redo");
        resetPath();
        ArrayList<int[]> arrayList = this.stackChange;
        if (arrayList != null && arrayList.size() > 0 && this.currentIndex < this.stackChange.size() - 1) {
            this.currentIndex++;
            if (((Boolean) this.checkMirrorStep.get(this.currentIndex)).booleanValue()) {
                Matrix matrix2 = new Matrix();
                matrix2.preScale(-1.0f, 1.0f);
                Bitmap bitmap = this.f74bm;
                this.f74bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), this.f74bm.getHeight(), matrix2, true);
                Bitmap bitmap2 = this.f74bm;
                bitmap2.getPixels(this.saveBitmapData, 0, bitmap2.getWidth(), 0, 0, this.f74bm.getWidth(), this.f74bm.getHeight());
            }
            int[] iArr = (int[]) this.stackChange.get(this.currentIndex);
            Bitmap bitmap3 = this.clippedBitmap;
            int i = this.bmWidth;
            bitmap3.setPixels(iArr, 0, i, 0, 0, i, this.bmHeight);
            invalidate();
        }
    }

    public void undo() {
        Log.d(this.TAG, "Undo");
        resetPath();
        ArrayList<int[]> arrayList = this.stackChange;
        if (arrayList != null && arrayList.size() > 0) {
            int i = this.currentIndex;
            if (i > 0) {
                this.currentIndex = i - 1;
                if (((Boolean) this.checkMirrorStep.get(this.currentIndex + 1)).booleanValue()) {
                    Matrix matrix2 = new Matrix();
                    matrix2.preScale(-1.0f, 1.0f);
                    Bitmap bitmap = this.f74bm;
                    this.f74bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), this.f74bm.getHeight(), matrix2, true);
                    Bitmap bitmap2 = this.f74bm;
                    bitmap2.getPixels(this.saveBitmapData, 0, bitmap2.getWidth(), 0, 0, this.f74bm.getWidth(), this.f74bm.getHeight());
                }
                int[] iArr = (int[]) this.stackChange.get(this.currentIndex);
                Bitmap bitmap3 = this.clippedBitmap;
                int i2 = this.bmWidth;
                bitmap3.setPixels(iArr, 0, i2, 0, 0, i2, this.bmHeight);
                invalidate();
            }
        }
    }

    public boolean checkUndoEnable() {
        ArrayList<int[]> arrayList = this.stackChange;
        return arrayList != null && arrayList.size() > 0 && this.currentIndex > 0;
    }

    public boolean checkRedoEnable() {
        ArrayList<int[]> arrayList = this.stackChange;
        return arrayList != null && arrayList.size() > 0 && this.currentIndex < this.stackChange.size() - 1;
    }

    public Bitmap drawBitmap(Bitmap bitmap) {
        int i = mode;
        if (i == ERASE_MODE || i == UNERASE_MODE) {
            if (mode == ERASE_MODE) {
                uneraser.setXfermode(new PorterDuffXfermode(Mode.SRC_ATOP));
            } else {
                uneraser.setXfermode(new PorterDuffXfermode(Mode.SRC));
            }
            float f = this.scale;
            if (f <= 1.0f) {
                f = 1.0f;
            }
            eraser.setStrokeWidth(((float) this.strokeWidth) / f);
            uneraser.setStrokeWidth(((float) this.strokeWidth) / f);
            newCanvas.drawPath(mPath, eraser);
            newCanvas.drawPath(mPathErase, uneraser);
        }
        return this.clippedBitmap;
    }

    public Bitmap saveDrawnBitmap() {
        Bitmap createBitmap = Bitmap.createBitmap(this.f74bm.getWidth(), this.f74bm.getHeight(), Config.ARGB_8888);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(createBitmap);
        canvas.save();
        canvas.drawBitmap(this.f74bm, 0.0f, 0.0f, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        canvas.drawBitmap(this.clippedBitmap, 0.0f, 0.0f, paint);
        return createBitmap;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(this.f74bm, this.matrix, this.mMaskPaint);
        canvas.drawBitmap(drawBitmap(this.f74bm), this.matrix, this.mBitmapPaint);
        int i = mode;
        if (i == MAGIC_MODE || i == MAGIC_MODE_RESTORE || i == ERASE_MODE || i == UNERASE_MODE) {
            canvas.drawBitmap(this.magicPointer, this.drawingPoint.x - ((float) (this.magicPointer.getWidth() / 2)), this.drawingPoint.y - ((float) (this.magicPointer.getHeight() / 2)), this.mMaskPaint);
        }
        super.onDraw(canvas);
    }

    public Bitmap magicEraseBitmap() {
        int i;
        int i2;
        int width = this.clippedBitmap.getWidth();
        int height = this.clippedBitmap.getHeight();
        if (this.touchPoint == null) {
            return this.clippedBitmap;
        }
        int[] iArr = new int[(this.clippedBitmap.getWidth() * this.clippedBitmap.getHeight())];
        Bitmap bitmap = this.clippedBitmap;
        bitmap.getPixels(iArr, 0, bitmap.getWidth(), 0, 0, this.clippedBitmap.getWidth(), this.clippedBitmap.getHeight());
        int i3 = (int) this.touchPoint.x;
        int i4 = (int) this.touchPoint.y;
        if (i3 > width || i3 < 0 || i4 > height || i4 < 0) {
            return this.clippedBitmap;
        }
        int i5 = (i4 * width) + i3;
        int i6 = iArr[i5];
        int[] iArr2 = this.saveBitmapData;
        int i7 = (iArr2[i5] >> 16) & 255;
        int i8 = (iArr2[i5] >> 8) & 255;
        int i9 = iArr2[i5] & 255;
        int i10 = 0;
        while (i10 < height) {
            int i11 = 0;
            while (i11 < width) {
                int i12 = (i10 * width) + i11;
                int i13 = (iArr[i12] >> 24) & 255;
                int[] iArr3 = this.saveBitmapData;
                int i14 = (iArr3[i12] >> 24) & 255;
                int i15 = (iArr3[i12] >> 16) & 255;
                int i16 = (iArr3[i12] >> 8) & 255;
                int i17 = iArr3[i12] & 255;
                int i18 = (this.lastBitmapData[i12] >> 24) & 255;
                if (i13 > 0) {
                    i = height;
                    i2 = width;
                    if (Math.abs(i15 - i7) < this.magicThreshold && Math.abs(i16 - i8) < this.magicThreshold && Math.abs(i17 - i9) < this.magicThreshold) {
                        iArr[i12] = 0;
                        i11++;
                        width = i2;
                        height = i;
                    }
                } else {
                    i2 = width;
                    i = height;
                }
                if (i18 > 0 && i13 == 0 && (Math.abs(i15 - i7) >= this.magicThreshold || Math.abs(i16 - i8) >= this.magicThreshold || Math.abs(i17 - i9) >= this.magicThreshold)) {
                    iArr[i12] = (i15 << 16) | (i16 << 8) | i17 | (i14 << 24);
                }
                i11++;
                width = i2;
                height = i;
            }
            int i19 = height;
            i10++;
            width = width;
        }
        int i20 = height;
        this.clippedBitmap.setPixels(iArr, 0, width, 0, 0, width, height);
        return this.clippedBitmap;
    }

    public Bitmap magicRestoreBitmap() {
        int width = this.clippedBitmap.getWidth();
        int height = this.clippedBitmap.getHeight();
        if (this.touchPoint == null) {
            return this.clippedBitmap;
        }
        int[] iArr = new int[(this.clippedBitmap.getWidth() * this.clippedBitmap.getHeight())];
        Bitmap bitmap = this.clippedBitmap;
        bitmap.getPixels(iArr, 0, bitmap.getWidth(), 0, 0, this.clippedBitmap.getWidth(), this.clippedBitmap.getHeight());
        int i = (int) this.touchPoint.x;
        int i2 = (int) this.touchPoint.y;
        if (i > width || i < 0 || i2 > height || i2 < 0) {
            return this.clippedBitmap;
        }
        int[] iArr2 = this.saveBitmapData;
        int i3 = (i2 * width) + i;
        int i4 = (iArr2[i3] >> 16) & 255;
        int i5 = (iArr2[i3] >> 8) & 255;
        int i6 = iArr2[i3] & 255;
        for (int i7 = 0; i7 < height; i7++) {
            for (int i8 = 0; i8 < width; i8++) {
                int i9 = (i7 * width) + i8;
                int i10 = (iArr[i9] >> 24) & 255;
                int i11 = (this.lastBitmapData[i9] >> 24) & 255;
                if (i10 == 0) {
                    int[] iArr3 = this.saveBitmapData;
                    int i12 = (iArr3[i9] >> 24) & 255;
                    int i13 = (iArr3[i9] >> 16) & 255;
                    int i14 = (iArr3[i9] >> 8) & 255;
                    int i15 = iArr3[i9] & 255;
                    if (Math.abs(i13 - i4) < this.magicThreshold && Math.abs(i14 - i5) < this.magicThreshold && Math.abs(i15 - i6) < this.magicThreshold) {
                        iArr[i9] = (i13 << 16) | (i14 << 8) | i15 | (i12 << 24);
                    }
                } else if (i10 > 0 && i11 == 0) {
                    int[] iArr4 = this.saveBitmapData;
                    int i16 = iArr4[i9];
                    int i17 = (iArr4[i9] >> 16) & 255;
                    int i18 = (iArr4[i9] >> 8) & 255;
                    int i19 = iArr4[i9] & 255;
                    if (Math.abs(i17 - i4) >= this.magicThreshold || Math.abs(i18 - i5) >= this.magicThreshold || Math.abs(i19 - i6) >= this.magicThreshold) {
                        iArr[i9] = 0;
                    }
                }
            }
        }
        this.clippedBitmap.setPixels(iArr, 0, width, 0, 0, width, height);
        return this.clippedBitmap;
    }

    public void saveLastMaskData() {
        Bitmap bitmap = this.clippedBitmap;
        bitmap.getPixels(this.lastBitmapData, 0, bitmap.getWidth(), 0, 0, this.clippedBitmap.getWidth(), this.clippedBitmap.getHeight());
    }

    public void resetPath() {
        mPath.reset();
        mPathErase.reset();
    }

    public void invalidateView() {
        invalidate();
    }

    private void touch_start(float f, float f2) {
        mPath.reset();
        mPathErase.reset();
        if (mode == ERASE_MODE) {
            mPath.moveTo(f, f2);
        } else {
            mPathErase.moveTo(f, f2);
        }
        this.f75mX = f;
        this.f76mY = f2;
    }

    private void touch_move(float f, float f2) {
        float abs = Math.abs(f - this.f75mX);
        float abs2 = Math.abs(f2 - this.f76mY);
        if (abs >= TOUCH_TOLERANCE || abs2 >= TOUCH_TOLERANCE) {
            if (mode == ERASE_MODE) {
                Path path = mPath;
                float f3 = this.f75mX;
                float f4 = this.f76mY;
                path.quadTo(f3, f4, (f + f3) / 2.0f, (f2 + f4) / 2.0f);
            } else {
                Path path2 = mPathErase;
                float f5 = this.f75mX;
                float f6 = this.f76mY;
                path2.quadTo(f5, f6, (f + f5) / 2.0f, (f2 + f6) / 2.0f);
            }
            this.f75mX = f;
            this.f76mY = f2;
        }
    }

    private void touch_up() {
        if (mode == ERASE_MODE) {
            mPath.lineTo(this.f75mX, this.f76mY);
        } else {
            mPathErase.lineTo(this.f75mX, this.f76mY);
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        int i = mode;
        if (i == ERASE_MODE || i == UNERASE_MODE) {
            y -= (float) POINTER_DISTANCE;
        }
        int i2 = mode;
        if (i2 == MAGIC_MODE || i2 == MAGIC_MODE_RESTORE || i2 == ERASE_MODE || i2 == UNERASE_MODE) {
            PointF pointF = this.drawingPoint;
            pointF.x = x;
            pointF.y = y;
        }
        if (mode != MOVING_MODE) {
            float[] fArr = new float[9];
            this.matrix.getValues(fArr);
            float f = fArr[0];
            RectF rectF = new RectF();
            this.matrix.mapRect(rectF);
            x = (x - rectF.left) / f;
            y = (y - rectF.top) / f;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.savedMatrix.set(this.matrix);
            this.start.set(motionEvent.getX(), motionEvent.getY());
            this.touchMode = 1;
            int i3 = mode;
            if (i3 == ERASE_MODE || i3 == UNERASE_MODE) {
                touch_start(x, y);
            } else if (i3 == MOVING_MODE) {
                this.DownPT.x = motionEvent.getX();
                this.DownPT.y = motionEvent.getY();
            }
            invalidate();
        } else if (actionMasked == 1) {
            int i4 = mode;
            if (i4 == ERASE_MODE || i4 == UNERASE_MODE) {
                touch_up();
                Log.d(this.TAG, "add to stack");
                addToStack(false);
            } else if (i4 == MAGIC_MODE || i4 == MAGIC_MODE_RESTORE) {
                PointF pointF2 = this.touchPoint;
                pointF2.x = x;
                pointF2.y = y;
                saveLastMaskData();
                ((EraserActivity) this.mContext).resetSeekBar();
            }
            ((EraserActivity) this.mContext).updateUndoButton();
            ((EraserActivity) this.mContext).updateRedoButton();
            invalidate();
            resetPath();
        } else if (actionMasked == 2) {
            int i5 = this.touchMode;
            if (i5 == 1) {
                int i6 = mode;
                if (i6 == ERASE_MODE || i6 == UNERASE_MODE) {
                    touch_move(x, y);
                } else if (i6 == MOVING_MODE) {
                    PointF pointF3 = new PointF(motionEvent.getX() - this.DownPT.x, motionEvent.getY() - this.DownPT.y);
                    this.matrix.postTranslate(pointF3.x, pointF3.y);
                    this.DownPT.x = motionEvent.getX();
                    this.DownPT.y = motionEvent.getY();
                } else if (i6 == MAGIC_MODE || i6 == MAGIC_MODE_RESTORE) {
                    PointF pointF4 = this.touchPoint;
                    pointF4.x = x;
                    pointF4.y = y;
                }
                invalidate();
            } else if (i5 == 2 && mode == MOVING_MODE) {
                float spacing = spacing(motionEvent);
                if (spacing > 5.0f) {
                    this.matrix.set(this.savedMatrix);
                    this.scale = spacing / this.oldDist;
                    Matrix matrix2 = this.matrix;
                    float f2 = this.scale;
                    matrix2.postScale(f2, f2, this.mid.x, this.mid.y);
                    String str = this.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("scale =");
                    sb.append(this.scale);
                    Log.d(str, sb.toString());
                }
                invalidate();
            }
        } else if (actionMasked == 5) {
            this.oldDist = spacing(motionEvent);
            if (this.oldDist > 5.0f) {
                this.savedMatrix.set(this.matrix);
                midPoint(this.mid, motionEvent);
                this.touchMode = 2;
            }
        } else if (actionMasked == 6) {
            this.touchMode = 0;
            Log.d(this.TAG, "mode=NONE");
        }
        return true;
    }

    private float spacing(MotionEvent motionEvent) {
        float x = motionEvent.getX(0) - motionEvent.getX(1);
        float y = motionEvent.getY(0) - motionEvent.getY(1);
        return (float) Math.sqrt((double) ((x * x) + (y * y)));
    }

    private void midPoint(PointF pointF, MotionEvent motionEvent) {
        pointF.set((motionEvent.getX(0) + motionEvent.getX(1)) / 2.0f, (motionEvent.getY(0) + motionEvent.getY(1)) / 2.0f);
    }
}

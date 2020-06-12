package com.photo.viedo.maker.photomaker.selffie.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import com.photo.viedo.maker.photomaker.R;

import java.io.File;
import java.io.FileOutputStream;

public class TryonView extends View {
    static Bitmap headBitmap;
    static Context mContext;
    public static int magicTouch;
    public static int mode;
    private final int ICON_HEIGHT = 23;
    private final int ICON_WIDTH = 23;
    private Paint mBitmapPaint = new Paint(4);
    private Bitmap mBm_lrtb = null;
    private Bitmap mBm_rotate = null;
    private HeadController mHeadRect = null;
    private HeadInfo mImageData;
    private boolean showRect = true;

    public TryonView(Context context, Bitmap bitmap, int i, int i2) {
        super(context);
        mContext = context;
        setLayerType(1, null);
        init(bitmap, i, i2);
    }

    public static void save() {
        Bitmap bitmap = headBitmap;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(Environment.getExternalStorageDirectory(), "pippo.png"));
            bitmap.compress(CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: 0000 */
    public void init(Bitmap bitmap, int i, int i2) {
        this.mBitmapPaint.setAntiAlias(true);
        headBitmap = bitmap;
        this.mImageData = new HeadInfo(headBitmap, i, i2);
        Rect rect = new Rect();
        rect.set(this.mImageData.getDrawCanvasRoi());
        HeadController headController = new HeadController(mContext, rect, this.mImageData, 2, false);
        this.mHeadRect = headController;
     //   Bitmap decodeResource = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.photo_crop_handler);
     //   Bitmap decodeResource2 = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.photo_crop_handler_rotate);
        int i3 = (int) (mContext.getResources().getDisplayMetrics().density * 23.0f);
        int i4 = (int) (mContext.getResources().getDisplayMetrics().density * 23.0f);
    //    this.mBm_lrtb = Bitmap.createScaledBitmap(decodeResource, i3, i4, true);
    //    this.mBm_rotate = Bitmap.createScaledBitmap(decodeResource2, i3, i4, true);
    //    decodeResource.recycle();
    //    decodeResource2.recycle();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        drawRectBdry(canvas, new Matrix());
        super.onDraw(canvas);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mHeadRect.InitMoveObject(motionEvent.getX() - ((float) this.mImageData.getPreviewPaddingX()), motionEvent.getY() - ((float) this.mImageData.getPreviewPaddingY()));
        } else if (action == 1) {
            this.mHeadRect.EndMoveObject();
            if (this.mHeadRect.getDrawBdry().contains(motionEvent.getX(), motionEvent.getY())) {
                this.showRect = true;
            } else {
                this.showRect = false;
            }
        } else if (action == 2) {
            this.mHeadRect.StartMoveObject(motionEvent.getX() - ((float) this.mImageData.getPreviewPaddingX()), motionEvent.getY() - ((float) this.mImageData.getPreviewPaddingY()));
        }
        invalidate();
        return true;
    }

    private void drawRectBdry(Canvas canvas, Matrix matrix) {
        if (this.mHeadRect != null) {
            Paint paint = new Paint();
            paint.setColor(-15638656);
            paint.setStyle(Style.FILL);
            float[] fArr = {this.mHeadRect.getDrawCenterPt().x, this.mHeadRect.getDrawCenterPt().y};
            matrix.mapPoints(fArr);
            RectF rectF = new RectF();
            rectF.set(this.mHeadRect.getDrawBdry());
            HeadController headController = this.mHeadRect;
            if (headController != null) {
                if (headController.GetObjectBitmap() != null) {
                    canvas.save();
                    canvas.rotate((float) this.mHeadRect.getAngle(), fArr[0], fArr[1]);
                    int width = this.mHeadRect.GetObjectBitmap().getWidth();
                    int height = this.mHeadRect.GetObjectBitmap().getHeight();
                    Rect rect = new Rect();
                    rect.set(0, 0, width, height);
                    if (!this.mHeadRect.GetObjectBitmap().isRecycled()) {
                        canvas.drawBitmap(this.mHeadRect.GetObjectBitmap(), rect, rectF, null);
                    }
                    canvas.restore();
                }
                if (this.showRect) {
                    canvas.save();
                    canvas.rotate((float) this.mHeadRect.getAngle(), fArr[0], fArr[1]);
                    Paint paint2 = paint;
                    canvas.drawRect(rectF.left - 2.0f, rectF.top - 2.0f, rectF.right + 2.0f, rectF.top + 2.0f, paint2);
                    canvas.drawRect(rectF.right - 2.0f, rectF.top - 2.0f, rectF.right + 2.0f, rectF.bottom + 2.0f, paint2);
                    canvas.drawRect(rectF.left - 2.0f, rectF.bottom - 2.0f, rectF.right + 2.0f, rectF.bottom + 2.0f, paint2);
                    canvas.drawRect(rectF.left - 2.0f, rectF.top - 2.0f, rectF.left + 2.0f, rectF.bottom + 2.0f, paint2);
                    canvas.drawBitmap(this.mBm_lrtb, rectF.left - ((float) (this.mBm_lrtb.getWidth() / 2)), rectF.top - ((float) (this.mBm_lrtb.getHeight() / 2)), null);
                    canvas.drawBitmap(this.mBm_lrtb, rectF.left - ((float) (this.mBm_lrtb.getWidth() / 2)), rectF.bottom - ((float) (this.mBm_lrtb.getHeight() / 2)), null);
                    canvas.drawBitmap(this.mBm_lrtb, rectF.right - ((float) (this.mBm_lrtb.getWidth() / 2)), rectF.bottom - ((float) (this.mBm_lrtb.getHeight() / 2)), null);
                    canvas.drawBitmap(this.mBm_rotate, rectF.right - ((float) (this.mBm_rotate.getWidth() / 2)), rectF.top - ((float) (this.mBm_rotate.getHeight() / 2)), null);
                    canvas.restore();
                }
            }
        }
    }
}

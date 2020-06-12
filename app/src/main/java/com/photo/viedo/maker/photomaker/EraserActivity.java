package com.photo.viedo.maker.photomaker;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;


import com.photo.viedo.maker.photomaker.selffie.view.HoverView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class EraserActivity extends Activity implements OnClickListener {
    int actionBarHeight;
    int bmHeight;
    double bmRatio;
    int bmWidth;
    int bottombarHeight;
    ImageView brushSize1Button;
    ImageView brushSize2Button;
    ImageView brushSize3Button;
    ImageView brushSize4Button;
    int currentColor = 0;
    RelativeLayout eraserLayout;
    Button eraserMainButton;
    ImageView eraserSubButton;
    private String imagePath;
    private Intent intent;
    private Bitmap mBitmap;
    private ContentResolver mContentResolver;
    double mDensity;
    HoverView mHoverView;
    RelativeLayout mLayout;
    RelativeLayout magicLayout;
    ImageView magicRemoveButton;
    ImageView magicRestoreButton;
    SeekBar magicSeekbar;
    Button nextButton;
    ImageView redoButton;
    ImageView undoButton;
    int viewHeight;
    double viewRatio;
    int viewWidth;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_eraser);
        this.mContentResolver = getContentResolver();
        byte[] byteArray = getIntent().getExtras().getByteArray("picture");
        assert byteArray != null;
        this.mBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        this.mLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        this.mDensity = (double) getResources().getDisplayMetrics().density;
        double d = this.mDensity;
        this.actionBarHeight = (int) (110.0d * d);
        this.bottombarHeight = (int) (d * 60.0d);
        this.viewWidth = getResources().getDisplayMetrics().widthPixels;
        this.viewHeight = (getResources().getDisplayMetrics().heightPixels - this.actionBarHeight) - this.bottombarHeight;
        this.viewRatio = ((double) this.viewHeight) / ((double) this.viewWidth);
        this.bmRatio = ((double) this.mBitmap.getHeight()) / ((double) this.mBitmap.getWidth());
        if (this.bmRatio < this.viewRatio) {
            int i = this.viewWidth;
            this.bmWidth = i;
            this.bmHeight = (int) (((double) i) * (((double) this.mBitmap.getHeight()) / ((double) this.mBitmap.getWidth())));
        } else {
            int i2 = this.viewHeight;
            this.bmHeight = i2;
            this.bmWidth = (int) (((double) i2) * (((double) this.mBitmap.getWidth()) / ((double) this.mBitmap.getHeight())));
        }
        this.mBitmap = Bitmap.createScaledBitmap(this.mBitmap, this.bmWidth, this.bmHeight, false);
        HoverView hoverView = new HoverView(this, this.mBitmap, this.bmWidth, this.bmHeight, this.viewWidth, this.viewHeight);
        this.mHoverView = hoverView;
        this.mHoverView.setLayoutParams(new LayoutParams(this.viewWidth, this.viewHeight));
        this.mLayout.addView(this.mHoverView);
        initButton();
    }

    public void initButton() {
        this.eraserMainButton = (Button) findViewById(R.id.eraseButton);
        this.eraserMainButton.setOnClickListener((OnClickListener) this);
        this.eraserSubButton = (ImageView) findViewById(R.id.erase_sub_button);
        this.eraserSubButton.setOnClickListener(this);
        this.brushSize1Button = (ImageView) findViewById(R.id.brush_size_1_button);
        this.brushSize1Button.setOnClickListener(this);
        this.brushSize2Button = (ImageView) findViewById(R.id.brush_size_2_button);
        this.brushSize2Button.setOnClickListener(this);
        this.brushSize3Button = (ImageView) findViewById(R.id.brush_size_3_button);
        this.brushSize3Button.setOnClickListener(this);
        this.brushSize4Button = (ImageView) findViewById(R.id.brush_size_4_button);
        this.brushSize4Button.setOnClickListener(this);
        this.magicSeekbar = (SeekBar) findViewById(R.id.magic_seekbar);
        this.magicSeekbar.setProgress(15);
        this.magicSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                EraserActivity.this.mHoverView.setMagicThreshold(seekBar.getProgress());
                int mode = EraserActivity.this.mHoverView.getMode();
                HoverView hoverView = EraserActivity.this.mHoverView;
                if (mode == HoverView.MAGIC_MODE) {
                    EraserActivity.this.mHoverView.magicEraseBitmap();
                } else {
                    int mode2 = EraserActivity.this.mHoverView.getMode();
                    HoverView hoverView2 = EraserActivity.this.mHoverView;
                    if (mode2 == HoverView.MAGIC_MODE_RESTORE) {
                        EraserActivity.this.mHoverView.magicRestoreBitmap();
                    }
                }
                EraserActivity.this.mHoverView.invalidateView();
            }
        });
        this.nextButton = (Button) findViewById(R.id.nextButton);
        this.nextButton.setOnClickListener(this);
        this.undoButton = (ImageView) findViewById(R.id.undoButton);
        this.undoButton.setOnClickListener(this);
        this.redoButton = (ImageView) findViewById(R.id.redoButton);
        this.redoButton.setOnClickListener(this);
        updateRedoButton();
        this.eraserLayout = (RelativeLayout) findViewById(R.id.eraser_layout);
        this.magicLayout = (RelativeLayout) findViewById(R.id.magicWand_layout);
        this.eraserMainButton.setSelected(true);
    }

    private Bitmap getBitmap(String str) {
        Uri imageUri = getImageUri(str);
        try {
            InputStream openInputStream = this.mContentResolver.openInputStream(imageUri);
            Options options = new Options();
            int i = 1;
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(openInputStream, null, options);
            openInputStream.close();
            if (options.outHeight > 1024 || options.outWidth > 1024) {
                i = (int) Math.pow(2.0d, (double) ((int) Math.round(Math.log(1024.0d / ((double) Math.max(options.outHeight, options.outWidth))) / Math.log(0.5d))));
            }
            Options options2 = new Options();
            options2.inSampleSize = i;
            InputStream openInputStream2 = this.mContentResolver.openInputStream(imageUri);
            Bitmap decodeStream = BitmapFactory.decodeStream(openInputStream2, null, options2);
            openInputStream2.close();
            return Bitmap.createBitmap(decodeStream, 0, 0, options2.outWidth, options2.outHeight, getOrientationMatrix(str), true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        } catch (Exception e3) {
            e3.printStackTrace();
            return null;
        }
    }

    public void resetSeekBar() {
        this.magicSeekbar.setProgress(0);
        this.mHoverView.setMagicThreshold(0);
    }

    private Uri getImageUri(String str) {
        return Uri.fromFile(new File(str));
    }

    private Matrix getOrientationMatrix(String str) {
        Matrix matrix = new Matrix();
        try {
            switch (new ExifInterface(str).getAttributeInt("Orientation", 1)) {
                case 2:
                    matrix.setScale(-1.0f, 1.0f);
                    break;
                case 3:
                    matrix.setRotate(180.0f);
                    break;
                case 4:
                    matrix.setRotate(180.0f);
                    matrix.postScale(-1.0f, 1.0f);
                    break;
                case 5:
                    matrix.setRotate(90.0f);
                    matrix.postScale(-1.0f, 1.0f);
                    break;
                case 6:
                    matrix.setRotate(90.0f);
                    break;
                case 7:
                    matrix.setRotate(-90.0f);
                    matrix.postScale(-1.0f, 1.0f);
                    break;
                case 8:
                    matrix.setRotate(-90.0f);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matrix;
    }


    public void resetMainButtonState() {
        this.eraserMainButton.setSelected(false);
    }

    public void resetSubEraserButtonState() {
        this.eraserSubButton.setSelected(false);
    }

    public void resetSubMagicButtonState() {
        this.magicRemoveButton.setSelected(false);
        this.magicRestoreButton.setSelected(false);
    }

    public void resetBrushButtonState() {
        this.brushSize1Button.setSelected(false);
        this.brushSize2Button.setSelected(false);
        this.brushSize3Button.setSelected(false);
        this.brushSize4Button.setSelected(false);
    }

    public void updateUndoButton() {
        if (this.mHoverView.checkUndoEnable()) {
            this.undoButton.setEnabled(true);
            this.undoButton.setAlpha(1.0f);
            return;
        }
        this.undoButton.setEnabled(false);
        this.undoButton.setAlpha(0.3f);
    }

    public void updateRedoButton() {
        if (this.mHoverView.checkRedoEnable()) {
            this.redoButton.setEnabled(true);
            this.redoButton.setAlpha(1.0f);
            return;
        }
        this.redoButton.setEnabled(false);
        this.redoButton.setAlpha(0.3f);
    }

    public void onClick(View view) {
        updateUndoButton();
        updateRedoButton();
        switch (view.getId()) {
            case R.id.brush_size_1_button /*2131230762*/:
                resetBrushButtonState();
                this.mHoverView.setEraseOffset(40);
                this.brushSize1Button.setSelected(true);
                return;
            case R.id.brush_size_2_button /*2131230763*/:
                resetBrushButtonState();
                this.mHoverView.setEraseOffset(60);
                this.brushSize2Button.setSelected(true);
                return;
            case R.id.brush_size_3_button /*2131230764*/:
                resetBrushButtonState();
                this.mHoverView.setEraseOffset(80);
                this.brushSize3Button.setSelected(true);
                return;
            case R.id.brush_size_4_button /*2131230765*/:
                resetBrushButtonState();
                this.mHoverView.setEraseOffset(100);
                this.brushSize4Button.setSelected(true);
                return;

            case R.id.eraseButton /*2131230802*/:
                this.mHoverView.switchMode(HoverView.ERASE_MODE);
                if (this.eraserLayout.getVisibility() == View.VISIBLE) {
                    this.eraserLayout.setVisibility(View.GONE);
                } else {
                    this.eraserLayout.setVisibility(View.VISIBLE);
                }
                this.magicLayout.setVisibility(View.GONE);
                resetMainButtonState();
                resetSubEraserButtonState();
                this.eraserSubButton.setSelected(true);
                this.eraserMainButton.setSelected(true);
                return;
            case R.id.erase_sub_button /*2131230803*/:
                this.mHoverView.switchMode(HoverView.ERASE_MODE);
                resetSubEraserButtonState();
                this.eraserSubButton.setSelected(true);
                return;


            case R.id.nextButton :
                Intent resultIntent = new Intent();
                resultIntent.putExtra("image", mHoverView.save());
                setResult(RESULT_OK, resultIntent);
                finish();
                return;
            case R.id.redoButton:
                findViewById(R.id.eraser_layout).setVisibility(View.GONE);
                findViewById(R.id.magicWand_layout).setVisibility(View.GONE);
                this.mHoverView.redo();
                updateUndoButton();
                updateRedoButton();
                return;
            case R.id.undoButton :
                findViewById(R.id.eraser_layout).setVisibility(View.GONE);
                findViewById(R.id.magicWand_layout).setVisibility(View.GONE);
                this.mHoverView.undo();
                if (this.mHoverView.checkUndoEnable()) {
                    this.undoButton.setEnabled(true);
                    this.undoButton.setAlpha(1.0f);
                } else {
                    this.undoButton.setEnabled(false);
                    this.undoButton.setAlpha(0.3f);
                }
                updateRedoButton();
                return;
            default:

        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}


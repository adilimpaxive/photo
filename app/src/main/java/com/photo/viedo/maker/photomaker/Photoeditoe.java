package com.photo.viedo.maker.photomaker;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;

import android.Manifest;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.photo.viedo.maker.photomaker.base.BaseActivity;
import com.photo.viedo.maker.photomaker.filters.FilterViewAdapter;
import com.photo.viedo.maker.photomaker.filters.Filterlisnter;
import com.photo.viedo.maker.photomaker.tools.EditingToolsAdapter;
import com.photo.viedo.maker.photomaker.tools.ToolType;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.PhotoFilter;
import ja.burhanrashid52.photoeditor.SaveSettings;
import ja.burhanrashid52.photoeditor.TextStyleBuilder;
import ja.burhanrashid52.photoeditor.ViewType;

public class Photoeditoe  extends BaseActivity implements OnPhotoEditorListener,
        View.OnClickListener , EditingToolsAdapter.OnItemSelected, Filterlisnter, EmojiBSFragment.EmojiListener, StickerBSFragment.StickerListener{

     PhotoEditorView mPhotoEditorView;
     ImageView imgUndo;
     ImageView imgRedo;
     ImageView rotate;
    static String statusLabel = "";
    int position;
    ImagesRecycleView imagesRecycleView;
    PhotoEditor mPhotoEditor;
    private StickerBSFragment mStickerBSFragment;
    private EmojiBSFragment mEmojiBSFragment;
    private TextView mTxtCurrentTool;
    private RecyclerView mRvTools, mRvFilters;
    Button btnsave, btncancel;
    private FilterViewAdapter mFilterViewAdapter = new FilterViewAdapter(this);
    private ConstraintLayout mRootView;
    private ConstraintSet mConstraintSet = new ConstraintSet();
    private boolean mIsFilterVisible;
    Uri mSaveImageUri;
    private EditingToolsAdapter mEditingToolsAdapter = new EditingToolsAdapter(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoeditoe);
        mPhotoEditorView = findViewById(R.id.photoEditorView);
        mRvTools = findViewById(R.id.rvConstraintTools);
        mTxtCurrentTool = findViewById(R.id.txtCurrentTool);
        mRvFilters = findViewById(R.id.rvFilterView);
        mRootView = findViewById(R.id.rootView);

        // Button save

           btnsave = findViewById(R.id.save);
           btnsave.setOnClickListener(this);

        // Apply listener on redo , undo and rotate
        rotate = findViewById(R.id.imgrotate);
        rotate.setOnClickListener(this);

        imgUndo = findViewById(R.id.imgUndo);
        imgUndo.setOnClickListener(this);

        imgRedo = findViewById(R.id.imgRedo);
        imgRedo.setOnClickListener(this);
        // get image
        Intent i = getIntent();
        position = i.getExtras().getInt("id");
        imagesRecycleView = new ImagesRecycleView(getApplicationContext(), MainActivity.uri);
        mPhotoEditorView.getSource().setImageURI(Uri.parse(imagesRecycleView.urii.get(position)));
        Toast.makeText(this, "" + position, Toast.LENGTH_SHORT).show();
// photoeditor
        mPhotoEditor = new PhotoEditor.Builder(this, mPhotoEditorView)
                .setPinchTextScalable(true) // set flag to make text scalable when pinch
                .build(); // build photo editor sdk
                  mPhotoEditor.setOnPhotoEditorListener(this);
// toolAdapter
        LinearLayoutManager llmTools = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvTools.setLayoutManager(llmTools);
        mRvTools.setAdapter(mEditingToolsAdapter);
 // Filter
        LinearLayoutManager llmFilters = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvFilters.setLayoutManager(llmFilters);
        mRvFilters.setAdapter(mFilterViewAdapter);



// Emoji and Sticker
        mEmojiBSFragment = new EmojiBSFragment();
        mEmojiBSFragment.setEmojiListener(this);
        mStickerBSFragment = new StickerBSFragment();
        mStickerBSFragment.setStickerListener(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    try {
                        mPhotoEditor.clearAllViews();
                        String uri = data.getStringExtra("imaage");
                        Bitmap bitmap = BitmapFactory.decodeFile(uri);
                        mPhotoEditorView.getSource().setImageBitmap(bitmap);
                    } catch (RuntimeException e) {
                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "lose your image", Toast.LENGTH_SHORT).show();

                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    try {
                        mPhotoEditor.clearAllViews();
                        String uri = data.getStringExtra("image");
                        Bitmap bitmap = BitmapFactory.decodeFile(uri);
                        mPhotoEditorView.getSource().setImageBitmap(bitmap);
                    } catch (RuntimeException e) {
                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, "lose your image", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgUndo:
                mPhotoEditor.undo();
                break;
            case R.id.imgRedo:
                mPhotoEditor.redo();
                break;
            case R.id.imgrotate:
                mPhotoEditorView.setRotation(mPhotoEditorView.getRotation() + 90);
            case R.id.save:
                 saveImage();
                break;
        }
    }

    @Override
    public void onColorChanged(int colorCode) {
        mPhotoEditor.setBrushColor(colorCode);
        mTxtCurrentTool.setText(R.string.label_brush);
      }
    @Override
    public void onBrushSizeChanged(int brushSize) {

      }
    @Override
    public void onFilterSelected(PhotoFilter photoFilter) {
        mPhotoEditor.setFilterEffect(photoFilter);
      }
    @Override
    public void onEmojiClick(String emojiUnicode) {
        mPhotoEditor.addEmoji(emojiUnicode);
        mTxtCurrentTool.setText(R.string.label_emoji);
      }
    @Override
    public void onStickerClick(Bitmap bitmap) {
        mPhotoEditor.addImage(bitmap);
        mTxtCurrentTool.setText(R.string.label_sticker);
      }

    @Override
    public void onToolSelected(ToolType toolType) {
        switch (toolType) {
            case CROP:
                Intent intent = new Intent(Photoeditoe.this, Cropimage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("ids", position);
                startActivityForResult(intent,1);
                break;
           case TEXT:
                TextEditorDialogFragment textEditorDialogFragment = TextEditorDialogFragment.show(this);
                textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {
                    @Override
                    public void onDone(String inputText, int colorCode, String font) {
                        final TextStyleBuilder styleBuilder = new TextStyleBuilder();
                        styleBuilder.withTextColor(colorCode);
                        try {
                            Typeface font_ = Typeface.createFromAsset(getAssets(), font);
                            styleBuilder.withTextFont(font_);
                        }catch (RuntimeException e){

                            e.printStackTrace();
                        }
                        mPhotoEditor.addText(inputText, styleBuilder);
                        mTxtCurrentTool.setText(R.string.label_text);
                    }
                });
                break;
            case FILTER:
                mTxtCurrentTool.setText(R.string.label_filter);
                showFilter(true);
                break;
           case ERASER:
               String str = "";
               String str2 = "picture";
               if (!checkInit()) {
                   Bitmap bitmap = ((BitmapDrawable) mPhotoEditorView.getSource().getDrawable()).getBitmap();
                   Bitmap converetdImage = getResizedBitmap( bitmap, 500);
                   ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                  converetdImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                   byte[] byteArray = byteArrayOutputStream.toByteArray();
                   Intent intent1 = new Intent(Photoeditoe.this, EraserActivity.class);
                   intent1.putExtra(str2, byteArray);
                   //   startActivityForResult(Intent.createChooser(intent1, "picture"), PICK_IMAGE);
                   startActivityForResult(intent1,2);
                   setLabel(str);
                   //  mPhotoEditor.brushEraser();
                   mTxtCurrentTool.setText(R.string.label_eraser_mode);
               }
                break;
            case EMOJI:
                mEmojiBSFragment.show(getSupportFragmentManager(), mEmojiBSFragment.getTag());
                break;
            case STICKER:
                mStickerBSFragment.show(getSupportFragmentManager(), mStickerBSFragment.getTag());
                break;
        }
    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    public boolean checkInit() {
        if (this.mPhotoEditorView.getSource().getDrawable() != null) {
            return false;
        }
        Toast.makeText(this, "please select photo", Toast.LENGTH_LONG).show();
        return true;
    }

    public static void setLabel(String str) {
        statusLabel = str;
    }

    @Override
    public void onEditTextChangeListener(final View rootView, String text, int colorCode) {
        TextEditorDialogFragment textEditorDialogFragment =
                TextEditorDialogFragment.show(this, text, colorCode);
        textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {
            @Override
            public void onDone(String inputText, int colorCode, String font) {
                final TextStyleBuilder styleBuilder = new TextStyleBuilder();
                Typeface font_ = Typeface.createFromAsset(getAssets(), font);
                styleBuilder.withTextColor(colorCode);
                styleBuilder.withTextFont(font_);
                mPhotoEditor.editText(rootView, inputText, styleBuilder);
                mTxtCurrentTool.setText(R.string.label_text);
                mTxtCurrentTool.setTypeface(font_);
            }
        });
    }


    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {

    }

    @Override
    public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {

    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {

    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {

    }

    void showFilter(boolean isVisible) {
        mIsFilterVisible = isVisible;
        mConstraintSet.clone(mRootView);

        if (isVisible) {
            mConstraintSet.clear(mRvFilters.getId(), ConstraintSet.START);
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.START);
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.END,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
        } else {
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
            mConstraintSet.clear(mRvFilters.getId(), ConstraintSet.END);
        }

        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(350);
        changeBounds.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        TransitionManager.beginDelayedTransition(mRootView, changeBounds);

        mConstraintSet.applyTo(mRootView);
    }




    private void saveImage() {
        if (requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            showLoading("Saving...");
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + ""
                    + System.currentTimeMillis() + ".jpeg");
            try {
                file.createNewFile();

                SaveSettings saveSettings = new SaveSettings.Builder()
                        .setClearViewsEnabled(true)
                        .setTransparencyEnabled(true)
                        .build();

                mPhotoEditor.saveAsFile(file.getAbsolutePath(), saveSettings, new PhotoEditor.OnSaveListener() {
                    @Override
                    public void onSuccess(@NonNull String imagePath) {
                        hideLoading();
                        showSnackbar("Image Saved Successfully");
                        mSaveImageUri = Uri.fromFile(new File(imagePath));
                        mPhotoEditorView.getSource().setImageURI(mSaveImageUri);


                        imagesRecycleView.urii.set(position, String.valueOf((mSaveImageUri)));
                        imagesRecycleView.notifyItemChanged(position);
                        Toast.makeText(Photoeditoe.this, ""+ position, Toast.LENGTH_SHORT).show();
                        imagesRecycleView.notifyDataSetChanged();
                     //   Toast.makeText(Photoeditoe.this, ""+mSaveImageUri, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        hideLoading();
                        showSnackbar("Failed to save Image");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                hideLoading();
                showSnackbar(e.getMessage());
            }
        }
    }




    @Override
    public void onBackPressed() {
        if (mIsFilterVisible) {
            showFilter(false);
            mTxtCurrentTool.setText(R.string.app_name);
        } else {
            super.onBackPressed();

        }
    }


}

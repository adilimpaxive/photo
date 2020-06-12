package com.photo.viedo.maker.photomaker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class Cropimage extends AppCompatActivity {
    ImageView selectedImage;
    int position;
    ImagesRecycleView imagesRecycleView;
    Uri outputURI;
    Button btn;
    Bitmap croppedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropimage);
        btn = findViewById(R.id.saveimg);

        Intent i = getIntent();
        position = i.getExtras().getInt("ids");
        selectedImage = findViewById(R.id.cropImageView);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resulteIntent = new Intent();
                resulteIntent.putExtra("imaage",Cropimage.this.save());
                Toast.makeText(Cropimage.this, croppedBitmap.toString(), Toast.LENGTH_SHORT).show();
                Cropimage.this.setResult(RESULT_OK,resulteIntent);

                Cropimage.this.finish();

            }
        });
        imagesRecycleView = new ImagesRecycleView(getApplicationContext(), MainActivity.uri);
        selectedImage.setImageURI(Uri.parse(imagesRecycleView.urii.get(position)));
        //  selectedImage.setImageUriAsync(imageAdapter.mArrayUri.get(position));
        Toast.makeText(this, "" + position, Toast.LENGTH_SHORT).show();
        outputURI = Uri.fromFile(new File(getCacheDir(), "Cropped.jpg"));
        CropImage.activity(Uri.parse(MainActivity.uri.get(position)))
                .start(this);

      /*  Crop.of(MainActivity.mArrayUri.get(position), outputURI)
                .asSquare()
                .start(this);*/


    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri resultUri = result.getUri();
            selectedImage.setImageURI(resultUri);
            //  selectedImage.setImageUriAsync(result.getUri());
            if (resultCode == RESULT_OK) {
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

            }
        }
    }
/*    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri resultUri = result.getUri();
            selectedImage.setImageURI(resultUri);

            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.P){
                try {
                    croppedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),(resultUri));
                    croppedBitmap = ImageDecoder.decodeBitmap(source);
                } catch (IOException | RuntimeException e) {
                    e.printStackTrace();
                }
            }

            //  selectedImage.setImageUriAsync(result.getUri());
            if (resultCode == RESULT_OK) {
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

            }
        }
    }*/



    @SuppressLint("WrongThread")
    public String save() {
        //  Uri uri = Uri.fromFile(new File(getCacheDir(), "Cropped.jpg"));
        Bitmap saveDrawnBitmap = croppedBitmap;
        File  externalStorageDirectory = Environment.getExternalStorageDirectory();
        StringBuilder sb = new StringBuilder();
        sb.append(externalStorageDirectory.getAbsolutePath());
        sb.append("/BackgroundRemover");

        File file = new File(sb.toString());
        file.mkdirs();
        File file2 = new File(file, "CroppedImage.png");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            saveDrawnBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        StringBuilder sb2 = new StringBuilder();
        sb2.append(externalStorageDirectory);
        sb2.append("/BackgroundRemover/");
        sb2.append("CroppedImage.png");
        return sb2.toString();
    }


    private void refreshGallery(File file) {
        Intent intent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        intent.setData(Uri.fromFile(file));
        this.sendBroadcast(intent);
    }

}


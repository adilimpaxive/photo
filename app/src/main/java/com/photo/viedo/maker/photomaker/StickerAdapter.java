package com.photo.viedo.maker.photomaker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.MyViewHolder> {
    Context ctx;
    int [] images;
    View view;
    private StickerBSFragment.StickerListener mStickerListener;

    public StickerAdapter(Context applicationContext, int[] imgs) {
        this.ctx = applicationContext;
        this.images = imgs;
        images = new int[] {R.drawable.bb};

    }

    public void setStickerListener(StickerBSFragment.StickerListener stickerListener) {
        mStickerListener = stickerListener;
    }

    public interface StickerListener {
        void onStickerClick(Bitmap bitmap);
    }

    @NonNull
    @Override
    public StickerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
         view = layoutInflater.inflate(R.layout.add_sticker,viewGroup,false);
        return new StickerAdapter.MyViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull StickerAdapter.MyViewHolder myViewHolder, int i) {
        myViewHolder.imgSticker.setImageResource(images[i]);


    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgSticker;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSticker = itemView.findViewById(R.id.imgStickers);

        }
    }
}

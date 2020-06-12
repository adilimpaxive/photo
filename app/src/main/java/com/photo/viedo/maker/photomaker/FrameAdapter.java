package com.photo.viedo.maker.photomaker;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class FrameAdapter extends RecyclerView.Adapter<FrameAdapter.MyViewHolder> {
    public static Object FrameListener;
    Context context;
    int [] imgs;
    VideoView video;
    View view;

    public FrameAdapter(Context applicationContext, Object frameListener, int[] imgss, VideoView videoView) {
        this.context = applicationContext;
        this.imgs = imgss;
       this. video = videoView;
        imgs = new  int[]{R.drawable.frame,R.drawable.bb};
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

        view = layoutInflater.inflate(R.layout.add_fram,viewGroup,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int Position) {
        myViewHolder.frameimg.setImageResource(imgs[Position]);
        myViewHolder. frameimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (video != null){
                 //   video.setOnClickListener(BitmapFactory.decodeResource(imgs[getItemViewType(Position)]));



                }
               /* Picasso.with(context)
                        .load(imgs[Position])
                        .fit()
                        .centerCrop()
                        .noPlaceholder()
                        .into(myViewHolder.frameimg);*/
               // if (video != null) {

                //    adapterListener.onClick((Bitmap)imgs.);
                    //  video.setOnClickListener(BitmapFactory.decodeResource(imgs[getLayoutPosition()]));

                    // adapterListener.onClick(BitmapFactory.decodeResource(imgs[getLayoutPosition()]));
                    // adapterListener.onClick(BitmapFactory.decodeResource(getPosition(),imgs[l]));
             //   }

            }
        });


    }
    @Override
    public int getItemCount() {
        return imgs.length;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView frameimg;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            frameimg = itemView.findViewById(R.id.frameimg);
            frameimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
           /* frameimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (video != null) {

                        adapterListener.onClick((Bitmap)imgs.);
                      //  video.setOnClickListener(BitmapFactory.decodeResource(imgs[getLayoutPosition()]));

                   *//* Picasso.with(context)
                            .load()
                            .fit()
                            .centerCrop()
                            .noPlaceholder()
                            .into(myViewHolder.frameimg);*//*
                       // adapterListener.onClick(BitmapFactory.decodeResource(imgs[getLayoutPosition()]));
                       // adapterListener.onClick(BitmapFactory.decodeResource(getPosition(),imgs[l]));
                    }

                }
            });*/

        }
    }


}

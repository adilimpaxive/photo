package com.photo.viedo.maker.photomaker;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.MyViewHolder> {
    Context ctx;
    int [] image;
    View view;
    VideoView videoViews;
    private View.OnClickListener filterlistener;
/*    public FilterAdapter(Context applicationContext, View.OnClickListener onClickListener, int[] imgs, VideoView videoView) {
        this.ctx = applicationContext;
        this.image = imgs;
        this.videoViews = videoView;
        this.filterlistener = onClickListener;
        image = new  int [R.drawable.overlay_10];
    }*/


    public FilterAdapter(Context applicationContext, int[] imgs, VideoView videoView) {
        this.ctx = applicationContext;
        this.image = imgs;
        this.videoViews = videoView;
        // this.filterlistener = onClickListener;
        image = new  int[]{R.drawable.overlay_2};
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        view = layoutInflater.inflate(R.layout.add_filter,viewGroup,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.imageView.setImageResource(image[i]);
        myViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoViews != null){

                }

            }
        });
    }
    @Override
    public int getItemCount() {
        return image.length;
    }

    public interface FilterListener {
        void onFilterSelect(Bitmap bitmap);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.filterimg);

        }
    }
}

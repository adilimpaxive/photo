package com.photo.viedo.maker.photomaker;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;


public class ImagesRecycleView extends RecyclerView.Adapter<ImagesRecycleView.MyViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {

    Context ctx;
   ArrayList<String> urii;
    View view;

    public ImagesRecycleView(Context ctx, ArrayList<String> uri) {
        this.ctx = ctx;
        this.urii = uri;
    }




    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.inflate(R.layout.item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.mImageRecyclerView.setImageURI(Uri.parse((urii.get(position))));
        holder.mImageRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ctx, "Images", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ctx, Photoeditoe.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", position);
                ctx.startActivity(intent);


            }
        });



        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urii.remove(position);
                notifyDataSetChanged();
            }
        });

    }





    @Override
    public int getItemCount() {
        return urii.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageRecyclerView;
        Button deletebtn;
        View view;
        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            mImageRecyclerView = itemView.findViewById(R.id.imageView);
            deletebtn = itemView.findViewById(R.id.deletebtn);
        }

    }


    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(urii, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(urii, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);

    }

    @Override
    public void onRowSelected(MyViewHolder myViewHolder) {
    }

    @Override
    public void onRowClear(MyViewHolder myViewHolder) {

    }


}



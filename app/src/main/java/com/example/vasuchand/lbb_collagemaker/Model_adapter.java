package com.example.vasuchand.lbb_collagemaker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

/**
 * Created by Vasu Chand on 5/9/2017.
 */

public class Model_adapter extends RecyclerView.Adapter<Model_adapter.MyViewHolder>  {
    private List<Model> Modellist;
    private Context mContext;

    private Model currentItem;
    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;


        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView)view.findViewById(R.id.image);




        }

    }



    public Model_adapter(Context context, List<Model> List)
    {
        this.Modellist = List;
        this.mContext = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);


        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

       currentItem = Modellist.get(position);
        Glide.with(mContext)
                .load(currentItem.getImageurl())
                .into(holder.imageView);

    }


    @Override
    public int getItemCount() {
        return Modellist.size();
    }

}

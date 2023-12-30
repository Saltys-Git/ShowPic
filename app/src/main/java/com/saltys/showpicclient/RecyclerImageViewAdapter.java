package com.saltys.showpicclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerImageViewAdapter extends RecyclerView.Adapter<RecyclerImageViewAdapter.ViewHolder> {
    private final Context context;
    private final ArrayList<String> urlArrayList;


    public RecyclerImageViewAdapter(Context context, ArrayList<String> urlArrayList) {
        this.context = context;
        this.urlArrayList = urlArrayList;
    }


    @NonNull
    @Override
    public RecyclerImageViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.images_view_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerImageViewAdapter.ViewHolder holder, int position) {
        Glide.with(context)
                .load(urlArrayList.get(position))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return urlArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagesImageView);
        }
    }
}

package com.example.oemscandemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.oemscandemo.R;
import com.example.oemscandemo.db.DBHelper;
import com.example.oemscandemo.model.DumImage;
import com.example.oemscandemo.utils.Event;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder> {
    private List<DumImage> imageList;
    private Context context;
    DBHelper helper;

    public ImageListAdapter(List<DumImage> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageListAdapter.ViewHolder holder, int position) {
        final DumImage image = imageList.get(position);
        if (image.getImagePath() != null) {
            Glide.with(context).load(image.getImagePath()).into(holder.imgShow);
        }
        holder.imgRemove.setOnClickListener(v -> {
            imageList.remove(image);
            EventBus.getDefault().post(Event.Add_Photo);
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgShow, imgRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgShow = itemView.findViewById(R.id.img_show);
            imgRemove = itemView.findViewById(R.id.img_remove);
        }
    }
}

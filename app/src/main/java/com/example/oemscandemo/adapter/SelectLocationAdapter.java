package com.example.oemscandemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.oemscandemo.R;
import com.example.oemscandemo.model.LocationsBean;

import java.util.List;


public class SelectLocationAdapter extends RecyclerView.Adapter<SelectLocationAdapter.ViewHolder> {
    private Context context;
    private List<LocationsBean> locationsBeanList;

    public SelectLocationAdapter(Context context, List<LocationsBean> locationsBeanList) {
        this.context = context;
        this.locationsBeanList = locationsBeanList;
    }

    @NonNull
    @Override
    public SelectLocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_location_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectLocationAdapter.ViewHolder holder, int position) {
        final LocationsBean locationsBean = locationsBeanList.get(position);
        String locations = locationsBean.getCode() + " - " + locationsBean.getName();
        holder.txtLocation.setText(locations);
        holder.txtAssetCount.setVisibility(View.GONE);
        holder.downloadProgress.setVisibility(View.GONE);
        holder.imgStatus.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return locationsBeanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtLocation, txtAssetCount;
        private ProgressBar downloadProgress;
        private ImageView imgStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtLocation = itemView.findViewById(R.id.txt_location);
            txtAssetCount = itemView.findViewById(R.id.txt_asset_count);
            downloadProgress = itemView.findViewById(R.id.pBar_location);
            imgStatus = itemView.findViewById(R.id.img_download_status);
        }
    }
}

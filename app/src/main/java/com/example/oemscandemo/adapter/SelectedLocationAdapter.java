package com.example.oemscandemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.oemscandemo.R;
import com.example.oemscandemo.db.DBHelper;
import com.example.oemscandemo.model.LocationsBean;
import com.example.oemscandemo.utils.MyApplication;

import java.util.List;

public class SelectedLocationAdapter extends RecyclerView.Adapter<SelectedLocationAdapter.ViewHolder> {
    private List<LocationsBean> locationList;
    private Context context;
    private DBHelper helper;

    public SelectedLocationAdapter(Context context, List<LocationsBean> locationList) {
        this.locationList = locationList;
        this.context = context;
    }

    @Override
    public SelectedLocationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_location_item, parent, false);
        helper = new DBHelper(MyApplication.getContext());
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SelectedLocationAdapter.ViewHolder holder, int position) {

        final LocationsBean location = locationList.get(position);
        String locations = location.getCode() + " - " + location.getName();
        holder.txtLocation.setText(locations);
        int assetCount = helper.getAssetCountByLocation(location.getLocationId());

        holder.txtAssetCount.setVisibility(View.VISIBLE);
        holder.downloadProgress.setVisibility(View.VISIBLE);
        holder.imgStatus.setVisibility(View.VISIBLE);
        if (location.getDownload() == -1) {
            holder.downloadProgress.setVisibility(View.VISIBLE);
            holder.txtAssetCount.setVisibility(View.GONE);
            holder.imgStatus.setVisibility(View.GONE);
        } else if (location.getDownload() != 0) {
            holder.downloadProgress.setVisibility(View.GONE);
            holder.txtAssetCount.setVisibility(View.VISIBLE);
            holder.txtAssetCount.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.txtAssetCount.setText(String.valueOf(assetCount));
            holder.imgStatus.setVisibility(View.VISIBLE);
            holder.imgStatus.setImageResource(R.drawable.ic_correct);
        } else if (location.getDownload() == 0) {
            holder.downloadProgress.setVisibility(View.GONE);
            holder.txtAssetCount.setVisibility(View.VISIBLE);
            holder.txtAssetCount.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.txtAssetCount.setText(String.valueOf(assetCount));
            holder.imgStatus.setVisibility(View.VISIBLE);
            holder.imgStatus.setImageResource(R.drawable.ic_fail);
        } else {
            holder.downloadProgress.setVisibility(View.GONE);
            holder.txtAssetCount.setVisibility(View.VISIBLE);
            holder.txtAssetCount.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.txtAssetCount.setText(String.valueOf(assetCount));
            holder.imgStatus.setVisibility(View.VISIBLE);
            holder.imgStatus.setImageResource(R.drawable.ic_fail);
        }
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtLocation, txtAssetCount;
        private ProgressBar downloadProgress;
        private ImageView imgStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            txtLocation = itemView.findViewById(R.id.txt_location);
            txtAssetCount = itemView.findViewById(R.id.txt_asset_count);
            downloadProgress = itemView.findViewById(R.id.pBar_location);
            imgStatus = itemView.findViewById(R.id.img_download_status);
        }
    }
}

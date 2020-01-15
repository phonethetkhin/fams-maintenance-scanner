package com.example.oemscandemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oemscandemo.R;
import com.example.oemscandemo.db.DBHelper;
import com.example.oemscandemo.model.AssetBean;
import com.example.oemscandemo.ui.ScanActivity;
import com.example.oemscandemo.utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AssetListAdapter extends RecyclerView.Adapter<AssetListAdapter.ViewHolder> implements Filterable {
    private List<AssetBean> assetList;
    private List<AssetBean> filterAssetList;
    private List<Object> timeScheduleList, maintenanceList;
    private List<Object> conditionScheduleList, conditionMaintenanceList;

    private Context mContext;
    private DBHelper helper;
    private ValueFilter valueFilter;

    private SharedPreferences prefs;
    private String prefFANo = "faNo";
    private String prefSelector = "selector";

    public AssetListAdapter(List<AssetBean> assetList, Context mContext) {
        this.assetList = assetList;
        this.filterAssetList = assetList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AssetListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.asset_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        helper = new DBHelper(MyApplication.getContext());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AssetListAdapter.ViewHolder holder, int position) {
        final AssetBean asset = assetList.get(position);
        timeScheduleList = helper.getAllScheduleObjects(asset.getId());
        conditionScheduleList = helper.getAllUnitScheduleObjects(asset.getId());
        maintenanceList = helper.getAllMaintenanceObjects(asset.getId());
        conditionMaintenanceList = helper.getAllConditionMaintenanceObjects(asset.getId());
        holder.faNumber.setText(asset.getFaNumber());
        holder.itemName.setText(asset.getItemName());
        holder.faCategory.setText(asset.getCategory());
        if (!asset.getActualLocation().equals("")) {
            holder.actualLocation.setVisibility(View.VISIBLE);
            holder.actualLocation.setText(asset.getActualLocation());
        }
        if (timeScheduleList.size() != 0 || conditionScheduleList.size() != 0) {
            holder.imgSchedule.setVisibility(View.VISIBLE);
        } else {
            holder.imgSchedule.setVisibility(View.GONE);
        }
        if (maintenanceList.size() != 0 || conditionMaintenanceList.size() != 0) {
            holder.imgMaintenance.setVisibility(View.VISIBLE);
        } else {
            holder.imgMaintenance.setVisibility(View.GONE);
        }

        holder.cardView.setOnClickListener(v -> {
            prefs = mContext.getSharedPreferences(prefFANo, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.commit();
            editor.putString("faNo", asset.getFaNumber());
            editor.apply();

            if (timeScheduleList.size() > 0 || maintenanceList.size() > 0) {

                prefs = mContext.getSharedPreferences(prefSelector, MODE_PRIVATE);
                SharedPreferences.Editor selectEditor = prefs.edit();
                selectEditor.clear();
                selectEditor.commit();

                Intent intent = new Intent(mContext.getApplicationContext(), ScanActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.getApplicationContext().startActivity(intent);

            } else {

                prefs = mContext.getSharedPreferences(prefSelector, MODE_PRIVATE);
                SharedPreferences.Editor selectEditor = prefs.edit();
                selectEditor.clear();
                selectEditor.commit();
                selectEditor.putInt("selectorNext", 1);
                selectEditor.apply();

                Intent intent = new Intent(mContext.getApplicationContext(), ScanActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.getApplicationContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return assetList.size();
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                List<AssetBean> filterList = new ArrayList<>();
                for (int i = 0; i < filterAssetList.size(); i++) {
                    if ((filterAssetList.get(i).getFaNumber().toUpperCase()).contains(constraint.toString().toUpperCase())) {
                        filterList.add(filterAssetList.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = filterAssetList.size();
                results.values = filterAssetList;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            assetList = (List<AssetBean>) results.values;
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView faNumber, itemName, faCategory, actualLocation;
        private ImageView imgMaintenance, imgSchedule;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            faNumber = itemView.findViewById(R.id.fa_number);
            itemName = itemView.findViewById(R.id.fa_item_name);
            faCategory = itemView.findViewById(R.id.fa_category);
            actualLocation = itemView.findViewById(R.id.actual_location);
            imgMaintenance = itemView.findViewById(R.id.img_maintenance);
            imgSchedule = itemView.findViewById(R.id.img_schedule);
            cardView = itemView.findViewById(R.id.remain_item);
        }
    }
}

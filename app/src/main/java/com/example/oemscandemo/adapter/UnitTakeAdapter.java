package com.example.oemscandemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oemscandemo.R;
import com.example.oemscandemo.db.DBHelper;
import com.example.oemscandemo.model.AssetBean;
import com.example.oemscandemo.model.AssetMaintenanceCondition;
import com.example.oemscandemo.ui.UnitTakingDetailActivity;

import java.util.List;

public class UnitTakeAdapter extends RecyclerView.Adapter<UnitTakeAdapter.ViewHolder> {
    private List<AssetBean> assetList;
    private Context context;
    private DBHelper helper;

    public UnitTakeAdapter(List<AssetBean> assetList, Context context) {
        this.assetList = assetList;
        this.context = context;
    }

    @NonNull
    @Override
    public UnitTakeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.unit_take_item, parent, false);
        helper = new DBHelper(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UnitTakeAdapter.ViewHolder holder, int position) {
        final AssetBean assetBean = assetList.get(position);
        List<AssetMaintenanceCondition> assetConditions = helper.getAssetConditionByAssetId(assetBean.getId());
        holder.assetNo.setText(assetBean.getFaNumber());
        holder.conditionCount.setText(assetConditions.size() + " conditions");
        holder.itemName.setText(assetBean.getItemName());
        holder.category.setText(assetBean.getCategory());
        holder.location.setText(assetBean.getActualLocation());
        for (AssetMaintenanceCondition condition : assetConditions) {
            if (condition.getUpdateUnit() != 0.0) {
                holder.imgUnit.setVisibility(View.VISIBLE);
            }
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context.getApplicationContext(), UnitTakingDetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("asset_id", assetBean.getId());
            intent.putExtra("asset_no", assetBean.getFaNumber());
            intent.putExtra("item_name", assetBean.getItemName());
            intent.putExtra("condition", assetBean.getCondition());
            intent.putExtra("location", helper.getLocationById(assetBean.getLocationId()).getName());
            intent.putExtra("cost_center", assetBean.getCostCenter());
            intent.putExtra("category", assetBean.getCategory());
            intent.putExtra("serial_no", assetBean.getSerialNo());
            context.getApplicationContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return assetList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView assetNo, conditionCount, itemName, category, location;
        ImageView imgUnit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            assetNo = itemView.findViewById(R.id.fa_number);
            conditionCount = itemView.findViewById(R.id.condition_count);
            itemName = itemView.findViewById(R.id.item_name);
            category = itemView.findViewById(R.id.category);
            location = itemView.findViewById(R.id.actual_location);
            imgUnit = itemView.findViewById(R.id.img_unit_take);
        }
    }
}

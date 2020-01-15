package com.example.oemscandemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import com.example.oemscandemo.model.ConditionScheduleMaintenanceDetail;
import com.example.oemscandemo.model.DumImage;
import com.example.oemscandemo.model.MaintenanceDetail;
import com.example.oemscandemo.ui.MaintenanceActivity;

import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class MaintenanceListAdapter extends RecyclerView.Adapter<MaintenanceListAdapter.ViewHolder> {
    List<Object> scheduleList;
    Context context;
    private SharedPreferences prefs;
    private String prefFANo = "faNo";
    DBHelper helper;

    public MaintenanceListAdapter(Context context, List<Object> scheduleList) {
        this.scheduleList = scheduleList;
        this.context = context;
    }

    @NonNull
    @Override
    public MaintenanceListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.maintenance_item, parent, false);
        MaintenanceListAdapter.ViewHolder viewHolder = new MaintenanceListAdapter.ViewHolder(v);
        helper = new DBHelper(context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MaintenanceListAdapter.ViewHolder holder, int position) {
        Map<String, String> schedule = (Map<String, String>) scheduleList.get(position);
        prefs = context.getSharedPreferences(prefFANo, MODE_PRIVATE);
        String faNo = prefs.getString("faNo", null);
        AssetBean assetBean = helper.getFixedAssetByFANo(faNo);
        String locationName = helper.getLocationById(assetBean.getLocationId()).getName();
        String locationCode = helper.getLocationById(assetBean.getLocationId()).getCode();
        final String scheduleId = schedule.get("scheduleId");
        final String scheduleItemId = schedule.get("scheduleItemId");
        final String scheduleListId = schedule.get("scheduleListId");
        final String scheduleUnitId = schedule.get("scheduleUnitId");
        final String scheduleCode = schedule.get("scheduleCode");
        final String scheduleName = schedule.get("scheduleName");
        final String scheduleTime = schedule.get("scheduleTime");
        final String scheduleDate = schedule.get("scheduleDate");
        final String unitScheduleId = schedule.get("unitScheduleId");
        final String unitScheduleName = schedule.get("unitScheduleName");
        final String unitScheduleCode = schedule.get("unitScheduleCode");
        final String unitType = schedule.get("unitType");
        final String conditionName = schedule.get("conditionName");
        final String scheduleUnit = schedule.get("scheduleUnit");
        final String maintenanceStatus = schedule.get("maintenanceStatus");
        final String maintenanceId = schedule.get("maintenanceId");

        if (scheduleCode != null) {
            holder.scheduleStatus.setVisibility(View.VISIBLE);
            holder.scheduleStatus.setText(R.string.time_schedule);
            holder.scheduleDate.setText(scheduleDate + " " + scheduleTime);
            holder.scheduleName.setText(scheduleName);
            holder.scheduleCode.setText(scheduleCode);
            holder.scheduleCode.setVisibility(View.VISIBLE);
        } else if (unitScheduleCode != null) {
            holder.scheduleStatus.setVisibility(View.VISIBLE);
            holder.scheduleStatus.setText(R.string.unit_schedule);
            holder.scheduleDate.setText(conditionName + " " + scheduleUnit + " " + unitType);
            holder.scheduleName.setText(unitScheduleName);
            holder.scheduleCode.setText(unitScheduleCode);
            holder.scheduleCode.setVisibility(View.VISIBLE);
        } else {
            holder.scheduleStatus.setVisibility(View.GONE);
            holder.scheduleDate.setVisibility(View.GONE);
            holder.scheduleCode.setVisibility(View.GONE);
            holder.scheduleName.setVisibility(View.VISIBLE);
            holder.scheduleName.setText("No Schedule");
            holder.scheduleName.setTextColor(Color.RED);
        }

        if (unitScheduleCode != null) {
            List<ConditionScheduleMaintenanceDetail> detailList = helper.getAllConditionMaintenanceDetails(Integer.parseInt(maintenanceId));
            for (ConditionScheduleMaintenanceDetail maintenanceDetail : detailList) {
                List<DumImage> images = helper.getDumImageByDetailId(maintenanceDetail.getId());
                if (images.size() != 0) {
                    holder.imgImage.setVisibility(View.VISIBLE);
                }
            }
        } else {
            List<MaintenanceDetail> detailList = helper.getAllMaintenanceDetails(Integer.parseInt(maintenanceId));
            for (MaintenanceDetail maintenanceDetail : detailList) {
                List<DumImage> images = helper.getDumImageByDetailId(maintenanceDetail.getId());
                if (images.size() != 0) {
                    holder.imgImage.setVisibility(View.VISIBLE);
                }
            }
        }

        if (maintenanceStatus.equals("STARTED") || maintenanceStatus.equals("RESUME") || maintenanceStatus.equals("FINISHED")) {
            holder.maintenanceStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        } else if (maintenanceStatus.equals("PAUSED")) {
            holder.maintenanceStatus.setTextColor(Color.RED);
        }
        holder.maintenanceStatus.setText(maintenanceStatus);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context.getApplicationContext(), MaintenanceActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (scheduleCode != null || unitScheduleCode != null) {
                intent.putExtra("no_schedule", 0);
            } else {
                intent.putExtra("no_schedule", 1);
            }
            intent.putExtra("asset_no", assetBean.getFaNumber());
            intent.putExtra("item_name", assetBean.getItemName());
            intent.putExtra("condition", assetBean.getCondition());
            intent.putExtra("location", locationCode + " - " + locationName);
            intent.putExtra("cost_center", assetBean.getCostCenter());
            intent.putExtra("category", assetBean.getCategory());
            intent.putExtra("serial_no", assetBean.getSerialNo());
            intent.putExtra("scheduleItemId", scheduleItemId);
            intent.putExtra("scheduleListId", scheduleListId);
            intent.putExtra("scheduleUnitId", scheduleUnitId);
            intent.putExtra("scheduleName", scheduleName);
            intent.putExtra("scheduleDate", holder.scheduleDate.getText());
            intent.putExtra("maintenanceStatus", maintenanceStatus);
            intent.putExtra("maintenanceId", maintenanceId);
            intent.putExtra("scheduleId", scheduleId);
            intent.putExtra("unitScheduleId", unitScheduleId);
            intent.putExtra("scheduleCode", scheduleCode);
            intent.putExtra("unitScheduleCode", unitScheduleCode);
            intent.putExtra("scheduleCodeStatus", holder.scheduleStatus.getText());
            if (scheduleName != null) {
                intent.putExtra("scheduleName", scheduleName);
            } else if (unitScheduleName != null) {
                intent.putExtra("scheduleName", unitScheduleName);
            }
            intent.putExtra("scheduleDate", holder.scheduleDate.getText());
            intent.putExtra("unit_type", unitType);
            intent.putExtra("condition_name", conditionName);
            intent.putExtra("schedule_unit", scheduleUnit);
            context.getApplicationContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView scheduleCode, scheduleStatus, scheduleName, scheduleDate, maintenanceStatus;
        private ImageView imgImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            scheduleCode = itemView.findViewById(R.id.schedule_code);
            scheduleStatus = itemView.findViewById(R.id.schedule_status);
            scheduleName = itemView.findViewById(R.id.maintenance_item_name);
            scheduleDate = itemView.findViewById(R.id.maintenance_item_date);
            maintenanceStatus = itemView.findViewById(R.id.maintenance_item_status);
            imgImage = itemView.findViewById(R.id.img_maintenance);
        }
    }
}

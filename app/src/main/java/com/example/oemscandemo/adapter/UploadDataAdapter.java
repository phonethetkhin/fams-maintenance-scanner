package com.example.oemscandemo.adapter;

import android.content.Context;
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
import com.example.oemscandemo.model.ConditionScheduleMaintenance;
import com.example.oemscandemo.model.ConditionScheduleMaintenanceDetail;
import com.example.oemscandemo.model.DumImage;
import com.example.oemscandemo.model.MaintenanceDetail;
import com.example.oemscandemo.utils.MyApplication;

import java.util.List;
import java.util.Map;

public class UploadDataAdapter extends RecyclerView.Adapter<UploadDataAdapter.ViewHolder> {
    private List<Object> scheduleList;
    Context context;
    private DBHelper helper;

    public UploadDataAdapter(List<Object> scheduleList, Context context) {
        this.scheduleList = scheduleList;
        this.context = context;
    }

    @NonNull
    @Override
    public UploadDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.upload_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        helper = new DBHelper(MyApplication.getContext());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UploadDataAdapter.ViewHolder holder, int position) {
        Map<String, String> schedule = (Map<String, String>) scheduleList.get(position);
        final String faNumber = schedule.get("faNumber");
        final String itemName = schedule.get("itemName");
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
        final String conditionUnitName = schedule.get("conditionUnitName");
        final String conditionUnitType = schedule.get("conditionUnitType");
        final String unit = schedule.get("unit");
        final String maintenanceStatus = schedule.get("maintenanceStatus");
        final String maintenanceId = schedule.get("maintenanceId");

        holder.assetNo.setText(faNumber);
        holder.itemName.setText(itemName);
        if (maintenanceStatus != null) {
            if (scheduleCode != null) {
                holder.scheduleCode.setVisibility(View.VISIBLE);
                holder.scheduleCode.setText(scheduleCode);
                holder.scheduleStatus.setVisibility(View.VISIBLE);
                holder.scheduleStatus.setText(R.string.time_schedule);
                if (scheduleName != null) {
                    holder.scheduleName.setVisibility(View.VISIBLE);
                    holder.scheduleName.setText(scheduleName);
                }
                holder.scheduleDate.setText(scheduleDate + " " + scheduleTime);

                List<MaintenanceDetail> detailList = helper.getAllMaintenanceDetails(Integer.parseInt(maintenanceId));
                for (MaintenanceDetail maintenanceDetail : detailList) {
                    List<DumImage> images = helper.getDumImageByDetailId(maintenanceDetail.getId());
                    if (images.size() != 0) {
                        holder.imgExit.setVisibility(View.VISIBLE);
                    }
                }
            } else if (unitScheduleCode != null) {
                holder.scheduleCode.setVisibility(View.VISIBLE);
                holder.scheduleCode.setText(unitScheduleCode);
                holder.scheduleStatus.setVisibility(View.VISIBLE);
                holder.scheduleStatus.setText(R.string.unit_schedule);
                if (unitScheduleName != null) {
                    holder.scheduleName.setVisibility(View.VISIBLE);
                    holder.scheduleName.setText(unitScheduleName);
                }
                ConditionScheduleMaintenance maintenance = helper.getConditionMaintenanceById(Integer.parseInt(maintenanceId));
                int current = (int) maintenance.getCurrentUnit();
                String currentUnit = conditionName + " " + current + " " + unitType;
                holder.scheduleDate.setText(currentUnit);

                List<ConditionScheduleMaintenanceDetail> detailList = helper.getAllConditionMaintenanceDetails(Integer.parseInt(maintenanceId));
                for (ConditionScheduleMaintenanceDetail conditionMaintenanceDetail : detailList) {
                    List<DumImage> images = helper.getDumImageByDetailId(conditionMaintenanceDetail.getId());
                    if (images.size() != 0) {
                        holder.imgExit.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                holder.scheduleCode.setVisibility(View.GONE);
                holder.scheduleName.setVisibility(View.GONE);
                holder.scheduleStatus.setVisibility(View.VISIBLE);
                holder.scheduleStatus.setText(R.string.no_schedule);
                holder.scheduleDate.setVisibility(View.GONE);

                List<MaintenanceDetail> detailList = helper.getAllMaintenanceDetails(Integer.parseInt(maintenanceId));
                for (MaintenanceDetail maintenanceDetail : detailList) {
                    List<DumImage> images = helper.getDumImageByDetailId(maintenanceDetail.getId());
                    if (images.size() != 0) {
                        holder.imgExit.setVisibility(View.VISIBLE);
                    }
                }
            }
            holder.maintenanceStatus.setVisibility(View.VISIBLE);
            if (maintenanceStatus.equals("PAUSED")) {
                holder.maintenanceStatus.setTextColor(Color.RED);
            }
            holder.maintenanceStatus.setText(maintenanceStatus);

        } else {
            String conditionCurrentUnit = conditionUnitName + " " + unit + " " + conditionUnitType;
            holder.scheduleDate.setText(conditionCurrentUnit);
            holder.scheduleStatus.setVisibility(View.VISIBLE);
            holder.scheduleStatus.setText("UNIT TAKING");
        }
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView assetNo, itemName, scheduleCode, scheduleName, scheduleDate, scheduleStatus, maintenanceStatus;
        private ImageView imgExit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            assetNo = itemView.findViewById(R.id.upload_faNo);
            itemName = itemView.findViewById(R.id.upload_item_name);
            scheduleCode = itemView.findViewById(R.id.upload_schedule_code);
            scheduleName = itemView.findViewById(R.id.upload_scheduleName);
            scheduleDate = itemView.findViewById(R.id.upload_scheduleDate);
            scheduleStatus = itemView.findViewById(R.id.upload_schedule_status);
            maintenanceStatus = itemView.findViewById(R.id.upload_maintenanceStatus);
            imgExit = itemView.findViewById(R.id.img_upload_exit);
        }
    }
}

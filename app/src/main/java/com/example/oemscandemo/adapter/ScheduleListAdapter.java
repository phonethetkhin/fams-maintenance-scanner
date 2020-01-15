package com.example.oemscandemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oemscandemo.R;
import com.example.oemscandemo.db.DBHelper;
import com.example.oemscandemo.model.AssetBean;
import com.example.oemscandemo.ui.MaintenanceActivity;

import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ViewHolder> {

    private List<Object> scheduleList;
    Context context;
    private SharedPreferences prefs;
    private String prefFANo = "faNo";
    DBHelper helper;

    public ScheduleListAdapter(List<Object> scheduleList, Context context) {
        this.scheduleList = scheduleList;
        this.context = context;
    }

    @NonNull
    @Override
    public ScheduleListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        helper = new DBHelper(context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleListAdapter.ViewHolder holder, int position) {
        Map<String, String> schedule = (Map<String, String>) scheduleList.get(position);
        final String scheduleId = schedule.get("scheduleId");
        final String scheduleItemId = schedule.get("scheduleItemId");
        final String scheduleListId = schedule.get("scheduleListId");
        final String scheduleUnitId = schedule.get("scheduleUnitId");
        final String scheduleCode = schedule.get("scheduleCode");
        final String scheduleName = schedule.get("scheduleName");
        final String scheduleTime = schedule.get("scheduleTime");
        final String scheduleDate = schedule.get("scheduleDate");
        final String unitType = schedule.get("unitType");
        final String conditionName = schedule.get("conditionName");
        final String unitScheduleId = schedule.get("unitScheduleId");
        final String unitScheduleCode = schedule.get("unitScheduleCode");
        final String scheduleUnit = schedule.get("scheduleUnit");

        prefs = context.getSharedPreferences(prefFANo, MODE_PRIVATE);
        String faNo = prefs.getString("faNo", null);
        AssetBean assetBean = helper.getFixedAssetByFANo(faNo);
        String locationName = helper.getLocationById(assetBean.getLocationId()).getName();
        String locationCode = helper.getLocationById(assetBean.getLocationId()).getCode();
        if (scheduleCode != null) {
            holder.scheduleCode.setText(scheduleCode);
            holder.scheduleCodeStatus.setVisibility(View.VISIBLE);
            holder.scheduleCodeStatus.setText(R.string.time_schedule);
            holder.scheduleDate.setText(scheduleDate + " " + scheduleTime);
        } else if (unitScheduleCode != null) {
            holder.scheduleCode.setText(unitScheduleCode);
            holder.scheduleCodeStatus.setVisibility(View.VISIBLE);
            holder.scheduleCodeStatus.setText(R.string.unit_schedule);
            holder.scheduleDate.setText(conditionName + " " + scheduleUnit + " " + unitType);
        }
        holder.scheduleName.setText(scheduleName);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context.getApplicationContext(), MaintenanceActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String maintenanceStatus = null;
            String maintenanceId = null;
            String description = null;
            intent.putExtra("no_schedule", 0);
            intent.putExtra("faId", assetBean.getId());
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
            intent.putExtra("scheduleCode", scheduleCode);
            intent.putExtra("scheduleName", scheduleName);
            intent.putExtra("schedule_unit", scheduleUnit);
            intent.putExtra("scheduleDate", scheduleDate);
            intent.putExtra("maintenanceStatus", maintenanceStatus);
            intent.putExtra("maintenanceId", maintenanceId);
            intent.putExtra("description", description);
            intent.putExtra("scheduleId", scheduleId);
            intent.putExtra("scheduleCode", scheduleCode);
            intent.putExtra("unitScheduleId", unitScheduleId);
            intent.putExtra("unitScheduleCode", unitScheduleCode);
            intent.putExtra("scheduleCodeStatus", holder.scheduleCodeStatus.getText());
            intent.putExtra("scheduleName", scheduleName);
            intent.putExtra("scheduleDate", holder.scheduleDate.getText());
            context.getApplicationContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView scheduleCode, scheduleCodeStatus, scheduleName, scheduleDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            scheduleCode = itemView.findViewById(R.id.schedule_code);
            scheduleCodeStatus = itemView.findViewById(R.id.schedule_status);
            scheduleName = itemView.findViewById(R.id.schedule_item_name);
            scheduleDate = itemView.findViewById(R.id.schedule_item_date);
        }
    }
}

package com.example.oemscandemo.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.oemscandemo.R;
import com.example.oemscandemo.db.DBHelper;
import com.example.oemscandemo.model.LocationsBean;
import com.example.oemscandemo.utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class UploadLocationAdapter extends RecyclerView.Adapter<UploadLocationAdapter.ViewHolder> {
    private List<LocationsBean> locationsList;
    private Context context;
    private DBHelper helper;
    private int selectedPosition = -1;
    private SharedPreferences prefs;
    private String prefSelectLocation = "select_location";

    public UploadLocationAdapter(List<LocationsBean> locationsList, Context context) {
        this.locationsList = locationsList;
        this.context = context;
    }

    @NonNull
    @Override
    public UploadLocationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_locate_item, parent, false);
        helper = new DBHelper(MyApplication.getContext());
        return new UploadLocationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UploadLocationAdapter.ViewHolder holder, int position) {
        final LocationsBean locationsBean = locationsList.get(position);
        String name = locationsBean.getCode() + " - " + locationsBean.getName();
        List<Object> scheduleList = helper.getTimeScheduleUploadObjects(locationsBean.getLocationId());
        List<Object> conditionScheduleList = helper.getConditionScheduleUploadObjects(locationsBean.getLocationId());
        List<Object> conditionUnitList = helper.getConditionUnitUploadObjects(locationsBean.getLocationId());
        List<Object> totalUploadList = new ArrayList<>();
        totalUploadList.addAll(scheduleList);
        totalUploadList.addAll(conditionScheduleList);
        totalUploadList.addAll(conditionUnitList);
        holder.txtLocate.setText(name);
        holder.txtCount.setText(String.valueOf(totalUploadList.size()));
        if (totalUploadList.size() > 0) {
            holder.txtStatus.setText(R.string.txt_maintenances);
        } else {
            holder.txtStatus.setText(R.string.txt_maintenance);
        }
        holder.rdButton.setTag(position);
        holder.itemView.setTag(position);

        if (position == selectedPosition) {
            holder.rdButton.setChecked(true);
        } else {
            holder.rdButton.setChecked(false);
        }

        holder.rdButton.setOnClickListener(onStateChangedListener(holder.rdButton, position));
        holder.rdButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                prefs = context.getSharedPreferences(prefSelectLocation, MODE_PRIVATE);
                SharedPreferences.Editor selectEditor = prefs.edit();
                selectEditor.clear();
                selectEditor.commit();
                selectEditor.putInt("select_locationId", locationsBean.getLocationId());
                selectEditor.apply();
            } else {
                holder.rdButton.setChecked(false);
            }
        });
    }

    private View.OnClickListener onStateChangedListener(final RadioButton radioButton, final int position) {
        return v -> {
            if (radioButton.isChecked()) {
                selectedPosition = position;
            } else {
                selectedPosition = -1;
            }
            notifyDataSetChanged();
        };
    }

    @Override
    public int getItemCount() {
        return locationsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtLocate, txtCount, txtStatus;
        private RadioButton rdButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rdButton = itemView.findViewById(R.id.radio_select_locate);
            txtLocate = itemView.findViewById(R.id.txt_locate);
            txtCount = itemView.findViewById(R.id.txt_count);
            txtStatus = itemView.findViewById(R.id.txt_status);
        }
    }
}

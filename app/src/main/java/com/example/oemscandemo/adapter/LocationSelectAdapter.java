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

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class LocationSelectAdapter extends RecyclerView.Adapter<LocationSelectAdapter.ViewHolder> {
    private List<LocationsBean> locationsList;
    private Context context;
    private DBHelper helper;
    private int selectedPosition = -1;
    private SharedPreferences prefs;
    private String prefSelectLocation = "select_location";

    public LocationSelectAdapter(List<LocationsBean> locationsList, Context context) {
        this.locationsList = locationsList;
        this.context = context;
    }

    @NonNull
    @Override
    public LocationSelectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_locate_item, parent, false);
        helper = new DBHelper(MyApplication.getContext());
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationSelectAdapter.ViewHolder holder, int position) {
        final LocationsBean locationsBean = locationsList.get(position);
        String name = locationsBean.getCode() + " - " + locationsBean.getName();
        int count = helper.getAssetCountByLocation(locationsBean.getLocationId());
        holder.txtLocate.setText(name);
        holder.txtCount.setText(String.valueOf(count));
        if (count > 0) {
            holder.txtStatus.setText(R.string.txt_assets);
        } else {
            holder.txtStatus.setText(R.string.txt_asset);
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

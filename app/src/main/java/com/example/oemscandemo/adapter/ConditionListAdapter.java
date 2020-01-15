package com.example.oemscandemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.oemscandemo.R;
import com.example.oemscandemo.model.AssetMaintenanceCondition;

import java.util.List;


public class ConditionListAdapter extends BaseAdapter {
    private List<AssetMaintenanceCondition> conditionList;
    private Context context;

    public ConditionListAdapter(List<AssetMaintenanceCondition> conditionList, Context context) {
        this.conditionList = conditionList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return conditionList.size();
    }

    @Override
    public Object getItem(int position) {
        return conditionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView conditionName, totalUnit, unitType;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.condition_item, parent, false);
        }
        AssetMaintenanceCondition condition = (AssetMaintenanceCondition) getItem(position);
        conditionName = convertView.findViewById(R.id.condition_name);
        totalUnit = convertView.findViewById(R.id.total_unit);
        unitType = convertView.findViewById(R.id.unit_type);
        int unit;
        if (condition.getUpdateUnit() != 0) {
            unit = (int) condition.getUpdateUnit();
        } else {
            unit = (int) condition.getTotalUnit();
        }
        conditionName.setText(condition.getConditionName());
        totalUnit.setText(String.valueOf(unit));
        unitType.setText(condition.getUnitType());
        return convertView;
    }
}

package com.example.oemscandemo.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.oemscandemo.R;
import com.example.oemscandemo.adapter.MaintenanceListAdapter;
import com.example.oemscandemo.db.DBHelper;
import com.example.oemscandemo.model.AssetBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MaintenanceListActivity extends AppCompatActivity {

    DBHelper helper;
    RecyclerView recyclerView;
    List<Object> timeMaintenanceList, conditionMaintenanceList;
    List<Object> dataList;
    MaintenanceListAdapter adapter;
    private SharedPreferences prefs;
    private String prefFANo = "faNo";
    TextView noMaintenance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_list);
        helper = new DBHelper(this);

        setContentView(R.layout.activity_maintenance_list);
        recyclerView = findViewById(R.id.recycler_maintenance_list);
        noMaintenance = findViewById(R.id.noMaintenance);
        prefs = getSharedPreferences(prefFANo, MODE_PRIVATE);
        String faNo = prefs.getString("faNo", null);

        AssetBean fixedAsset = helper.getFixedAssetByFANo(faNo);
        timeMaintenanceList = helper.getAllMaintenanceObjects(fixedAsset.getId());
        conditionMaintenanceList = helper.getAllConditionMaintenanceObjects(fixedAsset.getId());
        List<Object> scheduleList = new ArrayList<>();
        scheduleList.addAll(timeMaintenanceList);
        scheduleList.addAll(conditionMaintenanceList);

        if (scheduleList.size() > 0) {

            dataList = new ArrayList<>();

            for (int i = 0; i < scheduleList.size(); i++) {

                Map<String, String> data = (Map<String, String>) scheduleList.get(i);

                if (!TextUtils.isEmpty(data.get("maintenanceStatus"))) {
                    dataList.add(data);

                    if (dataList.size() > 0) {
                        adapter = new MaintenanceListAdapter(getApplicationContext(), dataList);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        adapter.notifyDataSetChanged();

                        recyclerView.setVisibility(View.VISIBLE);

                    } else {
                        noMaintenance.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else {
            noMaintenance.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        this.getParent().onBackPressed();
        finish();
    }
}

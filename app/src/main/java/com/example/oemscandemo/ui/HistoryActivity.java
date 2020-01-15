package com.example.oemscandemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import com.example.oemscandemo.R;
import com.example.oemscandemo.adapter.ConditionHistoryAdapter;
import com.example.oemscandemo.adapter.TimeHistoryAdapter;
import com.example.oemscandemo.db.DBHelper;
import com.example.oemscandemo.model.ConditionScheduleMaintenanceDetail;
import com.example.oemscandemo.model.MaintenanceDetail;

import java.util.Collections;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    DBHelper helper;
    Toolbar toolbar;
    ListView listView;
    TimeHistoryAdapter timeAdapter;
    ConditionHistoryAdapter conditionAdapter;
    List<MaintenanceDetail> timeDetailList;
    List<ConditionScheduleMaintenanceDetail> conditionDetailList;
    TextView historyFA;
    String faNo, maintenanceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        helper = new DBHelper(this);
        toolbar = findViewById(R.id.toolbar);
        historyFA = findViewById(R.id.history_fa);
        listView = findViewById(R.id.historyList);
        setupToolbar();

        faNo = getIntent().getStringExtra("h_fa_no");

        historyFA.setText(faNo);

        helper = new DBHelper(this);

        maintenanceId = getIntent().getStringExtra("h_maintenance_id");

        int status = getIntent().getIntExtra("status", 0);
        if (status == 0) {
            timeDetailList = helper.getAllMaintenanceDetails(Integer.parseInt(maintenanceId));

            Collections.sort(timeDetailList, (detail1, detail2) -> {
                if (detail1.getUpdatedDateAsString() != null && detail2.getUpdatedDateAsString() != null) {
                    return detail1.getUpdatedDateAsString().compareTo(detail2.getUpdatedDateAsString());
                }
                return 0;
            });

            timeAdapter = new TimeHistoryAdapter(timeDetailList, getApplicationContext());
            listView.setAdapter(timeAdapter);
            timeAdapter.notifyDataSetChanged();
        } else {
            conditionDetailList = helper.getAllConditionMaintenanceDetails(Integer.parseInt(maintenanceId));

            Collections.sort(conditionDetailList, (detail1, detail2) -> {
                if (detail1.getUpdatedDateAsString() != null && detail2.getUpdatedDateAsString() != null) {
                    return detail1.getUpdatedDateAsString().compareTo(detail2.getUpdatedDateAsString());
                }
                return 0;
            });

            conditionAdapter = new ConditionHistoryAdapter(conditionDetailList, getApplicationContext());
            listView.setAdapter(conditionAdapter);
            conditionAdapter.notifyDataSetChanged();
        }
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        getWindow().setWindowAnimations(0);
    }

    @Override
    public void onBackPressed() {

        int noSchedule = getIntent().getIntExtra("h_no_schedule", 0);
        int faId = getIntent().getIntExtra("h_fa_id", 0);
        String itemName = getIntent().getStringExtra("h_item_name");
        String condition = getIntent().getStringExtra("h_condition");
        String location = getIntent().getStringExtra("h_location");
        String costCenter = getIntent().getStringExtra("h_cost_center");
        String category = getIntent().getStringExtra("h_category");
        String serialNo = getIntent().getStringExtra("h_serial_no");
        String scheduleId = getIntent().getStringExtra("h_schedule_id");
        String scheduleCode = getIntent().getStringExtra("h_schedule_code");
        String scheduleCodeStatus = getIntent().getStringExtra("h_schedule_code_status");
        String scheduleName = getIntent().getStringExtra("h_schedule_name");
        String scheduleDate = getIntent().getStringExtra("h_schedule_date");
        String scheduleUnit = getIntent().getStringExtra("h_schedule_unit");
        String scheduleUnitId = getIntent().getStringExtra("h_schedule_unit_id");
        String unitScheduleId = getIntent().getStringExtra("h_unit_schedule_id");
        String unitScheduleCode = getIntent().getStringExtra("h_unit_schedule_code");
        String conditionName = getIntent().getStringExtra("h_condition_name");
        String unitType = getIntent().getStringExtra("h_unit_type");
        String maintenanceStatus = getIntent().getStringExtra("h_maintenance_status");
        String scheduleItemId = getIntent().getStringExtra("h_schedule_item_id");
        String scheduleListId = getIntent().getStringExtra("h_schedule_list_id");
        String remark = getIntent().getStringExtra("h_remark");
        String currentUnit = getIntent().getStringExtra("h_currentUnit");

        Intent intent = new Intent(HistoryActivity.this, MaintenanceActivity.class);
        intent.putExtra("no_schedule", noSchedule);
        intent.putExtra("faId", faId);
        intent.putExtra("asset_no", faNo);
        intent.putExtra("item_name", itemName);
        intent.putExtra("condition", condition);
        intent.putExtra("location", location);
        intent.putExtra("cost_center", costCenter);
        intent.putExtra("category", category);
        intent.putExtra("serial_no", serialNo);
        intent.putExtra("scheduleItemId", scheduleItemId);
        intent.putExtra("scheduleListId", scheduleListId);
        intent.putExtra("scheduleCode", scheduleCode);
        intent.putExtra("scheduleName", scheduleName);
        intent.putExtra("scheduleDate", scheduleDate);
        intent.putExtra("schedule_unit", scheduleUnit);
        intent.putExtra("scheduleUnitId", scheduleUnitId);
        intent.putExtra("unitScheduleId", unitScheduleId);
        intent.putExtra("unitScheduleCode", unitScheduleCode);
        intent.putExtra("condition_name", conditionName);
        intent.putExtra("unit_type", unitType);
        intent.putExtra("maintenanceStatus", maintenanceStatus);
        intent.putExtra("maintenanceId", maintenanceId);
        intent.putExtra("scheduleId", scheduleId);
        intent.putExtra("scheduleCode", scheduleCode);
        intent.putExtra("scheduleCodeStatus", scheduleCodeStatus);
        intent.putExtra("scheduleName", scheduleName);
        intent.putExtra("scheduleDate", scheduleDate);
        intent.putExtra("remark", remark);
        intent.putExtra("currentUnit", currentUnit);
        startActivity(intent);
        finish();
    }
}

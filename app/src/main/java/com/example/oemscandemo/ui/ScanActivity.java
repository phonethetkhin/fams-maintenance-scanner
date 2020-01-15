package com.example.oemscandemo.ui;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.oemscandemo.R;
import com.example.oemscandemo.db.DBHelper;
import com.example.oemscandemo.model.AssetBean;
import com.example.oemscandemo.model.DownloadInfo;

import java.util.ArrayList;
import java.util.List;

public class ScanActivity extends TabActivity implements View.OnClickListener {
    DBHelper helper;
    Toolbar toolbar;
    TabHost tabHost;
    List<AssetBean> assetList = new ArrayList<>();
    List<Object> timeScheduleList, unitScheduleList, timeMaintenances, conditionMaintenances;
    FloatingActionButton fabAdd;
    TextView noSchedule, selectedFANo, selectedFAStatus, selectedItemName,
            selected_FACategory, startDate, endDate, tvCount;

    int schedule = 0;
    int maintenance = 0;

    private SharedPreferences prefs;
    private String prefData = "maintenanceData";
    private String prefNoSchedule = "noSchedule";
    private String prefFANo = "faNo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        helper = new DBHelper(this);
        toolbar = findViewById(R.id.toolbar);
        noSchedule = findViewById(R.id.noSchedule);
        selectedFANo = findViewById(R.id.selected_fa_number);
        selectedFAStatus = findViewById(R.id.selected_fa_status);
        selectedItemName = findViewById(R.id.selected_fa_item_name);
        selected_FACategory = findViewById(R.id.selected_fa_category);
        startDate = findViewById(R.id.download_start_date);
        endDate = findViewById(R.id.download_end_date);
        fabAdd = findViewById(R.id.fab_add);

        setupToolbar();
        assetList = helper.getAllFA();
        prefs = getSharedPreferences(prefFANo, MODE_PRIVATE);
        String faNo = prefs.getString("faNo", null);

        AssetBean fixedAsset = helper.getFixedAssetByFANo(faNo);
        selectedFANo.setText(fixedAsset.getFaNumber());
        if (fixedAsset.getCondition().equals("OPR")) {
            selectedFAStatus.setText("OPERATIONAL");
        }
        selectedItemName.setText(fixedAsset.getItemName());
        selected_FACategory.setText(fixedAsset.getCategory());

        int id = 1;
        final DownloadInfo info = helper.getDownloadInfoById(id);
        startDate.setText(info.getDownloadStartDate());
        endDate.setText(info.getDownloadEndDate());
        timeScheduleList = helper.getAllScheduleObjects(fixedAsset.getId());
        unitScheduleList = helper.getAllUnitScheduleObjects(fixedAsset.getId());
        List<Object> schedules = new ArrayList<>();
        schedules.addAll(timeScheduleList);
        schedules.addAll(unitScheduleList);
        timeMaintenances = helper.getAllMaintenanceObjects(fixedAsset.getId());
        conditionMaintenances = helper.getAllConditionMaintenanceObjects(fixedAsset.getId());
        List<Object> maintenances = new ArrayList<>();
        maintenances.addAll(timeMaintenances);
        maintenances.addAll(conditionMaintenances);
        schedule = schedules.size();
        maintenance = maintenances.size();

        tabHost = getTabHost();
        this.setNewTab(this, tabHost, "tab1", R.string.textTabTitle1, schedule, R.drawable.custom_profile_icon, ScheduleActivity.class);
        this.setNewTab(this, tabHost, "tab2", R.string.textTabTitle2, maintenance, R.drawable.custom_profile_icon, MaintenanceListActivity.class);
        fabAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fab_add) {

            prefs = getSharedPreferences(prefFANo, MODE_PRIVATE);
            String faNo = prefs.getString("faNo", null);

            AssetBean fixedAsset = helper.getFixedAssetByFANo(faNo);
            int faId = fixedAsset.getId();
            String condition = fixedAsset.getCondition();

            Intent intent = new Intent(ScanActivity.this, MaintenanceActivity.class);
            intent.putExtra("faId", faId);
            intent.putExtra("asset_no", faNo);
            intent.putExtra("item_name", fixedAsset.getItemName());
            intent.putExtra("condition", condition);
            intent.putExtra("location", helper.getLocationById(fixedAsset.getLocationId()).getName());
            intent.putExtra("cost_center", fixedAsset.getCostCenter());
            intent.putExtra("category", fixedAsset.getCategory());
            intent.putExtra("serial_no", fixedAsset.getSerialNo());
            prefs = getSharedPreferences(prefData, MODE_PRIVATE);
            SharedPreferences.Editor dataEditor = prefs.edit();
            dataEditor.clear().commit();

            prefs = getSharedPreferences(prefNoSchedule, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear().commit();

            String scheduleId = null;
            String scheduleItemId = null;
            String scheduleListId = null;
            String scheduleName = null;
            String scheduleDate = null;
            String maintenanceStatus = null;
            String maintenanceId = null;
            String description = null;

            intent.putExtra("no_schedule", 1);
            intent.putExtra("scheduleId", scheduleId);
            intent.putExtra("scheduleItemId", scheduleItemId);
            intent.putExtra("scheduleListId", scheduleListId);
            intent.putExtra("scheduleName", scheduleName);
            intent.putExtra("scheduleDate", scheduleDate);
            intent.putExtra("maintenanceStatus", maintenanceStatus);
            intent.putExtra("maintenanceId", maintenanceId);
            intent.putExtra("description", description);
            startActivity(intent);
            finish();
        }
    }

    private void setNewTab(Context context, TabHost tabHost, String tag, int title, int size, int background, Class<?> content) {

        TabHost.TabSpec tabSpec = tabHost.newTabSpec(tag);
        tabSpec.setIndicator(getTabIndicator(tabHost.getContext(), title, size, background));
        tabSpec.setContent(new Intent(this, content));
        tabHost.addTab(tabSpec);
    }

    private View getTabIndicator(Context context, int title, int size, int background) {

        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
        TextView tv = view.findViewById(R.id.tab_label);
        View viewCount = view.findViewById(R.id.view_count);

        tv.setText(title);
        tv.setTextColor(Color.BLACK);
        tvCount = view.findViewById(R.id.count);
        tvCount.setText(Integer.toString(size));
        viewCount.setBackgroundResource(background);

        return view;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setupToolbar() {
        toolbar.setTitle(R.string.title_maintenance);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        getWindow().setWindowAnimations(0);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), AssetListActivity.class);
        startActivity(intent);
        finish();
    }
}

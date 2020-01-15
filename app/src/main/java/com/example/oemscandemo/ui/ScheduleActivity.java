package com.example.oemscandemo.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.oemscandemo.R;
import com.example.oemscandemo.adapter.ScheduleListAdapter;
import com.example.oemscandemo.db.DBHelper;
import com.example.oemscandemo.model.AssetBean;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {

    DBHelper helper;
    RecyclerView recyclerView;
    List<Object> timeScheduleList, unitScheduleList;
    ScheduleListAdapter adapter;

    private SharedPreferences prefs;
    private String prefFANo = "faNo";
    TextView noSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        helper = new DBHelper(this);
        recyclerView = findViewById(R.id.recycler_schedule_list);
        noSchedule = findViewById(R.id.noSchedule);

        prefs = getSharedPreferences(prefFANo, MODE_PRIVATE);
        String faNo = prefs.getString("faNo", null);

        AssetBean fixedAsset = helper.getFixedAssetByFANo(faNo);
        timeScheduleList = helper.getAllScheduleObjects(fixedAsset.getId());
        unitScheduleList = helper.getAllUnitScheduleObjects(fixedAsset.getId());
        List<Object> scheduleList = new ArrayList<>();
        scheduleList.addAll(timeScheduleList);
        scheduleList.addAll(unitScheduleList);
        if (scheduleList.size() > 0) {

            adapter = new ScheduleListAdapter(scheduleList, getApplicationContext());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            adapter.notifyDataSetChanged();

            recyclerView.setVisibility(View.VISIBLE);

        } else {
            noSchedule.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        this.getParent().onBackPressed();
        finish();
    }
}

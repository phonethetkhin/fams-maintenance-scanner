package com.example.oemscandemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.oemscandemo.R;
import com.example.oemscandemo.adapter.UploadDataAdapter;
import com.example.oemscandemo.db.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class UploadedDataActivity extends AppCompatActivity {
    DBHelper helper;
    Toolbar toolbar;
    List<Object> uploadedScheduleList, uploadedConditionScheduleList, uploadedConditionUnitList;
    RecyclerView recyclerView;
    TextView txtNoUpload;
    UploadDataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploaded_data);
        helper = new DBHelper(this);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_list_uploaded);
        txtNoUpload = findViewById(R.id.txt_no_upload);

        setupToolbar();
        getUploadedData();
    }

    private void getUploadedData() {
        uploadedScheduleList = helper.getAllUploadedScheduleData();
        uploadedConditionScheduleList = helper.getAllUploadedConditionScheduleData();
        uploadedConditionUnitList = helper.getAllUploadedConditionData();
        List<Object> uploadedList = new ArrayList<>();
        uploadedList.addAll(uploadedScheduleList);
        uploadedList.addAll(uploadedConditionScheduleList);
        uploadedList.addAll(uploadedConditionUnitList);

        if (uploadedList.size() > 0) {
            adapter = new UploadDataAdapter(uploadedList, getApplicationContext());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter.notifyDataSetChanged();
        } else {
            recyclerView.setVisibility(View.GONE);
            txtNoUpload.setVisibility(View.VISIBLE);
        }
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        getWindow().setWindowAnimations(0);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }
}

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
import com.example.oemscandemo.adapter.UnitTakeAdapter;
import com.example.oemscandemo.db.DBHelper;
import com.example.oemscandemo.model.AssetBean;
import com.example.oemscandemo.model.AssetMaintenanceCondition;

import java.util.ArrayList;
import java.util.List;

public class UnitTakingActivity extends AppCompatActivity {

    private DBHelper helper;
    private Toolbar toolbar;
    private List<AssetBean> assetList = new ArrayList<>();
    private RecyclerView assetRecycler;
    private TextView txtNoUnitTake;
    private UnitTakeAdapter unitTakeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_taking);
        helper = new DBHelper(this);
        toolbar = findViewById(R.id.toolbar);
        assetRecycler = findViewById(R.id.unit_take_recycler);
        txtNoUnitTake = findViewById(R.id.txt_no_unit_take);

        List<AssetBean> assetBeanList = helper.getAllFA();
        for (AssetBean assetBean : assetBeanList) {
            List<AssetMaintenanceCondition> conditionList = helper.getAssetConditionByAssetId(assetBean.getId());
            if (conditionList.size() != 0) {
                assetList.add(assetBean);
            }
        }

        unitTakeAdapter = new UnitTakeAdapter(assetList, getApplicationContext());
        assetRecycler.setAdapter(unitTakeAdapter);
        assetRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        getUnitTakingList();
        setupToolbar();
    }

    private void getUnitTakingList() {
        if (assetList.size() != 0) {
            unitTakeAdapter.notifyDataSetChanged();
        } else {
            assetRecycler.setVisibility(View.GONE);
            txtNoUnitTake.setVisibility(View.VISIBLE);
        }
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.nav_title_unit_taking);
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

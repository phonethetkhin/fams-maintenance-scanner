package com.example.oemscandemo.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oemscandemo.R;
import com.example.oemscandemo.adapter.ConditionListAdapter;
import com.example.oemscandemo.db.DBHelper;
import com.example.oemscandemo.model.AssetBean;
import com.example.oemscandemo.model.AssetMaintenanceCondition;
import com.example.oemscandemo.model.AssetMaintenanceConditionUnitBean;
import com.example.oemscandemo.model.User;
import com.example.oemscandemo.utils.Event;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class UnitTakingDetailActivity extends AppCompatActivity {
    private List<AssetMaintenanceCondition> conditionList;
    private DBHelper helper;
    private Toolbar toolbar;
    private Dialog dialog;
    private TextView txtAssetNo, txtItemName, txtCondition, txtLocation, txtCostCenter,
            txtCategory, txtSerialNo, conditionName, totalUnit, unitType;
    private ListView conditions;
    private ConditionListAdapter conditionAdapter;
    private EditText edUpdateUnit;
    private Button btnUpdateUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_taking_detail);
        helper = new DBHelper(this);
        toolbar = findViewById(R.id.toolbar);
        txtAssetNo = findViewById(R.id.fa_no);
        txtItemName = findViewById(R.id.item_name);
        txtCondition = findViewById(R.id.condition);
        txtLocation = findViewById(R.id.location);
        txtCostCenter = findViewById(R.id.cost_center);
        txtCategory = findViewById(R.id.category);
        txtSerialNo = findViewById(R.id.serial_no);
        conditions = findViewById(R.id.condition_list);

        String faNo = getIntent().getStringExtra("asset_no");
        String locationName = getIntent().getStringExtra("location");
        AssetBean assetBean = helper.getFixedAssetByFANo(faNo);
        String locationCode = helper.getLocationById(assetBean.getLocationId()).getCode();
        String location = locationCode + " - " + locationName;

        txtAssetNo.setText(faNo);
        txtItemName.setText(getIntent().getStringExtra("item_name"));
        txtCondition.setText(getIntent().getStringExtra("condition"));
        txtLocation.setText(location);
        txtCostCenter.setText(getIntent().getStringExtra("cost_center"));
        txtCategory.setText(getIntent().getStringExtra("category"));
        txtSerialNo.setText(getIntent().getStringExtra("serial_no"));

        conditionList = new ArrayList<>();
        conditionAdapter = new ConditionListAdapter(conditionList, this);
        conditions.setAdapter(conditionAdapter);
        conditions.setOnItemClickListener((parent, view, position, id) -> UpdateKilometer(position));
        forShow();
        setupToolbar();
        EventBus.getDefault().register(this);
    }

    private void forShow() {
        List<AssetMaintenanceCondition> assetConditions = helper.getAssetConditionByAssetId(getIntent().getIntExtra("asset_id", 0));

        conditionList.clear();
        conditionList.addAll(assetConditions);
        conditionAdapter.notifyDataSetChanged();
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
        Intent intent = new Intent(getApplicationContext(), UnitTakingActivity.class);
        startActivity(intent);
        finish();
    }

    public void UpdateKilometer(int position) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_kilometer_form);

        AssetMaintenanceCondition condition = conditionList.get(position);
        conditionName = dialog.findViewById(R.id.condition_name);
        totalUnit = dialog.findViewById(R.id.total_unit);
        unitType = dialog.findViewById(R.id.unit_type);
        edUpdateUnit = dialog.findViewById(R.id.ed_update_unit);
        btnUpdateUnit = dialog.findViewById(R.id.btn_update_unit);

        int currentUnit;
        if (condition.getUpdateUnit() != 0) {
            currentUnit = (int) condition.getUpdateUnit();
        } else {
            currentUnit = (int) condition.getTotalUnit();
        }

        conditionName.setText(condition.getConditionName());
        totalUnit.setText(String.valueOf(currentUnit));
        unitType.setText(condition.getUnitType());

        btnUpdateUnit.setOnClickListener(v -> {
            String updateUnit = String.valueOf(edUpdateUnit.getText());
            if (!updateUnit.equals("")) {
                int updUnit = Integer.parseInt(updateUnit);
                if (updUnit > currentUnit) {
                    Double uu = Double.valueOf(updateUnit);
                    AssetMaintenanceCondition assetCondition = conditionList.get(position);
                    assetCondition.setUpdateUnit(uu);
                    helper.updateAssetCondition(assetCondition);
                    AssetMaintenanceConditionUnitBean conditionUnitBean = new AssetMaintenanceConditionUnitBean();
                    conditionUnitBean.setConditionId(assetCondition.getId());
                    conditionUnitBean.setAssetId(assetCondition.getAssetId());
                    conditionUnitBean.setConditionName(assetCondition.getConditionName());
                    conditionUnitBean.setUnit(uu);
                    conditionUnitBean.setUnitType(assetCondition.getUnitType());
                    Calendar c = Calendar.getInstance();
                    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault());
                    String scanTime = dateFormat.format(c.getTime());
                    conditionUnitBean.setTakingDate(scanTime);
                    User user = helper.getUserById(1);
                    int userId = user.getUserId();
                    conditionUnitBean.setTakingBy(userId);
                    conditionUnitBean.setFlag(1);
                    conditionUnitBean.setUploadFlag(0);
                    helper.insertAssetConditionUnit(conditionUnitBean);
                    helper.close();
                    EventBus.getDefault().post(Event.Condition_Update);
                    dialog.cancel();
                } else {
                    Toast.makeText(getApplicationContext(), "Update Unit must be greater than Current Unit!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Update Unit must not be empty!", Toast.LENGTH_SHORT).show();
            }

        });
        dialog.show();
    }

    @Subscribe
    public void onEvent(Event event) {
        if (event.equals(Event.Condition_Update)) {
            forShow();
        }
    }
}

package com.example.oemscandemo.ui;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oemscandemo.R;
import com.example.oemscandemo.adapter.ImageListAdapter;
import com.example.oemscandemo.db.DBHelper;
import com.example.oemscandemo.model.AssetBean;
import com.example.oemscandemo.model.ConditionScheduleMaintenance;
import com.example.oemscandemo.model.ConditionScheduleMaintenanceDetail;
import com.example.oemscandemo.model.DumImage;
import com.example.oemscandemo.model.Maintenance;
import com.example.oemscandemo.model.MaintenanceDetail;
import com.example.oemscandemo.utils.Event;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MaintenanceActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CAMERA_PERMISSION_CODE = 3;
    private List<DumImage> imageList;
    private DBHelper helper;
    private Toolbar toolbar;
    private Button btnStart, btnPause, btnFinish, btnResume, btnHistory;
    private TextView txtFaNo, txtItemName, txtCondition, txtLocation, txtCostCenter, txtCategory, txtSerialNo,
            txtTimeScheduleCode, txtTimeScheduleCodeStatus, txtTimeScheduleName, txtTimeScheduleDate, txtTimeScheduleStatus,
            txtUnitScheduleCode, txtUnitScheduleCodeStatus, txtUnitScheduleName, txtTotalUnit, txtCurrentUnit,
            txtUnitScheduleStatus, txtNewScheduleName, txtNewScheduleStatus;
    private CardView newCard, timeScheduleCard, unitScheduleCard, editCard;
    private RecyclerView imgRecycler;
    private ImageListAdapter imageListAdapter;
    private TextInputLayout tiCurrentUnit, tiRemark;
    private EditText edCurrentUnit, edRemark;

    private Uri mCapturedImageURI;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    String faNo, itemName, condition, location, costCenter, category, serialNo, scheduleId, scheduleCode, scheduleCodeStatus,
            scheduleName, scheduleDate, maintenanceStatus, maintenanceId, currentUnit, remark, unitType, conditionName, scheduleUnitId,
            scheduleUnit, unitScheduleId, unitScheduleCode, scheduleItemId, scheduleListId;
    int noSchedule, faId;
    boolean cancel;
    View focusView;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
    Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);
        helper = new DBHelper(this);
        toolbar = findViewById(R.id.toolbar);
        txtFaNo = findViewById(R.id.fa_no);
        txtItemName = findViewById(R.id.item_name);
        txtCondition = findViewById(R.id.condition);
        txtLocation = findViewById(R.id.location);
        txtCostCenter = findViewById(R.id.cost_center);
        txtCategory = findViewById(R.id.category);
        txtSerialNo = findViewById(R.id.serial_no);
        txtTimeScheduleCode = findViewById(R.id.time_schedule_code);
        txtTimeScheduleCodeStatus = findViewById(R.id.time_schedule_code_status);
        txtTimeScheduleName = findViewById(R.id.time_schedule_item_name);
        txtTimeScheduleDate = findViewById(R.id.time_schedule_date);
        txtTimeScheduleStatus = findViewById(R.id.time_schedule_status);
        txtUnitScheduleCode = findViewById(R.id.unit_schedule_code);
        txtUnitScheduleCodeStatus = findViewById(R.id.unit_schedule_code_status);
        txtUnitScheduleName = findViewById(R.id.unit_schedule_item_name);
        txtTotalUnit = findViewById(R.id.total_unit);
        txtCurrentUnit = findViewById(R.id.current_unit);
        txtUnitScheduleStatus = findViewById(R.id.unit_schedule_status);
        txtNewScheduleName = findViewById(R.id.new_schedule);
        txtNewScheduleStatus = findViewById(R.id.new_schedule_status);
        tiCurrentUnit = findViewById(R.id.ti_current_unit);
        tiRemark = findViewById(R.id.ti_remark);
        edCurrentUnit = findViewById(R.id.write_current_unit);
        edRemark = findViewById(R.id.write_remark);
        newCard = findViewById(R.id.new_card);
        timeScheduleCard = findViewById(R.id.time_schedule_card);
        unitScheduleCard = findViewById(R.id.unit_schedule_card);
        editCard = findViewById(R.id.edit_card);
        imgRecycler = findViewById(R.id.img_recycler);
        btnStart = findViewById(R.id.startBtn);
        btnPause = findViewById(R.id.pauseBtn);
        btnFinish = findViewById(R.id.finishBtn);
        btnResume = findViewById(R.id.resumeBtn);
        btnHistory = findViewById(R.id.historyBtn);

        forShow();
        setupToolbar();
        imageList = new ArrayList<>();
        imageListAdapter = new ImageListAdapter(imageList, getApplicationContext());
        imgRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        imgRecycler.setAdapter(imageListAdapter);
        imageListAdapter.notifyDataSetChanged();

        edRemark.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        btnResume.setOnClickListener(this);
        btnHistory.setOnClickListener(this);
        EventBus.getDefault().register(this);
    }

    private void forShow() {
        noSchedule = getIntent().getIntExtra("no_schedule", 0);
        faId = getIntent().getIntExtra("faId", 0);
        faNo = getIntent().getStringExtra("asset_no");
        itemName = getIntent().getStringExtra("item_name");
        condition = getIntent().getStringExtra("condition");
        location = getIntent().getStringExtra("location");
        costCenter = getIntent().getStringExtra("cost_center");
        category = getIntent().getStringExtra("category");
        serialNo = getIntent().getStringExtra("serial_no");
        scheduleId = getIntent().getStringExtra("scheduleId");
        scheduleCode = getIntent().getStringExtra("scheduleCode");
        scheduleCodeStatus = getIntent().getStringExtra("scheduleCodeStatus");
        scheduleName = getIntent().getStringExtra("scheduleName");
        scheduleDate = getIntent().getStringExtra("scheduleDate");
        maintenanceStatus = getIntent().getStringExtra("maintenanceStatus");
        maintenanceId = getIntent().getStringExtra("maintenanceId");
        scheduleUnit = getIntent().getStringExtra("schedule_unit");
        scheduleUnitId = getIntent().getStringExtra("scheduleUnitId");
        unitScheduleId = getIntent().getStringExtra("unitScheduleId");
        unitScheduleCode = getIntent().getStringExtra("unitScheduleCode");
        unitType = getIntent().getStringExtra("unit_type");
        conditionName = getIntent().getStringExtra("condition_name");
        scheduleItemId = getIntent().getStringExtra("scheduleItemId");
        scheduleListId = getIntent().getStringExtra("scheduleListId");


        txtFaNo.setText(faNo);
        txtItemName.setText(itemName);
        if (condition.equals("OPR"))
            txtCondition.setText(R.string.txt_opr);
        txtLocation.setText(location);
        txtCostCenter.setText(costCenter);
        txtCategory.setText(category);
        txtSerialNo.setText(serialNo);
        txtTimeScheduleCode.setText(scheduleCode);
        txtTimeScheduleCodeStatus.setText(scheduleCodeStatus);
        txtTimeScheduleName.setText(scheduleName);
        txtTimeScheduleDate.setText(scheduleDate);
        txtUnitScheduleCode.setText(unitScheduleCode);
        txtUnitScheduleCodeStatus.setText(scheduleCodeStatus);
        txtUnitScheduleName.setText(scheduleName);
        txtTotalUnit.setText(scheduleDate);

        if (maintenanceStatus == null) {
            txtTimeScheduleStatus.setText(R.string.sch_new);
            txtUnitScheduleStatus.setText(R.string.sch_new);
            txtNewScheduleStatus.setText(R.string.sch_new);
            btnStart.setVisibility(View.VISIBLE);
        } else {
            if (maintenanceStatus.equals("STARTED")) {
                txtTimeScheduleStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                txtUnitScheduleStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                txtNewScheduleStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                btnStart.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                btnFinish.setVisibility(View.VISIBLE);
            } else if (maintenanceStatus.equals("PAUSED")) {
                txtTimeScheduleStatus.setTextColor(getResources().getColor(R.color.colorRed));
                txtUnitScheduleStatus.setTextColor(getResources().getColor(R.color.colorRed));
                txtNewScheduleStatus.setTextColor(getResources().getColor(R.color.colorRed));
                btnStart.setVisibility(View.GONE);
                btnPause.setVisibility(View.GONE);
                btnResume.setVisibility(View.VISIBLE);
                btnFinish.setVisibility(View.VISIBLE);
            } else if (maintenanceStatus.equals("RESUME")) {
                txtTimeScheduleStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                txtUnitScheduleStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                txtNewScheduleStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                btnStart.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                btnResume.setVisibility(View.GONE);
                btnFinish.setVisibility(View.VISIBLE);
            } else if (maintenanceStatus.equals("FINISHED")) {
                txtTimeScheduleStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                txtUnitScheduleStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                txtNewScheduleStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
                btnStart.setVisibility(View.GONE);
                btnPause.setVisibility(View.GONE);
                btnResume.setVisibility(View.GONE);
                btnFinish.setVisibility(View.GONE);
                editCard.setVisibility(View.GONE);
            }
            txtTimeScheduleStatus.setText(maintenanceStatus);
            txtNewScheduleStatus.setText(maintenanceStatus);
            txtUnitScheduleStatus.setText(maintenanceStatus);
        }

        txtNewScheduleName.setText("No Schedule");
        if (noSchedule != 0) {
            newCard.setVisibility(View.VISIBLE);
            timeScheduleCard.setVisibility(View.GONE);
            unitScheduleCard.setVisibility(View.GONE);
        } else {
            newCard.setVisibility(View.GONE);
            if (unitScheduleCode != null) {
                unitScheduleCard.setVisibility(View.VISIBLE);
                timeScheduleCard.setVisibility(View.GONE);
            } else {
                timeScheduleCard.setVisibility(View.VISIBLE);
                unitScheduleCard.setVisibility(View.GONE);
            }
        }
        if (unitScheduleCode != null) {
            if (maintenanceId != null) {
                ConditionScheduleMaintenance maintenance = helper.getConditionMaintenanceById(Integer.parseInt(maintenanceId));
                if (maintenance.getCurrentUnit() != 0.0) {
                    int current = (int) maintenance.getCurrentUnit();
                    currentUnit = "Current Unit : " + current + " " + unitType;
                    txtCurrentUnit.setVisibility(View.VISIBLE);
                    txtCurrentUnit.setText(currentUnit);
                } else {
                    tiCurrentUnit.setVisibility(View.VISIBLE);
                }
            } else {
                tiCurrentUnit.setVisibility(View.VISIBLE);
            }
            tiRemark.setVisibility(View.VISIBLE);
        } else {
            txtCurrentUnit.setVisibility(View.VISIBLE);
            tiCurrentUnit.setVisibility(View.GONE);
            tiRemark.setVisibility(View.VISIBLE);
        }
        remark = getIntent().getStringExtra("remark");
        if (remark != null) {
            edRemark.setText(remark);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.write_remark) {
            edRemark.setFocusable(true);
            edRemark.setFocusableInTouchMode(true);
        } else if (id == R.id.startBtn) {
            cancel = false;
            focusView = null;
            String edUnit = edCurrentUnit.getText().toString();
            if (unitScheduleCode != null) {
                if (edUnit.equals("") && TextUtils.isEmpty(edUnit)) {
                    edCurrentUnit.setError(getString(R.string.error_empty_current_unit));
                    focusView = edCurrentUnit;
                    cancel = true;
                }
            }

            if (cancel) {
                focusView.requestFocus();
            } else {
                ButtonStart();
            }
        } else if (id == R.id.pauseBtn) {
            ButtonPause();
        } else if (id == R.id.resumeBtn) {
            ButtonResume();
        } else if (id == R.id.finishBtn) {
            ButtonFinish();
        } else if (id == R.id.historyBtn) {
            ButtonHistory();
        }
    }

    private void ButtonStart() {
        AssetBean assetBean = helper.getFixedAssetByFANo(faNo);
        if (maintenanceId == null) {
            String startDate = dateFormat.format(c.getTime());
            String updatedDate = dateFormat.format(c.getTime());
            if (unitScheduleCode != null) {
                if (!helper.checkConditionMaintenanceDetailFinish("STARTED", unitScheduleCode, assetBean.getId(), assetBean.getLocationId())
                        && !helper.checkConditionMaintenanceDetailFinish("PAUSED", unitScheduleCode, assetBean.getId(), assetBean.getLocationId())
                        && !helper.checkConditionMaintenanceDetailFinish("RESUME", unitScheduleCode, assetBean.getId(), assetBean.getLocationId())) {
                    String edCurrent = edCurrentUnit.getText().toString();
                    ConditionScheduleMaintenance conditionMaintenance = new ConditionScheduleMaintenance();
                    conditionMaintenance.setMaintenanceId(0);
                    conditionMaintenance.setInsertScheduleId(Integer.parseInt(scheduleUnitId));
                    conditionMaintenance.setScheduleId(Integer.parseInt(unitScheduleId));
                    conditionMaintenance.setScheduleUnit(Double.parseDouble(scheduleUnit));
                    conditionMaintenance.setCurrentUnit(Double.parseDouble(edCurrent));
                    conditionMaintenance.setStartDateAsString(startDate);
                    conditionMaintenance.setStatus("STARTED");
                    conditionMaintenance.setAssetId(faId);
                    conditionMaintenance.setFlag(1);

                    helper.insertConditionMaintenance(conditionMaintenance);

                    ConditionScheduleMaintenanceDetail conditionMaintenanceDetail = new ConditionScheduleMaintenanceDetail();
                    List<String> data = helper.getConditionMaintenanceId();
                    String lastMaintenanceId = data.get(0);
                    ConditionScheduleMaintenance lastConditionMaintenance = helper.getConditionMaintenanceById(Integer.parseInt(lastMaintenanceId));
                    conditionMaintenanceDetail.setMaintenanceId(lastConditionMaintenance.getId());
                    conditionMaintenanceDetail.setStatus("STARTED");
                    conditionMaintenanceDetail.setUpdatedDateAsString(updatedDate);

                    if (!TextUtils.isEmpty(edRemark.getText().toString()))
                        conditionMaintenanceDetail.setRemark(edRemark.getText().toString());

                    helper.insertConditionMaintenanceDetail(conditionMaintenanceDetail);

                    if (imageList.size() != 0) {
                        for (DumImage image : imageList) {
                            DumImage dumImage = new DumImage();
                            List<String> lastMaintenanceDetail = helper.getConditionMaintenanceDetailId();
                            String lastMaintenanceDetailId = lastMaintenanceDetail.get(0);
                            dumImage.setMaintenanceDetailId(Integer.parseInt(lastMaintenanceDetailId));
                            dumImage.setImagePath(image.getImagePath());
                            dumImage.setFlag(1);
                            helper.addDumImage(dumImage);
                        }
                    }
                    Intent intent = new Intent(MaintenanceActivity.this, ScanActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Please finish your old maintenance!!!", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (!helper.checkMaintenanceDetailFinish("STARTED", scheduleCode, assetBean.getId(), assetBean.getLocationId()) &&
                        !helper.checkMaintenanceDetailFinish("PAUSED", scheduleCode, assetBean.getId(), assetBean.getLocationId()) &&
                        !helper.checkMaintenanceDetailFinish("RESUME", scheduleCode, assetBean.getId(), assetBean.getLocationId())) {
                    Maintenance maintenance = new Maintenance();
                    maintenance.setId(0);
                    maintenance.setAssetId(faId);
                    if (scheduleListId != null)
                        maintenance.setScheduleListId(Integer.parseInt(scheduleListId));
                    maintenance.setStartDateAsString(startDate);
                    maintenance.setStatus("STARTED");
                    maintenance.setMaintenanceBy(helper.getUserById(1).getId());
                    maintenance.setFlag(1);

                    helper.insertMaintenance(maintenance);

                    MaintenanceDetail maintenanceDetail = new MaintenanceDetail();
                    List<String> data = helper.getMaintenanceId();
                    String lastMaintenanceId = data.get(0);
                    Maintenance lastMaintenance = helper.getMaintenanceById(Integer.parseInt(lastMaintenanceId));
                    maintenanceDetail.setMaintenanceId(lastMaintenance.getMaintenanceId());
                    maintenanceDetail.setStatus("STARTED");
                    maintenanceDetail.setUpdatedDateAsString(updatedDate);

                    if (!TextUtils.isEmpty(edRemark.getText().toString()))
                        maintenanceDetail.setRemark(edRemark.getText().toString());

                    helper.insertMaintenanceDetail(maintenanceDetail);

                    if (imageList.size() != 0) {
                        for (DumImage image : imageList) {
                            DumImage dumImage = new DumImage();
                            List<String> lastMaintenanceDetail = helper.getMaintenanceDetailId();
                            String lastMaintenanceDetailId = lastMaintenanceDetail.get(0);
                            dumImage.setMaintenanceDetailId(Integer.parseInt(lastMaintenanceDetailId));
                            dumImage.setImagePath(image.getImagePath());
                            dumImage.setFlag(1);
                            helper.addDumImage(dumImage);
                        }
                    }
                    Intent intent = new Intent(MaintenanceActivity.this, ScanActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Please finish your old maintenance!!!", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private void ButtonPause() {
        String updatedDate = dateFormat.format(c.getTime());
        if (!TextUtils.isEmpty(maintenanceId)) {
            if (unitScheduleCode != null) {
                ConditionScheduleMaintenance conditionMaintenance = helper.getConditionMaintenanceById(Integer.parseInt(maintenanceId));
                conditionMaintenance.setStatus("PAUSED");
                conditionMaintenance.setFlag(1);
                conditionMaintenance.setUploadFlag(0);

                helper.updateConditionMaintenance(conditionMaintenance);

                ConditionScheduleMaintenanceDetail conditionMaintenanceDetail = new ConditionScheduleMaintenanceDetail();
                conditionMaintenanceDetail.setMaintenanceId(conditionMaintenance.getId());
                conditionMaintenanceDetail.setStatus("PAUSED");
                conditionMaintenanceDetail.setUpdatedDateAsString(updatedDate);

                if (!TextUtils.isEmpty(edRemark.getText().toString()))
                    conditionMaintenanceDetail.setRemark(edRemark.getText().toString());

                helper.insertConditionMaintenanceDetail(conditionMaintenanceDetail);

                if (imageList.size() != 0) {
                    for (DumImage image : imageList) {
                        DumImage dumImage = new DumImage();
                        List<String> lastMaintenanceDetail = helper.getConditionMaintenanceDetailId();
                        String lastMaintenanceDetailId = lastMaintenanceDetail.get(0);
                        dumImage.setMaintenanceDetailId(Integer.parseInt(lastMaintenanceDetailId));
                        dumImage.setImagePath(image.getImagePath());
                        dumImage.setFlag(1);
                        helper.addDumImage(dumImage);
                    }
                }
            } else {
                Maintenance maintenance = helper.getMaintenanceById(Integer.parseInt(maintenanceId));
                maintenance.setStatus("PAUSED");
                maintenance.setMaintenanceBy(helper.getUserById(1).getId());
                maintenance.setFlag(1);
                maintenance.setUploadFlag(0);

                helper.updateMaintenance(maintenance);

                MaintenanceDetail maintenanceDetail = new MaintenanceDetail();
                maintenanceDetail.setMaintenanceId(maintenance.getMaintenanceId());
                maintenanceDetail.setStatus("PAUSED");
                maintenanceDetail.setUpdatedDateAsString(updatedDate);

                if (!TextUtils.isEmpty(edRemark.getText().toString()))
                    maintenanceDetail.setRemark(edRemark.getText().toString());

                helper.insertMaintenanceDetail(maintenanceDetail);

                if (imageList.size() != 0) {
                    for (DumImage image : imageList) {
                        DumImage dumImage = new DumImage();
                        List<String> lastMaintenanceDetail = helper.getMaintenanceDetailId();
                        String lastMaintenanceDetailId = lastMaintenanceDetail.get(0);
                        dumImage.setMaintenanceDetailId(Integer.parseInt(lastMaintenanceDetailId));
                        dumImage.setImagePath(image.getImagePath());
                        dumImage.setFlag(1);
                        helper.addDumImage(dumImage);
                    }
                }
            }
            Intent intent = new Intent(MaintenanceActivity.this, ScanActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void ButtonResume() {
        String updatedDate = dateFormat.format(c.getTime());
        if (unitScheduleCode != null) {
            ConditionScheduleMaintenance conditionMaintenance = helper.getConditionMaintenanceById(Integer.parseInt(maintenanceId));
            conditionMaintenance.setStatus("RESUME");
            conditionMaintenance.setFlag(1);
            conditionMaintenance.setUploadFlag(0);

            helper.updateConditionMaintenance(conditionMaintenance);

            ConditionScheduleMaintenanceDetail conditionMaintenanceDetail = new ConditionScheduleMaintenanceDetail();
            conditionMaintenanceDetail.setMaintenanceId(conditionMaintenance.getId());
            conditionMaintenanceDetail.setStatus("RESUME");
            conditionMaintenanceDetail.setUpdatedDateAsString(updatedDate);

            if (!TextUtils.isEmpty(edRemark.getText().toString()))
                conditionMaintenanceDetail.setRemark(edRemark.getText().toString());

            helper.insertConditionMaintenanceDetail(conditionMaintenanceDetail);

            if (imageList.size() != 0) {
                for (DumImage image : imageList) {
                    DumImage dumImage = new DumImage();
                    List<String> lastMaintenanceDetail = helper.getConditionMaintenanceDetailId();
                    String lastMaintenanceDetailId = lastMaintenanceDetail.get(0);
                    dumImage.setMaintenanceDetailId(Integer.parseInt(lastMaintenanceDetailId));
                    dumImage.setImagePath(image.getImagePath());
                    dumImage.setFlag(1);
                    helper.addDumImage(dumImage);
                }
            }
        } else {
            Maintenance maintenance = helper.getMaintenanceById(Integer.parseInt(maintenanceId));
            maintenance.setStatus("RESUME");
            maintenance.setMaintenanceBy(helper.getUserById(1).getId());
            maintenance.setFlag(1);
            maintenance.setUploadFlag(0);

            helper.updateMaintenance(maintenance);

            MaintenanceDetail maintenanceDetail = new MaintenanceDetail();
            maintenanceDetail.setMaintenanceId(maintenance.getMaintenanceId());
            maintenanceDetail.setStatus("RESUME");
            maintenanceDetail.setUpdatedDateAsString(updatedDate);

            if (!TextUtils.isEmpty(edRemark.getText().toString()))
                maintenanceDetail.setRemark(edRemark.getText().toString());

            helper.insertMaintenanceDetail(maintenanceDetail);

            if (imageList.size() != 0) {
                for (DumImage image : imageList) {
                    DumImage dumImage = new DumImage();
                    List<String> lastMaintenanceDetail = helper.getMaintenanceDetailId();
                    String lastMaintenanceDetailId = lastMaintenanceDetail.get(0);
                    dumImage.setMaintenanceDetailId(Integer.parseInt(lastMaintenanceDetailId));
                    dumImage.setImagePath(image.getImagePath());
                    dumImage.setFlag(1);
                    helper.addDumImage(dumImage);
                }
            }

        }
        Intent intent = new Intent(MaintenanceActivity.this, ScanActivity.class);
        startActivity(intent);
        finish();
    }

    private void ButtonFinish() {
        if (!TextUtils.isEmpty(maintenanceId)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure to finish!");
            builder.setPositiveButton("OK", (dialog, which) -> {

                String updatedDate = dateFormat.format(c.getTime());
                String endDate = dateFormat.format(c.getTime());

                if (unitScheduleCode != null) {
                    ConditionScheduleMaintenance conditionMaintenance = helper.getConditionMaintenanceById(Integer.parseInt(maintenanceId));
                    conditionMaintenance.setStatus("FINISHED");
                    conditionMaintenance.setEndDateAsString(endDate);
                    conditionMaintenance.setFlag(1);
                    conditionMaintenance.setUploadFlag(0);

                    helper.updateConditionMaintenance(conditionMaintenance);

                    ConditionScheduleMaintenanceDetail conditionMaintenanceDetail = new ConditionScheduleMaintenanceDetail();
                    conditionMaintenanceDetail.setMaintenanceId(conditionMaintenance.getId());
                    conditionMaintenanceDetail.setStatus("FINISHED");
                    conditionMaintenanceDetail.setUpdatedDateAsString(updatedDate);

                    if (!TextUtils.isEmpty(edRemark.getText().toString()))
                        conditionMaintenanceDetail.setRemark(edRemark.getText().toString());

                    helper.insertConditionMaintenanceDetail(conditionMaintenanceDetail);

                    if (imageList.size() != 0) {
                        for (DumImage image : imageList) {
                            DumImage dumImage = new DumImage();
                            List<String> lastMaintenanceDetail = helper.getConditionMaintenanceDetailId();
                            String lastMaintenanceDetailId = lastMaintenanceDetail.get(0);
                            dumImage.setMaintenanceDetailId(Integer.parseInt(lastMaintenanceDetailId));
                            dumImage.setImagePath(image.getImagePath());
                            dumImage.setFlag(1);
                            helper.addDumImage(dumImage);
                        }
                    }
                } else {
                    Maintenance maintenance = helper.getMaintenanceById(Integer.parseInt(maintenanceId));
                    maintenance.setStatus("FINISHED");
                    maintenance.setEndDateAsString(endDate);
                    maintenance.setMaintenanceBy(helper.getUserById(1).getId());
                    maintenance.setFlag(1);
                    maintenance.setUploadFlag(0);

                    helper.updateMaintenance(maintenance);

                    MaintenanceDetail maintenanceDetail = new MaintenanceDetail();
                    maintenanceDetail.setMaintenanceId(maintenance.getMaintenanceId());
                    maintenanceDetail.setStatus("FINISHED");
                    maintenanceDetail.setUpdatedDateAsString(updatedDate);

                    if (!TextUtils.isEmpty(edRemark.getText().toString()))
                        maintenanceDetail.setRemark(edRemark.getText().toString());

                    helper.insertMaintenanceDetail(maintenanceDetail);

                    if (imageList.size() != 0) {
                        for (DumImage image : imageList) {
                            DumImage dumImage = new DumImage();
                            List<String> lastMaintenanceDetail = helper.getMaintenanceDetailId();
                            String lastMaintenanceDetailId = lastMaintenanceDetail.get(0);
                            dumImage.setMaintenanceDetailId(Integer.parseInt(lastMaintenanceDetailId));
                            dumImage.setImagePath(image.getImagePath());
                            dumImage.setFlag(1);
                            helper.addDumImage(dumImage);
                        }
                    }
                }
                Intent intent = new Intent(MaintenanceActivity.this, ScanActivity.class);
                startActivity(intent);
                finish();
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();
        }
    }

    private void ButtonHistory() {

        if (!TextUtils.isEmpty(maintenanceId)) {
            if (helper.getAllMaintenanceDetails(Integer.parseInt(maintenanceId)).size() > 0 ||
                    helper.getAllConditionMaintenanceDetails(Integer.parseInt(maintenanceId)).size() > 0) {
                Intent intent = new Intent(MaintenanceActivity.this, HistoryActivity.class);
                intent.putExtra("h_no_schedule", noSchedule);
                intent.putExtra("h_fa_id", faId);
                intent.putExtra("h_fa_no", faNo);
                intent.putExtra("h_item_name", itemName);
                intent.putExtra("h_condition", condition);
                intent.putExtra("h_location", location);
                intent.putExtra("h_cost_center", costCenter);
                intent.putExtra("h_category", category);
                intent.putExtra("h_serial_no", serialNo);
                intent.putExtra("h_schedule_id", scheduleId);
                intent.putExtra("h_schedule_code", scheduleCode);
                intent.putExtra("h_schedule_code_status", scheduleCodeStatus);
                intent.putExtra("h_schedule_name", scheduleName);
                intent.putExtra("h_schedule_date", scheduleDate);
                intent.putExtra("h_schedule_unit", scheduleUnit);
                intent.putExtra("h_schedule_unit_id", scheduleUnitId);
                intent.putExtra("h_unit_schedule_id", unitScheduleId);
                intent.putExtra("h_unit_schedule_code", unitScheduleCode);
                intent.putExtra("h_condition_name", conditionName);
                intent.putExtra("h_unit_type", unitType);
                intent.putExtra("h_maintenance_status", maintenanceStatus);
                intent.putExtra("h_maintenance_id", maintenanceId);
                intent.putExtra("h_schedule_item_id", scheduleItemId);
                intent.putExtra("h_schedule_list_id", scheduleListId);
                intent.putExtra("h_remark", edRemark.getText().toString());
                intent.putExtra("h_currentUnit", edCurrentUnit.getText().toString());
                if (unitScheduleCode != null) {
                    intent.putExtra("status", 1);
                }
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "No maintenance history for this asset!!!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "No maintenance history for this asset!!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        if (maintenanceStatus != null) {
            if (maintenanceStatus.equals("FINISHED")) {
                for (int i = 0; i < menu.size(); i++)
                    menu.getItem(i).setVisible(false);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_camera:
                if (imageList.size() < 3) {
                    selectImage();
                } else {
                    Toast.makeText(getApplicationContext(), "can't add greater then 3 photos!!!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MaintenanceActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals("Take Photo")) {
                activeTakePhoto();
            } else if (items[item].equals("Choose from Gallery")) {
                activeGallery();
            } else {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void activeTakePhoto() {
        checkPermission();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                goToCam();
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION_CODE);
            }
        } else {
            goToCam();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Application need camera permission", Toast.LENGTH_SHORT).show();
            } else {
                goToCam();
            }
        }
    }

    private void goToCam() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            String fileName = "temp.jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            mCapturedImageURI = getContentResolver()
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            takePictureIntent
                    .putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void activeGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case RESULT_LOAD_IMAGE:
                if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver()
                            .query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    DumImage image = new DumImage();
                    List<String> lastMaintenanceDetail = helper.getMaintenanceDetailId();
                    if (lastMaintenanceDetail.size() != 0) {
                        String lastMaintenanceDetailId = lastMaintenanceDetail.get(0);
                        image.setMaintenanceDetailId(Integer.parseInt(lastMaintenanceDetailId));
                    }
                    image.setImagePath(picturePath);
                    image.setFlag(1);
                    imageList.add(image);
                    EventBus.getDefault().post(Event.Add_Photo);
                }
            case REQUEST_IMAGE_CAPTURE:
                if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

                    String[] projection = {MediaStore.Images.Media.DATA};
                    Cursor cursor = managedQuery(mCapturedImageURI, projection, null, null, null);
                    int column_index_data = cursor.getColumnIndexOrThrow(
                            MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String picturePath = cursor.getString(column_index_data);

                    DumImage image = new DumImage();
                    List<String> lastMaintenanceDetail = helper.getMaintenanceDetailId();
                    if (lastMaintenanceDetail.size() != 0) {
                        String lastMaintenanceDetailId = lastMaintenanceDetail.get(0);
                        image.setMaintenanceDetailId(Integer.parseInt(lastMaintenanceDetailId));
                    }
                    image.setImagePath(picturePath);
                    image.setFlag(1);
                    imageList.add(image);
                    EventBus.getDefault().post(Event.Add_Photo);
                }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        if (mCapturedImageURI != null) {
            outState.putString("mCapturedImageURI", mCapturedImageURI.toString());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey("mCapturedImageURI")) {
            mCapturedImageURI = Uri.parse(
                    savedInstanceState.getString("mCapturedImageURI"));
        }
    }

    @Subscribe
    public void onEvent(Event event) {
        if (event.equals(Event.Add_Photo)) {
            imageListAdapter = new ImageListAdapter(imageList, getApplicationContext());
            imgRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            imgRecycler.setAdapter(imageListAdapter);
            imageListAdapter.notifyDataSetChanged();
        }
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_maintenance_detail);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        getWindow().setWindowAnimations(0);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MaintenanceActivity.this, ScanActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

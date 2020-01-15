package com.example.oemscandemo.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oemscandemo.R;
import com.example.oemscandemo.adapter.UploadDataAdapter;
import com.example.oemscandemo.db.DBHelper;
import com.example.oemscandemo.model.AssetMaintenanceConditionUnitBean;
import com.example.oemscandemo.model.ConditionScheduleMaintenance;
import com.example.oemscandemo.model.ConditionScheduleMaintenanceDetail;
import com.example.oemscandemo.model.DeviceInfo;
import com.example.oemscandemo.model.DumImage;
import com.example.oemscandemo.model.LocationsBean;
import com.example.oemscandemo.model.Maintenance;
import com.example.oemscandemo.model.MaintenanceDetail;
import com.example.oemscandemo.model.MaintenanceEvidence;
import com.example.oemscandemo.model.StaticContent;
import com.example.oemscandemo.model.UploadConditionMaintenance;
import com.example.oemscandemo.model.UploadConditionUnit;
import com.example.oemscandemo.model.UploadMaintenance;
import com.example.oemscandemo.model.User;
import com.example.oemscandemo.retrofit.ApiService;
import com.example.oemscandemo.retrofit.Constant;
import com.example.oemscandemo.retrofit.ServiceApi;
import com.example.oemscandemo.retrofit.ServiceGenerator;
import com.example.oemscandemo.utils.Event;
import com.example.oemscandemo.utils.NetworkUtils;
import com.google.gson.internal.LinkedTreeMap;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadMaintenanceActivity extends AppCompatActivity implements View.OnClickListener {

    DBHelper helper;
    Toolbar toolbar;
    List<Maintenance> maintenanceLists;
    List<MaintenanceDetail> maintenanceDetailList;
    List<ConditionScheduleMaintenance> conditionMaintenanceList;
    List<ConditionScheduleMaintenanceDetail> conditionMaintenanceDetailList;
    List<AssetMaintenanceConditionUnitBean> assetConditionList;
    List<Object> scheduleList, conditionScheduleList, conditionUnitList, totalUploadList;
    RecyclerView recyclerView;
    UploadDataAdapter adapter;
    Button btnUpload, back;
    ProgressDialog dialog;
    TextView allUploaded, uploadLocation, uploadMaintenanceCount;

    private String deviceCode, licenseKey, token;
    private int userId;
    private int locationUploadId;
    private int uploadCount;

    private SharedPreferences prefs;
    private String prefName = "user";
    private String prefLicense = "licenseKey";
    private String prefSelectLocation = "select_location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_maintenance);
        helper = new DBHelper(this);
        toolbar = findViewById(R.id.toolbar);
        btnUpload = findViewById(R.id.btnUpload);
        back = findViewById(R.id.btnUploadBack);
        allUploaded = findViewById(R.id.all_uploaded);
        uploadLocation = findViewById(R.id.upload_location);
        uploadMaintenanceCount = findViewById(R.id.upload_maintenance_count);
        recyclerView = findViewById(R.id.recycler_list_upload);

        setupToolbar();

        dialog = new ProgressDialog(this);
        dialog.setTitle("Uploading Maintenance...");
        dialog.setMessage("Please...Wait...!!!");
        dialog.setCancelable(false);
        dialog.setIndeterminate(false);

        locationUploadId = getIntent().getIntExtra("location_upload_id", 0);
        scheduleList = helper.getTimeScheduleUploadObjects(locationUploadId);
        conditionScheduleList = helper.getConditionScheduleUploadObjects(locationUploadId);
        conditionUnitList = helper.getConditionUnitUploadObjects(locationUploadId);
        totalUploadList = new ArrayList<>();
        totalUploadList.addAll(scheduleList);
        totalUploadList.addAll(conditionScheduleList);
        totalUploadList.addAll(conditionUnitList);
        showList();

        uploadLocation.setText(helper.getLocationById(locationUploadId).getName());
        uploadMaintenanceCount.setText(uploadCount + " Maintenances");

        maintenanceLists = helper.getAllMaintenance(locationUploadId);
        conditionMaintenanceList = helper.getAllConditionMaintenance(locationUploadId);
        assetConditionList = helper.getAllAssetMaintenanceCondition(locationUploadId);
        if (maintenanceLists.size() == 0 && conditionMaintenanceList.size() == 0 && assetConditionList.size() == 0) {
            allUploaded.setVisibility(View.VISIBLE);
            btnUpload.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);
        }
        btnUpload.setOnClickListener(this);
        back.setOnClickListener(this);
        EventBus.getDefault().register(this);
    }

    private void showList() {
        if (totalUploadList.size() > 0) {
            uploadCount = totalUploadList.size();

            adapter = new UploadDataAdapter(totalUploadList, getApplicationContext());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        } else {
            uploadCount = 0;
            List<Object> emptyList = new ArrayList<>();
            adapter = new UploadDataAdapter(emptyList, getApplicationContext());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnUpload) {
            NetworkUtils utils = new NetworkUtils(this);
            if (utils.isConnectingToInternet()) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle(R.string.title_upload_confirm);
                alertDialog.setMessage(R.string.warning_upload_confirm);
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton(R.string.btn_ok, (dialog, which) -> uploadMaintenance());
                alertDialog.setNegativeButton(R.string.btn_cancel, (dialog, which) -> {
                });
                alertDialog.show();
            }
        } else if (id == R.id.btnUploadBack) {
            onBackPressed();
        }
    }

    private void uploadMaintenance() {
        dialog.show();
        DeviceInfo deviceInfo = helper.getDeviceInfoById(1);
        deviceCode = deviceInfo.getDeviceCode();
        prefs = Objects.requireNonNull(getSharedPreferences(prefLicense, MODE_PRIVATE));
        licenseKey = prefs.getString("license_key", null);
        prefs = Objects.requireNonNull(getSharedPreferences(prefName, MODE_PRIVATE));
        User user = helper.getUserById(1);
        userId = user.getUserId();
        token = prefs.getString("token", null);
        final ApiService service = new ApiService();
        Map<String, Object> request = new HashMap<>();
        LocationsBean locationsBean = helper.getLocationById(locationUploadId);
        request.put("appType", Constant.APP_TYPE);
        request.put("deviceCode", deviceCode);
        request.put("licenseKey", licenseKey);
        request.put("userId", userId);
        request.put("token", token);
        request.put("downloadId", locationsBean.getDownload());

        service.uploadFileStart(request, new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful()) {
                    Map<String, Object> result = response.body();
                    double uploadId = (double) result.get("uploadId");
                    String status = (String) result.get("status");
                    if (status.equals("SUCCESS")) {
                        uploadConditionUnit(uploadId);
                        uploadTimeMaintenance(uploadId);
                        uploadConditionMaintenance(uploadId);
                    }
                    EventBus.getDefault().post(Event.After_Upload);
                } else {
                    dialog.dismiss();
                    btnUpload.setVisibility(View.GONE);
                    back.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        EventBus.getDefault().post(Event.After_Upload);
    }

    private void uploadTimeMaintenance(double uploadId) {
        maintenanceLists = helper.getAllMaintenance(locationUploadId);

        for (int i = 0; i < maintenanceLists.size(); i++) {

            final Maintenance maintenance = maintenanceLists.get(i);

            maintenanceDetailList = helper.getAllUploadMaintenanceDetails(maintenance.getMaintenanceId());
            if (maintenanceDetailList.size() != 0) {
                for (MaintenanceDetail maintenanceDetail : maintenanceDetailList) {

                    maintenanceDetail.setUploadId((int) uploadId);

                    helper.updateMaintenanceDetail(maintenanceDetail);
                    helper.close();

                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    Callable<List<StaticContent>> callable1 = () -> {

                        List<DumImage> images = helper.getDumImageByDetailId(maintenanceDetail.getId());
                        List<StaticContent> contents = new ArrayList<>();
                        if (images.size() != 0) {
                            uploadImage(images, contents);
                        }
                        return contents;
                    };

                    List<MaintenanceEvidence> evidenceList = new ArrayList<>();
                    List<StaticContent> contents;
                    Future<List<StaticContent>> future1 = executorService.submit(callable1);

                    try {
                        contents = future1.get();
                        for (StaticContent content : contents) {
                            MaintenanceEvidence maintenanceEvidence = new MaintenanceEvidence();
                            maintenanceEvidence.setMaintenanceDetailId(maintenanceDetail.getId());
                            maintenanceEvidence.setContent(content);
                            evidenceList.add(maintenanceEvidence);
                        }

                    } catch (Exception e) {
                        executorService.shutdown();
                        e.printStackTrace();
                    }
                    maintenanceDetail.setEvidenceList(evidenceList);
                }
                UploadMaintenance uploadMaintenance = new UploadMaintenance();
                uploadMaintenance.setId(maintenance.getId());
                uploadMaintenance.setScheduleListId(maintenance.getScheduleListId());
                uploadMaintenance.setAssetId(maintenance.getAssetId());
                uploadMaintenance.setStartDateAsString(maintenance.getStartDateAsString());
                uploadMaintenance.setEndDateAsString(maintenance.getEndDateAsString());
                uploadMaintenance.setStatus(maintenance.getStatus());
                uploadMaintenance.setMaintenanceBy(maintenance.getMaintenanceBy());
                uploadMaintenance.setDetailList(maintenanceDetailList);

                Map<String, Object> requestTimeMaintenance = new HashMap<>();
                requestTimeMaintenance.put("appType", Constant.APP_TYPE);
                requestTimeMaintenance.put("deviceCode", deviceCode);
                requestTimeMaintenance.put("licenseKey", licenseKey);
                requestTimeMaintenance.put("userId", userId);
                requestTimeMaintenance.put("token", token);
                requestTimeMaintenance.put("maintenance", uploadMaintenance);
                ApiService service = new ApiService();
                service.timeMaintenanceUpload(requestTimeMaintenance, new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                Map<String, Object> result1 = response.body();
                                double id = (double) result1.get("id");
                                Map<String, Object> request4 = new HashMap<>();
                                request4.put("appType", Constant.APP_TYPE);
                                request4.put("deviceCode", deviceCode);
                                request4.put("licenseKey", licenseKey);
                                request4.put("userId", userId);
                                request4.put("token", token);
                                request4.put("uploadId", (int) uploadId);
                                service.uploadFileEnd(request4, new Callback<Map<String, Object>>() {
                                    @Override
                                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                                        if (response.isSuccessful()) {
                                            maintenance.setId((int) id);
                                            maintenance.setUploadFlag(1);
                                            helper.updateMaintenance(maintenance);
                                            EventBus.getDefault().post(Event.After_Upload);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                                        dialog.dismiss();
                                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            maintenanceDetailList = helper.getAllUploadFailMaintenanceDetails(maintenance.getMaintenanceId());
                            for (MaintenanceDetail detail : maintenanceDetailList) {
                                detail.setUploadId(0);
                                helper.updateMaintenanceDetail(detail);
                                helper.close();
                            }
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Upload Fail!!!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        maintenanceDetailList = helper.getAllUploadFailMaintenanceDetails(maintenance.getMaintenanceId());
                        for (MaintenanceDetail detail : maintenanceDetailList) {
                            detail.setUploadId(0);
                            helper.updateMaintenanceDetail(detail);
                            helper.close();
                        }
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Unknown Error occur in uploading process.\nCheck your upload server!!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        EventBus.getDefault().post(Event.After_Upload);
    }

    private void uploadConditionMaintenance(double uploadId) {
        conditionMaintenanceList = helper.getAllConditionMaintenance(locationUploadId);
        for (int i = 0; i < conditionMaintenanceList.size(); i++) {
            final ConditionScheduleMaintenance maintenance = conditionMaintenanceList.get(i);

            conditionMaintenanceDetailList = helper.getAllUploadConditionMaintenanceDetails(maintenance.getId());
            if (conditionMaintenanceDetailList.size() != 0) {
                for (ConditionScheduleMaintenanceDetail maintenanceDetail : conditionMaintenanceDetailList) {

                    maintenanceDetail.setDeviceUploadId((int) uploadId);
                    maintenanceDetail.setUpdatedBy(userId);

                    helper.updateConditionMaintenanceDetail(maintenanceDetail);
                    helper.close();

                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    Callable<List<StaticContent>> callable1 = () -> {

                        List<DumImage> images = helper.getDumImageByDetailId(maintenanceDetail.getId());
                        List<StaticContent> contents = new ArrayList<>();
                        if (images.size() != 0) {
                            uploadImage(images, contents);
                        }
                        return contents;
                    };

                    Future<List<StaticContent>> future1 = executorService.submit(callable1);
                    try {
                        List<DumImage> images = helper.getDumImageByDetailId(maintenanceDetail.getId());
                        if (images.size() != 0) {
                            maintenanceDetail.setContentList(future1.get());
                        }
                    } catch (Exception e) {
                        executorService.shutdown();
                        e.printStackTrace();
                    }

                }
                UploadConditionMaintenance uploadMaintenance = new UploadConditionMaintenance();
                uploadMaintenance.setId(maintenance.getMaintenanceId());
                uploadMaintenance.setScheduleId(maintenance.getScheduleId());
                uploadMaintenance.setDeviceUploadId((int) uploadId);
                uploadMaintenance.setScheduleUnit(maintenance.getScheduleUnit());
                uploadMaintenance.setCurrentUnit(maintenance.getCurrentUnit());
                uploadMaintenance.setStartDateAsString(maintenance.getStartDateAsString());
                uploadMaintenance.setEndDateAsString(maintenance.getEndDateAsString());
                uploadMaintenance.setStatus(maintenance.getStatus());
                uploadMaintenance.setAssetId(maintenance.getAssetId());
                uploadMaintenance.setDetailList(conditionMaintenanceDetailList);

                Map<String, Object> requestConditionMaintenance = new HashMap<>();
                requestConditionMaintenance.put("appType", Constant.APP_TYPE);
                requestConditionMaintenance.put("deviceCode", deviceCode);
                requestConditionMaintenance.put("licenseKey", licenseKey);
                requestConditionMaintenance.put("userId", userId);
                requestConditionMaintenance.put("token", token);
                requestConditionMaintenance.put("maintenance", uploadMaintenance);
                ApiService service = new ApiService();
                service.conditionMaintenanceUpload(requestConditionMaintenance, new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                Map<String, Object> result2 = response.body();
                                double id = (double) result2.get("id");
                                Map<String, Object> request4 = new HashMap<>();
                                request4.put("appType", Constant.APP_TYPE);
                                request4.put("deviceCode", deviceCode);
                                request4.put("licenseKey", licenseKey);
                                request4.put("userId", userId);
                                request4.put("token", token);
                                request4.put("uploadId", (int) uploadId);
                                service.uploadFileEnd(request4, new Callback<Map<String, Object>>() {
                                    @Override
                                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                                        if (response.isSuccessful()) {
                                            maintenance.setMaintenanceId((int) id);
                                            maintenance.setDeviceUploadId((int) uploadId);
                                            maintenance.setUploadFlag(1);
                                            helper.updateConditionMaintenance(maintenance);
                                            EventBus.getDefault().post(Event.After_Upload);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                                        dialog.dismiss();
                                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        } else {
                            conditionMaintenanceDetailList = helper.getAllUploadFailConditionMaintenanceDetails(maintenance.getId());
                            for (ConditionScheduleMaintenanceDetail conditionMaintenanceDetail : conditionMaintenanceDetailList) {
                                conditionMaintenanceDetail.setDeviceUploadId(0);
                                helper.updateConditionMaintenanceDetail(conditionMaintenanceDetail);
                            }
                            Toast.makeText(getApplicationContext(), "Upload Fail!!!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        conditionMaintenanceDetailList = helper.getAllUploadFailConditionMaintenanceDetails(maintenance.getId());
                        for (ConditionScheduleMaintenanceDetail conditionMaintenanceDetail : conditionMaintenanceDetailList) {
                            conditionMaintenanceDetail.setDeviceUploadId(0);
                            helper.updateConditionMaintenanceDetail(conditionMaintenanceDetail);
                        }
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Unknown Error occur in uploading process.\nCheck your upload server!!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        EventBus.getDefault().post(Event.After_Upload);
    }

    private void uploadConditionUnit(double uploadId) {
        assetConditionList = helper.getAllAssetMaintenanceCondition(locationUploadId);
        for (AssetMaintenanceConditionUnitBean conditionBean : assetConditionList) {
            UploadConditionUnit conditionUnitBean = new UploadConditionUnit();
            conditionUnitBean.setConditionId(conditionBean.getConditionId());
            conditionUnitBean.setUnit(conditionBean.getUnit());
            conditionUnitBean.setTakingDate(conditionBean.getTakingDate());
            conditionUnitBean.setTakingBy(conditionBean.getTakingBy());
            Map<String, Object> request3 = new HashMap<>();
            request3.put("appType", Constant.APP_TYPE);
            request3.put("deviceCode", deviceCode);
            request3.put("licenseKey", licenseKey);
            request3.put("userId", userId);
            request3.put("token", token);
            request3.put("assetConditionUnit", conditionUnitBean);

            ApiService service = new ApiService();
            service.conditionUpload(request3, new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    if (response.isSuccessful()) {
                        if (response.code() == 200) {

                            Map<String, Object> request4 = new HashMap<>();
                            request4.put("appType", Constant.APP_TYPE);
                            request4.put("deviceCode", deviceCode);
                            request4.put("licenseKey", licenseKey);
                            request4.put("userId", userId);
                            request4.put("token", token);
                            request4.put("uploadId", (int) uploadId);
                            service.uploadFileEnd(request4, new Callback<Map<String, Object>>() {
                                @Override
                                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                                    if (response.isSuccessful()) {
                                        conditionBean.setUploadFlag(1);
                                        helper.updateAssetConditionUnit(conditionBean);
                                        EventBus.getDefault().post(Event.After_Upload);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else if (response.code() == 408) {
                        dialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                        builder.setTitle(R.string.dialog_session_expired);
                        builder.setCancelable(false);
                        builder.setPositiveButton("Ok", (dialog, which) -> {
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        });
                        builder.show();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Upload Fail!!!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        EventBus.getDefault().post(Event.After_Upload);
    }

    private void uploadImage(List<DumImage> images, List<StaticContent> contentList) {

        File file = new File(images.get(0).getImagePath());
        RequestBody reqFile = RequestBody.create(MediaType.parse("image"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "android/maintenance");
        ServiceApi mApi = ServiceGenerator.createService(ServiceApi.class);
        Call<LinkedTreeMap> imageUpload = mApi.postImage(Constant.APP_TYPE, deviceCode, licenseKey, userId, token, body, name);
        try {
            LinkedTreeMap result = imageUpload.execute().body();
            Map<String, String> map = (Map<String, String>) ((List) result.get("uploadFiles")).get(0);
            StaticContent staticContent = new StaticContent();
            staticContent.setFileName(map.get("fileName"));
            staticContent.setFileSize(map.get("fileSize"));
            staticContent.setFilePath(map.get("filePath"));
            staticContent.setFileType(StaticContent.FileType.valueOf(String.valueOf(map.get("fileType"))));

            images.remove(0);
            contentList.add(staticContent);

            if (images.size() > 0) {
                uploadImage(images, contentList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onEvent(Event event) {
        if (event.equals(Event.After_Upload)) {
            scheduleList = helper.getTimeScheduleUploadObjects(locationUploadId);
            conditionScheduleList = helper.getConditionScheduleUploadObjects(locationUploadId);
            conditionUnitList = helper.getConditionUnitUploadObjects(locationUploadId);
            totalUploadList = new ArrayList<>();
            totalUploadList.addAll(scheduleList);
            totalUploadList.addAll(conditionScheduleList);
            totalUploadList.addAll(conditionUnitList);
            if (totalUploadList.size() > 0) {
                uploadCount = totalUploadList.size();

                adapter = new UploadDataAdapter(totalUploadList, getApplicationContext());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            } else {
                uploadCount = 0;
                List<Object> emptyList = new ArrayList<>();
                adapter = new UploadDataAdapter(emptyList, getApplicationContext());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setVisibility(View.GONE);
                allUploaded.setVisibility(View.VISIBLE);
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Upload Finish!", Toast.LENGTH_SHORT).show();
            }
            adapter.notifyDataSetChanged();
            btnUpload.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);
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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), SelectUploadLocationActivity.class);
        startActivity(intent);
        prefs = getSharedPreferences(prefSelectLocation, MODE_PRIVATE);
        SharedPreferences.Editor dataEditor = prefs.edit();
        dataEditor.clear().commit();
        finish();
    }
}

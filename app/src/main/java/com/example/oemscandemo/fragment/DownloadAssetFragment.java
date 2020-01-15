package com.example.oemscandemo.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.oemscandemo.R;
import com.example.oemscandemo.adapter.SelectLocationAdapter;
import com.example.oemscandemo.adapter.SelectedLocationAdapter;
import com.example.oemscandemo.db.DBHelper;
import com.example.oemscandemo.model.AssetBean;
import com.example.oemscandemo.model.AssetMaintenanceCondition;
import com.example.oemscandemo.model.ConditionScheduleMaintenance;
import com.example.oemscandemo.model.ConditionScheduleMaintenanceDetail;
import com.example.oemscandemo.model.DeviceInfo;
import com.example.oemscandemo.model.DownloadInfo;
import com.example.oemscandemo.model.LocationsBean;
import com.example.oemscandemo.model.Maintenance;
import com.example.oemscandemo.model.MaintenanceDetail;
import com.example.oemscandemo.model.Schedule;
import com.example.oemscandemo.model.ScheduleItem;
import com.example.oemscandemo.model.ScheduleList;
import com.example.oemscandemo.model.UnitSchedule;
import com.example.oemscandemo.model.User;
import com.example.oemscandemo.retrofit.ApiService;
import com.example.oemscandemo.retrofit.Constant;
import com.example.oemscandemo.ui.HomeActivity;
import com.example.oemscandemo.ui.LoginActivity;
import com.example.oemscandemo.utils.Event;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class DownloadAssetFragment extends Fragment implements View.OnClickListener {

    private List<LocationsBean> locationsList;
    private List<LocationsBean> selectedLocations = null;
    private List<AssetBean> assetList;
    private ArrayList<String> titleList;
    private ArrayList<String> locateIdList;
    private ArrayList<Integer> locationIdList = new ArrayList<>();
    boolean[] checkedItem;
    List<UnitSchedule> unitScheduleList = new ArrayList<>();


    private DBHelper helper;
    private Button btnChooseLocation, btnDownload, btnDownloadBack;
    private RecyclerView mRecyclerView;
    private SelectedLocationAdapter selectedlocationAdapter;
    private SelectLocationAdapter selectLocationAdapter;
    private EditText editFrom, editTo;
    private String deviceCode, licenseKey, token;
    private int userId;
    private int deviceAppId;

    private SharedPreferences prefs;
    private String prefName = "user";
    private String prefLicense = "licenseKey";
    private String prefDeviceAppId = "deviceAppId";

    public DownloadAssetFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download_asset, container, false);
        helper = new DBHelper(getActivity());
        editFrom = view.findViewById(R.id.fromDate);
        editTo = view.findViewById(R.id.toDate);
        btnChooseLocation = view.findViewById(R.id.btn_choose_location);
        btnDownload = view.findViewById(R.id.btn_download);
        btnDownloadBack = view.findViewById(R.id.btn_download_back);

        locationsList = new ArrayList<>();
        mRecyclerView = view.findViewById(R.id.selected_locationList);
        selectedlocationAdapter = new SelectedLocationAdapter(getContext(), locationsList);
        mRecyclerView.setAdapter(selectedlocationAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        selectedlocationAdapter.notifyDataSetChanged();
        getLocationList();

        editFrom.setOnClickListener(this);
        editTo.setOnClickListener(this);
        btnChooseLocation.setOnClickListener(this);
        btnDownload.setOnClickListener(this);
        btnDownloadBack.setOnClickListener(this);
        EventBus.getDefault().register(this);
        return view;
    }

    private void getLocationList() {
        if (helper.getInfoCount() != 0) {
            int id = 1;
            final DownloadInfo info = helper.getDownloadInfoById(id);

            final ArrayList<LocationsBean> locations = new ArrayList<>();
            ArrayList<String> items = new ArrayList<>(Arrays.asList(info.getLocations().split(",")));

            for (int i = 0; i < items.size(); i++) {

                int locationId = Integer.parseInt(items.get(i));
                if (locationId != 0) {
                    LocationsBean location = helper.getLocationById(locationId);
                    locations.add(location);
                }
            }

            Collections.sort(locations, (location1, location2) -> {
                if (location1.getCode() != null && location2.getCode() != null) {
                    return location1.getCode().compareTo(location2.getCode());
                }
                return 0;
            });

            editFrom.setText(info.getDownloadStartDate());
            editTo.setText(info.getDownloadEndDate());
            selectedlocationAdapter = new SelectedLocationAdapter(getContext(), locations);
            mRecyclerView.setAdapter(selectedlocationAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            selectedlocationAdapter.notifyDataSetChanged();
        }
    }

    private void SelectLocation() {
        titleList = new ArrayList<>();
        locationIdList = new ArrayList<>();
        locationsList = helper.getAllLocation();
        for (LocationsBean locationsBean : locationsList) {
            titleList.add(locationsBean.getName());
        }
        Collections.sort(titleList);
        String[] titles = titleList.toArray(new String[0]);
        checkedItem = new boolean[titles.length];

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_location_title);
        builder.setCancelable(false);
        builder.setMultiChoiceItems(titles, checkedItem, (dialogInterface, i, b) -> {
            if (b) {
                locationIdList.add(i);
            } else if (locationIdList.contains(i)) {
                locationIdList.remove(Integer.valueOf(i));
            }
        });
        builder.setPositiveButton(R.string.btn_done, (dialog, which) -> {
            String item;
            selectedLocations = new ArrayList<>();
            for (int i = 0; i < locationIdList.size(); i++) {
                item = titles[locationIdList.get(i)];
                LocationsBean location = helper.getLocationByName(item);
                selectedLocations.add(location);
            }

            Collections.sort(selectedLocations, (location1, location2) -> {
                if (location1.getCode() != null && location2.getCode() != null) {
                    return location1.getCode().compareTo(location2.getCode());
                }
                return 0;
            });

            selectLocationAdapter = new SelectLocationAdapter(getContext(), selectedLocations);
            mRecyclerView.setAdapter(selectLocationAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            selectLocationAdapter.notifyDataSetChanged();
            EventBus.getDefault().post(Event.Selected_Location);
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_choose_location) {
            SelectLocation();
        } else if (id == R.id.fromDate) {

            final Calendar c = Calendar.getInstance();

            DatePickerDialog dpd = new DatePickerDialog(getContext(),
                    (view1, year, monthOfYear, dayOfMonth) -> {

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String formatedDate = sdf.format(new Date(year - 1900, monthOfYear, dayOfMonth));
                        editFrom.setText(formatedDate);

                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
            dpd.show();

        } else if (id == R.id.toDate) {

            final Calendar c = Calendar.getInstance();

            DatePickerDialog dpd = new DatePickerDialog(getContext(),
                    (view12, year, monthOfYear, dayOfMonth) -> {

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String formatedDate = sdf.format(new Date(year - 1900, monthOfYear, dayOfMonth));
                        editTo.setText(formatedDate);

                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
            dpd.show();

        } else if (id == R.id.btn_download) {
            if (selectedLocations != null) {
                if (selectedLocations.size() != 0) {
                    if (!TextUtils.isEmpty(editFrom.getText().toString()) && !TextUtils.isEmpty(editTo.getText().toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(R.string.title_download_confirm);
                        builder.setMessage(R.string.warning_down_confirm);
                        builder.setCancelable(false);
                        builder.setPositiveButton(R.string.btn_done, (dialog, which) -> saveData());
                        builder.setNegativeButton(R.string.btn_cancel, (dialog, which) -> {
                        });
                        builder.show();
                    } else {
                        Toast.makeText(getContext(), "Please fill start date and end date!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Please select location!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Please select location!", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.btn_download_back) {
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
        }
    }

    private void saveData() {
        btnDownload.setVisibility(View.GONE);
        DeviceInfo deviceInfo = helper.getDeviceInfoById(1);
        deviceCode = deviceInfo.getDeviceCode();
        prefs = Objects.requireNonNull(getActivity()).getSharedPreferences(prefLicense, MODE_PRIVATE);
        licenseKey = prefs.getString("license_key", null);
        prefs = Objects.requireNonNull(getActivity()).getSharedPreferences(prefName, MODE_PRIVATE);
        User user = helper.getUserById(1);
        userId = user.getUserId();
        token = prefs.getString("token", null);
        prefs = getActivity().getSharedPreferences(prefDeviceAppId, MODE_PRIVATE);
        deviceAppId = prefs.getInt("device_app_id", 0);

        final ApiService service = new ApiService();

        helper.exportDB();
        helper.deleteAllAssets();
        helper.deleteInfo();
        helper.deleteAllMaintenance();
        helper.deleteAllMaintenanceDetails();
        helper.deleteAllConditionMaintenance();
        helper.deleteAllConditionMaintenanceDetails();
        helper.deleteUnitScheduleCondition();
        helper.deleteAssetCondition();
        helper.deleteAssetConditionUnit();
        helper.deleteAllSchedule();
        helper.deleteAllScheduleItem();
        helper.deleteAllScheduleList();
        helper.deleteAllDumImage();

        LocationsBean location;
        String locationIds = "";
        locateIdList = new ArrayList<>();
        for (LocationsBean locationsBean : selectedLocations) {

            location = helper.getLocationById(locationsBean.getLocationId());
            location.setDownload(-1);

            helper.updateLocation(location);
            locateIdList.add(String.valueOf(locationsBean.getLocationId()));
            if (locationIds != "") {
                locationIds = locationIds + ",";
            }
            locationIds = locationIds + locationsBean.getLocationId();

            location.setStatus(1);
            helper.updateLocation(location);
            EventBus.getDefault().post(Event.Selected_Progress);

            Map<String, Object> request = new HashMap<>();
            request.put("appType", Constant.APP_TYPE);
            request.put("deviceCode", deviceCode);
            request.put("licenseKey", licenseKey);
            request.put("userId", userId);
            request.put("token", token);
            request.put("deviceAppId", deviceAppId);
            request.put("locationIds", String.valueOf(locationsBean.getLocationId()));

            final LocationsBean finalLocation = location;
            service.assetSearch(request, new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                    if (response.isSuccessful()) {
                        if (response.code() == 200) {
                            ArrayList<Object> assetsBeanArrayList;
                            Map<String, Object> result = response.body();
                            assetsBeanArrayList = (ArrayList<Object>) result.get("assetList");
                            String status = (String) result.get("status");
                            double downloadId = (double) result.get("downloadId");
                            if (status.equals("SUCCESS")) {
                                if (assetsBeanArrayList != null) {
                                    for (Object object : assetsBeanArrayList) {
                                        Map<String, Object> s = (Map<String, Object>) object;
                                        double id = (double) s.get("assetId");
                                        double locationId = (double) s.get("locationId");
                                        if (!helper.checkAssetExists((int) id)) {
                                            AssetBean assetBean = new AssetBean();
                                            assetBean.setId((int) id);
                                            assetBean.setLocationId((int) locationId);
                                            assetBean.setCostCenter((String) s.get("costCenter"));
                                            assetBean.setActualLocation((String) s.get("actualLocation"));
                                            assetBean.setFaNumber((String) s.get("assetNo"));
                                            assetBean.setItemName((String) s.get("itemName"));
                                            assetBean.setCondition((String) s.get("condition"));
                                            assetBean.setCategory((String) s.get("category"));
                                            assetBean.setBrand((String) s.get("brand"));
                                            assetBean.setModel((String) s.get("model"));
                                            assetBean.setSerialNo((String) s.get("serialNo"));
                                            helper.addFA(assetBean);
                                        }
                                    }

                                    String assetIds;
                                    String assetIdLists = "";
                                    assetList = helper.getAssetByLocation(finalLocation.getLocationId());
                                    if (assetList.size() != 0) {
                                        for (AssetBean fixedAsset : assetList) {
                                            helper.addFA(fixedAsset);
                                            assetIds = String.valueOf(fixedAsset.getId());

                                            if (assetIdLists != "") {
                                                assetIdLists = assetIds + ",";
                                            }
                                            assetIdLists = assetIdLists + assetIds;

                                            String fromDate = editFrom.getText().toString();
                                            String toDate = editTo.getText().toString();
                                            Map<String, Object> request1 = new HashMap<>();
                                            request1.put("appType", Constant.APP_TYPE);
                                            request1.put("deviceCode", deviceCode);
                                            request1.put("licenseKey", licenseKey);
                                            request1.put("userId", userId);
                                            request1.put("token", token);
                                            request1.put("deviceAppId", deviceAppId);
                                            request1.put("assetIds", assetIds);
                                            request1.put("fromDate", fromDate);
                                            request1.put("toDate", toDate);

                                            service.timeSchedule(request1, new Callback<Map<String, Object>>() {
                                                @Override
                                                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                                                    if (response.isSuccessful()) {
                                                        if (response.code() == 200) {
                                                            ArrayList<Object> scheduleArrayList;
                                                            ArrayList<Object> scheduleItemArrayList;
                                                            ArrayList<Object> scheduleListArrayList;
                                                            Map<String, Object> result1 = response.body();
                                                            scheduleArrayList = (ArrayList<Object>) result1.get("scheduleList");
                                                            double downloadId = (double) result1.get("downloadId");
                                                            String status = (String) result1.get("status");
                                                            if (status.equals("SUCCESS")) {
                                                                if (scheduleArrayList != null) {

                                                                    for (Object object : scheduleArrayList) {
                                                                        Map<String, Object> s = (Map<String, Object>) object;
                                                                        scheduleItemArrayList = (ArrayList<Object>) s.get("itemList");
                                                                        double id = (double) s.get("id");
                                                                        Schedule schedule = new Schedule();
                                                                        schedule.setId((int) id);
                                                                        schedule.setName((String) s.get("name"));
                                                                        schedule.setScheduleCode((String) s.get("scheduleCode"));
                                                                        schedule.setScheduleType((String) s.get("scheduleType"));
                                                                        schedule.setStartDateAsString((String) s.get("startDateAsString"));
                                                                        schedule.setEndDateAsString((String) s.get("endDateAsString"));
                                                                        schedule.setTimeAsString((String) s.get("timeAsString"));
                                                                        schedule.setScheduleStatus((String) s.get("maintenanceStatus"));
                                                                        if (!helper.checkScheduleExists(schedule.getId())) {
                                                                            helper.insertSchedule(schedule);
                                                                        }
                                                                        if (scheduleItemArrayList != null) {
                                                                            for (Object itemObject : scheduleItemArrayList) {
                                                                                Map<String, Object> mapItem = (Map<String, Object>) itemObject;
                                                                                scheduleListArrayList = (ArrayList<Object>) mapItem.get("scheduleList");
                                                                                double itemId = (double) mapItem.get("id");
                                                                                double scheduleId = (double) mapItem.get("scheduleId");
                                                                                double assetId = (double) mapItem.get("assetId");
                                                                                ScheduleItem scheduleItem = new ScheduleItem();
                                                                                scheduleItem.setId((int) itemId);
                                                                                scheduleItem.setScheduleId((int) scheduleId);
                                                                                scheduleItem.setAssetId((int) assetId);
                                                                                scheduleItem.setStartDateAsString((String) mapItem.get("startDateAsString"));
                                                                                scheduleItem.setEndDateAsString((String) mapItem.get("endDateAsString"));
                                                                                scheduleItem.setScheduleStatus((String) mapItem.get("maintenanceStatus"));

                                                                                helper.insertScheduleItem(scheduleItem);

                                                                                if (scheduleListArrayList != null) {
                                                                                    for (Object scheduleListObject : scheduleListArrayList) {
                                                                                        Map<String, Object> mapScheduleList = (Map<String, Object>) scheduleListObject;
                                                                                        double scheduleListId = (double) mapScheduleList.get("id");
                                                                                        double scheduleItemId = (double) mapScheduleList.get("scheduleItemId");
                                                                                        ScheduleList scheduleList = new ScheduleList();
                                                                                        scheduleList.setId((int) scheduleListId);
                                                                                        scheduleList.setScheduleItemId((int) scheduleItemId);
                                                                                        scheduleList.setScheduleDateAsString((String) mapScheduleList.get("scheduleDateAsString"));
                                                                                        scheduleList.setStatus((String) mapScheduleList.get("status"));

                                                                                        helper.insertScheduleList(scheduleList);
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                            finalLocation.setDownload((int) downloadId);
                                                            finalLocation.setStatus(2);
                                                            helper.updateLocation(finalLocation);
                                                            EventBus.getDefault().post(Event.Select_Done);
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                                                    Toast.makeText(getContext(), "Unknown Error occur in downloading process.\nCheck your download server!!!", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                            Map<String, Object> request2 = new HashMap<>();
                                            request2.put("appType", Constant.APP_TYPE);
                                            request2.put("deviceCode", deviceCode);
                                            request2.put("licenseKey", licenseKey);
                                            request2.put("userId", userId);
                                            request2.put("token", token);
                                            request2.put("deviceAppId", deviceAppId);
                                            request2.put("assetIds", assetIds);
                                            service.conditionSchedule(request2, new Callback<Map<String, Object>>() {
                                                @Override
                                                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                                                    if (response.isSuccessful()) {
                                                        if (response.code() == 200) {
                                                            ArrayList<Object> ScheduleArrayList;
                                                            Map<String, Object> result2 = response.body();
                                                            ScheduleArrayList = (ArrayList<Object>) result2.get("scheduleList");
                                                            double downloadId = (double) result2.get("downloadId");
                                                            String status = (String) result2.get("status");
                                                            if (status.equals("SUCCESS")) {
                                                                if (ScheduleArrayList != null) {
                                                                    for (Object object : ScheduleArrayList) {
                                                                        Map<String, Object> s = (Map<String, Object>) object;
                                                                        double assetId = (double) s.get("assetId");
                                                                        double scheduleId = (double) s.get("scheduleId");
                                                                        double scheduleUnit = (double) s.get("scheduleUnit");
                                                                        UnitSchedule unitSchedule = new UnitSchedule();
                                                                        unitSchedule.setScheduleName((String) s.get("scheduleName"));
                                                                        unitSchedule.setUnitType((String) s.get("unitType"));
                                                                        unitSchedule.setAssetId((int) assetId);
                                                                        unitSchedule.setScheduleCode((String) s.get("scheduleCode"));
                                                                        unitSchedule.setConditionName((String) s.get("conditionName"));
                                                                        unitSchedule.setScheduleId(scheduleId);
                                                                        unitSchedule.setScheduleUnit(scheduleUnit);
                                                                        unitScheduleList.add(unitSchedule);
                                                                        helper.insertUnitScheduleCondition(unitSchedule);
                                                                    }
                                                                }
                                                            }
                                                            finalLocation.setDownload((int) downloadId);
                                                            finalLocation.setStatus(2);
                                                            helper.updateLocation(finalLocation);
                                                            EventBus.getDefault().post(Event.Select_Done);
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                                                    Toast.makeText(getContext(), "Unknown Error occur in downloading process.\nCheck your download server!!!", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                            Map<String, Object> request3 = new HashMap<>();
                                            request3.put("appType", Constant.APP_TYPE);
                                            request3.put("deviceCode", deviceCode);
                                            request3.put("licenseKey", licenseKey);
                                            request3.put("userId", userId);
                                            request3.put("token", token);
                                            request3.put("assetIds", assetIds);
                                            service.conditionDownload(request3, new Callback<Map<String, Object>>() {
                                                @Override
                                                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                                                    if (response.isSuccessful()) {
                                                        if (response.code() == 200) {
                                                            ArrayList<Object> conditionArrayList;
                                                            Map<String, Object> result3 = response.body();
                                                            String status = (String) result3.get("status");
                                                            conditionArrayList = (ArrayList<Object>) result3.get("conditionList");
                                                            if (status.equals("SUCCESS")) {
                                                                if (conditionArrayList != null) {
                                                                    for (Object object : conditionArrayList) {
                                                                        Map<String, Object> s = (Map<String, Object>) object;
                                                                        double id = (double) s.get("id");
                                                                        double assetId = (double) s.get("assetId");
                                                                        AssetMaintenanceCondition assetCondition = new AssetMaintenanceCondition();
                                                                        assetCondition.setId((int) id);
                                                                        assetCondition.setRegDate((String) s.get("regDate"));
                                                                        assetCondition.setUpdDate((String) s.get("updDate"));
                                                                        assetCondition.setAssetId((int) assetId);
                                                                        assetCondition.setConditionName((String) s.get("conditionName"));
                                                                        assetCondition.setUnitType((String) s.get("unitType"));
                                                                        assetCondition.setTotalUnit((double) s.get("totalUnit"));
                                                                        if (s.get("updateUnit") != null) {
                                                                            assetCondition.setUpdateUnit((double) s.get("updateUnit"));
                                                                        }
                                                                        helper.insertAssetCondition(assetCondition);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                                                    Toast.makeText(getContext(), "Unknown Error occur in downloading process.\nCheck your download server!!!", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                            Map<String, Object> request4 = new HashMap<>();
                                            request4.put("appType", Constant.APP_TYPE);
                                            request4.put("deviceCode", deviceCode);
                                            request4.put("licenseKey", licenseKey);
                                            request4.put("userId", userId);
                                            request4.put("token", token);
                                            request4.put("assetIds", assetIds);

                                            service.timeMaintenanceDownload(request4, new Callback<Map<String, Object>>() {
                                                @Override
                                                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                                                    if (response.isSuccessful()) {
                                                        if (response.code() == 200) {
                                                            ArrayList<Object> MaintenanceTimeArrayList, detailList;
                                                            Map<String, Object> result4 = response.body();
                                                            MaintenanceTimeArrayList = (ArrayList<Object>) result4.get("maintenanceList");

                                                            if (MaintenanceTimeArrayList != null) {
                                                                for (Object object : MaintenanceTimeArrayList) {
                                                                    Map<String, Object> s = (Map<String, Object>) object;
                                                                    detailList = (ArrayList<Object>) s.get("detailList");
                                                                    double id = (double) s.get("id");
                                                                    double scheduleListId = (double) s.get("scheduleListId");
                                                                    double assetId = (double) s.get("assetId");
                                                                    double maintenanceBy = (double) s.get("maintenanceBy");
                                                                    Maintenance maintenance = new Maintenance();
                                                                    maintenance.setMaintenanceId((int) id);
                                                                    maintenance.setId((int) id);
                                                                    maintenance.setScheduleListId((int) scheduleListId);
                                                                    maintenance.setAssetId((int) assetId);
                                                                    maintenance.setStartDateAsString((String) s.get("startDateAsString"));
                                                                    maintenance.setEndDateAsString((String) s.get("endDateAsString"));
                                                                    maintenance.setStatus((String) s.get("status"));
                                                                    maintenance.setMaintenanceBy((int) maintenanceBy);
                                                                    helper.addMaintenance(maintenance);
                                                                    if (detailList != null) {
                                                                        for (Object o : detailList) {
                                                                            Map<String, Object> so = (Map<String, Object>) o;
                                                                            double detailId = (double) so.get("id");
                                                                            double maintenanceId = (double) so.get("maintenanceId");
                                                                            double uploadId = (double) so.get("uploadId");
                                                                            MaintenanceDetail maintenanceDetail = new MaintenanceDetail();
                                                                            maintenanceDetail.setId((int) detailId);
                                                                            maintenanceDetail.setMaintenanceId((int) maintenanceId);
                                                                            maintenanceDetail.setUploadId((int) uploadId);
                                                                            maintenanceDetail.setStatus((String) so.get("status"));
                                                                            maintenanceDetail.setUpdatedDateAsString((String) so.get("updatedDateAsString"));
                                                                            maintenanceDetail.setRemark((String) so.get("remark"));

                                                                            helper.insertMaintenanceDetail(maintenanceDetail);

                                                                        }
                                                                    }

                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                                                    Toast.makeText(getContext(), "Unknown Error occur in downloading process.\nCheck your download server!!!", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                            Map<String, Object> request5 = new HashMap<>();
                                            request5.put("appType", Constant.APP_TYPE);
                                            request5.put("deviceCode", deviceCode);
                                            request5.put("licenseKey", licenseKey);
                                            request5.put("userId", userId);
                                            request5.put("token", token);
                                            request5.put("assetIds", assetIds);

                                            service.conditionMaintenanceDownload(request5, new Callback<Map<String, Object>>() {
                                                @Override
                                                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                                                    if (response.isSuccessful()) {
                                                        if (response.code() == 200) {
                                                            ArrayList<Object> MaintenanceConditionArrayList, detailList;
                                                            Map<String, Object> result5 = response.body();
                                                            MaintenanceConditionArrayList = (ArrayList<Object>) result5.get("maintenanceList");

                                                            if (MaintenanceConditionArrayList != null) {
                                                                for (Object object : MaintenanceConditionArrayList) {
                                                                    Map<String, Object> s = (Map<String, Object>) object;
                                                                    detailList = (ArrayList<Object>) s.get("detailList");
                                                                    double id = (double) s.get("id");
                                                                    double scheduleId = (double) s.get("scheduleId");
                                                                    double deviceUploadId = (double) s.get("device_upload_id");
                                                                    double scheduleUnit = (double) s.get("scheduleUnit");
                                                                    double currentUnit = (double) s.get("currentUnit");
                                                                    double assetId = (double) s.get("assetId");
                                                                    ConditionScheduleMaintenance conditionMaintenance = new ConditionScheduleMaintenance();
                                                                    conditionMaintenance.setId((int) id);
                                                                    UnitSchedule unitSchedule = new UnitSchedule();
                                                                    unitSchedule.setScheduleName((String) s.get("scheduleName"));
                                                                    unitSchedule.setUnitType((String) s.get("unitType"));
                                                                    unitSchedule.setAssetId((int) assetId);
                                                                    unitSchedule.setScheduleCode((String) s.get("scheduleCode"));
                                                                    unitSchedule.setConditionName((String) s.get("conditionName"));
                                                                    unitSchedule.setScheduleId(scheduleId);
                                                                    unitSchedule.setScheduleUnit(scheduleUnit);

                                                                    helper.insertUnitScheduleCondition(unitSchedule);
                                                                    List<String> lastScheduleId = helper.getUnitScheduleId();
                                                                    String lastId = lastScheduleId.get(0);
                                                                    conditionMaintenance.setInsertScheduleId(Integer.parseInt(lastId));
                                                                    conditionMaintenance.setMaintenanceId((int) id);
                                                                    conditionMaintenance.setScheduleId((int) scheduleId);
                                                                    conditionMaintenance.setDeviceUploadId((int) deviceUploadId);
                                                                    conditionMaintenance.setScheduleUnit(scheduleUnit);
                                                                    conditionMaintenance.setCurrentUnit(currentUnit);
                                                                    conditionMaintenance.setStartDateAsString((String) s.get("startDateAsString"));
                                                                    conditionMaintenance.setEndDateAsString((String) s.get("endDateAsString"));
                                                                    conditionMaintenance.setStatus((String) s.get("status"));
                                                                    conditionMaintenance.setAssetId((int) assetId);

                                                                    helper.addConditionMaintenance(conditionMaintenance);

                                                                    if (detailList != null) {
                                                                        for (Object o : detailList) {
                                                                            Map<String, Object> so = (Map<String, Object>) o;
                                                                            double conMainDetailId = (double) so.get("id");
                                                                            double detailDeviceUploadId = (double) so.get("deviceUploadId");
                                                                            double detailMaintenanceId = (double) so.get("maintenanceId");
                                                                            double detailUpdatedBy = (double) so.get("updatedBy");

                                                                            ConditionScheduleMaintenanceDetail conditionMaintenanceDetail = new ConditionScheduleMaintenanceDetail();
                                                                            conditionMaintenanceDetail.setId((int) conMainDetailId);
                                                                            conditionMaintenanceDetail.setDeviceUploadId((int) detailDeviceUploadId);
                                                                            conditionMaintenanceDetail.setMaintenanceId((int) detailMaintenanceId);
                                                                            conditionMaintenanceDetail.setUpdatedBy((int) detailUpdatedBy);
                                                                            conditionMaintenanceDetail.setStatus((String) so.get("status"));
                                                                            conditionMaintenanceDetail.setUpdatedDateAsString((String) so.get("updatedDateAsString"));
                                                                            conditionMaintenanceDetail.setRemark((String) so.get("remark"));

                                                                            helper.insertConditionMaintenanceDetail(conditionMaintenanceDetail);

                                                                        }
                                                                    }

                                                                }
                                                            }
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                                                    Toast.makeText(getContext(), "Unknown Error occur in downloading process.\nCheck your download server!!!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    } else {

                                    }
                                    if (finalLocation.getStatus() != 2) {
                                        finalLocation.setDownload((int) downloadId);
                                        finalLocation.setStatus(2);
                                        helper.updateLocation(finalLocation);
                                    }
                                    EventBus.getDefault().post(Event.Select_Done);
                                }
                                Toast.makeText(getContext(), "Download Finish!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            finalLocation.setDownload(0);
                            finalLocation.setStatus(4);
                            helper.updateLocation(finalLocation);
                            EventBus.getDefault().post(Event.Select_Empty);
                        }
                    } else if (response.code() == 408) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle(R.string.dialog_session_expired);
                        builder.setCancelable(false);
                        builder.setPositiveButton("Ok", (dialog, which) -> {
                            finalLocation.setDownload(0);
                            finalLocation.setStatus(4);
                            helper.updateLocation(finalLocation);
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            getActivity().startActivity(intent);
                            getActivity().finish();
                        });
                        builder.show();
                    } else {
                        finalLocation.setDownload(0);
                        finalLocation.setStatus(4);
                        helper.updateLocation(finalLocation);
                        EventBus.getDefault().post(Event.Select_Empty);
                    }
                }

                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Toast.makeText(getContext(), "Download Fail!", Toast.LENGTH_SHORT).show();
                    finalLocation.setDownload(0);
                    finalLocation.setStatus(4);
                    helper.updateLocation(finalLocation);
                    EventBus.getDefault().post(Event.Select_Empty);


                }
            });
        }
        btnDownloadBack.setVisibility(View.VISIBLE);

        Calendar c = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
        String downloadDate = dateFormat.format(c.getTime());
        String fromDate = editFrom.getText().toString();
        String toDate = editTo.getText().toString();

        DownloadInfo info = new DownloadInfo();
        info.setDownloadUserId(user.getId());
        info.setDownloadDate(downloadDate);
        info.setLocations(locationIds);
        info.setDownloadStartDate(fromDate);
        info.setDownloadEndDate(toDate);
        helper.addInfo(info);
        helper.close();
        getLocationList();
    }

    @Subscribe
    public void onEvent(Event event) {

        if (event.equals(Event.Selected_Progress) || event.equals(Event.Select_Done) || event.equals(Event.Select_Empty)) {
            getLocationList();
        } else if (event.equals(Event.Selected_Location)) {
            selectLocationAdapter = new SelectLocationAdapter(getContext(), selectedLocations);
            mRecyclerView.setAdapter(selectLocationAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            selectLocationAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}

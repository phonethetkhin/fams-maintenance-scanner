package com.example.oemscandemo.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oemscandemo.R;
import com.example.oemscandemo.adapter.LocationSelectAdapter;
import com.example.oemscandemo.db.DBHelper;
import com.example.oemscandemo.model.DownloadInfo;
import com.example.oemscandemo.model.LocationsBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SelectLocationActivity extends AppCompatActivity {

    private DBHelper helper;
    private Toolbar toolbar;
    private List<LocationsBean> locationsList;
    private RecyclerView recyclerLocation;
    private TextView titleLocation;
    private Button btnMaintenance;
    private LocationSelectAdapter maintenanceLocationAdapter;

    private int locationId;
    private SharedPreferences prefs;
    private String prefName = "user";
    private String prefSelectLocation = "select_location";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        helper = new DBHelper(this);
        toolbar = findViewById(R.id.toolbar);
        recyclerLocation = findViewById(R.id.recycler_select_location);
        titleLocation = findViewById(R.id.txt_location_title);
        btnMaintenance = findViewById(R.id.btn_go_maintenance);

        setupToolbar();

        titleLocation.setText(R.string.downloadLocation);
        locationsList = new ArrayList<>();
        maintenanceLocationAdapter = new LocationSelectAdapter(locationsList, getApplicationContext());
        recyclerLocation.setAdapter(maintenanceLocationAdapter);
        recyclerLocation.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        getLocationList();
    }

    private void getLocationList() {
        if (helper.getInfoCount() != 0) {
            int id = 1;
            final DownloadInfo info = helper.getDownloadInfoById(id);

            final ArrayList<LocationsBean> locations = new ArrayList<>();
            ArrayList<String> items = new ArrayList<>(Arrays.asList(info.getLocations().split(",")));

            for (int i = 0; i < items.size(); i++) {
                int locationId = Integer.parseInt(items.get(i));
                if (helper.getAssetCountByLocation(locationId) != 0) {
                    LocationsBean location = helper.getLocationById(locationId);
                    locations.add(location);
                }
            }

            locationsList.clear();
            locationsList.addAll(locations);
            maintenanceLocationAdapter.notifyDataSetChanged();

            Collections.sort(locations, (location11, location2) -> {
                if (location11.getCode() != null && location2.getCode() != null) {
                    return location11.getCode().compareTo(location2.getCode());
                }
                return 0;
            });
        }
    }

    public void GoForMaintenance(View view) {
        prefs = getSharedPreferences(prefSelectLocation, MODE_PRIVATE);
        locationId = prefs.getInt("select_locationId", 0);
        boolean cancel = false;
        if (locationId <= 0) {
            cancel = true;
        }
        if (cancel) {
            Toast.makeText(getApplicationContext(), "please select location!!!", Toast.LENGTH_SHORT).show();
        } else {
            goToMaintenance();
        }
    }

    private void goToMaintenance() {
        Intent intent = new Intent(SelectLocationActivity.this, AssetListActivity.class);
        prefs = getSharedPreferences(prefName, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("locationId", locationId);
        editor.apply();
        startActivity(intent);
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
        Intent intent = new Intent(SelectLocationActivity.this, HomeActivity.class);
        startActivity(intent);
        prefs = getSharedPreferences(prefSelectLocation, MODE_PRIVATE);
        SharedPreferences.Editor dataEditor = prefs.edit();
        dataEditor.clear().commit();
        finish();
    }
}

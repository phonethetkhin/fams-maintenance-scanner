package com.example.oemscandemo.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oemscandemo.R;
import com.example.oemscandemo.db.DBHelper;
import com.example.oemscandemo.fragment.DownloadAssetFragment;
import com.example.oemscandemo.fragment.HomeFragment;
import com.example.oemscandemo.fragment.LocationListFragment;
import com.example.oemscandemo.model.DeviceInfo;
import com.example.oemscandemo.model.DownloadInfo;
import com.example.oemscandemo.model.LocationsBean;
import com.example.oemscandemo.model.User;
import com.example.oemscandemo.retrofit.ApiService;
import com.example.oemscandemo.retrofit.Constant;
import com.example.oemscandemo.utils.Event;
import com.example.oemscandemo.utils.HomeListener;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, HomeListener {

    private DBHelper helper;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Stage mStage = Stage.HOME;

    private SharedPreferences prefs;
    private String prefName = "user";
    private String prefLicense = "licenseKey";
    TextView loginUser, lastSync;

    enum Stage {
        HOME,
        DOWNLOAD,
        LOCATION
    }

    private String deviceCode, licenseKey, token;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        helper = new DBHelper(this);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        Menu unitTakeMenu = navigationView.getMenu();
        prefs = getSharedPreferences(prefName, MODE_PRIVATE);
        int unitTakeStatus = prefs.getInt("unitTakeStatus", 0);
        if (unitTakeStatus != 1) {
            unitTakeMenu.findItem(R.id.nav_unit_taking).setVisible(false);
        } else {
            unitTakeMenu.findItem(R.id.nav_unit_taking).setVisible(true);
        }
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        loginUser = hView.findViewById(R.id.txt_login_user);
        lastSync = hView.findViewById(R.id.txt_last_sync);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        mStage = Stage.HOME;

        int download = getIntent().getIntExtra("download", 0);
        int userStatus = getIntent().getIntExtra("userStatus", 0);
        if (userStatus == 1) {
            helper.deleteAllLocations();
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
        }
        if (download == 1) {
            OnLocationSearch();
        }
        getWindow().setWindowAnimations(0);
        prefs = getSharedPreferences(prefName, MODE_PRIVATE);

        User user = helper.getUserById(1);
        loginUser.setText(user.getName());
        int id = 1;
        if (helper.getInfoCount() != 0) {
            final DownloadInfo info = helper.getDownloadInfoById(id);
            String downloadDate = convertStringDateToAnotherStringDate(info.getDownloadDate(), "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd HH:mm:ss");
            lastSync.setVisibility(View.VISIBLE);
            lastSync.setText(downloadDate);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.nav_home:
                onHome();
                break;
            case R.id.nav_download:
                if (helper.getLocationCount() != 0) {
                    onDownload();
                } else {
                    Toast.makeText(getApplicationContext(), "Locations are not exists!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_maintenance:
                Intent intentMaintenance = new Intent(HomeActivity.this, SelectLocationActivity.class);
                intentMaintenance.putExtra("toSelect", -1);
                startActivity(intentMaintenance);
                finish();
                break;
            case R.id.nav_unit_taking:
                if (helper.getLocationCount() != 0) {
                    startActivity(new Intent(getApplicationContext(), UnitTakingActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Locations are not exists!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_upload:
                if (helper.getUploadMaintenanceCount() > 0 || helper.getUploadConditionMaintenanceCount() > 0 ||
                        helper.getUploadAssetConditionCount() > 0) {
                    Intent intentUpload = new Intent(HomeActivity.this, SelectUploadLocationActivity.class);
                    startActivity(intentUpload);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "There is no maintenance to upload!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_location:
                onLocation();
                break;
            case R.id.nav_uploaded:
                Intent intentHistory = new Intent(HomeActivity.this, UploadedDataActivity.class);
                startActivity(intentHistory);
                break;
            case R.id.nav_exit:
                this.finishAffinity();
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
            default:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void OnLocationSearch() {
        DeviceInfo deviceInfo = helper.getDeviceInfoById(1);
        deviceCode = deviceInfo.getDeviceCode();
        prefs = getSharedPreferences(prefLicense, MODE_PRIVATE);
        licenseKey = prefs.getString("license_key", null);
        prefs = getSharedPreferences(prefName, MODE_PRIVATE);
        User user = helper.getUserById(1);
        userId = user.getUserId();
        prefs = getSharedPreferences(prefName, MODE_PRIVATE);
        token = prefs.getString("token", null);

        Map<String, Object> request = new HashMap<>();
        request.put("appType", Constant.APP_TYPE);
        request.put("deviceCode", deviceCode);
        request.put("licenseKey", licenseKey);
        request.put("userId", userId);
        request.put("token", token);
        ApiService apiService = new ApiService();
        apiService.locationSearch(request, new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Object> locationsBeanArrayList;
                    Map<String, Object> result = response.body();
                    locationsBeanArrayList = (ArrayList<Object>) result.get("locationList");
                    if (locationsBeanArrayList != null) {
                        for (Object object : locationsBeanArrayList) {
                            Map<String, Object> s = (Map<String, Object>) object;
                            double id = (double) s.get("id");
                            if (helper.checkLocationExists((int) id)) {
                                LocationsBean updateLocation = helper.getLocationById((int) id);
                                updateLocation.setLocationId((int) id);
                                updateLocation.setCode((String) s.get("code"));
                                updateLocation.setName((String) s.get("name"));
                                helper.updateLocation(updateLocation);
                            } else {
                                LocationsBean newLocation = new LocationsBean();
                                newLocation.setLocationId((int) id);
                                newLocation.setCode((String) s.get("code"));
                                newLocation.setName((String) s.get("name"));
                                helper.addLocation(newLocation);
                            }
                        }
                        EventBus.getDefault().post(Event.Location);
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onHome() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        transaction.replace(R.id.fragment_container, homeFragment);
        transaction.addToBackStack(null);
        getSupportActionBar().setTitle("Home");
        mStage = Stage.HOME;
        transaction.commit();
    }

    @Override
    public void onDownload() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        DownloadAssetFragment downloadFragment = new DownloadAssetFragment();
        transaction.replace(R.id.fragment_container, downloadFragment);
        transaction.addToBackStack(null);
        getSupportActionBar().setTitle("Download");
        mStage = Stage.DOWNLOAD;
        transaction.commit();
    }

    @Override
    public void onLocation() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        LocationListFragment locationFragment = new LocationListFragment();
        transaction.replace(R.id.fragment_container, locationFragment);
        transaction.addToBackStack(null);
        getSupportActionBar().setTitle("Location List");
        mStage = Stage.LOCATION;
        transaction.commit();
    }

    private String convertStringDateToAnotherStringDate(String stringDate, String stringDateFormat, String returnDateFormat) {

        try {
            Date date = new SimpleDateFormat(stringDateFormat).parse(stringDate);
            String returnDate = new SimpleDateFormat(returnDateFormat).format(date);
            return returnDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void onBackPressed() {
        switch (mStage) {
            case HOME:
                ActivityCompat.finishAffinity(this);
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case DOWNLOAD:
                drawer.closeDrawer(GravityCompat.START);
                onHome();
                break;
            case LOCATION:
                drawer.closeDrawer(GravityCompat.START);
                onHome();
            default:
                drawer.closeDrawer(GravityCompat.START);
                onHome();
                break;
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}

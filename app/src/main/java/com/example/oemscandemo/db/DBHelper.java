package com.example.oemscandemo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

import com.example.oemscandemo.model.AssetBean;
import com.example.oemscandemo.model.AssetMaintenanceCondition;
import com.example.oemscandemo.model.AssetMaintenanceConditionUnitBean;
import com.example.oemscandemo.model.ConditionScheduleMaintenance;
import com.example.oemscandemo.model.ConditionScheduleMaintenanceDetail;
import com.example.oemscandemo.model.DeviceInfo;
import com.example.oemscandemo.model.DownloadInfo;
import com.example.oemscandemo.model.DumImage;
import com.example.oemscandemo.model.LocationsBean;
import com.example.oemscandemo.model.Maintenance;
import com.example.oemscandemo.model.MaintenanceDetail;
import com.example.oemscandemo.model.Schedule;
import com.example.oemscandemo.model.ScheduleItem;
import com.example.oemscandemo.model.ScheduleList;
import com.example.oemscandemo.model.ServerSetting;
import com.example.oemscandemo.model.UnitSchedule;
import com.example.oemscandemo.model.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.example.oemscandemo.utils.MyApplication.getContext;


public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper mInstance = null;
    private static final String DB_NAME = "maintenance.db";
    private static final int DB_VERSION = 4;

    private static final String FILE_DIR = "Maintenance";
    private static final String DB_PATH = Environment.getExternalStorageDirectory() + File.separator + FILE_DIR + File.separator;

    //for server port
    private static final String TABLE_SERVER_SETTING = "tbl_setting";
    private static final String SERVER_ID = "id";
    private static final String SETTING_GROUP = "setting_group";
    private static final String SETTING_NAME = "setting_name";
    private static final String SETTING_VALUE = "setting_value";

    //for user
    private static final String TABLE_ACCOUNT = "tbl_account";
    private static final String KEY_ACCOUNT_ID = "id";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_LOGIN_ID = "loginId";
    private static final String KEY_USER_NAME = "name";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_JOIN_DATE = "joined_date";
    private static final String KEY_LAST_LOGIN_DATE = "last_login_date";

    //for device Info
    private static final String TABLE_DEVICE_INFO = "tbl_device_info";
    private static final String DEV_ID = "id";
    private static final String DEV_CODE = "device_code";
    private static final String DEV_NAME = "device_name";
    private static final String DEV_BRAND = "brand";
    private static final String DEV_MODEL = "model";
    private static final String DEV_OS_VERSION = "os_Version";
    private static final String DEV_DEVICE = "device";
    private static final String DEV_MAC_WIFI = "mac_wifi";
    private static final String DEV_ID_ONE = "device_id_one";
    private static final String DEV_ID_TWO = "device_id_two";
    private static final String DEV_SERIAL_NO = "serial_no";
    private static final String DEV_ANDROID_ID = "android_id";
    private static final String DEV_FINGERPRINT = "fingerPrint";
    private static final String DEV_IMEI = "imei";

    //for locations
    private static final String TABLE_LOCATION = "tbl_location";
    private static final String KEY_LOCATION_ID = "location_id";
    private static final String KEY_PARENT_NAME = "parent_name";
    private static final String KEY_LOCATION_CODE = "code";
    private static final String KEY_LOCATION_NAME = "name";
    private static final String KEY_LOCATION_DOWNLOAD = "download";
    private static final String KEY_LOCATION_UPLOAD = "upload";
    private static final String KEY_LOCATION_STATUS = "status";

    //for fixed assets
    private static final String TABLE_FIXED_ASSET = "tbl_asset";
    private static final String KEY_FA_ID = "id";
    private static final String LOCATION_ID = "location_id";
    private static final String COST_CENTER = "cost_center";
    private static final String ACTUAL_LOCATION = "actual_location";
    private static final String KEY_FA_NUMBER = "fa_number";
    private static final String KEY_ITEM_NAME = "item_name";
    private static final String KEY_CONDITION = "condition";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_BRAND = "brand";
    private static final String KEY_MODEL = "model";
    private static final String KEY_SERIAL_NO = "serial_no";

    //for download info
    private static final String TABLE_DOWNLOAD_INFO = "tbl_download_info";
    private static final String KEY_ID = "id";
    private static final String KEY_DOWNLOAD_USER_ID = "download_user_id";
    private static final String KEY_DOWNLOAD_DATE = "download_date";
    private static final String KEY_LOCATIONS = "locations";
    private static final String KEY_START_DATE = "start_date";
    private static final String KEY_END_DATE = "end_date";

    //for schedule
    private static final String TABLE_SCHEDULE = "tbl_schedule";
    private static final String SCHEDULE_ID = "id";
    private static final String SCHEDULE_NAME = "name";
    private static final String SCHEDULE_CODE = "code";
    private static final String SCHEDULE_TYPE = "type";
    private static final String SCHEDULE_START_DATE = "schedule_start_date";
    private static final String SCHEDULE_END_DATE = "schedule_end_date";
    private static final String SCHEDULE_TIME = "time";
    private static final String SCHEDULE_STATUS = "schedule_status";

    //for schedule_item
    private static final String TABLE_SCHEDULE_ITEM = "tbl_schedule_item";
    private static final String ITEM_ID = "id";
    private static final String ITEM_SCHEDULE_ID = "schedule_id";
    private static final String ITEM_ASSET_ID = "asset_id";
    private static final String ITEM_START_DATE = "item_start_date";
    private static final String ITEM_END_DATE = "item_end_date";
    private static final String ITEM_STATUS = "item_status";

    //for schedule_list
    private static final String TABLE_SCHEDULE_LIST = "tbl_schedule_list";
    private static final String SCHEDULE_LIST_ID = "id";
    private static final String SCHEDULE_ITEM_ID = "schedule_item_id";
    private static final String SCHEDULE_DATE = "schedule_date";
    private static final String SCHEDULE_LIST_STATUS = "schedule_list_status";

    //for unit schedule
    private static final String TABLE_SCHEDULE_UNIT = "tbl_schedule_unit";
    private static final String UNIT_CON_ID = "id";
    private static final String UNIT_CON_SCH_NAME = "schedule_name";
    private static final String UNIT_CON_SCH_UNIT_TYPE = "unit_type";
    private static final String UNIT_CON_SCH_ASSET_ID = "asset_id";
    private static final String UNIT_CON_SCH_CODE = "unit_schedule_code";
    private static final String UNIT_CON_SCH_CON_NAME = "condition_name";
    private static final String UNIT_CON_SCH_ID = "schedule_id";
    private static final String UNIT_CON_SCH_UNIT = "schedule_unit";

    //for asset condition
    private static final String TABLE_ASSET_CONDITION = "tbl_asset_condition";
    private static final String ASSET_CON_ID = "id";
    private static final String ASSET_CON_REG_DATE = "reg_date";
    private static final String ASSET_CON_UPD_DATE = "upd_date";
    private static final String ASSET_CON_ASSET_ID = "asset_id";
    private static final String ASSET_CON_NAME = "condition_name";
    private static final String ASSET_CON_UNIT_TYPE = "unit_type";
    private static final String ASSET_CON_TOTAL_UNIT = "total_unit";
    private static final String ASSET_CON_UPDATE_UNIT = "update_unit";

    //for asset condition unit
    private static final String TABLE_ASSET_CONDITION_UNIT = "tbl_asset_condition_unit";
    private static final String ASSET_UNIT_CON_ID = "condition_id";
    private static final String ASSET_UNIT_ASSET_ID = "asset_id";
    private static final String ASSET_UNIT_CON_NAME = "condition_name";
    private static final String ASSET_UNIT_CON_UNIT = "unit";
    private static final String ASSET_UNIT_TYPE = "unit_type";
    private static final String ASSET_UNIT_TAKING_DATE = "taking_date";
    private static final String ASSET_UNIT_TAKING_BY = "taking_by";
    private static final String ASSET_UNIT_FLAG = "flag";
    private static final String ASSET_UNIT_UPLOAD_FLAG = "upload_flag";

    //for time maintenance
    private static final String TABLE_MAINTENANCE = "tbl_maintenance";
    private static final String MAINTENANCE_INSERT_ID = "id";
    private static final String MAINTENANCE_ID = "maintenance_id";
    private static final String MAINTENANCE_SCHEDULE_ID = "schedule_list_id";
    private static final String ASSET_ID = "asset_id";
    private static final String START_DATE = "start_date";
    private static final String END_DATE = "end_date";
    private static final String MAINTENANCE_STATUS = "status";
    private static final String MAINTENANCE_BY = "maintenance_by";
    private static final String MAINTENANCE_FLAG = "flag";
    private static final String UPLOAD_FLAG = "upload_flag";

    //for time maintenance_detail
    private static final String TABLE_MAINTENANCE_DETAIL = "tbl_maintenance_detail";
    private static final String MAINTENANCE_DETAIL_ID = "id";
    private static final String INSERT_MAINTENANCE_ID = "insert_maintenance_id";
    private static final String UPLOAD_ID = "upload_id";
    private static final String MAINTENANCE_STATUS_DETAIL = "status";
    private static final String UPDATED_DATE = "updated_date";
    private static final String REMARK = "remark";

    //for condition maintenance
    private static final String TABLE_CON_MAINTENANCE = "tbl_condition_maintenance";
    private static final String CON_MAIN_ID = "id";
    private static final String CON_INSERT_SCHEDULE_ID = "insert_schedule_id";
    private static final String CON_MAIN_INSERT_ID = "maintenance_id";
    private static final String CON_MAIN_SCHEDULE_ID = "schedule_id";
    private static final String CON_MAIN_DEVICE_UPLOAD_ID = "device_upload_id";
    private static final String CON_MAIN_SCHEDULE_UNIT = "schedule_unit";
    private static final String CON_MAIN_CURRENT_UNIT = "current_unit";
    private static final String CON_MAIN_START_DATE = "start_date";
    private static final String CON_MAIN_END_DATE = "end_date";
    private static final String CON_MAIN_STATUS = "status";
    private static final String CON_MAIN_ASSET_ID = "asset_id";
    private static final String CON_MAIN_FLAG = "flag";
    private static final String CON_MAIN_UPLOAD_FLAG = "upload_flag";

    //for condition maintenance detail
    private static final String TABLE_CON_MAINTENANCE_DETAIL = "tbl_condition_maintenance_detail";
    private static final String CON_MAINTENANCE_DETAIL_ID = "id";
    private static final String CON_DEVICE_UPLOAD_ID = "device_upload_id";
    private static final String CON_INSERT_MAINTENANCE_ID = "maintenance_id";
    private static final String CON_UPDATED_BY = "updated_by";
    private static final String CON_STATUS = "status";
    private static final String CON_UPDATED_DATE = "updated_date";
    private static final String CON_MAINTENANCE_REMARK = "remark";

    //for dum image
    private static final String TABLE_DUM_IMAGE = "tbl_dum_image";
    private static final String DUM_IMAGE_ID = "id";
    private static final String DUM_IMAGE_MAINTENANCE_DETAIL_ID = "maintenance_detail_id";
    private static final String DUM_IMAGE_PATH = "img_path";
    private static final String DUM_IMAGE_FLAG = "flag";

    public static DBHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DBHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    public DBHelper getInstance() {
        return mInstance;
    }

    public DBHelper(Context context) {
        super(context, DB_PATH + DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //query for port
        String CREATE_TABLE_SERVER = "CREATE TABLE " + TABLE_SERVER_SETTING + " (" + SERVER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + SETTING_GROUP + " TEXT," + SETTING_NAME + " TEXT," +
                SETTING_VALUE + " TEXT)";
        db.execSQL(CREATE_TABLE_SERVER);

        //query for user
        String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_ACCOUNT + " (" + KEY_ACCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_USER_ID + " INTEGER," +
                KEY_LOGIN_ID + " TEXT," + KEY_USER_NAME + " TEXT," + KEY_PASSWORD + " TEXT," + KEY_JOIN_DATE + " TEXT," + KEY_LAST_LOGIN_DATE + " TEXT)";
        db.execSQL(CREATE_TABLE_USER);

        //query for deviceInfo
        String CREATE_TABLE_DEVICE = "CREATE TABLE " + TABLE_DEVICE_INFO + " (" + DEV_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + DEV_CODE + " TEXT," + DEV_NAME + " TEXT," + DEV_BRAND + " TEXT," +
                DEV_MODEL + " TEXT," + DEV_OS_VERSION + " TEXT," + DEV_DEVICE + " TEXT," + DEV_MAC_WIFI + " TEXT," + DEV_ID_ONE + " TEXT," +
                DEV_ID_TWO + " TEXT," + DEV_SERIAL_NO + " TEXT," + DEV_ANDROID_ID + " TEXT," + DEV_FINGERPRINT + " TEXT," + DEV_IMEI + " TEXT)";
        db.execSQL(CREATE_TABLE_DEVICE);

        //query for location
        String CREATE_TABLE_LOCATION = "CREATE TABLE " + TABLE_LOCATION + " (" + KEY_LOCATION_ID + " INTEGER PRIMARY KEY," + KEY_PARENT_NAME + " TEXT," +
                KEY_LOCATION_CODE + " TEXT," + KEY_LOCATION_NAME + " TEXT," + KEY_LOCATION_DOWNLOAD + " INTEGER," + KEY_LOCATION_UPLOAD + " INTEGER," + KEY_LOCATION_STATUS + " INTEGER DEFAULT 0)";
        db.execSQL(CREATE_TABLE_LOCATION);

        //query for asset
        String CREATE_TABLE_ASSETS = "CREATE TABLE " + TABLE_FIXED_ASSET + " (" + KEY_FA_ID + " INTEGER PRIMARY KEY," + LOCATION_ID + " INTEGER," + COST_CENTER + " TEXT," + ACTUAL_LOCATION + " TEXT," +
                KEY_FA_NUMBER + " TEXT," + KEY_ITEM_NAME + " TEXT," + KEY_CONDITION + " TEXT," + KEY_CATEGORY + " TEXT," + KEY_BRAND + " TEXT," + KEY_MODEL + " TEXT," + KEY_SERIAL_NO + " TEXT)";
        db.execSQL(CREATE_TABLE_ASSETS);

        //query for download info
        String CREATE_TABLE_DOWNLOAD = "CREATE TABLE " + TABLE_DOWNLOAD_INFO + " (" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DOWNLOAD_USER_ID + " INTEGER," + KEY_DOWNLOAD_DATE + " TEXT," +
                KEY_LOCATIONS + " TEXT," + KEY_START_DATE + " TEXT," + KEY_END_DATE + " TEXT)";
        db.execSQL(CREATE_TABLE_DOWNLOAD);

        //query for schedule
        String CREATE_TABLE_SCHEDULE = "CREATE TABLE " + TABLE_SCHEDULE + " (" + SCHEDULE_ID + " INTEGER PRIMARY KEY," + SCHEDULE_NAME + " TEXT," + SCHEDULE_CODE + " TEXT," +
                SCHEDULE_TYPE + " TEXT," + SCHEDULE_START_DATE + " TEXT," + SCHEDULE_END_DATE + " TEXT," + SCHEDULE_TIME + " TEXT," + SCHEDULE_STATUS + " TEXT)";
        db.execSQL(CREATE_TABLE_SCHEDULE);

        //query for schedule item
        String CREATE_TABLE_SCHEDULE_ITEM = "CREATE TABLE " + TABLE_SCHEDULE_ITEM + " (" + ITEM_ID + " INTEGER PRIMARY KEY," + ITEM_SCHEDULE_ID + " INTEGER," +
                ITEM_ASSET_ID + " INTEGER," + ITEM_START_DATE + " TEXT," + ITEM_END_DATE + " TEXT," + ITEM_STATUS + " TEXT)";
        db.execSQL(CREATE_TABLE_SCHEDULE_ITEM);

        //query for schedule list
        String CREATE_TABLE_SCHEDULE_LIST = "CREATE TABLE " + TABLE_SCHEDULE_LIST + " (" + SCHEDULE_LIST_ID + " INTEGER PRIMARY KEY," + SCHEDULE_ITEM_ID + " INTEGER," + SCHEDULE_DATE + " TEXT," +
                SCHEDULE_LIST_STATUS + " TEXT)";
        db.execSQL(CREATE_TABLE_SCHEDULE_LIST);

        //query for schedule condition
        String CREATE_TABLE_SCHEDULE_CONDITION = "CREATE TABLE " + TABLE_SCHEDULE_UNIT + " (" + UNIT_CON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                UNIT_CON_SCH_NAME + " TEXT," + UNIT_CON_SCH_UNIT_TYPE + " TEXT," + UNIT_CON_SCH_ASSET_ID + " INTEGER," + UNIT_CON_SCH_CODE + " TEXT," +
                UNIT_CON_SCH_CON_NAME + " TEXT," + UNIT_CON_SCH_ID + " DOUBLE," + UNIT_CON_SCH_UNIT + " DOUBLE)";
        db.execSQL(CREATE_TABLE_SCHEDULE_CONDITION);

        //query for asset condition
        String CREATE_TABLE_ASSET_CONDITION = "CREATE TABLE " + TABLE_ASSET_CONDITION + " (" + ASSET_CON_ID + " INTEGER PRIMARY KEY," + ASSET_CON_REG_DATE + " TEXT," + ASSET_CON_UPD_DATE + " TEXT," +
                ASSET_CON_ASSET_ID + " INTEGER," + ASSET_CON_NAME + " TEXT," + ASSET_CON_UNIT_TYPE + " TEXT," + ASSET_CON_TOTAL_UNIT + " DOUBLE," + ASSET_CON_UPDATE_UNIT + " DOUBLE)";
        db.execSQL(CREATE_TABLE_ASSET_CONDITION);

        //query for asset condition unit
        String CREATE_TABLE_ASSET_CONDITION_UNIT = "CREATE TABLE " + TABLE_ASSET_CONDITION_UNIT + " (" + ASSET_UNIT_CON_ID + " INTEGER," + ASSET_UNIT_ASSET_ID + " INTEGER," +
                ASSET_UNIT_CON_NAME + " TEXT," + ASSET_UNIT_CON_UNIT + " DOUBLE," + ASSET_UNIT_TYPE + " TEXT," + ASSET_UNIT_TAKING_DATE + " TEXT," + ASSET_UNIT_TAKING_BY + " INTEGER," +
                ASSET_UNIT_FLAG + " INTEGER," + ASSET_UNIT_UPLOAD_FLAG + " INTEGER)";
        db.execSQL(CREATE_TABLE_ASSET_CONDITION_UNIT);

        //query for time maintenance
        String CREATE_TABLE_MAINTENANCE = "CREATE TABLE " + TABLE_MAINTENANCE + " (" + MAINTENANCE_INSERT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MAINTENANCE_ID + "  INTEGER," + MAINTENANCE_SCHEDULE_ID + " INTEGER," + ASSET_ID + " INTEGER," + START_DATE + " TEXT," + END_DATE +
                " TEXT," + MAINTENANCE_STATUS + " TEXT," + MAINTENANCE_BY + " INTEGER," + MAINTENANCE_FLAG +
                " INTEGER DEFAULT 0," + UPLOAD_FLAG + " INTEGER DEFAULT 0)";
        db.execSQL(CREATE_TABLE_MAINTENANCE);

        //query for time maintenance detail
        String CREATE_TABLE_MAINTENANCE_DETAIL = "CREATE TABLE " + TABLE_MAINTENANCE_DETAIL + " (" + MAINTENANCE_DETAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                INSERT_MAINTENANCE_ID + " INTEGER," + UPLOAD_ID + " INTEGER," + MAINTENANCE_STATUS_DETAIL +
                " TEXT," + UPDATED_DATE + " TEXT," + REMARK + " TEXT)";
        db.execSQL(CREATE_TABLE_MAINTENANCE_DETAIL);

        //query for condition maintenance
        String CREATE_TABLE_CON_MAINTENANCE = "CREATE TABLE " + TABLE_CON_MAINTENANCE + " (" + CON_MAIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + CON_INSERT_SCHEDULE_ID +
                " INTEGER," + CON_MAIN_INSERT_ID + " INTEGER," + CON_MAIN_SCHEDULE_ID + " INTEGER," + CON_MAIN_DEVICE_UPLOAD_ID + " INTEGER," + CON_MAIN_SCHEDULE_UNIT + " DOUBLE," +
                CON_MAIN_CURRENT_UNIT + " DOUBLE," + CON_MAIN_START_DATE + " TEXT," + CON_MAIN_END_DATE + " TEXT," + CON_MAIN_STATUS + " TEXT," + CON_MAIN_ASSET_ID + " INTEGER," +
                CON_MAIN_FLAG + " INTEGER DEFAULT 0," + CON_MAIN_UPLOAD_FLAG + " INTEGER DEFAULT 0)";
        db.execSQL(CREATE_TABLE_CON_MAINTENANCE);

        //query for time maintenance detail
        String CREATE_TABLE_CON_MAINTENANCE_DETAIL = "CREATE TABLE " + TABLE_CON_MAINTENANCE_DETAIL + " (" + CON_MAINTENANCE_DETAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CON_DEVICE_UPLOAD_ID + " INTEGER," + CON_INSERT_MAINTENANCE_ID + " INTEGER," + CON_UPDATED_BY + " INTEGER," + CON_STATUS + " TEXT," + CON_UPDATED_DATE + " TEXT," +
                CON_MAINTENANCE_REMARK + " TEXT)";
        db.execSQL(CREATE_TABLE_CON_MAINTENANCE_DETAIL);

        //query for dum image
        String CREATE_TABLE_DUM_IMAGE = "CREATE TABLE " + TABLE_DUM_IMAGE + " (" + DUM_IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + DUM_IMAGE_MAINTENANCE_DETAIL_ID + " INTEGER," +
                DUM_IMAGE_PATH + " TEXT," + DUM_IMAGE_FLAG + " INTEGER)";
        db.execSQL(CREATE_TABLE_DUM_IMAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE_SERVER = "DROP TABLE IF EXISTS " + TABLE_SERVER_SETTING;
        db.execSQL(DROP_TABLE_SERVER);

        String DROP_TABLE_USER = "DROP TABLE IF EXISTS " + TABLE_ACCOUNT;
        db.execSQL(DROP_TABLE_USER);

        String DROP_TABLE_DEVICE = "DROP TABLE IF EXISTS " + TABLE_DEVICE_INFO;
        db.execSQL(DROP_TABLE_DEVICE);

        String DROP_TABLE_LOCATION = "DROP TABLE IF EXISTS " + TABLE_LOCATION;
        db.execSQL(DROP_TABLE_LOCATION);

        String DROP_TABLE_ASSETS = "DROP TABLE IF EXISTS " + TABLE_FIXED_ASSET;
        db.execSQL(DROP_TABLE_ASSETS);

        String DROP_TABLE_DOWNLOAD = "DROP TABLE IF EXISTS " + TABLE_DOWNLOAD_INFO;
        db.execSQL(DROP_TABLE_DOWNLOAD);

        String DROP_TABLE_SCHEDULE = "DROP TABLE IF EXISTS " + TABLE_SCHEDULE;
        db.execSQL(DROP_TABLE_SCHEDULE);

        String DROP_TABLE_SCHEDULE_ITEM = "DROP TABLE IF EXISTS " + TABLE_SCHEDULE_ITEM;
        db.execSQL(DROP_TABLE_SCHEDULE_ITEM);

        String DROP_TABLE_SCHEDULE_LIST = "DROP TABLE IF EXISTS " + TABLE_SCHEDULE_LIST;
        db.execSQL(DROP_TABLE_SCHEDULE_LIST);

        String DROP_TABLE_SCHEDULE_CONDITION = "DROP TABLE IF EXISTS " + TABLE_SCHEDULE_UNIT;
        db.execSQL(DROP_TABLE_SCHEDULE_CONDITION);

        String DROP_TABLE_ASSET_CONDITION = "DROP TABLE IF EXISTS " + TABLE_ASSET_CONDITION;
        db.execSQL(DROP_TABLE_ASSET_CONDITION);

        String DROP_TABLE_ASSET_CONDITION_UNIT = "DROP TABLE IF EXISTS " + TABLE_ASSET_CONDITION_UNIT;
        db.execSQL(DROP_TABLE_ASSET_CONDITION_UNIT);

        String DROP_TABLE_MAINTENANCE = "DROP TABLE IF EXISTS " + TABLE_MAINTENANCE;
        db.execSQL(DROP_TABLE_MAINTENANCE);

        String DROP_TABLE_MAINTENANCE_DETAIL = "DROP TABLE IF EXISTS " + TABLE_MAINTENANCE_DETAIL;
        db.execSQL(DROP_TABLE_MAINTENANCE_DETAIL);

        String DROP_TABLE_CON_MAINTENANCE = "DROP TABLE IF EXISTS " + TABLE_CON_MAINTENANCE;
        db.execSQL(DROP_TABLE_CON_MAINTENANCE);

        String DROP_TABLE_CON_MAINTENANCE_DETAIL = "DROP TABLE IF EXISTS " + TABLE_CON_MAINTENANCE_DETAIL;
        db.execSQL(DROP_TABLE_CON_MAINTENANCE_DETAIL);

        String DROP_TABLE_DUM_IMAGE = "DROP TABLE IF EXISTS " + TABLE_DUM_IMAGE;
        db.execSQL(DROP_TABLE_DUM_IMAGE);
        onCreate(db);
    }


    //-----------------------------------------------------------------------------------------------------------------------------
    //serverSetting
    //insert port
    public void addSetting(ServerSetting serverSetting) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(SETTING_GROUP, serverSetting.getSettingGroup());
            values.put(SETTING_NAME, serverSetting.getSettingName());
            values.put(SETTING_VALUE, serverSetting.getSettingValue());
            db.insert(TABLE_SERVER_SETTING, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //get setting by id
    public ServerSetting getSettingById(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SERVER_SETTING, new String[]{SERVER_ID, SETTING_GROUP, SETTING_NAME, SETTING_VALUE}, SERVER_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return new ServerSetting(cursor.getInt(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));

    }

    //update setting
    public void updateSetting(ServerSetting serverSetting) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SETTING_GROUP, serverSetting.getSettingGroup());
        values.put(SETTING_NAME, serverSetting.getSettingName());
        values.put(SETTING_VALUE, serverSetting.getSettingValue());

        db.update(TABLE_SERVER_SETTING, values, SERVER_ID + "=" + serverSetting.getId(), null);
    }

    //get all setting list
    public ArrayList<ServerSetting> getAllSetting() {

        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<ServerSetting> settingList = null;
        try {
            settingList = new ArrayList<>();
            String QUERY = "SELECT * FROM " + TABLE_SERVER_SETTING;
            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    ServerSetting setting = new ServerSetting();
                    setting.setId(cursor.getInt(0));
                    setting.setSettingGroup(cursor.getString(1));
                    setting.setSettingName(cursor.getString(2));
                    setting.setSettingValue(cursor.getString(3));

                    settingList.add(setting);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return settingList;
    }

    //count of setting
    public int getSettingCount() {

        int num;
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            String QUERY = "SELECT * FROM " + TABLE_SERVER_SETTING;
            Cursor cursor = db.rawQuery(QUERY, null);
            num = cursor.getCount();
            cursor.close();
            db.close();
            return num;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //--------------------------------------------------------------------------------------------------------------------------
    //user
    //insert user
    public void insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_USER_ID, user.getUserId());
            values.put(KEY_LOGIN_ID, user.getLoginId());
            values.put(KEY_USER_NAME, user.getName());
            values.put(KEY_PASSWORD, user.getPassword());
            values.put(KEY_JOIN_DATE, user.getJoinedDate());
            values.put(KEY_LAST_LOGIN_DATE, user.getLastLoginDate());
            db.insert(TABLE_ACCOUNT, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //update user
    public void updateUser(User user) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, user.getUserId());
        values.put(KEY_LOGIN_ID, user.getLoginId());
        values.put(KEY_USER_NAME, user.getName());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_JOIN_DATE, user.getJoinedDate());
        values.put(KEY_LAST_LOGIN_DATE, user.getLastLoginDate());

        db.update(TABLE_ACCOUNT, values, KEY_ACCOUNT_ID + "=" + user.getId(), null);
    }

    //get user by loginId
    public User getUserById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ACCOUNT, new String[]{KEY_ACCOUNT_ID, KEY_USER_ID, KEY_LOGIN_ID
                        , KEY_USER_NAME, KEY_PASSWORD, KEY_JOIN_DATE, KEY_LAST_LOGIN_DATE}, KEY_ACCOUNT_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        return new User(cursor.getInt(0),
                cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                cursor.getString(5), cursor.getString(6));
    }

    //get user by loginId for login
    public String getSingleEntry(String loginId) {

        SQLiteDatabase db = this.getReadableDatabase();

        {
            Cursor cursor = db.query(TABLE_ACCOUNT, null, KEY_LOGIN_ID + "=?", new String[]{loginId}, null, null, null);
            if (cursor.getCount() < 1) {
                cursor.close();

                return "NOT EXIST";
            }
            cursor.moveToFirst();
            String password = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD));
            cursor.close();

            return password;
        }
    }

    //check user exists
    public boolean userExists(int id) {

        SQLiteDatabase db;
        db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ACCOUNT + " WHERE " + KEY_ACCOUNT_ID + "=" + id, null);
        boolean exists = false;
        if (cursor.moveToFirst()) {
            exists = (cursor.getCount() > 0);
        }
        cursor.close();

        return exists;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    //device info
    //insert deviceInfo
    public void addDeviceInfo(DeviceInfo deviceInfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(DEV_CODE, deviceInfo.getDeviceCode());
            values.put(DEV_NAME, deviceInfo.getDeviceName());
            values.put(DEV_BRAND, deviceInfo.getBrand());
            values.put(DEV_MODEL, deviceInfo.getModel());
            values.put(DEV_OS_VERSION, deviceInfo.getOsVersion());
            values.put(DEV_DEVICE, deviceInfo.getDevice());
            values.put(DEV_MAC_WIFI, deviceInfo.getMacWifi());
            values.put(DEV_ID_ONE, deviceInfo.getDeviceId1());
            values.put(DEV_ID_TWO, deviceInfo.getDeviceId2());
            values.put(DEV_SERIAL_NO, deviceInfo.getSerialNo());
            values.put(DEV_ANDROID_ID, deviceInfo.getAndroidId());
            values.put(DEV_FINGERPRINT, deviceInfo.getFingerPrint());
            values.put(DEV_IMEI, deviceInfo.getImei());
            db.insert(TABLE_DEVICE_INFO, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //update device info
    public void updateDeviceInfo(DeviceInfo deviceInfo) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DEV_CODE, deviceInfo.getDeviceCode());
        values.put(DEV_NAME, deviceInfo.getDeviceName());
        values.put(DEV_BRAND, deviceInfo.getBrand());
        values.put(DEV_MODEL, deviceInfo.getModel());
        values.put(DEV_OS_VERSION, deviceInfo.getOsVersion());
        values.put(DEV_DEVICE, deviceInfo.getDevice());
        values.put(DEV_MAC_WIFI, deviceInfo.getMacWifi());
        values.put(DEV_ID_ONE, deviceInfo.getDeviceId1());
        values.put(DEV_ID_TWO, deviceInfo.getDeviceId2());
        values.put(DEV_SERIAL_NO, deviceInfo.getSerialNo());
        values.put(DEV_ANDROID_ID, deviceInfo.getAndroidId());
        values.put(DEV_FINGERPRINT, deviceInfo.getFingerPrint());
        values.put(DEV_IMEI, deviceInfo.getImei());

        db.update(TABLE_DEVICE_INFO, values, DEV_ID + "=" + deviceInfo.getId(), null);
    }

    //get user by loginId
    public DeviceInfo getDeviceInfoById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DEVICE_INFO, new String[]{DEV_ID, DEV_CODE, DEV_NAME, DEV_BRAND, DEV_MODEL, DEV_OS_VERSION,
                DEV_DEVICE, DEV_MAC_WIFI, DEV_ID_ONE, DEV_ID_TWO, DEV_SERIAL_NO, DEV_ANDROID_ID, DEV_FINGERPRINT,
                DEV_IMEI}, DEV_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        return new DeviceInfo(cursor.getInt(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8),
                cursor.getString(9), cursor.getString(10), cursor.getString(11), cursor.getString(12),
                cursor.getString(13));
    }

    //count of device
    public int getDeviceCount() {

        int num;
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            String QUERY = "SELECT * FROM " + TABLE_DEVICE_INFO;
            Cursor cursor = db.rawQuery(QUERY, null);
            num = cursor.getCount();
            cursor.close();
            db.close();
            return num;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //check device exists
    public boolean checkDeviceExit(String deviceName) {

        SQLiteDatabase db;
        db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DEVICE_INFO + " WHERE " + DEV_NAME + "='" + deviceName + "'", null);
        boolean exists = false;
        if (cursor.moveToFirst()) {
            exists = (cursor.getCount() > 0);
        }
        cursor.close();

        return exists;
    }

    //----------------------------------------------------------------------------------------------------------------------------
    //Location
    //insert location
    public void addLocation(LocationsBean locations) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_LOCATION_ID, locations.getLocationId());
            values.put(KEY_PARENT_NAME, locations.getParentName());
            values.put(KEY_LOCATION_CODE, locations.getCode());
            values.put(KEY_LOCATION_NAME, locations.getName());
            values.put(KEY_LOCATION_DOWNLOAD, locations.getDownload());
            values.put(KEY_LOCATION_UPLOAD, locations.getUpload());
            values.put(KEY_LOCATION_STATUS, locations.getStatus());
            db.insert(TABLE_LOCATION, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //update location
    public void updateLocation(LocationsBean locations) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PARENT_NAME, locations.getParentName());
        values.put(KEY_LOCATION_CODE, locations.getCode());
        values.put(KEY_LOCATION_NAME, locations.getName());
        values.put(KEY_LOCATION_DOWNLOAD, locations.getDownload());
        values.put(KEY_LOCATION_UPLOAD, locations.getUpload());
        values.put(KEY_LOCATION_STATUS, locations.getStatus());

        db.update(TABLE_LOCATION, values, KEY_LOCATION_ID + "=" + locations.getLocationId(), null);
    }


    //get all location
    public ArrayList<LocationsBean> getAllLocation() {

        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<LocationsBean> locations = null;
        try {
            locations = new ArrayList<>();
            String QUERY = "SELECT * FROM " + TABLE_LOCATION;
            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    LocationsBean location = new LocationsBean();
                    location.setLocationId(cursor.getInt(0));
                    location.setParentName(cursor.getString(1));
                    location.setCode(cursor.getString(2));
                    location.setName(cursor.getString(3));
                    location.setDownload(cursor.getInt(4));
                    location.setUpload(cursor.getInt(5));
                    location.setStatus(cursor.getInt(6));
                    locations.add(location);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return locations;
    }

    //get location by download status 1
    public ArrayList<LocationsBean> getDownloadedLocation() {

        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<LocationsBean> locations = null;
        try {
            locations = new ArrayList<>();
            String QUERY = "SELECT * FROM " + TABLE_LOCATION + " WHERE " + KEY_LOCATION_DOWNLOAD + "=1";
            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    LocationsBean location = new LocationsBean();
                    location.setLocationId(cursor.getInt(0));
                    location.setParentName(cursor.getString(1));
                    location.setCode(cursor.getString(2));
                    location.setName(cursor.getString(3));
                    location.setDownload(cursor.getInt(4));
                    location.setUpload(cursor.getInt(5));
                    location.setStatus(cursor.getInt(6));
                    locations.add(location);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return locations;
    }

    //get location by Id
    public LocationsBean getLocationById(int locationId) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_LOCATION, new String[]{KEY_LOCATION_ID, KEY_PARENT_NAME, KEY_LOCATION_CODE, KEY_LOCATION_NAME,
                        KEY_LOCATION_DOWNLOAD, KEY_LOCATION_UPLOAD, KEY_LOCATION_STATUS}, KEY_LOCATION_ID + "=?",
                new String[]{String.valueOf(locationId)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        return new LocationsBean(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getInt(4), cursor.getInt(5), (cursor.getInt(6)));
    }

    //get location by name
    public LocationsBean getLocationByName(String name) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_LOCATION, new String[]{KEY_LOCATION_ID, KEY_PARENT_NAME, KEY_LOCATION_CODE, KEY_LOCATION_NAME,
                        KEY_LOCATION_DOWNLOAD, KEY_LOCATION_UPLOAD, KEY_LOCATION_STATUS}, KEY_LOCATION_NAME + "=?",
                new String[]{name}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        return new LocationsBean(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getInt(4), cursor.getInt(5), (cursor.getInt(6)));
    }

    //check location exists
    public boolean checkLocationExists(int locationId) {

        SQLiteDatabase db;
        db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_LOCATION + " WHERE " + KEY_LOCATION_ID + "='" + locationId + "'", null);
        boolean exists = false;
        if (cursor.moveToFirst()) {
            exists = (cursor.getCount() > 0);
        }
        cursor.close();

        return exists;
    }

    //count of location
    public int getLocationCount() {

        int num;
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            String QUERY = "SELECT * FROM " + TABLE_LOCATION;
            Cursor cursor = db.rawQuery(QUERY, null);
            num = cursor.getCount();
            cursor.close();
            db.close();
            return num;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //count of location by id
    public int getLocationCountById(int id) {

        int num;
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            String QUERY = "SELECT * FROM " + TABLE_LOCATION + " WHERE " + KEY_LOCATION_ID + id;
            Cursor cursor = db.rawQuery(QUERY, null);
            num = cursor.getCount();
            cursor.close();
            db.close();
            return num;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    //------------------------------------------------------------------------------------------------------
    //insert assets
    public void addFA(AssetBean asset) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_FA_ID, asset.getId());
            values.put(LOCATION_ID, asset.getLocationId());
            values.put(COST_CENTER, asset.getCostCenter());
            values.put(ACTUAL_LOCATION, asset.getActualLocation());
            values.put(KEY_FA_NUMBER, asset.getFaNumber());
            values.put(KEY_ITEM_NAME, asset.getItemName());
            values.put(KEY_CONDITION, asset.getCondition());
            values.put(KEY_CATEGORY, asset.getCategory());
            values.put(KEY_BRAND, asset.getBrand());
            values.put(KEY_MODEL, asset.getModel());
            values.put(KEY_SERIAL_NO, asset.getSerialNo());
            db.insert(TABLE_FIXED_ASSET, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //get fixedAsset by faNo
    public AssetBean getFixedAssetByFANo(String faNo) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_FIXED_ASSET, new String[]{KEY_FA_ID, LOCATION_ID, COST_CENTER, ACTUAL_LOCATION, KEY_FA_NUMBER,
                        KEY_ITEM_NAME, KEY_CONDITION, KEY_CATEGORY, KEY_BRAND, KEY_MODEL, KEY_SERIAL_NO}, KEY_FA_NUMBER + "=?",
                new String[]{faNo}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        AssetBean fixedAsset = new AssetBean(cursor.getInt(0), cursor.getInt(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10));

        return fixedAsset;
    }

    //count of assets
    public int getAssetCount() {

        int num = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            String QUERY = "SELECT * FROM " + TABLE_FIXED_ASSET;
            Cursor cursor = db.rawQuery(QUERY, null);
            num = cursor.getCount();
            cursor.close();
            db.close();
            return num;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    //get all assets
    public ArrayList<AssetBean> getAllFA() {

        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<AssetBean> assetList = null;
        try {
            assetList = new ArrayList<>();
            String QUERY = "SELECT * FROM " + TABLE_FIXED_ASSET;
            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {

                while (cursor.moveToNext()) {

                    AssetBean fixedAsset = new AssetBean();
                    fixedAsset.setId(cursor.getInt(0));
                    fixedAsset.setLocationId(cursor.getInt(1));
                    fixedAsset.setCostCenter(cursor.getString(2));
                    fixedAsset.setActualLocation(cursor.getString(3));
                    fixedAsset.setFaNumber(cursor.getString(4));
                    fixedAsset.setItemName(cursor.getString(5));
                    fixedAsset.setCondition(cursor.getString(6));
                    fixedAsset.setCategory(cursor.getString(7));
                    fixedAsset.setBrand(cursor.getString(8));
                    fixedAsset.setModel(cursor.getString(9));
                    fixedAsset.setSerialNo(cursor.getString(10));
                    assetList.add(fixedAsset);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return assetList;
    }


    public ArrayList<AssetBean> getAssetByLocation(int locationId) {

        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<AssetBean> assetList = null;
        try {
            assetList = new ArrayList<>();
            String QUERY = "SELECT * FROM " + TABLE_FIXED_ASSET + " WHERE " + LOCATION_ID + "=" + locationId;
            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {

                while (cursor.moveToNext()) {

                    AssetBean fixedAsset = new AssetBean();
                    fixedAsset.setId(cursor.getInt(0));
                    fixedAsset.setLocationId(cursor.getInt(1));
                    fixedAsset.setCostCenter(cursor.getString(2));
                    fixedAsset.setActualLocation(cursor.getString(3));
                    fixedAsset.setFaNumber(cursor.getString(4));
                    fixedAsset.setItemName(cursor.getString(5));
                    fixedAsset.setCondition(cursor.getString(6));
                    fixedAsset.setCategory(cursor.getString(7));
                    fixedAsset.setBrand(cursor.getString(8));
                    fixedAsset.setModel(cursor.getString(9));
                    fixedAsset.setSerialNo(cursor.getString(10));
                    assetList.add(fixedAsset);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return assetList;
    }

    //count of assets by location
    public int getAssetCountByLocation(int locationId) {

        int num = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            String QUERY = "SELECT * FROM " + TABLE_FIXED_ASSET + " WHERE " + LOCATION_ID + "=" + locationId;
            Cursor cursor = db.rawQuery(QUERY, null);
            num = cursor.getCount();
            cursor.close();
            db.close();
            return num;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    //check Asset exists
    public boolean checkAssetExists(int assetId) {

        SQLiteDatabase db;
        db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FIXED_ASSET + " WHERE " + KEY_FA_ID + "='" + assetId + "'", null);
        boolean exists = false;
        if (cursor.moveToFirst()) {
            exists = (cursor.getCount() > 0);
        }
        cursor.close();

        return exists;
    }

    //------------------------------------------------------------------------------------------------------
    //insert info
    public void addInfo(DownloadInfo info) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_DOWNLOAD_USER_ID, info.getDownloadUserId());
            values.put(KEY_DOWNLOAD_DATE, getDateTime());
            values.put(KEY_LOCATIONS, info.getLocations());
            values.put(KEY_START_DATE, info.getDownloadStartDate());
            values.put(KEY_END_DATE, info.getDownloadEndDate());
            db.insert(TABLE_DOWNLOAD_INFO, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //get info by id
    public DownloadInfo getDownloadInfoById(int id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DOWNLOAD_INFO, new String[]{KEY_ID,
                        KEY_DOWNLOAD_USER_ID, KEY_DOWNLOAD_DATE, KEY_LOCATIONS, KEY_START_DATE, KEY_END_DATE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DownloadInfo downloadInfo = new DownloadInfo(cursor.getInt(0),
                cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));

        return downloadInfo;
    }

    //count of info
    public int getInfoCount() {

        int num = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            String QUERY = "SELECT * FROM " + TABLE_DOWNLOAD_INFO;
            Cursor cursor = db.rawQuery(QUERY, null);
            num = cursor.getCount();
            cursor.close();
            db.close();
            return num;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    //------------------------------------------------------------------------------------------------------
    //insert schedule
    public void insertSchedule(Schedule schedule) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(SCHEDULE_ID, schedule.getId());
            values.put(SCHEDULE_NAME, schedule.getName());
            values.put(SCHEDULE_CODE, schedule.getScheduleCode());
            values.put(SCHEDULE_TYPE, schedule.getScheduleType());
            values.put(SCHEDULE_START_DATE, schedule.getStartDateAsString());
            values.put(SCHEDULE_END_DATE, schedule.getEndDateAsString());
            values.put(SCHEDULE_TIME, schedule.getTimeAsString());
            values.put(SCHEDULE_STATUS, schedule.getScheduleStatus());
            db.insert(TABLE_SCHEDULE, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //------------------------------------------------------------------------------------------------------
    //insert scheduleItem
    public void insertScheduleItem(ScheduleItem scheduleItem) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(ITEM_ID, scheduleItem.getId());
            values.put(ITEM_SCHEDULE_ID, scheduleItem.getScheduleId());
            values.put(ITEM_ASSET_ID, scheduleItem.getAssetId());
            values.put(ITEM_START_DATE, scheduleItem.getStartDateAsString());
            values.put(ITEM_END_DATE, scheduleItem.getEndDateAsString());
            values.put(ITEM_STATUS, scheduleItem.getScheduleStatus());
            db.insert(TABLE_SCHEDULE_ITEM, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //---------------------------------------------------------
    //insert scheduleList
    public void insertScheduleList(ScheduleList scheduleList) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(SCHEDULE_LIST_ID, scheduleList.getId());
            values.put(SCHEDULE_ITEM_ID, scheduleList.getScheduleItemId());
            values.put(SCHEDULE_DATE, scheduleList.getScheduleDateAsString());
            values.put(SCHEDULE_LIST_STATUS, scheduleList.getStatus());
            db.insert(TABLE_SCHEDULE_LIST, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //---------------------------------------------------------
    //insert unit schedule
    public void insertUnitScheduleCondition(UnitSchedule unitSchedule) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(UNIT_CON_SCH_NAME, unitSchedule.getScheduleName());
            values.put(UNIT_CON_SCH_UNIT_TYPE, unitSchedule.getUnitType());
            values.put(UNIT_CON_SCH_ASSET_ID, unitSchedule.getAssetId());
            values.put(UNIT_CON_SCH_CODE, unitSchedule.getScheduleCode());
            values.put(UNIT_CON_SCH_CON_NAME, unitSchedule.getConditionName());
            values.put(UNIT_CON_SCH_ID, unitSchedule.getScheduleId());
            values.put(UNIT_CON_SCH_UNIT, unitSchedule.getScheduleUnit());
            db.insert(TABLE_SCHEDULE_UNIT, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //get all schedule condition by assetId
    public ArrayList<UnitSchedule> getUnitScheduleByAssetId(int assetId) {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<UnitSchedule> schedules = null;
        try {
            schedules = new ArrayList<>();
            String query = null;
            query = "SELECT * FROM " + TABLE_SCHEDULE_UNIT + " WHERE " + UNIT_CON_SCH_ASSET_ID + "=" + assetId;

            Cursor cursor = db.rawQuery(query, null);
            if (!cursor.isLast()) {

                while (cursor.moveToNext()) {

                    UnitSchedule unitSchedule = new UnitSchedule();
                    unitSchedule.setId(cursor.getInt(0));
                    unitSchedule.setScheduleName(cursor.getString(1));
                    unitSchedule.setUnitType(cursor.getString(2));
                    unitSchedule.setAssetId(cursor.getInt(3));
                    unitSchedule.setScheduleCode(cursor.getString(4));
                    unitSchedule.setConditionName(cursor.getString(5));
                    unitSchedule.setScheduleId(cursor.getDouble(6));
                    unitSchedule.setScheduleUnit(cursor.getDouble(7));
                    schedules.add(unitSchedule);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return schedules;
    }

    //get all schedule condition by scheduleId
    public ArrayList<UnitSchedule> getAllUnitScheduleByScheduleId(int scheduleId) {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<UnitSchedule> schedules = null;
        try {
            schedules = new ArrayList<>();
            String query = null;
            query = "SELECT * FROM " + TABLE_SCHEDULE_UNIT + " WHERE " + UNIT_CON_SCH_ID + "=" + scheduleId;

            Cursor cursor = db.rawQuery(query, null);
            if (!cursor.isLast()) {

                while (cursor.moveToNext()) {

                    UnitSchedule unitSchedule = new UnitSchedule();
                    unitSchedule.setId(cursor.getInt(0));
                    unitSchedule.setScheduleName(cursor.getString(1));
                    unitSchedule.setUnitType(cursor.getString(2));
                    unitSchedule.setAssetId(cursor.getInt(3));
                    unitSchedule.setScheduleCode(cursor.getString(4));
                    unitSchedule.setConditionName(cursor.getString(5));
                    unitSchedule.setScheduleId(cursor.getDouble(6));
                    unitSchedule.setScheduleUnit(cursor.getDouble(7));
                    schedules.add(unitSchedule);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return schedules;
    }

    //get schedule condition by scheduleId
    public UnitSchedule getUnitScheduleByScheduleId(int scheduleId) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SCHEDULE_UNIT, new String[]{UNIT_CON_ID, UNIT_CON_SCH_NAME, UNIT_CON_SCH_UNIT_TYPE, UNIT_CON_SCH_ASSET_ID,
                        UNIT_CON_SCH_CODE, UNIT_CON_SCH_CON_NAME, UNIT_CON_SCH_ID, UNIT_CON_SCH_UNIT}, UNIT_CON_SCH_ID + "=?",
                new String[]{String.valueOf(scheduleId)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        return new UnitSchedule(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                cursor.getInt(3), cursor.getString(4), cursor.getString(5), cursor.getDouble(6), cursor.getDouble(7));
    }

    //check Asset exists
    public boolean checkUnitScheduleExists(int scheduleId) {

        SQLiteDatabase db;
        db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SCHEDULE_UNIT + " WHERE " + UNIT_CON_SCH_ID + "=" + scheduleId + "", null);
        boolean exists = false;
        if (cursor.moveToFirst()) {
            exists = (cursor.getCount() > 0);
        }
        cursor.close();

        return exists;
    }

    //---------------------------------------------------------
    //insert asset condition
    public void insertAssetCondition(AssetMaintenanceCondition assetCondition) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(ASSET_CON_ID, assetCondition.getId());
            values.put(ASSET_CON_REG_DATE, assetCondition.getRegDate());
            values.put(ASSET_CON_UPD_DATE, assetCondition.getUpdDate());
            values.put(ASSET_CON_ASSET_ID, assetCondition.getAssetId());
            values.put(ASSET_CON_NAME, assetCondition.getConditionName());
            values.put(ASSET_CON_UNIT_TYPE, assetCondition.getUnitType());
            values.put(ASSET_CON_TOTAL_UNIT, assetCondition.getTotalUnit());
            values.put(ASSET_CON_UPDATE_UNIT, assetCondition.getUpdateUnit());
            db.insert(TABLE_ASSET_CONDITION, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //update asset condition
    public boolean updateAssetCondition(AssetMaintenanceCondition assetCondition) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ASSET_CON_REG_DATE, assetCondition.getRegDate());
        values.put(ASSET_CON_UPD_DATE, assetCondition.getUpdDate());
        values.put(ASSET_CON_ASSET_ID, assetCondition.getAssetId());
        values.put(ASSET_CON_NAME, assetCondition.getConditionName());
        values.put(ASSET_CON_UNIT_TYPE, assetCondition.getUnitType());
        values.put(ASSET_CON_TOTAL_UNIT, assetCondition.getTotalUnit());
        values.put(ASSET_CON_UPDATE_UNIT, assetCondition.getUpdateUnit());

        return db.update(TABLE_ASSET_CONDITION, values, ASSET_CON_ID + "=" + assetCondition.getId(), null) > 0;
    }

    //get all asset condition by assetId
    public ArrayList<AssetMaintenanceCondition> getAssetConditionByAssetId(int assetId) {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<AssetMaintenanceCondition> assetConditions = null;
        try {
            assetConditions = new ArrayList<>();
            String query = null;
            query = "SELECT * FROM " + TABLE_ASSET_CONDITION + " WHERE " + ASSET_CON_ASSET_ID + "=" + assetId;

            Cursor cursor = db.rawQuery(query, null);
            if (!cursor.isLast()) {

                while (cursor.moveToNext()) {

                    AssetMaintenanceCondition assetCondition = new AssetMaintenanceCondition();
                    assetCondition.setId(cursor.getInt(0));
                    assetCondition.setRegDate(cursor.getString(1));
                    assetCondition.setUpdDate(cursor.getString(2));
                    assetCondition.setAssetId(cursor.getInt(3));
                    assetCondition.setConditionName(cursor.getString(4));
                    assetCondition.setUnitType(cursor.getString(5));
                    assetCondition.setTotalUnit(cursor.getDouble(6));
                    assetCondition.setUpdateUnit(cursor.getDouble(7));
                    assetConditions.add(assetCondition);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return assetConditions;
    }

    //get asset condition by conditionId
    public AssetMaintenanceCondition getAssetConditionByConId(int conditionId) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ASSET_CONDITION, new String[]{ASSET_CON_ID, ASSET_CON_REG_DATE, ASSET_CON_UPD_DATE, ASSET_CON_ASSET_ID,
                        ASSET_CON_NAME, ASSET_CON_UNIT_TYPE, ASSET_CON_TOTAL_UNIT, ASSET_CON_UPDATE_UNIT}, ASSET_CON_ID + "=?",
                new String[]{String.valueOf(conditionId)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        return new AssetMaintenanceCondition(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                cursor.getInt(3), cursor.getString(4), cursor.getString(5), cursor.getDouble(6), cursor.getDouble(7));
    }

    //get all maintenance
    public ArrayList<AssetMaintenanceConditionUnitBean> getAllAssetMaintenanceCondition(int locationId) {

        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<AssetMaintenanceConditionUnitBean> conditions = null;
        try {
            conditions = new ArrayList<>();

            String QUERY = " SELECT\n" +
                    "    con_unit.condition_id AS conditionId,\n" +
                    "    con_unit.asset_id AS assetId,\n" +
                    "    con_unit.condition_name AS conditionName,\n" +
                    "    con_unit.unit AS unit,\n" +
                    "    con_unit.unit_type AS unitType,\n" +
                    "    con_unit.taking_date AS takingDate,\n" +
                    "    con_unit.taking_by AS takingBy,\n" +
                    "    con_unit.flag AS flag,\n" +
                    "    con_unit.upload_flag AS uploadFlag\n" +
                    "    FROM tbl_asset_condition_unit con_unit\n" +
                    "    LEFT JOIN tbl_asset asset ON asset.id=con_unit.asset_id\n" +
                    "WHERE flag=1 AND upload_flag=0 AND asset.location_id=" + locationId;

            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {

                while (cursor.moveToNext()) {

                    AssetMaintenanceConditionUnitBean condition = new AssetMaintenanceConditionUnitBean();
                    condition.setConditionId(cursor.getInt(0));
                    condition.setAssetId(cursor.getInt(1));
                    condition.setConditionName(cursor.getString(2));
                    condition.setUnit(cursor.getDouble(3));
                    condition.setUnitType(cursor.getString(4));
                    condition.setTakingDate(cursor.getString(5));
                    condition.setTakingBy(cursor.getInt(6));
                    condition.setFlag(cursor.getInt(7));
                    condition.setUploadFlag(cursor.getInt(8));
                    conditions.add(condition);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return conditions;
    }

    //------------------------------------------------------------------------
    //insert asset condition unit
    public void insertAssetConditionUnit(AssetMaintenanceConditionUnitBean assetUnit) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(ASSET_UNIT_CON_ID, assetUnit.getConditionId());
            values.put(ASSET_UNIT_ASSET_ID, assetUnit.getAssetId());
            values.put(ASSET_UNIT_CON_NAME, assetUnit.getConditionName());
            values.put(ASSET_UNIT_CON_UNIT, assetUnit.getUnit());
            values.put(ASSET_UNIT_TYPE, assetUnit.getUnitType());
            values.put(ASSET_UNIT_TAKING_DATE, assetUnit.getTakingDate());
            values.put(ASSET_UNIT_TAKING_BY, assetUnit.getTakingBy());
            values.put(ASSET_UNIT_FLAG, assetUnit.getFlag());
            values.put(ASSET_UNIT_UPLOAD_FLAG, assetUnit.getUploadFlag());
            db.insert(TABLE_ASSET_CONDITION_UNIT, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //update asset condition unit
    public boolean updateAssetConditionUnit(AssetMaintenanceConditionUnitBean assetUnit) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ASSET_UNIT_ASSET_ID, assetUnit.getAssetId());
        values.put(ASSET_UNIT_CON_NAME, assetUnit.getConditionName());
        values.put(ASSET_UNIT_CON_UNIT, assetUnit.getUnit());
        values.put(ASSET_UNIT_TYPE, assetUnit.getUnitType());
        values.put(ASSET_UNIT_TAKING_DATE, assetUnit.getTakingDate());
        values.put(ASSET_UNIT_TAKING_BY, assetUnit.getTakingBy());
        values.put(ASSET_UNIT_FLAG, assetUnit.getFlag());
        values.put(ASSET_UNIT_UPLOAD_FLAG, assetUnit.getUploadFlag());

        return db.update(TABLE_ASSET_CONDITION_UNIT, values, ASSET_UNIT_CON_ID + "=" + assetUnit.getConditionId(), null) > 0;
    }

    //get asset condition unit by conditionId
    public ArrayList<AssetMaintenanceConditionUnitBean> getAssetConditionUnitByConId(int conId) {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<AssetMaintenanceConditionUnitBean> assetConditions = null;
        try {
            assetConditions = new ArrayList<>();
            String query = null;
            query = "SELECT * FROM " + TABLE_ASSET_CONDITION_UNIT + " WHERE " + ASSET_UNIT_CON_ID + "=" + conId;

            Cursor cursor = db.rawQuery(query, null);
            if (!cursor.isLast()) {

                while (cursor.moveToNext()) {

                    AssetMaintenanceConditionUnitBean assetConditionUnit = new AssetMaintenanceConditionUnitBean();
                    assetConditionUnit.setConditionId(cursor.getInt(0));
                    assetConditionUnit.setAssetId(cursor.getInt(1));
                    assetConditionUnit.setConditionName(cursor.getString(2));
                    assetConditionUnit.setUnit(cursor.getDouble(3));
                    assetConditionUnit.setUnitType(cursor.getString(4));
                    assetConditionUnit.setTakingDate(cursor.getString(5));
                    assetConditionUnit.setTakingBy(cursor.getInt(6));
                    assetConditionUnit.setFlag(cursor.getInt(7));
                    assetConditionUnit.setUploadFlag(cursor.getInt(8));
                    assetConditions.add(assetConditionUnit);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return assetConditions;
    }

    //get upload count
    public int getUploadAssetConditionCount() {

        int num = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            String QUERY = "SELECT * FROM " + TABLE_ASSET_CONDITION_UNIT + " WHERE " + ASSET_UNIT_FLAG + "=" + 1 + " AND " + ASSET_UNIT_UPLOAD_FLAG + "=" + 0;
            Cursor cursor = db.rawQuery(QUERY, null);
            num = cursor.getCount();
            cursor.close();
            db.close();
            return num;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    //-------------------------------------------------------------------------
    //add maintenance
    public void addMaintenance(Maintenance maintenance) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(MAINTENANCE_INSERT_ID, maintenance.getMaintenanceId());
            values.put(MAINTENANCE_ID, maintenance.getId());
            values.put(MAINTENANCE_SCHEDULE_ID, maintenance.getScheduleListId());
            values.put(ASSET_ID, maintenance.getAssetId());
            values.put(START_DATE, maintenance.getStartDateAsString());
            values.put(END_DATE, maintenance.getEndDateAsString());
            values.put(MAINTENANCE_STATUS, maintenance.getStatus());
            values.put(MAINTENANCE_BY, maintenance.getMaintenanceBy());
            values.put(MAINTENANCE_FLAG, maintenance.getFlag());
            values.put(UPLOAD_FLAG, maintenance.getUploadFlag());
            db.insert(TABLE_MAINTENANCE, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //insert maintenance
    public void insertMaintenance(Maintenance maintenance) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(MAINTENANCE_ID, maintenance.getId());
            values.put(MAINTENANCE_SCHEDULE_ID, maintenance.getScheduleListId());
            values.put(ASSET_ID, maintenance.getAssetId());
            values.put(START_DATE, maintenance.getStartDateAsString());
            values.put(END_DATE, maintenance.getEndDateAsString());
            values.put(MAINTENANCE_STATUS, maintenance.getStatus());
            values.put(MAINTENANCE_BY, maintenance.getMaintenanceBy());
            values.put(MAINTENANCE_FLAG, maintenance.getFlag());
            values.put(UPLOAD_FLAG, maintenance.getUploadFlag());
            db.insert(TABLE_MAINTENANCE, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //get maintenance by id
    public Maintenance getMaintenanceById(int id) {

        Maintenance maintenance = null;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MAINTENANCE, new String[]{MAINTENANCE_INSERT_ID, MAINTENANCE_ID, MAINTENANCE_SCHEDULE_ID, ASSET_ID,
                        START_DATE, END_DATE, MAINTENANCE_STATUS, MAINTENANCE_BY, MAINTENANCE_FLAG, UPLOAD_FLAG}, MAINTENANCE_INSERT_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            maintenance = new Maintenance(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2),
                    cursor.getInt(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                    cursor.getInt(7), cursor.getInt(8), cursor.getInt(9));

            cursor.close();
        }
        return maintenance;
    }

    //get maintenance by id
    public Maintenance getMaintenanceByMaintenanceId(int id) {

        Maintenance maintenance = null;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MAINTENANCE, new String[]{MAINTENANCE_INSERT_ID, MAINTENANCE_ID, MAINTENANCE_SCHEDULE_ID, ASSET_ID,
                        START_DATE, END_DATE, MAINTENANCE_STATUS, MAINTENANCE_BY, MAINTENANCE_FLAG, UPLOAD_FLAG}, MAINTENANCE_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            maintenance = new Maintenance(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2),
                    cursor.getInt(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                    cursor.getInt(7), cursor.getInt(8), cursor.getInt(9));

            cursor.close();
        }
        return maintenance;
    }

    //update maintenance
    public boolean updateMaintenance(Maintenance maintenance) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MAINTENANCE_ID, maintenance.getId());
        values.put(MAINTENANCE_SCHEDULE_ID, maintenance.getScheduleListId());
        values.put(ASSET_ID, maintenance.getAssetId());
        values.put(START_DATE, maintenance.getStartDateAsString());
        values.put(END_DATE, maintenance.getEndDateAsString());
        values.put(MAINTENANCE_STATUS, maintenance.getStatus());
        values.put(MAINTENANCE_BY, maintenance.getMaintenanceBy());
        values.put(MAINTENANCE_FLAG, maintenance.getFlag());
        values.put(UPLOAD_FLAG, maintenance.getUploadFlag());

        return db.update(TABLE_MAINTENANCE, values, MAINTENANCE_INSERT_ID + "=" + maintenance.getMaintenanceId(), null) > 0;
    }

    //get all maintenance by locationId
    public ArrayList<Maintenance> getAllMaintenance(int locationId) {

        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Maintenance> maintenances = null;
        try {
            maintenances = new ArrayList<>();

            String QUERY = " SELECT\n" +
                    "   m.id AS Id,\n" +
                    "   m.maintenance_id AS maintenanceId,\n" +
                    "   m.schedule_list_id AS scheduleListId,\n" +
                    "   m.asset_id AS assetId,\n" +
                    "   m.start_date As startDate,\n" +
                    "   m.end_date As endDate,\n" +
                    "   m.status AS maintenanceStatus,\n" +
                    "   m.maintenance_by As maintenanceBy,\n" +
                    "   m.flag As flag,\n" +
                    "   m.upload_flag As uploadFlag\n" +
                    "   FROM tbl_maintenance m\n" +
                    "   LEFT JOIN tbl_asset a ON a.id=m.asset_id\n" +
                    "   LEFT JOIN tbl_schedule_list sl ON sl.id=m.schedule_list_id\n" +
                    "   LEFT JOIN tbl_schedule_item si ON si.id=sl.schedule_item_id\n" +
                    "   LEFT JOIN tbl_schedule s ON s.id=si.schedule_id\n" +
                    "   WHERE flag=1 AND upload_flag=0 AND a.location_id=" + locationId;

            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {

                while (cursor.moveToNext()) {

                    Maintenance maintenance = new Maintenance();
                    maintenance.setMaintenanceId(cursor.getInt(0));
                    maintenance.setId(cursor.getInt(1));
                    maintenance.setScheduleListId(cursor.getInt(2));
                    maintenance.setAssetId(cursor.getInt(3));
                    maintenance.setStartDateAsString(cursor.getString(4));
                    maintenance.setEndDateAsString(cursor.getString(5));
                    maintenance.setStatus(cursor.getString(6));
                    maintenance.setMaintenanceBy(cursor.getInt(7));
                    maintenance.setFlag(cursor.getInt(8));
                    maintenance.setUploadFlag(cursor.getInt(9));
                    maintenances.add(maintenance);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return maintenances;
    }

    //get uploaded count
    public int getUploadedCount() {

        int num = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            String QUERY = "SELECT * FROM " + TABLE_MAINTENANCE + " WHERE " + MAINTENANCE_FLAG + "=" + 1 + " AND " + UPLOAD_FLAG + "=" + 1;
            Cursor cursor = db.rawQuery(QUERY, null);
            num = cursor.getCount();
            cursor.close();
            db.close();
            return num;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    //get upload count
    public int getUploadMaintenanceCount() {

        int num = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            String QUERY = "SELECT * FROM " + TABLE_MAINTENANCE + " WHERE " + MAINTENANCE_FLAG + "=" + 1 + " AND " + UPLOAD_FLAG + "=" + 0;
            Cursor cursor = db.rawQuery(QUERY, null);
            num = cursor.getCount();
            cursor.close();
            db.close();
            return num;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    //check maintenance detail finish
    public boolean checkMaintenanceDetailFinish(String status, String scheduleCode, int id, int locationId) {

        SQLiteDatabase db = this.getReadableDatabase();

        String QUERY = "SELECT * FROM tbl_maintenance main left join tbl_asset a on a.id =main.asset_id " +
                "left join tbl_schedule_list sl on sl.id=main.schedule_list_id" +
                " left join tbl_schedule_item si on si.id=sl.schedule_item_id" +
                " left join tbl_schedule s on s.id=si.schedule_id where s.code = '" + scheduleCode +
                "' and status = '" + status +
                "' and a.id = " + id + " and a.location_id=" + locationId;

        Cursor cursor = db.rawQuery(QUERY, null);
        boolean exists = false;
        if (cursor.moveToFirst()) {
            exists = (cursor.getCount() > 0);
        }
        cursor.close();
        db.close();
        return exists;
    }

    //---------------------------------------------------------
    //insert maintenanceDetail
    public void insertMaintenanceDetail(MaintenanceDetail maintenanceDetail) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(INSERT_MAINTENANCE_ID, maintenanceDetail.getMaintenanceId());
            values.put(UPLOAD_ID, maintenanceDetail.getUploadId());
            values.put(MAINTENANCE_STATUS_DETAIL, maintenanceDetail.getStatus());
            values.put(UPDATED_DATE, maintenanceDetail.getUpdatedDateAsString());
            values.put(REMARK, maintenanceDetail.getRemark());
            db.insert(TABLE_MAINTENANCE_DETAIL, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //update maintenance detail
    public boolean updateMaintenanceDetail(MaintenanceDetail detail) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(INSERT_MAINTENANCE_ID, detail.getMaintenanceId());
        values.put(UPLOAD_ID, detail.getUploadId());
        values.put(MAINTENANCE_STATUS_DETAIL, detail.getStatus());
        values.put(UPDATED_DATE, detail.getUpdatedDateAsString());
        values.put(REMARK, detail.getRemark());
        return db.update(TABLE_MAINTENANCE_DETAIL, values, MAINTENANCE_DETAIL_ID + "=" + detail.getId(), null) > 0;
    }

    //get all maintenanceDetail by flag
    public ArrayList<MaintenanceDetail> getAllMaintenanceDetails(int maintenanceId) {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<MaintenanceDetail> maintenanceDetails = null;
        try {
            maintenanceDetails = new ArrayList<>();
            String query = null;
            query = "SELECT * FROM " + TABLE_MAINTENANCE_DETAIL + " WHERE " + INSERT_MAINTENANCE_ID + "=" + maintenanceId;

            Cursor cursor = db.rawQuery(query, null);
            if (!cursor.isLast()) {

                while (cursor.moveToNext()) {

                    MaintenanceDetail maintenanceDetail = new MaintenanceDetail();
                    maintenanceDetail.setId(cursor.getInt(0));
                    maintenanceDetail.setMaintenanceId(cursor.getInt(1));
                    maintenanceDetail.setUploadId(cursor.getInt(2));
                    maintenanceDetail.setStatus(cursor.getString(3));
                    maintenanceDetail.setUpdatedDateAsString(cursor.getString(4));
                    maintenanceDetail.setRemark(cursor.getString(5));
                    maintenanceDetails.add(maintenanceDetail);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maintenanceDetails;
    }

    public ArrayList<MaintenanceDetail> getAllUploadMaintenanceDetails(int maintenanceId) {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<MaintenanceDetail> maintenanceDetails = null;
        try {
            maintenanceDetails = new ArrayList<>();
            String query = null;
            query = "SELECT * FROM " + TABLE_MAINTENANCE_DETAIL + " WHERE " + INSERT_MAINTENANCE_ID + "=" + maintenanceId + " AND " + UPLOAD_ID + "=0";

            Cursor cursor = db.rawQuery(query, null);
            if (!cursor.isLast()) {

                while (cursor.moveToNext()) {

                    MaintenanceDetail maintenanceDetail = new MaintenanceDetail();
                    maintenanceDetail.setId(cursor.getInt(0));
                    maintenanceDetail.setMaintenanceId(cursor.getInt(1));
                    maintenanceDetail.setUploadId(cursor.getInt(2));
                    maintenanceDetail.setStatus(cursor.getString(3));
                    maintenanceDetail.setUpdatedDateAsString(cursor.getString(4));
                    maintenanceDetail.setRemark(cursor.getString(5));
                    maintenanceDetails.add(maintenanceDetail);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maintenanceDetails;
    }

    public ArrayList<MaintenanceDetail> getAllUploadFailMaintenanceDetails(int maintenanceId) {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<MaintenanceDetail> maintenanceDetails = null;
        try {
            maintenanceDetails = new ArrayList<>();
            String query = null;
            query = "SELECT * FROM " + TABLE_MAINTENANCE_DETAIL + " WHERE " + INSERT_MAINTENANCE_ID + "=" + maintenanceId + " AND " + UPLOAD_ID + "!=0";

            Cursor cursor = db.rawQuery(query, null);
            if (!cursor.isLast()) {

                while (cursor.moveToNext()) {

                    MaintenanceDetail maintenanceDetail = new MaintenanceDetail();
                    maintenanceDetail.setId(cursor.getInt(0));
                    maintenanceDetail.setMaintenanceId(cursor.getInt(1));
                    maintenanceDetail.setUploadId(cursor.getInt(2));
                    maintenanceDetail.setStatus(cursor.getString(3));
                    maintenanceDetail.setUpdatedDateAsString(cursor.getString(4));
                    maintenanceDetail.setRemark(cursor.getString(5));
                    maintenanceDetails.add(maintenanceDetail);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maintenanceDetails;
    }

    //get time maintenance detail by id
    public MaintenanceDetail getMaintenanceDetailByMainId(int maintenanceId) {

        MaintenanceDetail maintenanceDetail = null;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MAINTENANCE_DETAIL, new String[]{MAINTENANCE_DETAIL_ID, INSERT_MAINTENANCE_ID, UPLOAD_ID,
                        MAINTENANCE_STATUS_DETAIL, UPDATED_DATE, REMARK}, INSERT_MAINTENANCE_ID + "=?",
                new String[]{String.valueOf(maintenanceId)}, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            maintenanceDetail = new MaintenanceDetail(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5));

            cursor.close();
        }
        return maintenanceDetail;
    }

    //------------------------------------------------------------------------------------------------------
    //add condition maintenance
    public void addConditionMaintenance(ConditionScheduleMaintenance conditionMaintenance) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(CON_MAIN_ID, conditionMaintenance.getId());
            values.put(CON_INSERT_SCHEDULE_ID, conditionMaintenance.getInsertScheduleId());
            values.put(CON_MAIN_INSERT_ID, conditionMaintenance.getMaintenanceId());
            values.put(CON_MAIN_SCHEDULE_ID, conditionMaintenance.getScheduleId());
            values.put(CON_MAIN_DEVICE_UPLOAD_ID, conditionMaintenance.getDeviceUploadId());
            values.put(CON_MAIN_SCHEDULE_UNIT, conditionMaintenance.getScheduleUnit());
            values.put(CON_MAIN_CURRENT_UNIT, conditionMaintenance.getCurrentUnit());
            values.put(CON_MAIN_START_DATE, conditionMaintenance.getStartDateAsString());
            values.put(CON_MAIN_END_DATE, conditionMaintenance.getEndDateAsString());
            values.put(CON_MAIN_STATUS, conditionMaintenance.getStatus());
            values.put(CON_MAIN_ASSET_ID, conditionMaintenance.getAssetId());
            values.put(CON_MAIN_FLAG, conditionMaintenance.getFlag());
            values.put(CON_MAIN_UPLOAD_FLAG, conditionMaintenance.getUploadFlag());
            db.insert(TABLE_CON_MAINTENANCE, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //insert condition maintenance
    public void insertConditionMaintenance(ConditionScheduleMaintenance conditionMaintenance) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(CON_INSERT_SCHEDULE_ID, conditionMaintenance.getInsertScheduleId());
            values.put(CON_MAIN_INSERT_ID, conditionMaintenance.getMaintenanceId());
            values.put(CON_MAIN_SCHEDULE_ID, conditionMaintenance.getScheduleId());
            values.put(CON_MAIN_DEVICE_UPLOAD_ID, conditionMaintenance.getDeviceUploadId());
            values.put(CON_MAIN_SCHEDULE_UNIT, conditionMaintenance.getScheduleUnit());
            values.put(CON_MAIN_CURRENT_UNIT, conditionMaintenance.getCurrentUnit());
            values.put(CON_MAIN_START_DATE, conditionMaintenance.getStartDateAsString());
            values.put(CON_MAIN_END_DATE, conditionMaintenance.getEndDateAsString());
            values.put(CON_MAIN_STATUS, conditionMaintenance.getStatus());
            values.put(CON_MAIN_ASSET_ID, conditionMaintenance.getAssetId());
            values.put(CON_MAIN_FLAG, conditionMaintenance.getFlag());
            values.put(CON_MAIN_UPLOAD_FLAG, conditionMaintenance.getUploadFlag());
            db.insert(TABLE_CON_MAINTENANCE, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //get condition maintenance by id
    public ConditionScheduleMaintenance getConditionMaintenanceById(int id) {

        ConditionScheduleMaintenance conditionMaintenance = null;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CON_MAINTENANCE, new String[]{CON_MAIN_ID, CON_INSERT_SCHEDULE_ID, CON_MAIN_INSERT_ID, CON_MAIN_SCHEDULE_ID, CON_MAIN_DEVICE_UPLOAD_ID, CON_MAIN_SCHEDULE_UNIT,
                        CON_MAIN_CURRENT_UNIT, CON_MAIN_START_DATE, CON_MAIN_END_DATE, CON_MAIN_STATUS, CON_MAIN_ASSET_ID, CON_MAIN_FLAG, CON_MAIN_UPLOAD_FLAG}, CON_MAIN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            conditionMaintenance = new ConditionScheduleMaintenance(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3),
                    cursor.getInt(4), cursor.getDouble(5), cursor.getDouble(6), cursor.getString(7),
                    cursor.getString(8), cursor.getString(9), cursor.getInt(10), cursor.getInt(11),
                    cursor.getInt(12));

            cursor.close();
        }
        return conditionMaintenance;
    }

    //update condition maintenance
    public boolean updateConditionMaintenance(ConditionScheduleMaintenance conditionMaintenance) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CON_INSERT_SCHEDULE_ID, conditionMaintenance.getInsertScheduleId());
        values.put(CON_MAIN_INSERT_ID, conditionMaintenance.getMaintenanceId());
        values.put(CON_MAIN_SCHEDULE_ID, conditionMaintenance.getScheduleId());
        values.put(CON_MAIN_DEVICE_UPLOAD_ID, conditionMaintenance.getDeviceUploadId());
        values.put(CON_MAIN_SCHEDULE_UNIT, conditionMaintenance.getScheduleUnit());
        values.put(CON_MAIN_CURRENT_UNIT, conditionMaintenance.getCurrentUnit());
        values.put(CON_MAIN_START_DATE, conditionMaintenance.getStartDateAsString());
        values.put(CON_MAIN_END_DATE, conditionMaintenance.getEndDateAsString());
        values.put(CON_MAIN_STATUS, conditionMaintenance.getStatus());
        values.put(CON_MAIN_ASSET_ID, conditionMaintenance.getAssetId());
        values.put(CON_MAIN_FLAG, conditionMaintenance.getFlag());
        values.put(CON_MAIN_UPLOAD_FLAG, conditionMaintenance.getUploadFlag());

        return db.update(TABLE_CON_MAINTENANCE, values, CON_MAIN_ID + "=" + conditionMaintenance.getId(), null) > 0;
    }

    //get all condition maintenance by locationID
    public ArrayList<ConditionScheduleMaintenance> getAllConditionMaintenance(int locationId) {

        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<ConditionScheduleMaintenance> conditionMaintenances = null;
        try {
            conditionMaintenances = new ArrayList<>();

            String QUERY = " SELECT\n" +
                    "   con_m.id AS Id,\n" +
                    "   con_m.insert_schedule_id AS insertScheduleId,\n" +
                    "   con_m.maintenance_id AS maintenanceId,\n" +
                    "   con_m.schedule_id AS scheduleListId,\n" +
                    "   con_m.schedule_unit AS scheduleUnit,\n" +
                    "   con_m.current_unit As currentUnit,\n" +
                    "   con_m.start_date As startDate,\n" +
                    "   con_m.end_date As endDate,\n" +
                    "   con_m.status AS maintenanceStatus,\n" +
                    "   con_m.asset_id AS assetId,\n" +
                    "   con_m.flag As flag,\n" +
                    "   con_m.upload_flag As uploadFlag\n" +
                    "   FROM tbl_condition_maintenance con_m\n" +
                    "   LEFT JOIN tbl_asset a ON a.id=con_m.asset_id\n" +
                    "  LEFT JOIN tbl_schedule_unit uni_sch ON uni_sch.id=con_m.schedule_id\n" +
                    "   WHERE flag=1 AND upload_flag=0 AND a.location_id=" + locationId;

            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {

                while (cursor.moveToNext()) {

                    ConditionScheduleMaintenance conditionMaintenance = new ConditionScheduleMaintenance();
                    conditionMaintenance.setId(cursor.getInt(0));
                    conditionMaintenance.setInsertScheduleId(cursor.getInt(1));
                    conditionMaintenance.setMaintenanceId(cursor.getInt(2));
                    conditionMaintenance.setScheduleId(cursor.getInt(3));
                    conditionMaintenance.setScheduleUnit(cursor.getDouble(4));
                    conditionMaintenance.setCurrentUnit(cursor.getDouble(5));
                    conditionMaintenance.setStartDateAsString(cursor.getString(6));
                    conditionMaintenance.setEndDateAsString(cursor.getString(7));
                    conditionMaintenance.setStatus(cursor.getString(8));
                    conditionMaintenance.setAssetId(cursor.getInt(9));
                    conditionMaintenance.setFlag(cursor.getInt(10));
                    conditionMaintenance.setUploadFlag(cursor.getInt(11));
                    conditionMaintenances.add(conditionMaintenance);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return conditionMaintenances;
    }

    //get upload condition maintenance count
    public int getUploadConditionMaintenanceCount() {

        int num = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            String QUERY = "SELECT * FROM " + TABLE_CON_MAINTENANCE + " WHERE " + CON_MAIN_FLAG + "=" + 1 + " AND " + CON_MAIN_UPLOAD_FLAG + "=" + 0;
            Cursor cursor = db.rawQuery(QUERY, null);
            num = cursor.getCount();
            cursor.close();
            db.close();
            return num;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    //check condition maintenance detail finish
    public boolean checkConditionMaintenanceDetailFinish(String status, String scheduleCode, int id, int locationId) {

        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "SELECT * FROM tbl_condition_maintenance main " +
                "left join tbl_asset a on a.id =main.asset_id " +
                "left join tbl_schedule_unit uni_sch on uni_sch.id=main.insert_schedule_id " +
                "where uni_sch.unit_schedule_code = '" + scheduleCode +
                "' and status = '" + status + "' and a.id=" + id +
                " and a.location_id=" + locationId;

        Cursor cursor = db.rawQuery(QUERY, null);
        boolean exists = false;
        if (cursor.moveToFirst()) {
            exists = (cursor.getCount() > 0);
        }
        cursor.close();
        db.close();
        return exists;
    }

    //------------------------------------------------------------------------------------------------------
    //insert condition maintenance detail
    public void insertConditionMaintenanceDetail(ConditionScheduleMaintenanceDetail maintenanceDetail) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(CON_DEVICE_UPLOAD_ID, maintenanceDetail.getDeviceUploadId());
            values.put(CON_INSERT_MAINTENANCE_ID, maintenanceDetail.getMaintenanceId());
            values.put(CON_UPDATED_BY, maintenanceDetail.getUpdatedBy());
            values.put(CON_STATUS, maintenanceDetail.getStatus());
            values.put(CON_UPDATED_DATE, maintenanceDetail.getUpdatedDateAsString());
            values.put(CON_MAINTENANCE_REMARK, maintenanceDetail.getRemark());
            db.insert(TABLE_CON_MAINTENANCE_DETAIL, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //update condition maintenance detail
    public boolean updateConditionMaintenanceDetail(ConditionScheduleMaintenanceDetail maintenanceDetail) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CON_DEVICE_UPLOAD_ID, maintenanceDetail.getDeviceUploadId());
        values.put(CON_INSERT_MAINTENANCE_ID, maintenanceDetail.getMaintenanceId());
        values.put(CON_UPDATED_BY, maintenanceDetail.getUpdatedBy());
        values.put(CON_STATUS, maintenanceDetail.getStatus());
        values.put(CON_UPDATED_DATE, maintenanceDetail.getUpdatedDateAsString());
        values.put(CON_MAINTENANCE_REMARK, maintenanceDetail.getRemark());
        return db.update(TABLE_CON_MAINTENANCE_DETAIL, values, CON_MAINTENANCE_DETAIL_ID + "=" + maintenanceDetail.getId(), null) > 0;
    }


    //get condition maintenance detail by id
    public ConditionScheduleMaintenanceDetail getConditionMaintenanceDetailByMainId(int maintenanceId) {

        ConditionScheduleMaintenanceDetail conditionMaintenanceDetail = null;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CON_MAINTENANCE_DETAIL, new String[]{CON_MAINTENANCE_DETAIL_ID, CON_DEVICE_UPLOAD_ID, CON_INSERT_MAINTENANCE_ID, CON_UPDATED_BY, CON_STATUS, CON_UPDATED_DATE,
                        CON_MAINTENANCE_REMARK}, CON_INSERT_MAINTENANCE_ID + "=?",
                new String[]{String.valueOf(maintenanceId)}, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            conditionMaintenanceDetail = new ConditionScheduleMaintenanceDetail(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3),
                    cursor.getString(4), cursor.getString(5), cursor.getString(6));

            cursor.close();
        }
        return conditionMaintenanceDetail;
    }

    //get all condition maintenanceDetail
    public ArrayList<ConditionScheduleMaintenanceDetail> getAllConditionMaintenanceDetails(int maintenanceId) {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ConditionScheduleMaintenanceDetail> maintenanceDetails = null;
        try {
            maintenanceDetails = new ArrayList<>();
            String query = null;
            query = "SELECT * FROM " + TABLE_CON_MAINTENANCE_DETAIL + " WHERE " + CON_INSERT_MAINTENANCE_ID + "=" + maintenanceId;

            Cursor cursor = db.rawQuery(query, null);
            if (!cursor.isLast()) {

                while (cursor.moveToNext()) {

                    ConditionScheduleMaintenanceDetail maintenanceDetail = new ConditionScheduleMaintenanceDetail();
                    maintenanceDetail.setId(cursor.getInt(0));
                    maintenanceDetail.setDeviceUploadId(cursor.getInt(1));
                    maintenanceDetail.setMaintenanceId(cursor.getInt(2));
                    maintenanceDetail.setUpdatedBy(cursor.getInt(3));
                    maintenanceDetail.setStatus(cursor.getString(4));
                    maintenanceDetail.setUpdatedDateAsString(cursor.getString(5));
                    maintenanceDetail.setRemark(cursor.getString(6));
                    maintenanceDetails.add(maintenanceDetail);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maintenanceDetails;
    }

    //get all upload condition maintenanceDetail
    public ArrayList<ConditionScheduleMaintenanceDetail> getAllUploadConditionMaintenanceDetails(int maintenanceId) {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ConditionScheduleMaintenanceDetail> maintenanceDetails = null;
        try {
            maintenanceDetails = new ArrayList<>();
            String query = null;
            query = "SELECT * FROM " + TABLE_CON_MAINTENANCE_DETAIL + " WHERE " + CON_INSERT_MAINTENANCE_ID + "=" + maintenanceId + " AND " + CON_DEVICE_UPLOAD_ID + "=0";

            Cursor cursor = db.rawQuery(query, null);
            if (!cursor.isLast()) {

                while (cursor.moveToNext()) {

                    ConditionScheduleMaintenanceDetail maintenanceDetail = new ConditionScheduleMaintenanceDetail();
                    maintenanceDetail.setId(cursor.getInt(0));
                    maintenanceDetail.setDeviceUploadId(cursor.getInt(1));
                    maintenanceDetail.setMaintenanceId(cursor.getInt(2));
                    maintenanceDetail.setUpdatedBy(cursor.getInt(3));
                    maintenanceDetail.setStatus(cursor.getString(4));
                    maintenanceDetail.setUpdatedDateAsString(cursor.getString(5));
                    maintenanceDetail.setRemark(cursor.getString(6));
                    maintenanceDetails.add(maintenanceDetail);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maintenanceDetails;
    }

    //get all upload fail condition maintenanceDetail
    public ArrayList<ConditionScheduleMaintenanceDetail> getAllUploadFailConditionMaintenanceDetails(int maintenanceId) {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ConditionScheduleMaintenanceDetail> maintenanceDetails = null;
        try {
            maintenanceDetails = new ArrayList<>();
            String query = null;
            query = "SELECT * FROM " + TABLE_CON_MAINTENANCE_DETAIL + " WHERE " + CON_INSERT_MAINTENANCE_ID + "=" + maintenanceId + " AND " + CON_DEVICE_UPLOAD_ID + "!=0";

            Cursor cursor = db.rawQuery(query, null);
            if (!cursor.isLast()) {

                while (cursor.moveToNext()) {

                    ConditionScheduleMaintenanceDetail maintenanceDetail = new ConditionScheduleMaintenanceDetail();
                    maintenanceDetail.setId(cursor.getInt(0));
                    maintenanceDetail.setDeviceUploadId(cursor.getInt(1));
                    maintenanceDetail.setMaintenanceId(cursor.getInt(2));
                    maintenanceDetail.setUpdatedBy(cursor.getInt(3));
                    maintenanceDetail.setStatus(cursor.getString(4));
                    maintenanceDetail.setUpdatedDateAsString(cursor.getString(5));
                    maintenanceDetail.setRemark(cursor.getString(6));
                    maintenanceDetails.add(maintenanceDetail);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maintenanceDetails;
    }

    //------------------------------------------------------------------------------------------------------
    //insert dum image
    public void addDumImage(DumImage dumImage) {

        SQLiteDatabase db = this.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(DUM_IMAGE_MAINTENANCE_DETAIL_ID, dumImage.getMaintenanceDetailId());
            values.put(DUM_IMAGE_PATH, dumImage.getImagePath());
            values.put(DUM_IMAGE_FLAG, dumImage.getFlag());
            db.insert(TABLE_DUM_IMAGE, null, values);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //update dum image
    public boolean updateDumImage(DumImage dumImage) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DUM_IMAGE_MAINTENANCE_DETAIL_ID, dumImage.getMaintenanceDetailId());
        values.put(DUM_IMAGE_PATH, dumImage.getImagePath());
        values.put(DUM_IMAGE_FLAG, dumImage.getFlag());
        return db.update(TABLE_DUM_IMAGE, values, DUM_IMAGE_ID + "=" + dumImage.getId(), null) > 0;
    }

    //get all dum image
    public ArrayList<DumImage> getAllDumImage() {

        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<DumImage> imageList = null;
        try {
            imageList = new ArrayList<>();
            String QUERY = "SELECT * FROM " + TABLE_DUM_IMAGE + " WHERE " + DUM_IMAGE_FLAG + "!=3";
            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {

                while (cursor.moveToNext()) {

                    DumImage image = new DumImage();
                    image.setId(cursor.getInt(0));
                    image.setMaintenanceDetailId(cursor.getInt(1));
                    image.setImagePath(cursor.getString(2));
                    image.setFlag(cursor.getInt(3));

                    imageList.add(image);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageList;
    }

    //get dum image by maintenance Id
    public ArrayList<DumImage> getDumImageByDetailId(int detailId) {

        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<DumImage> dumImages = null;
        try {
            dumImages = new ArrayList<>();
            String query = null;
            query = "SELECT * FROM " + TABLE_DUM_IMAGE + " WHERE " + DUM_IMAGE_MAINTENANCE_DETAIL_ID + "=" + detailId;

            Cursor cursor = db.rawQuery(query, null);
            if (!cursor.isLast()) {

                while (cursor.moveToNext()) {

                    DumImage dumImage = new DumImage();
                    dumImage.setId(cursor.getInt(0));
                    dumImage.setMaintenanceDetailId(cursor.getInt(1));
                    dumImage.setImagePath(cursor.getString(2));
                    dumImage.setFlag(cursor.getInt(3));
                    dumImages.add(dumImage);
                }
                cursor.close();
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dumImages;
    }

    //delete dum Image by image id
    public void deleteDumImageByImageId(int maintenanceDetailId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DUM_IMAGE, DUM_IMAGE_ID + "=" + maintenanceDetailId, null);
    }

    //delete all image
    public void deleteAllDumImage() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DUM_IMAGE, null);
        db.delete(TABLE_DUM_IMAGE, null, null);
        cursor.close();
    }

    //------------------------------------------------------------------------------------------------------
    //check schedule exists
    public boolean checkScheduleExists(int scheduleId) {

        SQLiteDatabase db;
        db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SCHEDULE + " WHERE " + SCHEDULE_ID + "='" + scheduleId + "'", null);
        boolean exists = false;
        if (cursor.moveToFirst()) {
            exists = (cursor.getCount() > 0);
        }
        cursor.close();

        return exists;
    }


    //get current date
    private String getDateTime() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date date = new Date();

        return dateFormat.format(date);
    }

    //delete all location
    public void deleteAllLocations() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_LOCATION, null);
        db.delete(TABLE_LOCATION, null, null);
        cursor.close();
    }

    //delete all assets
    public void deleteAllAssets() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FIXED_ASSET, null);
        db.delete(TABLE_FIXED_ASSET, null, null);
        cursor.close();
    }

    //delete download info
    public void deleteInfo() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DOWNLOAD_INFO, null);
        db.delete(TABLE_DOWNLOAD_INFO, null, null);
        cursor.close();
    }

    //delete schedule
    public void deleteAllSchedule() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SCHEDULE, null);
        db.delete(TABLE_SCHEDULE, null, null);
        cursor.close();
    }

    //delete maintenance
    public void deleteAllMaintenance() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MAINTENANCE, null);
        db.delete(TABLE_MAINTENANCE, null, null);
        cursor.close();
    }

    //delete maintenance detail
    public void deleteAllMaintenanceDetails() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MAINTENANCE_DETAIL, null);
        db.delete(TABLE_MAINTENANCE_DETAIL, null, null);
        cursor.close();
    }

    //delete maintenance
    public void deleteAllConditionMaintenance() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CON_MAINTENANCE, null);
        db.delete(TABLE_CON_MAINTENANCE, null, null);
        cursor.close();
    }

    //delete maintenance detail
    public void deleteAllConditionMaintenanceDetails() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CON_MAINTENANCE_DETAIL, null);
        db.delete(TABLE_CON_MAINTENANCE_DETAIL, null, null);
        cursor.close();
    }

    //delete unit schedule condition
    public void deleteUnitScheduleCondition() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SCHEDULE_UNIT, null);
        db.delete(TABLE_SCHEDULE_UNIT, null, null);
        cursor.close();
    }


    //delete asset condition
    public void deleteAssetCondition() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ASSET_CONDITION, null);
        db.delete(TABLE_ASSET_CONDITION, null, null);
        cursor.close();
    }

    //delete asset condition unit
    public void deleteAssetConditionUnit() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ASSET_CONDITION_UNIT, null);
        db.delete(TABLE_ASSET_CONDITION_UNIT, null, null);
        cursor.close();
    }

    //delete schedule item
    public void deleteAllScheduleItem() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SCHEDULE_ITEM, null);
        db.delete(TABLE_SCHEDULE_ITEM, null, null);
        cursor.close();
    }

    //delete schedule list
    public void deleteAllScheduleList() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SCHEDULE_LIST, null);
        db.delete(TABLE_SCHEDULE_LIST, null, null);
        cursor.close();
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

    public void exportDB() {

        Calendar c = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String backupDate = dateFormat.format(c.getTime());

        try {
            String filePath = Environment.getExternalStorageDirectory() + "/Maintenance_Backup/";
            File sd = new File(filePath);
            if (!sd.exists()) {
                sd.mkdirs();
            }
            if (sd.canWrite()) {
                String currentDBPath = DB_PATH + DB_NAME;
                String backupDBPath = backupDate + "_ams_maintenance.db";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Backup DB,ERROR", Toast.LENGTH_SHORT).show();
        }
    }

    //get all scheduleData
    public List<Object> getAllScheduleObjects(int assetId) {

        SQLiteDatabase db = this.getReadableDatabase();

        List<Object> scheduleLists = new ArrayList<>();
        Map<String, String> schedule;

        try {

            String QUERY = "SELECT\n" +
                    "            sch.id AS scheduleId,\n" +
                    "            sch_item.id AS scheduleItemId,\n" +
                    "            sch_list.id AS scheduleListId,\n" +
                    "            sch.code AS scheduleCode,\n" +
                    "            sch.name AS scheduleName,\n" +
                    "            sch.time AS scheduleTime,\n" +
                    "            sch_list.schedule_date AS scheduleDate\n" +
                    "        FROM tbl_schedule_list sch_list\n" +
                    "            LEFT JOIN tbl_schedule_item sch_item ON sch_item.id=sch_list.schedule_item_id\n" +
                    "            LEFT JOIN tbl_schedule sch ON sch.id=sch_item.schedule_id\n" +
                    "        WHERE  sch_list.id NOT IN (SELECT schedule_list_id FROM tbl_maintenance) AND sch_item.asset_id= " + assetId;

            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {

                    schedule = new HashMap<>();

                    schedule.put("scheduleId", cursor.getString(0));
                    schedule.put("scheduleItemId", cursor.getString(1));
                    schedule.put("scheduleListId", cursor.getString(2));
                    schedule.put("scheduleCode", cursor.getString(3));
                    schedule.put("scheduleName", cursor.getString(4));
                    schedule.put("scheduleTime", cursor.getString(5));
                    schedule.put("scheduleDate", cursor.getString(6));
                    scheduleLists.add(schedule);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return scheduleLists;
    }

    //get all scheduleData
    public List<Object> getAllUnitScheduleObjects(int assetId) {

        SQLiteDatabase db = this.getReadableDatabase();

        List<Object> scheduleLists = new ArrayList<>();
        Map<String, String> schedule;

        try {

            String QUERY = "SELECT\n" +
                    "            uni_sch.id AS scheduleUnitId,\n" +
                    "            uni_sch.schedule_id AS unitScheduleId,\n" +
                    "            uni_sch.schedule_name AS scheduleName,\n" +
                    "            uni_sch.unit_type AS unitType,\n" +
                    "            uni_sch.unit_schedule_code AS unitScheduleCode,\n" +
                    "            uni_sch.condition_name AS conditionName,\n" +
                    "            uni_sch.schedule_unit AS scheduleUnit\n" +
                    "        FROM tbl_schedule_unit uni_sch\n" +
                    "        WHERE uni_sch.id NOT IN (SELECT insert_schedule_id FROM tbl_condition_maintenance) AND uni_sch.asset_id= " + assetId;

            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {

                    schedule = new HashMap<>();

                    schedule.put("scheduleUnitId", cursor.getString(0));
                    schedule.put("unitScheduleId", cursor.getString(1));
                    schedule.put("scheduleName", cursor.getString(2));
                    schedule.put("unitType", cursor.getString(3));
                    schedule.put("unitScheduleCode", cursor.getString(4));
                    schedule.put("conditionName", cursor.getString(5));
                    schedule.put("scheduleUnit", cursor.getString(6));

                    scheduleLists.add(schedule);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return scheduleLists;
    }


    //get all maintenanceData
    public List<Object> getUnitMaintenanceObjects(int assetId) {

        SQLiteDatabase db = this.getReadableDatabase();

        List<Object> scheduleLists = new ArrayList<>();
        Map<String, String> schedule;

        try {

            String QUERY = "    SELECT\n" +

                    "    uni_sch.id AS scheduleUnitId,\n" +
                    "    uni_sch.schedule_id AS scheduleId,\n" +
                    "    uni_sch.schedule_name AS scheduleName,\n" +
                    "    uni_sch.unit_type AS unitType,\n" +
                    "    uni_sch.unit_schedule_code AS scheduleCode,\n" +
                    "    uni_sch.condition_name AS conditionName,\n" +
                    "    uni_sch.schedule_unit AS scheduleUnit,\n" +
                    "    main.status AS maintenanceStatus,\n" +
                    "    main.id AS maintenanceId\n" +
                    "  FROM tbl_maintenance main\n" +
                    "    LEFT JOIN tbl_schedule_unit uni_sch ON uni_sch.id=main.schedule_list_id\n" +
                    "   WHERE main.asset_id=" + assetId;

            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {

                    schedule = new HashMap<>();

                    schedule.put("scheduleUnitId", cursor.getString(0));
                    schedule.put("scheduleId", cursor.getString(1));
                    schedule.put("scheduleName", cursor.getString(2));
                    schedule.put("unitType", cursor.getString(3));
                    schedule.put("scheduleCode", cursor.getString(4));
                    schedule.put("conditionName", cursor.getString(5));
                    schedule.put("scheduleUnit", cursor.getString(6));
                    schedule.put("maintenanceStatus", cursor.getString(7));
                    schedule.put("maintenanceId", cursor.getString(8));
                    scheduleLists.add(schedule);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return scheduleLists;
    }

    //get all maintenanceData
    public List<Object> getAllMaintenanceObjects(int assetId) {

        SQLiteDatabase db = this.getReadableDatabase();

        List<Object> scheduleLists = new ArrayList<>();
        Map<String, String> schedule;

        try {

            String QUERY = "    SELECT\n" +

                    "    sch.id AS scheduleId,\n" +
                    "    sch_item.id AS scheduleItemId,\n" +
                    "    sch_list.id AS scheduleListId,\n" +
                    "    sch.code AS scheduleCode,\n" +
                    "    sch.name AS scheduleName,\n" +
                    "    sch.time AS scheduleTime,\n" +
                    "    sch_list.schedule_date AS scheduleDate,\n" +
                    "    main.status AS maintenanceStatus,\n" +
                    "    main.id AS maintenanceId\n" +
                    "  FROM tbl_maintenance main\n" +
                    "    LEFT JOIN tbl_schedule_list sch_list ON sch_list.id=main.schedule_list_id\n" +
                    "    LEFT JOIN tbl_schedule_item sch_item ON sch_item.id=sch_list.schedule_item_id\n" +
                    "    LEFT JOIN tbl_schedule sch ON sch.id=sch_item.schedule_id\n" +
                    "   WHERE main.asset_id=" + assetId;

            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {

                    schedule = new HashMap<>();

                    schedule.put("scheduleId", cursor.getString(0));
                    schedule.put("scheduleItemId", cursor.getString(1));
                    schedule.put("scheduleListId", cursor.getString(2));
                    schedule.put("scheduleCode", cursor.getString(3));
                    schedule.put("scheduleName", cursor.getString(4));
                    schedule.put("scheduleTime", cursor.getString(5));
                    schedule.put("scheduleDate", cursor.getString(6));
                    schedule.put("maintenanceStatus", cursor.getString(7));
                    schedule.put("maintenanceId", cursor.getString(8));
                    scheduleLists.add(schedule);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return scheduleLists;
    }


    //get all condition maintenanceData
    public List<Object> getAllConditionMaintenanceObjects(int assetId) {

        SQLiteDatabase db = this.getReadableDatabase();

        List<Object> scheduleLists = new ArrayList<>();
        Map<String, String> schedule;

        try {

            String QUERY = " SELECT\n" +
                    "    uni_sch.id AS scheduleUnitId,\n" +
                    "    uni_sch.schedule_id AS unitScheduleId,\n" +
                    "    uni_sch.schedule_name AS unitScheduleName,\n" +
                    "    uni_sch.unit_type AS unitType,\n" +
                    "    uni_sch.unit_schedule_code AS unitScheduleCode,\n" +
                    "    uni_sch.condition_name AS conditionName,\n" +
                    "    uni_sch.schedule_unit AS scheduleUnit,\n" +
                    "    con_main.status AS maintenanceStatus,\n" +
                    "    con_main.id AS maintenanceId\n" +
                    "  FROM tbl_condition_maintenance con_main\n" +
                    "  LEFT JOIN tbl_schedule_unit uni_sch ON uni_sch.id=con_main.insert_schedule_id\n" +
                    "   WHERE con_main.asset_id=" + assetId;

            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {

                    schedule = new HashMap<>();

                    schedule.put("scheduleUnitId", cursor.getString(0));
                    schedule.put("unitScheduleId", cursor.getString(1));
                    schedule.put("unitScheduleName", cursor.getString(2));
                    schedule.put("unitType", cursor.getString(3));
                    schedule.put("unitScheduleCode", cursor.getString(4));
                    schedule.put("conditionName", cursor.getString(5));
                    schedule.put("scheduleUnit", cursor.getString(6));
                    schedule.put("maintenanceStatus", cursor.getString(7));
                    schedule.put("maintenanceId", cursor.getString(8));
                    scheduleLists.add(schedule);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return scheduleLists;
    }


    public List<Object> getTimeScheduleUploadObjects(int locationId) {

        SQLiteDatabase db = this.getReadableDatabase();

        List<Object> scheduleLists = new ArrayList<>();
        Map<String, String> schedule;

        try {

            String QUERY = " SELECT\n" +
                    "\t    a.fa_number AS faNumber,\n" +
                    "\t    a.item_name AS itemName,\n" +
                    "\t    sl.schedule_date AS scheduleDate,\n" +
                    "\t    sl.id AS scheduleListId,\n" +
                    "\t    si.id AS scheduleItemId,\n" +
                    "\t    s.code AS scheduleCode,\n" +
                    "\t    s.name AS scheduleName,\n" +
                    "\t    s.id AS scheduleId,\n" +
                    "\t    s.time AS scheduleTime,\n" +
                    "\t    m.status AS maintenanceStatus,\n" +
                    "\t    m.id AS maintenanceId\n" +
                    "FROM tbl_maintenance m\n" +
                    "\t     LEFT JOIN tbl_asset a ON a.id=m.asset_id\n" +
                    "\t     LEFT JOIN tbl_schedule_list sl ON sl.id=m.schedule_list_id\n" +
                    "\t     LEFT JOIN tbl_schedule_item si ON si.id=sl.schedule_item_id\n" +
                    "\t     LEFT JOIN tbl_schedule s ON s.id=si.schedule_id\n" +
                    "WHERE flag=1 AND upload_flag=0 AND a.location_id=" + locationId;

            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {

                    schedule = new HashMap<>();

                    schedule.put("faNumber", cursor.getString(0));
                    schedule.put("itemName", cursor.getString(1));
                    schedule.put("scheduleDate", cursor.getString(2));
                    schedule.put("scheduleListId", cursor.getString(3));
                    schedule.put("scheduleItemId", cursor.getString(4));
                    schedule.put("scheduleCode", cursor.getString(5));
                    schedule.put("scheduleName", cursor.getString(6));
                    schedule.put("scheduleId", cursor.getString(7));
                    schedule.put("scheduleTime", cursor.getString(8));
                    schedule.put("maintenanceStatus", cursor.getString(9));
                    schedule.put("maintenanceId", cursor.getString(10));
                    scheduleLists.add(schedule);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return scheduleLists;
    }


    public List<Object> getConditionScheduleUploadObjects(int locationId) {

        SQLiteDatabase db = this.getReadableDatabase();

        List<Object> scheduleLists = new ArrayList<>();
        Map<String, String> schedule;

        try {

            String QUERY = " SELECT\n" +
                    "\t    a.fa_number AS faNumber,\n" +
                    "\t    a.item_name AS itemName,\n" +
                    "\t    uni_sch.id AS scheduleUnitId,\n" +
                    "\t    uni_sch.schedule_id AS unitScheduleId,\n" +
                    "\t    uni_sch.schedule_name AS unitScheduleName,\n" +
                    "\t    uni_sch.unit_type AS unitType,\n" +
                    "\t    uni_sch.unit_schedule_code AS unitScheduleCode,\n" +
                    "\t    uni_sch.condition_name AS conditionName,\n" +
                    "\t    uni_sch.schedule_unit AS scheduleUnit,\n" +
                    "\t    con_m.status AS maintenanceStatus,\n" +
                    "\t    con_m.id AS maintenanceId\n" +
                    "FROM tbl_condition_maintenance con_m\n" +
                    "\t     LEFT JOIN tbl_asset a ON a.id=con_m.asset_id\n" +
                    "\t     LEFT JOIN tbl_schedule_unit uni_sch ON uni_sch.id=con_m.insert_schedule_id\n" +
                    "WHERE flag=1 AND upload_flag=0 AND a.location_id=" + locationId;

            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {

                    schedule = new HashMap<>();

                    schedule.put("faNumber", cursor.getString(0));
                    schedule.put("itemName", cursor.getString(1));
                    schedule.put("scheduleUnitId", cursor.getString(2));
                    schedule.put("unitScheduleId", cursor.getString(3));
                    schedule.put("unitScheduleName", cursor.getString(4));
                    schedule.put("unitType", cursor.getString(5));
                    schedule.put("unitScheduleCode", cursor.getString(6));
                    schedule.put("conditionName", cursor.getString(7));
                    schedule.put("scheduleUnit", cursor.getString(8));
                    schedule.put("maintenanceStatus", cursor.getString(9));
                    schedule.put("maintenanceId", cursor.getString(10));
                    scheduleLists.add(schedule);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return scheduleLists;
    }


    public List<Object> getConditionUnitUploadObjects(int locationId) {

        SQLiteDatabase db = this.getReadableDatabase();

        List<Object> scheduleLists = new ArrayList<>();
        Map<String, String> schedule;

        try {

            String QUERY = " SELECT\n" +
                    "\t    asset.fa_number AS faNumber,\n" +
                    "\t    asset.item_name AS itemName,\n" +
                    "\t    con_unit.unit AS unit,\n" +
                    "\t    con_unit.condition_name AS conditionUnitName,\n" +
                    "\t    con_unit.unit_type AS conditionUnitType\n" +
                    "FROM tbl_asset_condition_unit con_unit\n" +
                    "\t     LEFT JOIN tbl_asset asset ON asset.id=con_unit.asset_id\n" +
                    "WHERE flag=1 AND upload_flag=0 AND asset.location_id=" + locationId;

            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {

                    schedule = new HashMap<>();

                    schedule.put("faNumber", cursor.getString(0));
                    schedule.put("itemName", cursor.getString(1));
                    schedule.put("unit", cursor.getString(2));
                    schedule.put("conditionUnitName", cursor.getString(3));
                    schedule.put("conditionUnitType", cursor.getString(4));
                    scheduleLists.add(schedule);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return scheduleLists;
    }

    //get condition unit upload history
    public List<Object> getAllUploadedConditionData() {

        SQLiteDatabase db = this.getReadableDatabase();

        List<Object> scheduleLists = new ArrayList<>();
        Map<String, String> schedule;

        try {

            String QUERY = " SELECT\n" +
                    "\t    asset.fa_number AS faNumber,\n" +
                    "\t    asset.item_name AS itemName,\n" +
                    "\t    con_unit.unit AS unit,\n" +
                    "\t    con_unit.condition_name AS conditionUnitName,\n" +
                    "\t    con_unit.unit_type AS conditionUnitType\n" +
                    "FROM tbl_asset_condition_unit con_unit\n" +
                    "\t     LEFT JOIN tbl_asset asset ON asset.id=con_unit.asset_id\n" +
                    "\t     LEFT JOIN tbl_asset_condition condition ON condition.id=con_unit.condition_id\n" +
                    "WHERE flag=1 AND upload_flag=1;";

            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {

                    schedule = new HashMap<>();
                    schedule.put("faNumber", cursor.getString(0));
                    schedule.put("itemName", cursor.getString(1));
                    schedule.put("unit", cursor.getString(2));
                    schedule.put("conditionUnitName", cursor.getString(3));
                    schedule.put("conditionUnitType", cursor.getString(4));
                    scheduleLists.add(schedule);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scheduleLists;
    }


    //get time schedule upload history
    public List<Object> getAllUploadedScheduleData() {

        SQLiteDatabase db = this.getReadableDatabase();

        List<Object> scheduleLists = new ArrayList<>();
        Map<String, String> schedule;

        try {

            String QUERY = " SELECT \n" +
                    "\t    a.fa_number AS faNumber,\n" +
                    "\t    a.item_name AS itemName,\n" +
                    "\t    sl.schedule_date AS scheduleDate,\n" +
                    "\t    sl.id AS scheduleListId,\n" +
                    "\t    si.id AS scheduleItemId,\n" +
                    "\t    s.code AS scheduleCode,\n" +
                    "\t    s.name AS scheduleName,\n" +
                    "\t    s.id AS scheduleId,\n" +
                    "\t    s.time AS scheduleTime,\n" +
                    "\t    m.status AS maintenanceStatus,\n" +
                    "\t    m.id AS maintenanceId\n" +
                    "FROM tbl_maintenance m\n" +
                    "\t     LEFT JOIN tbl_asset a ON a.id=m.asset_id\n" +
                    "\t     LEFT JOIN tbl_schedule_list sl ON sl.id=m.schedule_list_id\n" +
                    "\t     LEFT JOIN tbl_schedule_item si ON si.id=sl.schedule_item_id\n" +
                    "\t     LEFT JOIN tbl_schedule s ON s.id=si.schedule_id\n" +
                    "   WHERE flag=1 AND upload_flag=1;";

            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {

                    schedule = new HashMap<>();

                    schedule.put("faNumber", cursor.getString(0));
                    schedule.put("itemName", cursor.getString(1));
                    schedule.put("scheduleDate", cursor.getString(2));
                    schedule.put("scheduleListId", cursor.getString(3));
                    schedule.put("scheduleItemId", cursor.getString(4));
                    schedule.put("scheduleCode", cursor.getString(5));
                    schedule.put("scheduleName", cursor.getString(6));
                    schedule.put("scheduleId", cursor.getString(7));
                    schedule.put("scheduleTime", cursor.getString(8));
                    schedule.put("maintenanceStatus", cursor.getString(9));
                    schedule.put("maintenanceId", cursor.getString(10));
                    scheduleLists.add(schedule);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scheduleLists;
    }


    //get condition schedule upload history
    public List<Object> getAllUploadedConditionScheduleData() {

        SQLiteDatabase db = this.getReadableDatabase();

        List<Object> scheduleLists = new ArrayList<>();
        Map<String, String> schedule;

        try {

            String QUERY = " SELECT\n" +
                    "\t    a.fa_number AS faNumber,\n" +
                    "\t    a.item_name AS itemName,\n" +
                    "\t    uni_sch.id AS scheduleUnitId,\n" +
                    "\t    uni_sch.schedule_id AS unitScheduleId,\n" +
                    "\t    uni_sch.schedule_name AS unitScheduleName,\n" +
                    "\t    uni_sch.unit_type AS unitType,\n" +
                    "\t    uni_sch.unit_schedule_code AS unitScheduleCode,\n" +
                    "\t    uni_sch.condition_name AS conditionName,\n" +
                    "\t    uni_sch.schedule_unit AS scheduleUnit,\n" +
                    "\t    con_m.status AS maintenanceStatus,\n" +
                    "\t    con_m.id AS maintenanceId\n" +
                    "FROM tbl_condition_maintenance con_m\n" +
                    "\t     LEFT JOIN tbl_asset a ON a.id=con_m.asset_id\n" +
                    "\t     LEFT JOIN tbl_schedule_unit uni_sch ON uni_sch.schedule_id=con_m.schedule_id\n" +
                    "WHERE flag=1 AND upload_flag=1;";

            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {

                    schedule = new HashMap<>();

                    schedule.put("faNumber", cursor.getString(0));
                    schedule.put("itemName", cursor.getString(1));
                    schedule.put("scheduleUnitId", cursor.getString(2));
                    schedule.put("unitScheduleId", cursor.getString(3));
                    schedule.put("unitScheduleName", cursor.getString(4));
                    schedule.put("unitType", cursor.getString(5));
                    schedule.put("unitScheduleCode", cursor.getString(6));
                    schedule.put("conditionName", cursor.getString(7));
                    schedule.put("scheduleUnit", cursor.getString(8));
                    schedule.put("maintenanceStatus", cursor.getString(9));
                    schedule.put("maintenanceId", cursor.getString(10));
                    scheduleLists.add(schedule);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scheduleLists;
    }

    public List<String> getMaintenanceId() {

        SQLiteDatabase db = this.getReadableDatabase();

        List<String> scheduleLists = new ArrayList<>();
        String lastMaintenanceId;

        try {
            String QUERY = "  SELECT   \n" +
                    "\t           main.seq As lastMaintenanceId\n" +
                    "      FROM sqlite_sequence main WHERE main.name='tbl_maintenance';";

            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    lastMaintenanceId = cursor.getString(0);
                    scheduleLists.add(lastMaintenanceId);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scheduleLists;
    }

    public List<String> getConditionMaintenanceId() {

        SQLiteDatabase db = this.getReadableDatabase();

        List<String> scheduleLists = new ArrayList<>();
        String lastMaintenanceId;

        try {
            String QUERY = "  SELECT   \n" +
                    "\t           main.seq As lastConditionMaintenanceId\n" +
                    "      FROM sqlite_sequence main WHERE main.name='tbl_condition_maintenance';";

            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    lastMaintenanceId = cursor.getString(0);
                    scheduleLists.add(lastMaintenanceId);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scheduleLists;
    }


    public List<String> getMaintenanceDetailId() {

        SQLiteDatabase db = this.getReadableDatabase();

        List<String> scheduleLists = new ArrayList<>();
        String lastMaintenanceDetailId;

        try {
            String QUERY = "  SELECT   \n" +
                    "\t           main.seq As lastMaintenanceDetailId\n" +
                    "      FROM sqlite_sequence main WHERE main.name='tbl_maintenance_detail';";

            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    lastMaintenanceDetailId = cursor.getString(0);
                    scheduleLists.add(lastMaintenanceDetailId);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scheduleLists;
    }

    public List<String> getConditionMaintenanceDetailId() {

        SQLiteDatabase db = this.getReadableDatabase();

        List<String> scheduleLists = new ArrayList<>();
        String lastMaintenanceDetailId;

        try {
            String QUERY = "  SELECT   \n" +
                    "\t           main.seq As lastMaintenanceDetailId\n" +
                    "      FROM sqlite_sequence main WHERE main.name='tbl_condition_maintenance_detail';";

            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    lastMaintenanceDetailId = cursor.getString(0);
                    scheduleLists.add(lastMaintenanceDetailId);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scheduleLists;
    }

    public List<String> getUnitScheduleId() {

        SQLiteDatabase db = this.getReadableDatabase();

        List<String> scheduleLists = new ArrayList<>();
        String lastScheduleId;

        try {
            String QUERY = "  SELECT   \n" +
                    "\t           main.seq As lastScheduleId\n" +
                    "      FROM sqlite_sequence main WHERE main.name='tbl_schedule_unit';";

            Cursor cursor = db.rawQuery(QUERY, null);
            if (!cursor.isLast()) {
                while (cursor.moveToNext()) {
                    lastScheduleId = cursor.getString(0);
                    scheduleLists.add(lastScheduleId);
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scheduleLists;
    }
}

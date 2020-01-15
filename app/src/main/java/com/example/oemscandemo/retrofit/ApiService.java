package com.example.oemscandemo.retrofit;

import com.example.oemscandemo.model.DeviceInfo;

import java.util.Map;

import retrofit2.Callback;

public class ApiService {
    ServiceApi api = ServiceGenerator.createService(ServiceApi.class);

    //server Connection Test
    public void connectionTest(Callback<Void> callback) {
        api.serverConnection(Constant.APP_TYPE).enqueue(callback);
    }

    //login
    public void login(Map<String, Object> request, Callback<Map<String, Object>> callback) {
        if (request != null) {
            api.login(request).enqueue(callback);
        }
    }

    //check register device
    public void checkRegister(DeviceInfo device, Callback<Map<String, Object>> callback) {
        if (device != null) {
            api.checkRegister(Constant.APP_TYPE, device).enqueue(callback);
        }
    }

    //register device info
    public void register(String loginId, String password, DeviceInfo device, Callback<Map<String, Object>> callback) {
        if (device != null && loginId != null && password != null) {
            api.deviceRegister(Constant.APP_TYPE, loginId, password, device).enqueue(callback);
        }
    }

    //check license
    public void checkLicense(int deviceAppId, String loginId, String password,
                             DeviceInfo device, Callback<Map<String, Object>> callback) {
        if (deviceAppId != 0 && loginId != null && password != null && device != null) {
            api.checkLicense(Constant.APP_TYPE, deviceAppId, true, loginId, password, device).enqueue(callback);
        }
    }

    //license request
    public void license(int deviceAppId, String loginId, String password, DeviceInfo device, Callback<Map<String, Object>> callback) {
        if (deviceAppId != 0 && loginId != null && password != null && device != null) {
            api.licenseRequest(Constant.APP_TYPE, deviceAppId, loginId, password, device).enqueue(callback);
        }
    }

    //location search
    public void locationSearch(Map<String, Object> request, Callback<Map<String, Object>> callback) {
        if (request != null) {
            api.locationSearch(request).enqueue(callback);
        }
    }

    //get asset by location
    public void assetSearch(Map<String, Object> request, Callback<Map<String, Object>> callback) {
        if (request != null) {
            api.getAssetByLocation(request).enqueue(callback);
        }
    }

    //get time schedule
    public void timeSchedule(Map<String, Object> request, Callback<Map<String, Object>> callback) {
        if (request != null) {
            api.timeSchedule(request).enqueue(callback);
        }
    }

    //get conditional schedule
    public void conditionSchedule(Map<String, Object> request, Callback<Map<String, Object>> callback) {
        if (request != null) {
            api.conditionalSchedule(request).enqueue(callback);
        }
    }

    //get condition
    public void conditionDownload(Map<String, Object> request, Callback<Map<String, Object>> callback) {
        if (request != null) {
            api.conditionDownload(request).enqueue(callback);
        }
    }

    //time Maintenance download
    public void timeMaintenanceDownload(Map<String, Object> request, Callback<Map<String, Object>> callback) {
        if (request != null) {
            api.timeMaintenanceDownload(request).enqueue(callback);
        }
    }

    //conditional Maintenance download
    public void conditionMaintenanceDownload(Map<String, Object> request, Callback<Map<String, Object>> callback) {
        if (request != null) {
            api.conditionMaintenanceDownload(request).enqueue(callback);
        }
    }

    //upload file start
    public void uploadFileStart(Map<String, Object> request, Callback<Map<String, Object>> callback) {
        if (request != null) {
            api.uploadStart(request).enqueue(callback);
        }
    }

    //time Maintenance upload
    public void timeMaintenanceUpload(Map<String, Object> request, Callback<Map<String, Object>> callback) {
        if (request != null) {
            api.timeMaintenanceUpload(request).enqueue(callback);
        }
    }

    //conditional Maintenance upload
    public void conditionMaintenanceUpload(Map<String, Object> request, Callback<Map<String, Object>> callback) {
        if (request != null) {
            api.conditionMaintenanceUpload(request).enqueue(callback);
        }
    }

    //conditional upload
    public void conditionUpload(Map<String, Object> request, Callback<Map<String, Object>> callback) {
        if (request != null) {
            api.conditionUpload(request).enqueue(callback);
        }
    }

    //upload file end
    public void uploadFileEnd(Map<String, Object> request, Callback<Map<String, Object>> callback) {
        if (request != null) {
            api.uploadEnd(request).enqueue(callback);
        }
    }
}

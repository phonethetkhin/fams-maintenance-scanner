package com.example.oemscandemo.model;

import java.util.List;

public class ConditionScheduleMaintenance {
    private int id;
    private int insertScheduleId;
    private int maintenanceId;
    private int scheduleId;
    private int deviceUploadId;
    private double scheduleUnit;
    private double currentUnit;
    private String startDateAsString;
    private String endDateAsString;
    private String status;
    private int assetId;
    private List<ConditionScheduleMaintenanceDetail> detailList;
    private int flag;
    private int uploadFlag;

    public ConditionScheduleMaintenance() {
    }

    public ConditionScheduleMaintenance(int id, int insertScheduleId, int maintenanceId, int scheduleId, int deviceUploadId,
                                        double scheduleUnit, double currentUnit, String startDateAsString, String endDateAsString, String status,
                                        int assetId, int flag, int uploadFlag) {
        this.id = id;
        this.insertScheduleId = insertScheduleId;
        this.maintenanceId = maintenanceId;
        this.scheduleId = scheduleId;
        this.deviceUploadId = deviceUploadId;
        this.scheduleUnit = scheduleUnit;
        this.currentUnit = currentUnit;
        this.startDateAsString = startDateAsString;
        this.endDateAsString = endDateAsString;
        this.status = status;
        this.assetId = assetId;
        this.flag = flag;
        this.uploadFlag = uploadFlag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInsertScheduleId() {
        return insertScheduleId;
    }

    public void setInsertScheduleId(int insertScheduleId) {
        this.insertScheduleId = insertScheduleId;
    }

    public int getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(int maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getDeviceUploadId() {
        return deviceUploadId;
    }

    public void setDeviceUploadId(int deviceUploadId) {
        this.deviceUploadId = deviceUploadId;
    }

    public double getScheduleUnit() {
        return scheduleUnit;
    }

    public void setScheduleUnit(double scheduleUnit) {
        this.scheduleUnit = scheduleUnit;
    }

    public double getCurrentUnit() {
        return currentUnit;
    }

    public void setCurrentUnit(double currentUnit) {
        this.currentUnit = currentUnit;
    }

    public String getStartDateAsString() {
        return startDateAsString;
    }

    public void setStartDateAsString(String startDateAsString) {
        this.startDateAsString = startDateAsString;
    }

    public String getEndDateAsString() {
        return endDateAsString;
    }

    public void setEndDateAsString(String endDateAsString) {
        this.endDateAsString = endDateAsString;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getUploadFlag() {
        return uploadFlag;
    }

    public void setUploadFlag(int uploadFlag) {
        this.uploadFlag = uploadFlag;
    }
}

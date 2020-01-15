package com.example.oemscandemo.model;

import java.util.List;

public class Maintenance {
    private int maintenanceId;
    private int id;
    private int scheduleListId;
    private int assetId;
    private String startDateAsString;
    private String endDateAsString;
    private String status;
    private int maintenanceBy;
    private List<MaintenanceDetail> detailList;
    private int flag;
    private int uploadFlag;

    public Maintenance() {
    }

    public Maintenance(int maintenanceId, int id, int scheduleListId, int assetId, String startDateAsString,
                       String endDateAsString, String status, int maintenanceBy, int flag, int uploadFlag) {
        this.maintenanceId = maintenanceId;
        this.id = id;
        this.scheduleListId = scheduleListId;
        this.assetId = assetId;
        this.startDateAsString = startDateAsString;
        this.endDateAsString = endDateAsString;
        this.status = status;
        this.maintenanceBy = maintenanceBy;
        this.flag = flag;
        this.uploadFlag = uploadFlag;
    }

    public int getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(int maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScheduleListId() {
        return scheduleListId;
    }

    public void setScheduleListId(int scheduleListId) {
        this.scheduleListId = scheduleListId;
    }

    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
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

    public int getMaintenanceBy() {
        return maintenanceBy;
    }

    public void setMaintenanceBy(int maintenanceBy) {
        this.maintenanceBy = maintenanceBy;
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

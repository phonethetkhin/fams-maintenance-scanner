package com.example.oemscandemo.model;

import java.util.List;

public class UploadConditionMaintenance {
    private int id;
    private int scheduleId;
    private int deviceUploadId;
    private double scheduleUnit;
    private double currentUnit;
    private String startDateAsString;
    private String endDateAsString;
    private String status;
    private int assetId;
    private List<ConditionScheduleMaintenanceDetail> detailList;


    public void setId(int id) {
        this.id = id;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public void setDeviceUploadId(int deviceUploadId) {
        this.deviceUploadId = deviceUploadId;
    }

    public void setScheduleUnit(double scheduleUnit) {
        this.scheduleUnit = scheduleUnit;
    }

    public void setCurrentUnit(double currentUnit) {
        this.currentUnit = currentUnit;
    }

    public void setStartDateAsString(String startDateAsString) {
        this.startDateAsString = startDateAsString;
    }

    public void setEndDateAsString(String endDateAsString) {
        this.endDateAsString = endDateAsString;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public void setDetailList(List<ConditionScheduleMaintenanceDetail> detailList) {
        this.detailList = detailList;
    }
}

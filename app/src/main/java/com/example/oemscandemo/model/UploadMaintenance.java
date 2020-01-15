package com.example.oemscandemo.model;

import java.util.List;

public class UploadMaintenance {
    private int id;

    private Integer scheduleListId;

    private Integer assetId;

    private String startDateAsString;

    private String endDateAsString;

    private String status;

    private Integer maintenanceBy;

    private List<MaintenanceDetail> detailList;

    public void setId(int id) {
        this.id = id;
    }

    public void setScheduleListId(Integer scheduleListId) {
        this.scheduleListId = scheduleListId;
    }

    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
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

    public void setMaintenanceBy(Integer maintenanceBy) {
        this.maintenanceBy = maintenanceBy;
    }

    public void setDetailList(List<MaintenanceDetail> detailList) {
        this.detailList = detailList;
    }
}

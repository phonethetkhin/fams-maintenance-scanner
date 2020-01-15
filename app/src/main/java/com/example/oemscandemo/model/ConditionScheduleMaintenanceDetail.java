package com.example.oemscandemo.model;

import java.util.List;

public class ConditionScheduleMaintenanceDetail {
    private int id;
    private int deviceUploadId;
    private int maintenanceId;
    private int updatedBy;
    private String status;
    private String updatedDateAsString;
    private String remark;
    private List<StaticContent> contentList;

    public ConditionScheduleMaintenanceDetail() {
    }

    public ConditionScheduleMaintenanceDetail(int id, int deviceUploadId, int maintenanceId, int updatedBy, String status, String updatedDateAsString, String remark) {
        this.id = id;
        this.deviceUploadId = deviceUploadId;
        this.maintenanceId = maintenanceId;
        this.updatedBy = updatedBy;
        this.status = status;
        this.updatedDateAsString = updatedDateAsString;
        this.remark = remark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeviceUploadId() {
        return deviceUploadId;
    }

    public void setDeviceUploadId(int deviceUploadId) {
        this.deviceUploadId = deviceUploadId;
    }

    public int getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(int maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdatedDateAsString() {
        return updatedDateAsString;
    }

    public void setUpdatedDateAsString(String updatedDateAsString) {
        this.updatedDateAsString = updatedDateAsString;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<StaticContent> getContentList() {
        return contentList;
    }

    public void setContentList(List<StaticContent> contentList) {
        this.contentList = contentList;
    }
}

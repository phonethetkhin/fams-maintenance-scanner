package com.example.oemscandemo.model;

import java.util.List;

public class MaintenanceDetail {
    private int id;
    private int maintenanceId;
    private int uploadId;
    private String status;
    private String updatedDateAsString;
    private String remark;
    private List<MaintenanceEvidence> evidenceList;

    public MaintenanceDetail() {
    }

    public MaintenanceDetail(int id, int maintenanceId, int uploadId, String status, String updatedDateAsString, String remark) {
        this.id = id;
        this.maintenanceId = maintenanceId;
        this.uploadId = uploadId;
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

    public int getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(int maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public int getUploadId() {
        return uploadId;
    }

    public void setUploadId(int uploadId) {
        this.uploadId = uploadId;
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

    public List<MaintenanceEvidence> getEvidenceList() {
        return evidenceList;
    }

    public void setEvidenceList(List<MaintenanceEvidence> evidenceList) {
        this.evidenceList = evidenceList;
    }
}

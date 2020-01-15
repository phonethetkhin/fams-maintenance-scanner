package com.example.oemscandemo.model;


import java.util.List;

public class ScheduleItem {
    private int id;
    private int scheduleId;
    private int assetId;
    private String startDateAsString;
    private String endDateAsString;
    private String scheduleStatus;
    private List<ScheduleList> scheduleList;

    public ScheduleItem() {
    }

    public ScheduleItem(int id, int scheduleId, int assetId, String startDateAsString, String endDateAsString, String scheduleStatus,
                        List<ScheduleList> scheduleList) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.assetId = assetId;
        this.startDateAsString = startDateAsString;
        this.endDateAsString = endDateAsString;
        this.scheduleStatus = scheduleStatus;
        this.scheduleList = scheduleList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
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

    public String getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(String scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public List<ScheduleList> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(List<ScheduleList> scheduleList) {
        this.scheduleList = scheduleList;
    }
}

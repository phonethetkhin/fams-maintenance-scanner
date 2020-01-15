package com.example.oemscandemo.model;

import java.util.List;

public class Schedule {
    private int id;
    private String name;
    private String scheduleCode;
    private String scheduleType;
    private String startDateAsString;
    private String endDateAsString;
    private String timeAsString;
    private String scheduleStatus;
    private List<ScheduleItem> itemList;

    public Schedule() {
    }

    public Schedule(int id, String name, String scheduleCode, String scheduleType, String startDateAsString, String endDateAsString,
                    String timeAsString, String scheduleStatus, List<ScheduleItem> itemList) {
        this.id = id;
        this.name = name;
        this.scheduleCode = scheduleCode;
        this.scheduleType = scheduleType;
        this.startDateAsString = startDateAsString;
        this.endDateAsString = endDateAsString;
        this.timeAsString = timeAsString;
        this.scheduleStatus = scheduleStatus;
        this.itemList = itemList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScheduleCode() {
        return scheduleCode;
    }

    public void setScheduleCode(String scheduleCode) {
        this.scheduleCode = scheduleCode;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
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

    public String getTimeAsString() {
        return timeAsString;
    }

    public void setTimeAsString(String timeAsString) {
        this.timeAsString = timeAsString;
    }

    public String getScheduleStatus() {
        return scheduleStatus;
    }

    public void setScheduleStatus(String scheduleStatus) {
        this.scheduleStatus = scheduleStatus;
    }

    public List<ScheduleItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<ScheduleItem> itemList) {
        this.itemList = itemList;
    }
}

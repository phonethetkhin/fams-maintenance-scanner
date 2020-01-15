package com.example.oemscandemo.model;

public class ScheduleList {
    private int id;
    private int scheduleItemId;
    private String scheduleDateAsString;
    private String status;

    public ScheduleList() {
    }

    public ScheduleList(int id, int scheduleItemId, String scheduleDateAsString, String status) {
        this.id = id;
        this.scheduleItemId = scheduleItemId;
        this.scheduleDateAsString = scheduleDateAsString;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScheduleItemId() {
        return scheduleItemId;
    }

    public void setScheduleItemId(int scheduleItemId) {
        this.scheduleItemId = scheduleItemId;
    }

    public String getScheduleDateAsString() {
        return scheduleDateAsString;
    }

    public void setScheduleDateAsString(String scheduleDateAsString) {
        this.scheduleDateAsString = scheduleDateAsString;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

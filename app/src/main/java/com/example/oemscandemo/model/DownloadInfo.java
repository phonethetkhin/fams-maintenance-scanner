package com.example.oemscandemo.model;

public class DownloadInfo {
    private int id;
    private int downloadUserId;
    private String downloadDate;
    private String locations;
    private String downloadStartDate;
    private String downloadEndDate;

    public DownloadInfo() {
    }

    public DownloadInfo(int id, int downloadUserId, String downloadDate, String locations, String downloadStartDate, String downloadEndDate) {
        this.id = id;
        this.downloadUserId = downloadUserId;
        this.downloadDate = downloadDate;
        this.locations = locations;
        this.downloadStartDate = downloadStartDate;
        this.downloadEndDate = downloadEndDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDownloadUserId() {
        return downloadUserId;
    }

    public void setDownloadUserId(int downloadUserId) {
        this.downloadUserId = downloadUserId;
    }

    public String getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(String downloadDate) {
        this.downloadDate = downloadDate;
    }

    public String getLocations() {
        return locations;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }

    public String getDownloadStartDate() {
        return downloadStartDate;
    }

    public void setDownloadStartDate(String downloadStartDate) {
        this.downloadStartDate = downloadStartDate;
    }

    public String getDownloadEndDate() {
        return downloadEndDate;
    }

    public void setDownloadEndDate(String downloadEndDate) {
        this.downloadEndDate = downloadEndDate;
    }
}

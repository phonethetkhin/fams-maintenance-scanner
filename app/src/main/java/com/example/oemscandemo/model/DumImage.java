package com.example.oemscandemo.model;

public class DumImage {
    private int id;
    private int maintenanceDetailId;
    private String imagePath;
    private int flag;

    public DumImage() {
    }

    public DumImage(int id, int maintenanceDetailId, String imagePath, int flag) {
        this.id = id;
        this.maintenanceDetailId = maintenanceDetailId;
        this.imagePath = imagePath;
        this.flag = flag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaintenanceDetailId() {
        return maintenanceDetailId;
    }

    public void setMaintenanceDetailId(int maintenanceDetailId) {
        this.maintenanceDetailId = maintenanceDetailId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}

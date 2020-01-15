package com.example.oemscandemo.model;

public class AssetBean {
    private int id;
    private int locationId;
    private String costCenter;
    private String actualLocation;
    private String faNumber;
    private String itemName;
    private String condition;
    private String category;
    private String brand;
    private String model;
    private String serialNo;

    public AssetBean() {
    }

    public AssetBean(int id, int locationId, String costCenter, String actualLocation, String faNumber, String itemName,
                     String condition, String category, String brand, String model, String serialNo) {
        this.id = id;
        this.locationId = locationId;
        this.costCenter = costCenter;
        this.actualLocation = actualLocation;
        this.faNumber = faNumber;
        this.itemName = itemName;
        this.condition = condition;
        this.category = category;
        this.brand = brand;
        this.model = model;
        this.serialNo = serialNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public String getActualLocation() {
        return actualLocation;
    }

    public void setActualLocation(String actualLocation) {
        this.actualLocation = actualLocation;
    }

    public String getFaNumber() {
        return faNumber;
    }

    public void setFaNumber(String faNumber) {
        this.faNumber = faNumber;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
}

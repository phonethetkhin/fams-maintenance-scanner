package com.example.oemscandemo.model;

public class AssetMaintenanceConditionUnitBean {
    private int conditionId;
    private int assetId;
    private String ConditionName;
    private Double unit;
    private String unitType;
    private String takingDate;
    private int takingBy;
    private int flag;
    private int uploadFlag;


    public AssetMaintenanceConditionUnitBean() {
    }

    public AssetMaintenanceConditionUnitBean(int conditionId, int assetId, String conditionName, Double unit, String unitType,
                                             String takingDate, int takingBy, int flag, int uploadFlag) {
        this.conditionId = conditionId;
        this.assetId = assetId;
        ConditionName = conditionName;
        this.unit = unit;
        this.unitType = unitType;
        this.takingDate = takingDate;
        this.takingBy = takingBy;
        this.flag = flag;
        this.uploadFlag = uploadFlag;
    }

    public int getConditionId() {
        return conditionId;
    }

    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
    }

    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public String getConditionName() {
        return ConditionName;
    }

    public void setConditionName(String conditionName) {
        ConditionName = conditionName;
    }

    public Double getUnit() {
        return unit;
    }

    public void setUnit(Double unit) {
        this.unit = unit;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getTakingDate() {
        return takingDate;
    }

    public void setTakingDate(String takingDate) {
        this.takingDate = takingDate;
    }

    public int getTakingBy() {
        return takingBy;
    }

    public void setTakingBy(int takingBy) {
        this.takingBy = takingBy;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getUploadFlag() {
        return uploadFlag;
    }

    public void setUploadFlag(int uploadFlag) {
        this.uploadFlag = uploadFlag;
    }
}

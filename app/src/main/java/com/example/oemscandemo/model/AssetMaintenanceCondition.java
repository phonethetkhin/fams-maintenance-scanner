package com.example.oemscandemo.model;

public class AssetMaintenanceCondition {
    private int id;
    private String regDate;
    private String updDate;
    private int assetId;
    private String conditionName;
    private String unitType;
    private double totalUnit;
    private double updateUnit;

    public AssetMaintenanceCondition() {
    }

    public AssetMaintenanceCondition(int id, String regDate, String updDate, int assetId, String conditionName, String unitType,
                                     double totalUnit, double updateUnit) {
        this.id = id;
        this.regDate = regDate;
        this.updDate = updDate;
        this.assetId = assetId;
        this.conditionName = conditionName;
        this.unitType = unitType;
        this.totalUnit = totalUnit;
        this.updateUnit = updateUnit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getUpdDate() {
        return updDate;
    }

    public void setUpdDate(String updDate) {
        this.updDate = updDate;
    }

    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public double getTotalUnit() {
        return totalUnit;
    }

    public void setTotalUnit(double totalUnit) {
        this.totalUnit = totalUnit;
    }

    public double getUpdateUnit() {
        return updateUnit;
    }

    public void setUpdateUnit(double updateUnit) {
        this.updateUnit = updateUnit;
    }

}

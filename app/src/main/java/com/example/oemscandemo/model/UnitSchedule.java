package com.example.oemscandemo.model;

public class UnitSchedule {
    private int id;
    private String scheduleName;
    private String unitType;
    private int assetId;
    private String scheduleCode;
    private String conditionName;
    private double scheduleId;
    private double scheduleUnit;

    public UnitSchedule() {
    }

    public UnitSchedule(int id, String scheduleName, String unitType, int assetId, String scheduleCode, String conditionName,
                        double scheduleId, double scheduleUnit) {
        this.id = id;
        this.scheduleName = scheduleName;
        this.unitType = unitType;
        this.assetId = assetId;
        this.scheduleCode = scheduleCode;
        this.conditionName = conditionName;
        this.scheduleId = scheduleId;
        this.scheduleUnit = scheduleUnit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public String getScheduleCode() {
        return scheduleCode;
    }

    public void setScheduleCode(String scheduleCode) {
        this.scheduleCode = scheduleCode;
    }

    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    public double getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(double scheduleId) {
        this.scheduleId = scheduleId;
    }

    public double getScheduleUnit() {
        return scheduleUnit;
    }

    public void setScheduleUnit(double scheduleUnit) {
        this.scheduleUnit = scheduleUnit;
    }
}

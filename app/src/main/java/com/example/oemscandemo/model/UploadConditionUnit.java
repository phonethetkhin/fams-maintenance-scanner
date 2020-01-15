package com.example.oemscandemo.model;

public class UploadConditionUnit {
    private int conditionId;
    private Double unit;
    private String takingDate;
    private int takingBy;

    public UploadConditionUnit() {
    }

    public int getConditionId() {
        return conditionId;
    }

    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
    }

    public Double getUnit() {
        return unit;
    }

    public void setUnit(Double unit) {
        this.unit = unit;
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
}

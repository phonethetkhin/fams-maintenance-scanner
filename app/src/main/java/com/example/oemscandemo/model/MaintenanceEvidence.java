package com.example.oemscandemo.model;

public class MaintenanceEvidence {
    private int maintenanceDetailId;
    private int contentId;
    private StaticContent content;

    public MaintenanceEvidence() {
    }

    public MaintenanceEvidence(int maintenanceDetailId, int contentId, StaticContent content) {
        this.maintenanceDetailId = maintenanceDetailId;
        this.contentId = contentId;
        this.content = content;
    }

    public int getMaintenanceDetailId() {
        return maintenanceDetailId;
    }

    public void setMaintenanceDetailId(int maintenanceDetailId) {
        this.maintenanceDetailId = maintenanceDetailId;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public StaticContent getContent() {
        return content;
    }

    public void setContent(StaticContent content) {
        this.content = content;
    }
}

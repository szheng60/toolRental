package com.toolrental.form;

import java.util.Date;

/**
 * Created by xinyu on 11/1/2017.
 */
public class ToolAvailabilityForm {
    private String startDate;
    private String endDate;
    private String customSearch;
    private String toolType;
    private String powerSource;
    private String subType;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCustomSearch() {
        return customSearch;
    }

    public void setCustomSearch(String customSearch) {
        this.customSearch = customSearch;
    }

    public String getToolType() {
        return toolType;
    }

    public void setToolType(String toolType) {
        this.toolType = toolType;
    }

    public String getPowerSource() {
        return powerSource;
    }

    public void setPowerSource(String powerSource) {
        this.powerSource = powerSource;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }
}

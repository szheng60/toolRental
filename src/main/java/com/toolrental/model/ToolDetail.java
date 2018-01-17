package com.toolrental.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xinyu on 11/1/2017.
 */
public class ToolDetail implements Serializable {
    private static final long serialVersionUID = 3905767990439555437L;
    private int toolId;
    private String toolType;
    private String toolSubType;
    private String toolPowerSource;
    private String shortDescription;
    private String fullDescription;
    private String specificDescription;
    private double rentalPrice;
    private double depositPrice;
    private List<String> accessories;

    public int getToolId() {
        return toolId;
    }

    public void setToolId(int toolId) {
        this.toolId = toolId;
    }

    public String getToolType() {
        return toolType;
    }

    public void setToolType(String toolType) {
        this.toolType = toolType;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public double getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(double rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public double getDepositPrice() {
        return depositPrice;
    }

    public void setDepositPrice(double depositPrice) {
        this.depositPrice = depositPrice;
    }

    public String getToolSubType() {
        return toolSubType;
    }

    public void setToolSubType(String toolSubType) {
        this.toolSubType = toolSubType;
    }

    public String getToolPowerSource() {
        return toolPowerSource;
    }

    public void setToolPowerSource(String toolPowerSource) {
        this.toolPowerSource = toolPowerSource;
    }

    public String getSpecificDescription() {
        return specificDescription;
    }

    public void setSpecificDescription(String specificDescription) {
        this.specificDescription = specificDescription;
    }

    public List<String> getAccessories() {
        return accessories;
    }

    public void setAccessories(List<String> accessories) {
        this.accessories = accessories;
    }
}

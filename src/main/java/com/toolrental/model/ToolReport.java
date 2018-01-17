package com.toolrental.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xinyu on 11/4/2017.
 */
public class ToolReport implements Serializable{
    private static final long serialVersionUID = 643599553605653056L;

    private int toolId;
    private String currentStatus;
    private Date date;
    private String Description;
    private double rentalProfit;
    private double totalCost;
    private double totalProfit;

    public int getToolId() {
        return toolId;
    }

    public void setToolId(int toolId) {
        this.toolId = toolId;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public double getRentalProfit() {
        return rentalProfit;
    }

    public void setRentalProfit(double rentalProfit) {
        this.rentalProfit = rentalProfit;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(double totalProfit) {
        this.totalProfit = totalProfit;
    }
}

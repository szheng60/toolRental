package com.toolrental.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xinyu on 11/1/2017.
 */
public class UserReservation implements Serializable{
    private static final long serialVersionUID = 7887686372070146479L;

    private int reservationId;
    private String tools;
    private Date startDate;
    private Date endDate;
    private String pickUpClerk;
    private String dropOffClerk;
    private int numberOfDays;
    private double totalDepositPrice;
    private double totalRentalPrice;

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getTools() {
        return tools;
    }

    public void setTools(String tools) {
        this.tools = tools;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getPickUpClerk() {
        return pickUpClerk;
    }

    public void setPickUpClerk(String pickUpClerk) {
        this.pickUpClerk = pickUpClerk;
    }

    public String getDropOffClerk() {
        return dropOffClerk;
    }

    public void setDropOffClerk(String dropOffClerk) {
        this.dropOffClerk = dropOffClerk;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public double getTotalDepositPrice() {
        return totalDepositPrice;
    }

    public void setTotalDepositPrice(double totalDepositPrice) {
        this.totalDepositPrice = totalDepositPrice;
    }

    public double getTotalRentalPrice() {
        return totalRentalPrice;
    }

    public void setTotalRentalPrice(double totalRentalPrice) {
        this.totalRentalPrice = totalRentalPrice;
    }
}

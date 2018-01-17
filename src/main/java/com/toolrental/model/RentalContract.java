package com.toolrental.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by xinyu on 11/5/2017.
 */
public class RentalContract implements Serializable{
    private static final long serialVersionUID = 1750297395567906453L;
    private int reservationId;
    private String pickUpClerkName;
    private String customerName;
    private String creditCardNumber;
    private Date startDate;
    private Date endDate;
    private List<ToolDetail> reservedTools;

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getPickUpClerkName() {
        return pickUpClerkName;
    }

    public void setPickUpClerkName(String pickUpClerkName) {
        this.pickUpClerkName = pickUpClerkName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
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

    public List<ToolDetail> getReservedTools() {
        return reservedTools;
    }

    public void setReservedTools(List<ToolDetail> reservedTools) {
        this.reservedTools = reservedTools;
    }
}

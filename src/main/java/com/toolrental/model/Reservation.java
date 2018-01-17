package com.toolrental.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xinyu on 11/5/2017.
 */
public class Reservation implements Serializable{
    private static final long serialVersionUID = -584583267795911083L;

    private int reservationId;
    private String customerShortName;
    private int customerId;
    private Date startDate;
    private Date endDate;

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getCustomerShortName() {
        return customerShortName;
    }

    public void setCustomerShortName(String customerShortName) {
        this.customerShortName = customerShortName;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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
}

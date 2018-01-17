package com.toolrental.model;

import java.io.Serializable;

/**
 * Created by xinyu on 11/5/2017.
 */
public class ReservationDetail implements Serializable{
    private static final long serialVersionUID = 7793139379671999421L;
    private int reservationId;
    private String customerFullName;
    private double totalDeposit;
    private double totalRentalPrice;

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getCustomerFullName() {
        return customerFullName;
    }

    public void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }

    public double getTotalDeposit() {
        return totalDeposit;
    }

    public void setTotalDeposit(double totalDeposit) {
        this.totalDeposit = totalDeposit;
    }

    public double getTotalRentalPrice() {
        return totalRentalPrice;
    }

    public void setTotalRentalPrice(double totalRentalPrice) {
        this.totalRentalPrice = totalRentalPrice;
    }
}

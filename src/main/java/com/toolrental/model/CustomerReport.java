package com.toolrental.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xinyu on 11/4/2017.
 */
public class CustomerReport implements Serializable {
    private static final long serialVersionUID = -9088145346967017944L;

    private int customerId;
    private String profileEmail;
    private String firstName;
    private String midName;
    private String lastName;
    private String email;
    private String phone;
    private int totalReservations;
    private int totalToolsRented;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getProfileEmail() {
        return profileEmail;
    }

    public void setProfileEmail(String profileEmail) {
        this.profileEmail = profileEmail;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMidName() {
        return midName;
    }

    public void setMidName(String midName) {
        this.midName = midName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getTotalReservations() {
        return totalReservations;
    }

    public void setTotalReservations(int totalReservations) {
        this.totalReservations = totalReservations;
    }

    public int getTotalToolsRented() {
        return totalToolsRented;
    }

    public void setTotalToolsRented(int totalToolsRented) {
        this.totalToolsRented = totalToolsRented;
    }
}

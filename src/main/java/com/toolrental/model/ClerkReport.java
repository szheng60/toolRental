package com.toolrental.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xinyu on 11/4/2017.
 */
public class ClerkReport implements Serializable {
    private static final long serialVersionUID = -9088145346967017944L;

    private int clerkId;
    private String firstName;
    private String midName;
    private String lastName;
    private String email;
    private Date hireDate;
    private int numberOfPickups;
    private int numberOfDropoffs;
    private int combinedTotal;

    public int getClerkId() {
        return clerkId;
    }

    public void setClerkId(int clerkId) {
        this.clerkId = clerkId;
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

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public int getNumberOfPickups() {
        return numberOfPickups;
    }

    public void setNumberOfPickups(int numberOfPickups) {
        this.numberOfPickups = numberOfPickups;
    }

    public int getNumberOfDropoffs() {
        return numberOfDropoffs;
    }

    public void setNumberOfDropoffs(int numberOfDropoffs) {
        this.numberOfDropoffs = numberOfDropoffs;
    }

    public int getCombinedTotal() {
        return combinedTotal;
    }

    public void setCombinedTotal(int combinedTotal) {
        this.combinedTotal = combinedTotal;
    }
}

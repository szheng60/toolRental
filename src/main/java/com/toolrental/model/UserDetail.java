package com.toolrental.model;

import java.io.Serializable;

/**
 * Created by xinyu on 11/1/2017.
 */
public class UserDetail implements Serializable{
    private static final long serialVersionUID = 2953438428111480995L;
    private String userEmail;
    private String fullName;
    private String fullAddress;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }
}

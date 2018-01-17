package com.toolrental.model;

import java.io.Serializable;

/**
 * Created by xinyu on 10/31/2017.
 */
public class PhoneNumber implements Serializable{

    private static final long serialVersionUID = 4886462047699724779L;

    private String customerEmail;
    private String subType;
    private String areaCode;
    private String phoneNumber;
    private String extension;

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}

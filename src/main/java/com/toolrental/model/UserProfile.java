package com.toolrental.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xinyu on 11/1/2017.
 */
public class UserProfile implements Serializable{
    private static final long serialVersionUID = -908464384939665828L;
    private List<PhoneNumber> phoneNumberList;
    private UserDetail userDetail;
    private List<UserReservation> userReservation;

    public List<PhoneNumber> getPhoneNumberList() {
        return phoneNumberList;
    }

    public void setPhoneNumberList(List<PhoneNumber> phoneNumberList) {
        this.phoneNumberList = phoneNumberList;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public List<UserReservation> getUserReservation() {
        return userReservation;
    }

    public void setUserReservation(List<UserReservation> userReservation) {
        this.userReservation = userReservation;
    }
}

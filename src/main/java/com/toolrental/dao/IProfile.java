package com.toolrental.dao;

import com.toolrental.model.PhoneNumber;
import com.toolrental.model.UserDetail;
import com.toolrental.model.UserReservation;

import java.util.List;

/**
 * Created by xinyu on 11/1/2017.
 */

public interface IProfile {
    List<PhoneNumber> retrievePhoneNumbers(String email);
    UserDetail retrieveUserDetail(String email);
    List<UserReservation> retrieveUserReservation(String email);
}

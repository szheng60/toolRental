package com.toolrental.service;

import com.toolrental.dao.IProfile;
import com.toolrental.model.PhoneNumber;
import com.toolrental.model.UserDetail;
import com.toolrental.model.UserProfile;
import com.toolrental.model.UserReservation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by xinyu on 11/1/2017.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProfileService implements IProfileService{
    @Autowired
    private IProfile profileDao;

    @Override
    public UserProfile retrieveUserProfile(String email) {
        UserProfile userProfile = new UserProfile();
        List<PhoneNumber> phoneNumberList = profileDao.retrievePhoneNumbers(email);

        if (CollectionUtils.isEmpty(phoneNumberList)) {
            List<PhoneNumber> newList = new ArrayList<>();
            userProfile.setPhoneNumberList(newList);
        } else {
            userProfile.setPhoneNumberList(phoneNumberList);
        }

        UserDetail userDetail = profileDao.retrieveUserDetail(email);
        userProfile.setUserDetail(userDetail);

        List<UserReservation> userReservationList = profileDao.retrieveUserReservation(email);
        if (CollectionUtils.isEmpty(userReservationList)) {
            List<UserReservation> newList = new ArrayList<>();
            userProfile.setUserReservation(newList);
        } else {
            userProfile.setUserReservation(userReservationList);
        }

        return userProfile;
    }
}

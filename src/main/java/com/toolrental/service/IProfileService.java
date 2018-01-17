package com.toolrental.service;

import com.toolrental.model.UserProfile;

/**
 * Created by xinyu on 11/1/2017.
 */
public interface IProfileService {
    UserProfile retrieveUserProfile(String email);
}

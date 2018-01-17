package com.toolrental.dao;

import com.toolrental.form.RegistrationForm;
import com.toolrental.model.*;

/**
 * Created by xinyu on 10/31/2017.
 */
public interface IRegistrationDao {

    RegistrationResult registerNewUser(RegistrationForm registrationForm);
}

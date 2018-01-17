package com.toolrental.service;

import com.toolrental.form.RegistrationForm;
import com.toolrental.model.*;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by xinyu on 10/31/2017.
 */
public interface IRegistrationService {
    RegistrationResult registerNewCustomer(RegistrationForm registrationForm);
}

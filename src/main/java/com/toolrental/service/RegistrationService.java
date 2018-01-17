package com.toolrental.service;

import com.toolrental.dao.IRegistrationDao;
import com.toolrental.form.RegistrationForm;
import com.toolrental.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by xinyu on 10/31/2017.
 */
@Service
public class RegistrationService implements IRegistrationService {
    @Autowired
    private IRegistrationDao registrationDao;

    @Override
    @Transactional
    public RegistrationResult registerNewCustomer(RegistrationForm registrationForm) {

        return registrationDao.registerNewUser(registrationForm);
    }
}

package com.toolrental.dao;

import com.toolrental.form.RegistrationForm;
import com.toolrental.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xinyu on 10/31/2017.
 */
@Repository
public class RegistrationDao implements IRegistrationDao{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String createNewUserSQL = "INSERT INTO user(first_name, mid_name, last_name, email, password) VALUES(?,?,?,?,?);";
    private String createNewPhoneNumberSQL = "INSERT INTO phonenumber(customerEmail, sub_type, area_code, phone_number, extension) VALUES(?,?,?,?,?);";
    private String createNewCreditCard = "INSERT INTO creditcard(number, name_on_card, exp_month, exp_year, cvc) VALUES(?,?,?,?,?);";
    private String createNewCustomer = "INSERT INTO Customer(userEmail,primary_phone,street,city,state,zip_code,card_no)VALUES(?,?,?,?,?,?,?);";

    private static Logger logger = LoggerFactory.getLogger(RegistrationDao.class);

    private boolean insertUser(User user) {
        int result = 0;

        result = jdbcTemplate.update(createNewUserSQL, new Object[]{user.getFirstName(), user.getMidName(), user.getLastName(), user.getEmail(), user.getPassword()});

        return result == 1;
    }
    private boolean insertPhoneNumber(PhoneNumber phoneNumber) {
        int result = 0;

            result = jdbcTemplate.update(createNewPhoneNumberSQL, new Object[]{phoneNumber.getCustomerEmail(), phoneNumber.getSubType(),
                    phoneNumber.getAreaCode(), phoneNumber.getPhoneNumber(), phoneNumber.getExtension()});

        return result == 1;
    }

    private boolean insertCreditCard(CreditCard creditCard) {
        int result = 0;

            result = jdbcTemplate.update(createNewCreditCard, new Object[]{creditCard.getNumber(), creditCard.getNameOnCard(), creditCard.getExpMonth(), creditCard.getExpYear(), creditCard.getCvc()});

        return result == 1;
    }

    private boolean insertCustomer(Customer customer) {
        int result = 0;

            result = jdbcTemplate.update(createNewCustomer, new Object[]{customer.getUserEmail(), customer.getPrimaryPhone(),
                    customer.getStreet(), customer.getCity(), customer.getState(), customer.getZipCode(), customer.getCardNo()});

        return result == 1;
    }

    @Override
    public RegistrationResult registerNewUser(RegistrationForm registrationForm) {
            User user = registrationForm.getUser();
            List<PhoneNumber> phoneNumber = registrationForm.getPhoneNumber();
            CreditCard creditCard = registrationForm.getCreditCard();
            Customer customer = registrationForm.getCustomer();
        RegistrationResult registrationResult = new RegistrationResult();

        if (!insertUser(user)) {
            registrationResult.setStatus(false);
            registrationResult.setMsg("Failed to register with current email address");
            return registrationResult;
        }
        if (!insertCreditCard(creditCard)) {
            registrationResult.setStatus(false);
            registrationResult.setMsg("Failed to register with current credit card");
            return registrationResult;
        }
        if (!insertCustomer(customer)) {
            registrationResult.setStatus(false);
            registrationResult.setMsg("Failed to register with current email address");
            return registrationResult;
        }
        if (!insertPhoneNumber(phoneNumber)) {
            registrationResult.setStatus(false);
            registrationResult.setMsg("Failed to register with current phone number");
            return registrationResult;
        }
        registrationResult.setStatus(true);
        registrationResult.setMsg("Successfully registered");
        return registrationResult;
    }
    private boolean insertPhoneNumber(List<PhoneNumber> phoneNumber) {
        boolean result = true;
        for(PhoneNumber pn: phoneNumber) {
            if (!insertPhoneNumber(pn)) {
                result = false;
                break;
            }
        }
        return result;
    }
}

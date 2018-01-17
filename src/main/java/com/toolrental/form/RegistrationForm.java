package com.toolrental.form;

import com.toolrental.model.CreditCard;
import com.toolrental.model.Customer;
import com.toolrental.model.PhoneNumber;
import com.toolrental.model.User;

import java.util.List;

/**
 * Created by xinyu on 10/31/2017.
 */
public class RegistrationForm {
    private User user;
    private List<PhoneNumber> phoneNumber;
    private CreditCard creditCard;
    private Customer customer;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<PhoneNumber> getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(List<PhoneNumber> phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}

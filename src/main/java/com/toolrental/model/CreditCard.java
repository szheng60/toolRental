package com.toolrental.model;

import java.io.Serializable;

/**
 * Created by xinyu on 10/31/2017.
 */
public class CreditCard implements Serializable{
    private static final long serialVersionUID = 6416417489136218878L;
    private String number;
    private String nameOnCard;
    private String cvc;
    private String expMonth;
    private String expYear;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public String getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(String expMonth) {
        this.expMonth = expMonth;
    }

    public String getExpYear() {
        return expYear;
    }

    public void setExpYear(String expYear) {
        this.expYear = expYear;
    }
}

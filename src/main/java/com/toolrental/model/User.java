package com.toolrental.model;

import java.io.Serializable;

/**
 * Created by xinyu on 10/17/2017.
 */
public class User implements Serializable{
    private static final long serialVersionUID = -5356830244788618694L;

    private String email;
    private String password;
    private String firstName;
    private String midName;
    private String lastName;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMidName() {
        return midName;
    }

    public void setMidName(String midName) {
        this.midName = midName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

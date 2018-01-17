package com.toolrental.form;

import javax.validation.constraints.NotNull;

/**
 * Created by xinyu on 10/25/2017.
 */
public class LoginForm {
    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String role;

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

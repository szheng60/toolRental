package com.toolrental.dao;

import com.toolrental.form.LoginForm;
import com.toolrental.model.LoginResult;
import com.toolrental.model.User;

/**
 * Created by xinyu on 10/25/2017.
 */
public interface ILoginDao {
    LoginResult loginAsCustomer(LoginForm loginForm);

    LoginResult loginAsClerk(LoginForm loginForm);

    LoginResult loginUpdate(User clerk);
}

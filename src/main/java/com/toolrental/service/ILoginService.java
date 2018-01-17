package com.toolrental.service;

import com.toolrental.form.LoginForm;
import com.toolrental.model.LoginResult;
import com.toolrental.model.User;

/**
 * Created by xinyu on 10/25/2017.
 */
public interface ILoginService {
    LoginResult login(LoginForm loginForm);
    LoginResult loginUpdate(User user);
}

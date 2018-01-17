package com.toolrental.service;

import com.toolrental.dao.ILoginDao;
import com.toolrental.dao.LoginDao;
import com.toolrental.form.LoginForm;
import com.toolrental.model.LoginResult;
import com.toolrental.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xinyu on 10/25/2017.
 */
@Service
public class LoginService implements ILoginService {

    @Autowired
    private ILoginDao loginDao;

    @Override
    public LoginResult login(LoginForm loginForm) {
        String role = loginForm.getRole();
        LoginResult result = null;
        if ("1".equals(role)) {
            result = loginDao.loginAsCustomer(loginForm);
        } else {
            result = loginDao.loginAsClerk(loginForm);
        }
        return result;
    }

    @Override
    public LoginResult loginUpdate(User user) {
        return loginDao.loginUpdate(user);
    }
}

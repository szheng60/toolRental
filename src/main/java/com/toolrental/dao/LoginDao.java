package com.toolrental.dao;

import com.toolrental.form.LoginForm;
import com.toolrental.model.LoginResult;
import com.toolrental.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by xinyu on 10/25/2017.
 */
@Repository
public class LoginDao implements ILoginDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String customerSQL = "SELECT userEmail from customer where userEmail=?";
    private String userLoginSQL = "SELECT first_name from user where email=? and password=?";
    private String clerkSQL = "SELECT userEmail from clerk where userEmail=?";

    private String updateClerkPasswordSQL = "UPDATE user SET password=? WHERE email=?";

    @Override
    public LoginResult loginAsCustomer(LoginForm loginForm) {
        LoginResult loginResult = new LoginResult();
        String userEmail = loginForm.getEmail();
        String password = loginForm.getPassword();
        String clerkName;
        try {
            clerkName = jdbcTemplate.queryForObject(clerkSQL, new Object[] {userEmail}, String.class);
        } catch(EmptyResultDataAccessException e) {
            clerkName = null;
        }
        if (clerkName != null) {
            loginResult.setStatus(1);
            loginResult.setMsg("Clerk could not login as customer");
            return loginResult;
        }
        String userName;
        try {
            userName = jdbcTemplate.queryForObject(customerSQL, new Object[] {userEmail}, String.class);
        } catch(EmptyResultDataAccessException e) {
            userName = null;
        }
        if (userName == null) {
            loginResult.setStatus(2);
            loginResult.setMsg("Customer name does not exist");
            return loginResult;
        }

        String loginName;
        try{
            loginName = jdbcTemplate.queryForObject(userLoginSQL, new Object[]{userEmail, password}, String.class);
        } catch (EmptyResultDataAccessException e) {
            loginName = null;
        }
        if (loginName == null) {
            loginResult.setStatus(3);
            loginResult.setMsg("Invalid password");
        } else {
            loginResult.setStatus(4);
            loginResult.setMsg("/customerhome");
            loginResult.setLoginName(loginName);
        }
        return loginResult;
    }

    @Override
    public LoginResult loginAsClerk(LoginForm loginForm) {
        LoginResult loginResult = new LoginResult();
        String userEmail = loginForm.getEmail();
        String password = loginForm.getPassword();
        String customerName;

        try {
            customerName = jdbcTemplate.queryForObject(customerSQL, new Object[] {userEmail}, String.class);
        } catch(EmptyResultDataAccessException e) {
            customerName = null;
        }
        if (customerName != null) {
            loginResult.setStatus(1);
            loginResult.setMsg("Customer could not login as clerk");
            return loginResult;
        }
        String clerkName;
        try {
            clerkName = jdbcTemplate.queryForObject(clerkSQL, new Object[] {userEmail}, String.class);
        } catch(EmptyResultDataAccessException e) {
            clerkName = null;
        }
        if (clerkName == null) {
            loginResult.setStatus(2);
            loginResult.setMsg("Clerk name does not exist");
            return loginResult;
        }
        if ("clerk".equals(password)) {
            loginResult.setStatus(99);
            loginResult.setMsg("Password need to be changed when first time login");
            return loginResult;
        }
        String loginName;
        try{
            loginName = jdbcTemplate.queryForObject(userLoginSQL, new Object[]{userEmail, password}, String.class);
        } catch (EmptyResultDataAccessException e) {
            loginName = null;
        }
        if (loginName == null) {
            loginResult.setStatus(3);
            loginResult.setMsg("Invalid password");

        } else {
            loginResult.setStatus(4);
            loginResult.setMsg("/clerkhome");
            loginResult.setLoginName(loginName);
        }
        return loginResult;
    }

    @Override
    public LoginResult loginUpdate(User clerk) {
        LoginResult loginResult = new LoginResult();
        int result = jdbcTemplate.update(updateClerkPasswordSQL, clerk.getPassword(), clerk.getEmail());
        if (result > 0) {
            loginResult.setStatus(98);
        }
        return loginResult;
    }
}

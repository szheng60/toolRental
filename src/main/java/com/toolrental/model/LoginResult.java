package com.toolrental.model;

/**
 * Created by xinyu on 10/25/2017.
 */
public class LoginResult {
    private String msg;
    private int status;
    private String loginName;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}

package com.bugaugaoshu.security.model;

/**
 * @author Pu Zhiwei {@literal puzhiweipuzhiwei@foxmail.com}
 * create          2019-11-27 23:01
 */
public class LoginDetails {
    private Boolean rememberMe;
    private String verifyCode;

    public LoginDetails(Boolean rememberMe, String verifyCode) {
        this.rememberMe = rememberMe;
        this.verifyCode = verifyCode;
    }

    public LoginDetails() {}

    public Boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}

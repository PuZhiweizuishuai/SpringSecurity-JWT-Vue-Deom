package com.bugaugaoshu.security.damain;

import com.bugaugaoshu.security.model.User;

/**
 * @author Pu Zhiwei {@literal puzhiweipuzhiwei@foxmail.com}
 * create          2019-12-02 18:39
 * 登陆成功后返回用户信息
 */
public class LoginResultDetails {
    private Integer status;
    private ResultDetails resultDetails;
    private User user;

    public ResultDetails getResultDetails() {
        return resultDetails;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setResultDetails(ResultDetails resultDetails) {
        this.resultDetails = resultDetails;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

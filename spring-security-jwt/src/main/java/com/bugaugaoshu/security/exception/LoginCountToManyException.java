package com.bugaugaoshu.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Pu Zhiwei {@literal puzhiweipuzhiwei@foxmail.com}
 * create          2019-11-30 16:40
 * 返回登陆请求过于频繁的异常
 */
public class LoginCountToManyException extends AuthenticationException {

    public LoginCountToManyException(String msg, Throwable t) {
        super(msg, t);
    }

    public LoginCountToManyException(String msg) {
        super(msg);
    }
}

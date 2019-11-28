package com.bugaugaoshu.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author puzhiwei
 * 继承 AuthenticationException 方便 Spring security 返回异常
 */
public class VerifyFailedException extends AuthenticationException {
    public VerifyFailedException(String msg, Throwable t) {
        super(msg, t);
    }

    public VerifyFailedException(String msg) {
        super(msg);
    }
}

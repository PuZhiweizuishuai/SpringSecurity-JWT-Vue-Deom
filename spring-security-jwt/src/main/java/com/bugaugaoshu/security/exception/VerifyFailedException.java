package com.bugaugaoshu.security.exception;

public class VerifyFailedException extends RuntimeException {

    public VerifyFailedException() {
        super();
    }

    public VerifyFailedException(String message) {
        super(message);
    }

    public VerifyFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public VerifyFailedException(Throwable cause) {
        super(cause);
    }

    protected VerifyFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

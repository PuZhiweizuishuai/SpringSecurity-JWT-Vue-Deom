package com.bugaugaoshu.security.advice;

import com.bugaugaoshu.security.controller.VerifyCodeController;
import com.bugaugaoshu.security.damain.ErrorDetails;
import com.bugaugaoshu.security.exception.VerifyFailedException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@RestControllerAdvice(assignableTypes = VerifyCodeController.class)
public class VerifyCodeControllerAdvice {

    @ExceptionHandler
    public HttpEntity verifyFailedExceptionHandler(VerifyFailedException e, HttpServletRequest request) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setTimestamp(LocalDateTime.now());
        errorDetails.setStatus(HttpStatus.FORBIDDEN.value());
        errorDetails.setError(HttpStatus.FORBIDDEN.getReasonPhrase());
        errorDetails.setMessage(e.getLocalizedMessage());
        errorDetails.setPath(request.getServletPath());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(errorDetails);
    }
}

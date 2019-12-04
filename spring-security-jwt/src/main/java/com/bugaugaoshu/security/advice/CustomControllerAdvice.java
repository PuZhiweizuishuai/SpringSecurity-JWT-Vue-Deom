package com.bugaugaoshu.security.advice;

import com.bugaugaoshu.security.controller.AdminController;
import com.bugaugaoshu.security.controller.HomeController;
import com.bugaugaoshu.security.controller.UserController;
import com.bugaugaoshu.security.damain.ErrorDetails;
import com.bugaugaoshu.security.exception.CustomizeException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author Pu Zhiwei {@literal puzhiweipuzhiwei@foxmail.com}
 * create          2019-11-30 18:57
 */
@RestControllerAdvice(assignableTypes = {UserController.class, HomeController.class, AdminController.class})
public class CustomControllerAdvice {
    @ExceptionHandler
    public HttpEntity customExceptionHandler(CustomizeException e, HttpServletRequest request) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setTimestamp(LocalDateTime.now());
        errorDetails.setStatus(HttpStatus.NOT_FOUND.value());
        errorDetails.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        errorDetails.setMessage(e.getLocalizedMessage());
        errorDetails.setPath(request.getServletPath());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(errorDetails);
    }
}

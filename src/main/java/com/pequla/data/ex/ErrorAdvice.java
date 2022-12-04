package com.pequla.data.ex;

import com.pequla.data.model.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ErrorAdvice {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorModel handleException(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        return ErrorModel.builder()
                .name(e.getClass().getSimpleName())
                .message(e.getMessage())
                .path(request.getServletPath())
                .timestamp(System.currentTimeMillis())
                .build();
    }
}

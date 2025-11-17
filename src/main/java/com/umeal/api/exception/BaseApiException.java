package com.umeal.api.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseApiException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String code;

    public BaseApiException(String message, HttpStatus httpStatus, String code) {

        super(message);
        this.httpStatus = httpStatus;
        this.code = code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }
}
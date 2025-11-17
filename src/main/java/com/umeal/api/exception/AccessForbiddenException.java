package com.umeal.api.exception;

import org.springframework.http.HttpStatus;

public class AccessForbiddenException extends BaseApiException {

    public AccessForbiddenException(String message) {
        super(
            message,
            HttpStatus.FORBIDDEN,
            "ACCESS_FORBIDDEN"
        );
    }
}
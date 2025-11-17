package com.umeal.api.exception.user;

import org.springframework.http.HttpStatus;

import com.umeal.api.exception.BaseApiException;

public class UserNotFoundException extends BaseApiException {

    public UserNotFoundException(String resourceName, Object id) {
        super(
            String.format("%s com ID %s não foi encontrado.", resourceName, id),
            HttpStatus.NOT_FOUND,
            "USER_NOT_FOUND"
        );
    }
}
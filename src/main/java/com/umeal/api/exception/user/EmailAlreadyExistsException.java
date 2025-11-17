package com.umeal.api.exception.user;

import org.springframework.http.HttpStatus;

import com.umeal.api.exception.BaseApiException;

public class EmailAlreadyExistsException extends BaseApiException {

    public EmailAlreadyExistsException() {
        super(
            String.format("O e-mail já está cadastrado."),
            HttpStatus.CONFLICT,
            "EMAIL_ALREADY_EXISTS"
        );
    }
}
package com.umeal.api.exception;

import org.springframework.http.HttpStatus;

import com.umeal.api.exception.BaseApiException;

public class ResourceNotFoundException extends BaseApiException {

    public ResourceNotFoundException(String resourceName, Object id) {
        super(
            String.format("%s com ID %s não foi encontrado.", resourceName, id),
            HttpStatus.NOT_FOUND,
            "RESOURCE_NOT_FOUND"
        );
    }
}
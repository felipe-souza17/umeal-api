package com.umeal.api.exception;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class ApiErrorResponse {
    
    private String code;
    private String message;
}
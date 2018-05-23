package com.project.pontointeligente.api.exceptions.handler;

import org.springframework.http.HttpStatus;

class ApiValidationError extends ApiError {

    private String field;

    ApiValidationError(HttpStatus status, String field, Throwable ex) {
        super(status, ex);
        this.field = field;
    }

    ApiValidationError(HttpStatus status, String field, String message, Throwable ex) {
        super(status, message, ex);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}

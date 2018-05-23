package com.project.pontointeligente.api.exceptions.handler;

import com.project.pontointeligente.api.exceptions.BusinessException;
import com.project.pontointeligente.api.exceptions.InfraestructureException;
import com.project.pontointeligente.api.exceptions.ResourceNotFoundException;
import com.project.pontointeligente.api.exceptions.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<ApiValidationError> handle(ValidationException e) {
        HttpStatus httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        ApiValidationError apiError = new ApiValidationError(httpStatus, e.getField(), e.getMessage(), e);
        return new ResponseEntity<>(apiError, httpStatus);
    }

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<ApiError> handle(BusinessException e) {
        return buildResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY, e);
    }

    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handle(ResourceNotFoundException e) {
        return buildResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY, e);
    }

    @ExceptionHandler(value = InfraestructureException.class)
    public ResponseEntity<ApiError> handle(InfraestructureException e) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    private ResponseEntity<ApiError> buildResponseEntity(HttpStatus httpStatus, Exception e) {
        ApiError apiError = new ApiError(httpStatus, e.getMessage(), e);
        return new ResponseEntity<>(apiError, httpStatus);
    }

}

package com.project.pontointeligente.api.exceptions.handler;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

class ApiValidationError extends ApiError {

    private List<ApiValidationDetail> details;

    ApiValidationError(HttpStatus status, String message, Throwable ex) {
        super(status, message, ex);
        this.details = new ArrayList<>();
    }

    ApiValidationError(HttpStatus status, String message, String field, String detailMessage, Throwable ex) {
        super(status, message, ex);
        this.details = new ArrayList<>();
        addDetail(field, detailMessage);
    }

    ApiValidationError(HttpStatus status, String message, List<ApiValidationDetail> details, Throwable ex) {
        super(status, message, ex);
        this.details = details;
    }

    void addDetail(String field, String detailMessage) {
        this.details.add(new ApiValidationDetail(field, detailMessage));
    }

    public List<ApiValidationDetail> getDetails() {
        return details;
    }

}

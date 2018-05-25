package com.project.pontointeligente.api.exceptions.handler;

class ApiValidationDetail {

    private String field;
    private String message;

    ApiValidationDetail(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

}

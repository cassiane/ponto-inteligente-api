package com.project.pontointeligente.api.exceptions;

public class InfraestructureException extends RuntimeException {

    public InfraestructureException(String message) {
        super(message);
    }

    public InfraestructureException(String message, Throwable cause) {
        super(message, cause);
    }

}

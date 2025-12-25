package com.nflp.nflp_backend.exception;

public class NLPServiceException extends RuntimeException {

    public NLPServiceException(String message) {
        super(message);
    }

    public NLPServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
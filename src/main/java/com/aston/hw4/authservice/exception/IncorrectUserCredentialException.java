package com.aston.hw4.authservice.exception;

public class IncorrectUserCredentialException extends RuntimeException {
    public IncorrectUserCredentialException(String message) {
        super(message);
    }
}

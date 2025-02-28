package com.denmit.eshop.authenticationservice.exception;

public class PasswordMismatchException extends RuntimeException {

    public PasswordMismatchException(String msg) {
        super(msg);
    }
}

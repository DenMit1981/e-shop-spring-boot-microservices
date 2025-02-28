package com.denmit.eshop.authenticationservice.exception;

public class UserIsPresentException extends RuntimeException {

    public UserIsPresentException(String msg) {
        super(msg);
    }
}

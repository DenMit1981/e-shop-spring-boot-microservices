package com.denmit.eshop.authenticationservice.exception;

public class WrongPasswordException extends RuntimeException {

    public WrongPasswordException(String msg) {
        super(msg);
    }
}

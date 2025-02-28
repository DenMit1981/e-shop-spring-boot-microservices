package com.denmit.eshop.paymentservice.exception;

public class IncorrectPaymentException extends RuntimeException {

    public IncorrectPaymentException(String message) {
        super(message);
    }
}

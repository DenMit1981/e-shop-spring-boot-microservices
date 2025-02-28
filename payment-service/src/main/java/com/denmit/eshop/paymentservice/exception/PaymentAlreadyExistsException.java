package com.denmit.eshop.paymentservice.exception;

public class PaymentAlreadyExistsException extends RuntimeException {

    public PaymentAlreadyExistsException() {
        super("This order has already been paid for");
    }
}


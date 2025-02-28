package com.denmit.eshop.paymentservice.exception;

public class ReceiverNotFoundException extends RuntimeException {

    public ReceiverNotFoundException() {
        super("Receiver not found");
    }
}

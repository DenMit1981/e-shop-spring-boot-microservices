package com.denmit.eshop.paymentservice.exception;

public class ReceiptNotFoundException extends RuntimeException {

    public ReceiptNotFoundException() {
        super("Receipt not found");
    }
}

package com.denmit.eshop.attachmentservice.exception;

public class AttachmentAccessException extends RuntimeException {

    public AttachmentAccessException() {
        super("You can add, replace or remove file only to your order");
    }
}

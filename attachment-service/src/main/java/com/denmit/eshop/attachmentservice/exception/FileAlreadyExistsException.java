package com.denmit.eshop.attachmentservice.exception;

public class FileAlreadyExistsException extends RuntimeException {

    public FileAlreadyExistsException() {
        super("File already exists for this order");
    }
}
